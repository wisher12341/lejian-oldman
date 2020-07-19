package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.response.GetAllLocationResponse;
import com.lejian.oldman.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/location")
public class LocationController {


    @Autowired
    private LocationService service;

    @RequestMapping("/getAllLocation")
    public GetAllLocationResponse getAllLocation(){
        GetAllLocationResponse response = new GetAllLocationResponse();
        response.setLocationVoList(service.getAllLocation());
        return response;
    }


}
