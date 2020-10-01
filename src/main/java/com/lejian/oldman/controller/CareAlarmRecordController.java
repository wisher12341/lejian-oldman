package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.GetAlarmByPageRequest;
import com.lejian.oldman.controller.contract.request.GetMainSecondAllCountRequest;
import com.lejian.oldman.controller.contract.request.UpdateHandleByLocationIdRequest;
import com.lejian.oldman.controller.contract.response.GetAlarmByPageResponse;
import com.lejian.oldman.controller.contract.response.GetMainSecondAllCountResponse;
import com.lejian.oldman.controller.contract.response.ResultResponse;
import com.lejian.oldman.service.CareAlarmRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@ResponseBody
@RequestMapping("/alarm")
public class CareAlarmRecordController {

    @Autowired
    private CareAlarmRecordService service;

    /**
     * 获取可视化页面 区域三 中间的 应急报警 统计数据
     * @return
     */
    @RequestMapping("/getAllTypeCountByArea")
    public GetMainSecondAllCountResponse getAllTypeCount(@RequestBody GetMainSecondAllCountRequest request){
        GetMainSecondAllCountResponse response=new GetMainSecondAllCountResponse();
        response.setSMap(service.getAllTypeCount(request.getAreaCustomOne(),request.getAreaVillage(),request.getAreaTown(),request.getAreaCountry()));
        response.sum();
        return response;

    }

    /**
     * 分页获取报警信息
     * @param request
     * @return
     */
    @RequestMapping("/getAlarmOldmanByPage")
    public GetAlarmByPageResponse getAlarmByPage(@RequestBody GetAlarmByPageRequest request){
        GetAlarmByPageResponse response=new GetAlarmByPageResponse();
        response.setAlarmVoList(service.getAlarmByPage(request.getNeedOldmanInfo(),request.getPageParam(),request.getAlarmSearchParam()));
        if(request.getNeedCount()){
            //todo
        }
        return response;
    }

    /**
     * 这些oid的处理状态改为已处理
     * @return
     */
    @RequestMapping("/updateHandleByLocationId")
    public ResultResponse updateHandleByLocationId(@RequestBody UpdateHandleByLocationIdRequest request){
        service.updateHandleByLocationId(request.getLocationId(),request.getIsHandle());
        return new ResultResponse();
    }


}
