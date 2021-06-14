package com.lejian.oldman.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.bo.*;
import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.enums.*;
import com.lejian.oldman.security.UserContext;
import com.lejian.oldman.utils.DateUtils;
import com.lejian.oldman.utils.LjReflectionUtils;
import com.lejian.oldman.utils.UserUtils;
import com.lejian.oldman.vo.WorkerDispatchVo;
import com.lejian.oldman.vo.WorkerVo;
import com.lejian.oldman.repository.*;
import com.lejian.oldman.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.lejian.oldman.common.ComponentRespCode.*;
import static com.lejian.oldman.utils.DateUtils.YYMMDD;
import static com.lejian.oldman.utils.DateUtils.YYMMDDHHMMSS;
import static com.lejian.oldman.utils.DateUtils.YYMMDDHHMMSS_1;

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
    @Autowired
    private VisualSettingRepository visualSettingRepository;
    @Autowired
    private OrganRepository organRepository;
    @Autowired
    private WorkerDispatchRepository workerDispatchRepository;


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
     */
    //todo 对该方法 根据服务人员Id进行加锁
    public void checkIn(String lng, String lat,String oid) {
        //服务人员
        WorkerBo workerBo = getWorkerByUser();
        //老人
        OldmanBo oldmanBo = oldmanRepository.findByOid(oid);

        beforeCheck(workerBo,oldmanBo,lng,lat);
        handleCheckIn(workerBo,oldmanBo,lng,lat);

    }

    /**
     * 服务人员签到处理
     * 1. 记录签到记录
     * 2. 老人状态变更
     * 3. 派单状态变更
     * @param workerBo
     * @param oldmanBo
     * @param lng
     * @param lat
     */
    @Transactional
    public void handleCheckIn(WorkerBo workerBo, OldmanBo oldmanBo, String lng, String lat) {
        WorkerCheckinBo workerCheckinBo = new WorkerCheckinBo();
        workerCheckinBo.setWorkerId(workerBo.getId());
        workerCheckinBo.setOid(oldmanBo.getOid());
        workerCheckinBo.setLng(lng);
        workerCheckinBo.setLat(lat);
        workerCheckinRepository.save(workerCheckinBo);

        handleOldmanStatus(oldmanBo);

        handleDispatch(workerBo,oldmanBo);
    }

    /**
     * 派单状态变更
     * 1. 之前未完成的订单状态变更
     * 2. 当前订单状态变更
     * @param workerBo
     * @param oldmanBo
     */
    private void handleDispatch(WorkerBo workerBo, OldmanBo oldmanBo) {
        workerDispatchRepository.notFinishUpdate(workerBo.getId(),LocalDateTime.now().format(YYMMDDHHMMSS));

        WorkerDispatchBo workerDispatchBo = new WorkerDispatchBo();
        workerDispatchBo.setId(workerBo.getCurrentDispatchId());
        if(oldmanBo.getStatusEnum()== OldmanEnum.Status.YELLOW){
            workerDispatchBo.setStatus(WorkerEnum.WorkerStatus.FINISH.getValue());
        }else{
            workerDispatchBo.setStatus(WorkerEnum.WorkerStatus.DOING.getValue());
        }
        workerDispatchRepository.dynamicUpdateByPkId(workerDispatchBo);
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
     * 根据当前账号查找服务人员
     * @return
     */
    private WorkerBo getWorkerByUser() {
        User user =UserContext.getLoginUser();
        if (user!=null){
            UserBo userBo = userRepository.getByUsername(user.getUsername());
            ACCOUNT_ERROR.checkNotNull(userBo);
            return workerRepository.getWorkerByUid(userBo.getId());
        }
        NO_DATA_FOUND.doThrowException("no worker found");
        return null;
    }

    /**
     * 检查数据正确性
     * 1. 距离上次签到至少相差xx分钟
     * 2. gps数据 距离老人的位置小于xx
     * 3. 签入时判断（签出不判断）：有相应派单，时间限制  +- 10分钟
     * @param workerBo
     * @param oldmanBo
     * @param longitude
     * @param latitude
     */
    private void beforeCheck(WorkerBo workerBo, OldmanBo oldmanBo, String longitude, String latitude) {
        //白名单
        if (Lists.newArrayList(139,140).contains(workerBo.getId())){
            return;
        }

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
//        if(distance > MAX_MAP_DISTANCE){
//            CHECKIN_OVER_DISTANCE.doThrowException();
//        }



        //check3
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        jpaSpecBo.getEqualMap().put("workerId", workerBo.getId());
        jpaSpecBo.getEqualMap().put("oid", oldmanBo.getOid());
        WorkerDispatchBo workerDispatchBo = workerDispatchRepository.findWithSpec(jpaSpecBo).stream().findFirst().orElse(null);
        if (workerDispatchBo == null) {
            CHECKIN_NO_DISPATCH.doThrowException();
        }
        workerBo.setCurrentDispatchId(workerDispatchBo.getId());
        if(oldmanBo.getStatusEnum()== OldmanEnum.Status.GREEN) {
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isBefore(workerDispatchBo.getStartTime().toLocalDateTime().minusMinutes(10))
                    || currentTime.isAfter(workerDispatchBo.getStartTime().toLocalDateTime().plusMinutes(10))) {
                CHECKIN_TIME.doThrowException();
            }
        }
    }

    /**
     * 分页获取某个时间段  最后一次 位置信息
     * @param startTime
     * @param endTime
     * @return
     */
    public List<WorkerCheckinBo> getWorkerLatestPositionByPage(String startTime, String endTime,PageParam pageParam) {
        String beyond= visualSettingRepository.getWorkerBeyond();
        List<WorkerCheckinBo> latestWorkerList = workerCheckinRepository.getLatestTimeByTimeAndAreaAndPage(startTime,endTime,pageParam,beyond);
        if(CollectionUtils.isNotEmpty(latestWorkerList)) {
            return workerCheckinRepository.getAllPositionByTime(latestWorkerList);
        }
        return Lists.newArrayList();

    }

    private WorkerVo convert(WorkerBo workerBo, List<WorkerCheckinBo> workerCheckinBoList) {
        WorkerVo workerVo = new WorkerVo();
        workerVo.setSex(BusinessEnum.find(workerBo.getSex(),OldmanEnum.Sex.class).getDesc());
        workerVo.setEducation(BusinessEnum.find(workerBo.getEducation(),OldmanEnum.Education.class).getDesc());
        workerVo.setAge(DateUtils.birthdayToAge(workerVo.getBirthday()));
        workerVo.setType(BusinessEnum.find(workerBo.getType(),WorkerEnum.Type.class).getDesc());
        BeanUtils.copyProperties(workerBo,workerVo);
        if(CollectionUtils.isNotEmpty(workerCheckinBoList)) {
            List<WorkerVo.Position> positionList = Lists.newArrayList();
            workerCheckinBoList.forEach(checkinBo -> positionList.add(new WorkerVo.Position(checkinBo.getLng(), checkinBo.getLat(), checkinBo.getCreateTime().toLocalDateTime().format(YYMMDDHHMMSS),0)));
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
        WorkerVo workerVo = convert(workerBo,workerCheckinBoList);


        //未开始的订单， 虚线
        JpaSpecBo jpaSpecBo1 = new JpaSpecBo();
        jpaSpecBo1.getEqualMap().put("workerId",workerId);
        jpaSpecBo1.getEqualMap().put("status",WorkerEnum.WorkerStatus.NOT_START.getValue());
        jpaSpecBo1.getLessEMap().put("startTime",DateUtils.toTimeStamp(endTime));
        jpaSpecBo1.getGreatEMap().put("startTime",DateUtils.toTimeStamp(startTime));
        List<WorkerDispatchBo> workerDispatchBoList = workerDispatchRepository.findWithSpec(jpaSpecBo1);
        if (CollectionUtils.isNotEmpty(workerCheckinBoList)){
            List<String> oidList = workerDispatchBoList.stream().map(WorkerDispatchBo::getOid).collect(Collectors.toList());
            Map<String,OldmanBo> oldmanBoMap = oldmanRepository.getByOids(oidList).stream().collect(Collectors.toMap(OldmanBo::getOid,Function.identity()));
            Map<Integer,LocationBo> locationBoMap = locationRepository.getByPkIds(oldmanBoMap.values().stream().map(OldmanBo::getLocationId).collect(Collectors.toList())).stream().collect(Collectors.toMap(LocationBo::getId,Function.identity()));;
            workerDispatchBoList.forEach(item->{
                OldmanBo bo = oldmanBoMap.get(item.getOid());
                LocationBo locationBo = locationBoMap.get(bo.getLocationId());
                WorkerVo.Position position = new WorkerVo.Position(locationBo.getPositionX(),locationBo.getPositionY(),DateUtils.format(item.getStartTime(),YYMMDDHHMMSS),1);
                workerVo.getPositionList().add(position);
            });
        }
        workerVo.getPositionList().stream().sorted(Comparator.comparing(WorkerVo.Position::getTime));

        return workerVo;
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
        JpaSpecBo jpaSpecBo = WorkerSearchParam.convert(param);
        if(param!=null) {
            if (StringUtils.isNotBlank(param.getOrganName())){
                JpaSpecBo bo = new JpaSpecBo();
                bo.getEqualMap().put("name",param.getOrganName());
                OrganBo organBo = organRepository.findWithSpec(bo).get(0);
                param.setOrganId(organBo.getId());
            }
            if (param.getSettingBeyond()!=null && param.getSettingBeyond()==true){
                String beyond = visualSettingRepository.getWorkerBeyond();
                param.setBeyond(beyond);
            }
            jpaSpecBo = WorkerSearchParam.convert(param);

        }
        return workerRepository.findByPageWithSpec(pageParam.getPageNo(),pageParam.getPageSize(),jpaSpecBo).stream().map(this::convert).collect(Collectors.toList());
    }
    @Transactional
    public void addWorkerByExcel(Pair<List<String>, List<List<String>>> excelData) {
        List<String> titleList=excelData.getFirst();
        List<List<String>> valueList=excelData.getSecond();

        List<WorkerBo> workerBoList = Lists.newArrayList();

        IntStream.range(0,valueList.size()).forEach(item->{
            WorkerBo workerBo=new WorkerBo();
            workerBoList.add(workerBo);
        });

        Map<String,Field> fieldMap= LjReflectionUtils.getFieldToMap(WorkerBo.class);

        try {
            for (int i = 0; i < titleList.size(); i++) {
                ExcelEnum excelEnum = ExcelEnum.findFieldName(titleList.get(i),WorkerExcelEnum.class);
                Field field = fieldMap.get(excelEnum.getFieldName());
                field.setAccessible(true);
                //纵向 遍历每个对象，一个属性一个属性 纵向赋值
                for (int j = 0; j < valueList.size(); j++) {
                    Object value=valueList.get(j).get(i);
                    if(StringUtils.isNotBlank(String.valueOf(value))) {
                        //转换成枚举值
                        Class<? extends BusinessEnum> enumClass = excelEnum.getEnumType();
                        if (enumClass != null) {
                            //需要 枚举转换
                            for (BusinessEnum businessEnum : enumClass.getEnumConstants()) {
                                if (businessEnum.getDesc().equals(value)) {
                                    value = businessEnum.getValue();
                                    break;
                                }
                            }
                        }
                        field.set(workerBoList.get(j), value);
                    }
                }
            }
        }catch (IllegalArgumentException | IllegalAccessException e){
            REFLECTION_ERROR.doThrowException("fail to addWorkerByExcel",e);
        }
        workerBoList.forEach(this::supplement);

        //todo 验证bo数据
        // left 添加 right更新
        Pair<List<WorkerBo>,List<WorkerBo>> pair = classifyDbType(workerBoList);
        workerRepository.batchAdd(pair.getFirst());
        workerRepository.batchUpdate(pair.getSecond());

    }

    private static final int PART_NUM=100;

    /**
     * 区分 哪些服务人员 添加 哪些老人更新
     * @param workerBoList
     * @return
     */
    private Pair<List<WorkerBo>,List<WorkerBo>> classifyDbType(List<WorkerBo> workerBoList) {
        List<WorkerBo> addList=Lists.newArrayList();
        List<WorkerBo> updateList=Lists.newArrayList();

        UserBo userBo = UserUtils.getUser();

        List<List<WorkerBo>> parts = Lists.partition(workerBoList, PART_NUM);
        parts.forEach(item->{
            List<String> idCardList=item.stream().map(WorkerBo::getIdCard).collect(Collectors.toList());
            Map<String,WorkerBo> existWorkerMap=workerRepository.getByIdCards(idCardList).stream().collect(Collectors.toMap(WorkerBo::getIdCard, Function.identity()));
            item.forEach(worker->{
                if(existWorkerMap.containsKey(worker.getIdCard())){
                    worker.setId(existWorkerMap.get(worker.getIdCard()).getId());
                    updateList.add(worker);
                } else{
                    worker.setAddUserId(userBo.getId());
                    addList.add(worker);
                }
            });
        });
        return Pair.of(addList,updateList);
    }

    /**
     * 补全数据
     */
    private void supplement(WorkerBo workerBo){
        workerBo.setBirthday(DateUtils.stringToLocalDate(workerBo.getIdCard().substring(6,14),YYMMDD));
    }

    public Long getWorkerCount(WorkerSearchParam workerSearchParam) {
        return workerRepository.countWithSpec(WorkerSearchParam.convert(workerSearchParam));
    }

    public void editWorker(WorkerParam workerParam) {
        workerRepository.dynamicUpdateByPkId(convert(workerParam));
    }


    private WorkerBo convert(WorkerParam workerParam) {
        WorkerBo workerBo=new WorkerBo();
        BeanUtils.copyProperties(workerParam,workerBo);
        workerBo.setBirthday(DateUtils.stringToLocalDate(workerParam.getIdCard().substring(6,14),YYMMDD));
        return workerBo;
    }

    public void deleteWorker(Integer id) {
        workerRepository.deleteById(id);
    }

    public Long getCount(WorkerSearchParam workerSearchParam) {
        if (StringUtils.isNotBlank(workerSearchParam.getOrganName())){
            JpaSpecBo jpaSpecBo = new JpaSpecBo();
            jpaSpecBo.getEqualMap().put("name",workerSearchParam.getOrganName());
            workerSearchParam.setOrganId(organRepository.findWithSpec(jpaSpecBo).get(0).getId());
        }
        JpaSpecBo jpaSpecBo = WorkerSearchParam.convert(workerSearchParam);
        return workerRepository.countWithSpec(jpaSpecBo);
    }

    /**
     * 归属地服务人员数量
     * @return
     */
    public Long getBeyondCount() {
        String beyond = visualSettingRepository.getWorkerBeyond();
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        jpaSpecBo.getEqualMap().put("beyond",beyond);
        Integer userRoleId = UserUtils.getUserRoleId();
        if (userRoleId!=null){
            jpaSpecBo.getEqualMap().put("addUserId",userRoleId);
        }
        return workerRepository.countWithSpec(jpaSpecBo);
    }

    public Map<String, String> getTypeMapCount() {
        Map<String, String> map = Maps.newHashMap();
        for(WorkerEnum workerEnum: WorkerEnum.Type.values()){
            map.put(workerEnum.getDesc(),"0");
        }
        List<Map<String,Object>> typeMapList=workerRepository.getTypeCountByBeyond(visualSettingRepository.getWorkerBeyond());

        typeMapList.forEach(item->{
            Integer type= (Integer) item.get("type");
            Long b=((BigInteger)item.get("count")).longValue();

            map.put(BusinessEnum.find(type, WorkerEnum.Type.class).getDesc(),String.valueOf(b));
        });
        return map;
    }


    public void dispatch(DispatchRequest request) {
        UserBo userBo = UserUtils.getUser();

        WorkerDispatchBo bo = new WorkerDispatchBo();
        bo.setOid(request.getOid());
        bo.setWorkerId(request.getWorkerId());
        bo.setStartTime(new Timestamp(request.getStartTime()));
        bo.setEndTime(new Timestamp(request.getEndTime()));
        bo.setUserId(userBo.getId());
        workerDispatchRepository.save(bo);
    }

    public List<WorkerDispatchVo> getWorkerDispatchByPage(PageParam pageParam, WorkerDispatchSearchParam workerDispatchSearchParam) {
        List<WorkerDispatchBo> workerDispatchBoList =  workerDispatchRepository.findByPageWithSpec(pageParam.getPageNo(),pageParam.getPageSize(),workerDispatchSearchParam.getJpaSpecBo());
        List<String> oidList = workerDispatchBoList.stream().map(WorkerDispatchBo::getOid).distinct().collect(Collectors.toList());
        List<Integer> workerIdList = workerDispatchBoList.stream().map(WorkerDispatchBo::getWorkerId).distinct().collect(Collectors.toList());
        Map<String,OldmanBo> oldmanBoMap = oldmanRepository.getByOids(oidList).stream().collect(Collectors.toMap(OldmanBo::getOid,Function.identity()));
        Map<Integer,WorkerBo> workerBoMap = workerRepository.getByPkIds(workerIdList).stream().collect(Collectors.toMap(WorkerBo::getId,Function.identity()));
        return workerDispatchBoList.stream().map(bo->{
            WorkerDispatchVo vo = new WorkerDispatchVo();
            vo.setOid(bo.getOid());
            vo.setOldmanName(oldmanBoMap.get(bo.getOid()).getName());
            vo.setWorkerId(bo.getWorkerId());
            vo.setWorkerName(workerBoMap.get(bo.getWorkerId()).getName());
            vo.setStartTime(DateUtils.format(bo.getStartTime(),YYMMDDHHMMSS_1));
            vo.setEndTime(DateUtils.format(bo.getEndTime(),YYMMDDHHMMSS_1));
            vo.setStatus(BusinessEnum.find(bo.getStatus(),WorkerEnum.WorkerStatus.class).getDesc());
            return vo;
        }).collect(Collectors.toList());
    }

    public Long getWorkerDispatchCount(WorkerDispatchSearchParam workerDispatchSearchParam) {
        return workerDispatchRepository.countWithSpec(workerDispatchSearchParam.getJpaSpecBo());
    }

    public WorkerVo getWorkerByAccount() {
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        UserBo userBo = UserUtils.getUser();
        jpaSpecBo.getEqualMap().put("userId",userBo.getId());
        return convert(workerRepository.findWithSpec(jpaSpecBo).get(0));
    }
}
