package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.controller.contract.request.OldmanSerachParmRequest;
import com.lejian.oldman.controller.contract.request.PollRequest;
import com.lejian.oldman.controller.contract.response.GetMainOldmanDataResponse;
import com.lejian.oldman.controller.contract.response.GetMainStaticDataResponse;
import com.lejian.oldman.controller.contract.response.PollResponse;
import com.lejian.oldman.enums.OldmanEnum;

import com.lejian.oldman.service.*;
import com.lejian.oldman.vo.LocationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 主页可视化查询
 */
@Controller
@ResponseBody
@RequestMapping("/main")
public class MainController {

    @Autowired
    private MainService mainService;
    @Autowired
    private CareAlarmRecordService careAlarmRecordService;
    @Autowired
    private OldmanService oldmanService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private WorkerService workerService;


//    /**
//     * 获取可视化页面 区域三 中间的 统计数据
//     * @return
//     */
//    @RequestMapping("/getMainSencondAllCount")
//    public GetMainSecondAllCountResponse getMainSencondAllCount(@RequestBody GetMainSecondAllCountRequest request){
//        GetMainSecondAllCountResponse response = new GetMainSecondAllCountResponse();
//        Pair<Long,Map<String, Long>> equipPair=service.getOldmanEquip(request.getAreaCustomOne());
//        response.setEquipMap(equipPair.getSecond());
//        response.setEquipCount(equipPair.getFirst());
//        response.setAlarmCount(careAlarmRecordService.getCount());
//        response.setYellowOldmanCount(oldmanService.getOldmanCountByStatus(OldmanEnum.Status.YELLOW));
//        Map<String,Long> homeServiceMap= Maps.newHashMap();
//        homeServiceMap.put("家庭服务",13L);
//        homeServiceMap.put("居家养老",20L);
//        homeServiceMap.put("长护险服务",11L);
//        response.setHomeServiceMap(homeServiceMap);
//
//
//        Map<String,Long> workerMap= Maps.newHashMap();
//        workerMap.put("医疗",3L);
//        workerMap.put("送餐",1L);
//        workerMap.put("居家养老",1L);
//        workerMap.put("长护险",5L);
//        response.setWorkerMap(workerMap);
//
//        response.sum();
//        return response;
//    }


    /**
     * 轮询最新数据
     * 1. 数字
     * 2. 楼状态
     */
    @RequestMapping("/poll")
    public PollResponse pollStatus(@RequestBody PollRequest request){
        PollResponse response=new PollResponse();
        /**
         * 只有YellowOldmanCount 需要设置status=2
         */
        OldmanSearchParam oldmanSearchParam=new OldmanSearchParam();
        oldmanSearchParam.setStatus(OldmanEnum.Status.YELLOW.getValue());

        response.setYellowOldmanCount(oldmanService.getOldmanCount(oldmanSearchParam));
        response.setAlarmCount(careAlarmRecordService.getCount(request.getOldmanSearchParam()));
        response.setWorkerCheckInCount(workerService.getCheckinCount(null)/2);
        Pair<Long,List<LocationVo>> pair = locationService.pollStatus(request.getTimestamp());
        if(pair!=null) {
            response.setLocationVoList(pair.getSecond());
            response.setTimestamp(pair.getFirst());
        }
        return response;
    }

    /**
     * 静态数据
     */
    @RequestMapping("/getMainStaticData")
    public GetMainStaticDataResponse getMainStaticData(@RequestBody PollRequest request){
        GetMainStaticDataResponse response=new GetMainStaticDataResponse();
        response.setWorkerMap(workerService.getTypeCount());
        response.setHomeServiceMap(oldmanService.getHomeServiceCount(request.getOldmanSearchParam()));
        response.setEquipCount(oldmanService.getEquipCount(request.getOldmanSearchParam()));
        response.setEquipMap(mainService.getOldmanEquipCount(request.getOldmanSearchParam()));
        response.setBirthdayCount(oldmanService.getBirthdayOldmanCount(request.getBirthdayLike()));
        response.sum();
        return response;
    }

    /**
     * 老人统计数据
     */
    @RequestMapping("/getOldmanCount")
    public GetMainOldmanDataResponse getOldmanCount(@RequestBody OldmanSerachParmRequest request){
        GetMainOldmanDataResponse response=new GetMainOldmanDataResponse();
        response.setSexMap(mainService.getOldmanGroup(request.getOldmanSearchParam(),OldmanEnum.Sex.class,"sex"));
        response.setAgeMap(mainService.getOldmanAge(request.getOldmanSearchParam()));
        response.setHjMap(mainService.getOldmanGroup(request.getOldmanSearchParam(),OldmanEnum.HouseholdType.class,"household_type"));
        response.setJtMap(mainService.getOldmanGroup(request.getOldmanSearchParam(),OldmanEnum.FamilyType.class,"family"));
        return response;
    }
}