package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.controller.contract.response.GetVisualSettingByPageResponse;
import com.lejian.oldman.controller.contract.response.GetVisualSettingResponse;
import com.lejian.oldman.controller.contract.response.ResultResponse;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.security.annotation.BackUserAuth;
import com.lejian.oldman.service.VisualSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@BackUserAuth
@Controller
@ResponseBody
@RequestMapping("/visual/setting")
public class VisualSettingController {


    @Autowired
    private VisualSettingService service;

    @RequestMapping("/getByPage")
    public GetVisualSettingByPageResponse getByPage(@RequestBody GetVisualSettingByPageRequest request){
        GetVisualSettingByPageResponse response = new GetVisualSettingByPageResponse();
        response.setVisualSettingVoList(service.getByPage(request.getPageParam()));
        response.setCount(service.getCount());
        return response;
    }


    @RequestMapping("/getById")
    public GetVisualSettingResponse getById(@RequestBody GetByIdRequest request){
        GetVisualSettingResponse response = new GetVisualSettingResponse();
        response.setVisualSettingVo(service.getById(request.getId()));
        return response;
    }

    @RequestMapping("/add")
    public ResultResponse add(@RequestBody SaveVisualSettingRequest request){
        service.add(request.getParam());
        return new ResultResponse();
    }

    @RequestMapping("/edit")
    public ResultResponse edit(@RequestBody SaveVisualSettingRequest request){
        service.edit(request.getParam());
        return new ResultResponse();
    }

    @RequestMapping("/delete")
    public ResultResponse delete(@RequestBody DeleteUserByIdRequest request){
        service.delete(request.getId());
        return new ResultResponse();
    }

    @RequestMapping("/select")
    public ResultResponse select(@RequestBody IdRequest request){
        service.select(request.getId());
        return new ResultResponse();
    }
}
