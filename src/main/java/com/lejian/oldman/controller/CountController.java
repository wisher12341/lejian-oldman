package com.lejian.oldman.controller;

import com.google.common.collect.Maps;
import com.lejian.oldman.controller.contract.request.GetMainSencondAllCountRequest;
import com.lejian.oldman.controller.contract.response.GetMainSencondAllCountResponse;
import com.lejian.oldman.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Controller
@ResponseBody
@RequestMapping("/count")
public class CountController {

    @Autowired
    private CountService service;

    /**
     * 获取可视化页面 区域三 中间的 统计数据
     * @return
     */
    @RequestMapping("/getMainSencondAllCount")
    public GetMainSencondAllCountResponse getMainSencondAllCount(GetMainSencondAllCountRequest request){
        GetMainSencondAllCountResponse response = new GetMainSencondAllCountResponse();
        response.setEquipMap(service.getOldmanEquip(request.getAreaCustomOne()));
        Map<String,Long> homeServiceMap= Maps.newHashMap();
        homeServiceMap.put("家庭服务",13L);
        homeServiceMap.put("居家养老",20L);
        homeServiceMap.put("长护险服务",11L);
        response.setHomeServiceMap(homeServiceMap);

        Map<String,Long> warnMap= Maps.newHashMap();
        warnMap.put("紧急报警",3L);
        warnMap.put("行为报警",1L);
        warnMap.put("规律报警",0L);
        response.setWarnMap(warnMap);

        Map<String,Long> workerMap= Maps.newHashMap();
        workerMap.put("医疗",3L);
        workerMap.put("送餐",1L);
        workerMap.put("居家养老",1L);
        workerMap.put("长护险",5L);
        response.setWorkerMap(workerMap);

        response.sum();
        return response;
    }

}
