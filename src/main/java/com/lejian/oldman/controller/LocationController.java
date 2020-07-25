package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.PollLocationStatusRequest;
import com.lejian.oldman.controller.contract.response.GetLocationListResponse;
import com.lejian.oldman.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/location")
public class LocationController {


    @Autowired
    private LocationService service;

    @RequestMapping("/getAllLocation")
    public GetLocationListResponse getAllLocation(){
        GetLocationListResponse response = new GetLocationListResponse();
        response.setLocationVoList(service.getAllLocation());
        return response;
    }



    /**
     * 轮询楼最新状态
     */
    @RequestMapping("/pollStatus")
    public GetLocationListResponse pollStatus(@RequestBody PollLocationStatusRequest request){
        GetLocationListResponse response=new GetLocationListResponse();
        response.setLocationVoList(service.pollStatus(request.getTimestamp()));
        return response;
    }

}
