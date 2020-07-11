package com.lejian.oldman.service;

import com.google.common.collect.Lists;
import com.lejian.oldman.bo.*;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.repository.*;
import com.lejian.oldman.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
    private static final long MIN_INTERVAL_TIME=1;

    /**
     * 服务人员签到签出 距离目标老人 最远的距离
     */
    private static final long MAX_MAP_DISTANCE=10;


    /**
     * 签到
     * @param lng 经度
     * @param lat 维度
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
        if(oldmanBo.getStatus()== OldmanEnum.Status.YELLOW){
            param.setStatus(OldmanEnum.Status.GREEN);
        }else{
            param.setStatus(OldmanEnum.Status.YELLOW);
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
            long interval = Duration.between(LocalDateTime.now(),lastWorkerCheckinBo.getCreateTime()).toMinutes();
            if(interval < MIN_INTERVAL_TIME){
                //todo 抛异常 间隔太短
            }
        }
        //check 2
        LocationBo locationBo = locationRepository.getByPkId(oldmanBo.getLocationId());
        long distance = MapUtils.distance(longitude,latitude,locationBo.getPositionX(),locationBo.getPositionY());
        if(distance > MAX_MAP_DISTANCE){
            //todo 抛异常 超过最大距离
        }
    }

}
