package com.lejian.oldman.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.bo.*;
import com.lejian.oldman.config.VarConfig;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.controller.contract.request.WorkerSearchParam;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.WorkerEnum;
import com.lejian.oldman.utils.DateUtils;
import com.lejian.oldman.vo.WorkerVo;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.repository.*;
import com.lejian.oldman.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.ACCOUNT_ERROR;
import static com.lejian.oldman.common.ComponentRespCode.CHECKIN_OVER_DISTANCE;
import static com.lejian.oldman.common.ComponentRespCode.CHECKIN_SHORT_TIME;
import static com.lejian.oldman.utils.DateUtils.YYMMDDHHMMSS;

@Slf4j
@Service
public class WorkerService {

    @Autowired
    private OldmanRepository oldmanRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private WorkerCheckinRepository workerCheckinRepository;
    @Autowired
    private LocationRepository locationRepository;

    /**
     * 服务人员签到签出 最小的时间间隔 分钟
     */
    //todo 变成可配置的
    private static final long MIN_INTERVAL_TIME=5;

    /**
     * 服务人员签到签出 距离目标老人 最远的距离 (米)
     */
    //todo 变成可配置的
    private static final double MAX_MAP_DISTANCE=2000;


    /**
     * 签到
     * @param lng 经度
     * @param lat 纬度
     * @param oid
     * @param token
     */
    //todo 对该方法 根据服务人员Id进行加锁
    public void checkIn(String lng, String lat,String oid,String token) {
        //服务人员
        WorkerBo workerBo = getWorkerByToken(token);
        //老人
        OldmanBo oldmanBo = oldmanRepository.findByOid(oid);

        beforeCheck(workerBo,oldmanBo,lng,lat);
        handleCheckIn(workerBo,oldmanBo,lng,lat);

    }

    /**
     * 服务人员签到处理
     * 1. 记录签到记录
     * 2. 老人状态变更
     * @param workerBo
     * @param oldmanBo
     * @param lng
     * @param lat
     */
    //todo 事务
    private void handleCheckIn(WorkerBo workerBo, OldmanBo oldmanBo, String lng, String lat) {
        WorkerCheckinBo workerCheckinBo = new WorkerCheckinBo();
        workerCheckinBo.setWorkerId(workerBo.getId());
        workerCheckinBo.setOid(oldmanBo.getOid());
        workerCheckinBo.setLng(lng);
        workerCheckinBo.setLat(lat);
        workerCheckinRepository.save(workerCheckinBo);

        handleOldmanStatus(oldmanBo);
    }

    /**
     * 服务人员签到签出 老人状态变更
     * 老人状态 变更  黄->绿，  绿，红->黄
     * @param oldmanBo
     */
    private void handleOldmanStatus(OldmanBo oldmanBo) {
        OldmanBo param = new OldmanBo();
        param.setId(oldmanBo.getId());
        //step 1
        if(oldmanBo.getStatusEnum()== OldmanEnum.Status.YELLOW){
            param.setStatusEnum(OldmanEnum.Status.GREEN);
        }else{
            param.setStatusEnum(OldmanEnum.Status.YELLOW);
        }
        oldmanRepository.dynamicUpdateByPkId(param);
    }

    /**
     * 根据账号token查找服务人员
     * @param token
     * @return
     */
    private WorkerBo getWorkerByToken(String token) {
        String username = token.split("&")[0].split("=")[1];
        String password = token.split("&")[1].split("=")[1];
        UserBo userBo = userRepository.getByUsernameAndPassword(username,password);
        ACCOUNT_ERROR.checkNotNull(userBo);
        return workerRepository.getWorkerByUid(userBo.getId());
    }

    /**
     * 检查数据正确性
     * 1. 距离上次签到至少相差xx分钟
     * 2. gps数据 距离老人的位置小于xx
     * @param workerBo
     * @param oldmanBo
     * @param longitude
     * @param latitude
     */
    private void beforeCheck(WorkerBo workerBo, OldmanBo oldmanBo, String longitude, String latitude) {
        //check 1
        //服务人员服务的上一个记录
        WorkerCheckinBo lastWorkerCheckinBo = workerCheckinRepository.getLatestRecordByWid(workerBo.getId());
        if(lastWorkerCheckinBo!=null){
            long interval = Duration.between(lastWorkerCheckinBo.getCreateTime().toLocalDateTime(),LocalDateTime.now()).toMinutes();
            if(interval < MIN_INTERVAL_TIME){
                CHECKIN_SHORT_TIME.doThrowException();
            }
        }
        //check 2
        LocationBo locationBo = locationRepository.getByPkId(oldmanBo.getLocationId());
        double distance = MapUtils.distance(longitude,latitude,locationBo.getPositionX(),locationBo.getPositionY());
        if(distance > MAX_MAP_DISTANCE){
            log.info("check in location:{},{}; oldman location:{},{}; distance:{}"
                    ,longitude,latitude,locationBo.getPositionX(),locationBo.getPositionY(),distance);
            CHECKIN_OVER_DISTANCE.doThrowException();
        }
    }

