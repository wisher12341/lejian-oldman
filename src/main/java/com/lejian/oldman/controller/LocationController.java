package com.lejian.oldman.controller;

import com.google.common.collect.Lists;
import com.lejian.oldman.controller.contract.request.GetLocationByPageRequest;
import com.lejian.oldman.controller.contract.request.IdRequest;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.controller.contract.request.SaveLocationRequest;
import com.lejian.oldman.controller.contract.response.GetLocationListResponse;
import com.lejian.oldman.controller.contract.response.ResultResponse;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.security.annotation.BackUserAuth;
import com.lejian.oldman.service.LocationService;
import com.lejian.oldman.service.OldmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@BackAdminAuth
@Controller
@ResponseBody
@RequestMapping("/location")
public class LocationController {


    @Autowired
    private LocationService service;

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


    @RequestMapping("/getLocationByPage")
    public GetLocationListResponse getLocationByPage(@RequestBody GetLocationByPageRequest request){
        GetLocationListResponse response=new GetLocationListResponse();
        response.setLocationVoList(service.getLocationByPage(request.getPageParam().getPageNo(),request.getPageParam().getPageSize(),request.getLocationSearchParam()));
        if (request.getNeedCount()) {
            response.setCount(service.getCount(request.getLocationSearchParam()));
        }
        return response;
    }

    @RequestMapping("/getById")
    public GetLocationListResponse getLocationByPage(@RequestBody IdRequest request){
        GetLocationListResponse response=new GetLocationListResponse();
        response.setLocationVoList(Lists.newArrayList(service.getById(request.getId())));
        return response;
    }



    @BackUserAuth
    @ResponseBody
    @RequestMapping("/add")
    public ResultResponse add(@RequestBody SaveLocationRequest request){
        ResultResponse response=new ResultResponse();
        service.add(request.getLocationParam());
        return response;
    }


    @BackUserAuth
    @ResponseBody
    @RequestMapping("/edit")
    public ResultResponse edit(@RequestBody SaveLocationRequest request){
        ResultResponse response=new ResultResponse();
        service.edit(request.getLocationParam());
        return response;
    }
}
