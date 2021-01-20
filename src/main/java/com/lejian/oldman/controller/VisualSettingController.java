package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.GetVisualSettingByPageRequest;
import com.lejian.oldman.controller.contract.response.GetVisualSettingListResponse;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.service.VisualSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@BackAdminAuth
@Controller
@ResponseBody
@RequestMapping("/visual/setting")
public class VisualSettingController {


    @Autowired
    private VisualSettingService service;

    @RequestMapping("/getByPage")
    public GetVisualSettingListResponse getByPage(@RequestBody GetVisualSettingByPageRequest request){
        GetVisualSettingListResponse response = new GetVisualSettingListResponse();
        response.setVisualSettingVoList(service.getByPage(request.getPageParam()));
        return response;
    }


}