    /**
     * 分页获取某个时间段  最后一次 位置信息
     * @param startTime
     * @param endTime
     * @return
     */
    public List<WorkerCheckinBo> getWorkerLatestPositionByPage(String startTime, String endTime,PageParam pageParam) {
        String beyond= VarConfig.getWorkerBeyond();
        List<WorkerCheckinBo> latestWorkerList = workerCheckinRepository.getLatestTimeByTimeAndAreaAndPage(startTime,endTime,pageParam,beyond);
        if(CollectionUtils.isNotEmpty(latestWorkerList)) {
            return workerCheckinRepository.getAllPositionByTime(latestWorkerList);
        }
        return Lists.newArrayList();

    }

    private WorkerVo convert(WorkerBo workerBo, List<WorkerCheckinBo> workerCheckinBoList) {
        WorkerVo workerVo = new WorkerVo();
        workerVo.setSex(BusinessEnum.find(workerBo.getSex(),OldmanEnum.Sex.class).getDesc());
        workerVo.setAge(DateUtils.birthdayToAge(workerVo.getBirthday()));
        BeanUtils.copyProperties(workerBo,workerVo);
        if(CollectionUtils.isNotEmpty(workerCheckinBoList)) {
            List<WorkerVo.Position> positionList = Lists.newArrayList();
            workerCheckinBoList.forEach(checkinBo -> positionList.add(new WorkerVo.Position(checkinBo.getLng(), checkinBo.getLat(), checkinBo.getCreateTime().toLocalDateTime().format(YYMMDDHHMMSS))));
            workerVo.setPositionList(positionList.stream().sorted(Comparator.comparing(WorkerVo.Position::getTime)).collect(Collectors.toList()));
        }
        return workerVo;
    }

    private WorkerVo convert(WorkerBo workerBo) {
        return convert(workerBo,null);
    }

    public WorkerVo getWorkerPositionByTime(String startTime, String endTime, Integer workerId) {
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        jpaSpecBo.getEqualMap().put("workerId",workerId);
        jpaSpecBo.getLessEMap().put("createTime", DateUtils.toTimeStamp(endTime));
        jpaSpecBo.getGreatEMap().put("createTime",DateUtils.toTimeStamp(startTime));
        List<WorkerCheckinBo> workerCheckinBoList = workerCheckinRepository.findWithSpec(jpaSpecBo);
        WorkerBo workerBo = new WorkerBo();
        workerBo.setId(workerId);
        return convert(workerBo,workerCheckinBoList);
    }


    /**
     * 该行政单位所属的服务人员 (key： VarConfig的值)
     * 1. 获取签到表，该时间范围内 服务人员id (分页)
     * 2. 根据服务人员id 获取这些服务人员的最新位置
     *
     * @param pageParam
     * @param startTime
     * @param endTime
     * @return
     */
    public List<WorkerVo> getWorkerPositionByPage(PageParam pageParam, String startTime, String endTime) {
        List<WorkerCheckinBo> workerCheckinBoList = getWorkerLatestPositionByPage(startTime, endTime, pageParam);
        if(CollectionUtils.isNotEmpty(workerCheckinBoList)) {
            Map<Integer, WorkerCheckinBo> workerPositionMap = workerCheckinBoList.stream().collect(Collectors.toMap(WorkerCheckinBo::getWorkerId, Function.identity()));
            List<WorkerBo> workerBoList = workerRepository.getByPkIds(Lists.newArrayList(workerPositionMap.keySet()));
            return workerBoList.stream().map(bo -> {
                WorkerCheckinBo workerCheckinBo = workerPositionMap.get(bo.getId());
                if (workerCheckinBo == null) {
                    return null;
                }
                return convert(bo, Lists.newArrayList(workerCheckinBo));
            }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public WorkerVo getWorkerByWid(Integer workerId) {
        return convert(workerRepository.getByPkId(workerId));
    }


    public Long getCheckinCount(OldmanSearchParam oldmanSearchParam) {
        return workerCheckinRepository.getCheckinCount(oldmanSearchParam);
    }

    public List<WorkerVo> getWorkerByPage(PageParam pageParam, WorkerSearchParam param) {
        JpaSpecBo jpaSpecBo=new JpaSpecBo();
        if(param!=null) {
            jpaSpecBo = param.convert();
        }
        return workerRepository.findByPageWithSpec(pageParam.getPageNo(),pageParam.getPageSize(),jpaSpecBo).stream().map(this::convert).collect(Collectors.toList());
    }

    public Map<String, Long> getTypeCount() {
        Map<String, Long> map = Maps.newHashMap();
        for(WorkerEnum workerEnum: WorkerEnum.Type.values()){
            map.put(workerEnum.getDesc(),0L);
        }
        List<Map<String,Object>> typeMapList=workerRepository.getTypeCountByBeyond(VarConfig.getWorkerBeyond());

        typeMapList.forEach(item->{
            Integer type= (Integer) item.get("type");
            Long b=((BigInteger)item.get("count")).longValue();

            map.put(BusinessEnum.find(type, WorkerEnum.Type.class).getDesc(),b);
        });
        return map;
    }
}
