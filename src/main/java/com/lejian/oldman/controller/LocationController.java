package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.controller.contract.response.GetLocationListResponse;
import com.lejian.oldman.service.LocationService;
import com.lejian.oldman.service.OldmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/location")
public class LocationController {


    @Autowired
    private LocationService service;
    @Autowired
    private OldmanService oldmanService;

    @RequestMapping("/getAllLocationByConfig")
    public GetLocationListResponse getAllLocationByConfig(){
        GetLocationListResponse response = new GetLocationListResponse();
        response.setLocationVoList(service.getAllLocationByConfig());
        return response;
    }

    @RequestMapping("/getLocationByArea")
    public GetLocationListResponse getLocationByArea(@RequestBody OldmanSearchParam oldmanSearchParam){
        GetLocationListResponse response=new GetLocationListResponse();
        response.setLocationVoList(service.getLocationByArea(oldmanSearchParam));
        return response;
    }

}
