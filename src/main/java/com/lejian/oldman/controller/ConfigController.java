package com.lejian.oldman.controller;

import com.lejian.oldman.bussiness.config.VarConfig;
import com.lejian.oldman.controller.contract.request.MapRequest;
import com.lejian.oldman.controller.contract.response.MapResponse;
import com.lejian.oldman.controller.contract.response.ResultResponse;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 配置数据管理
 */
@BackAdminAuth
@Controller
@ResponseBody
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;
    @Autowired
    private VarConfig varConfig;

    /**
     * 获取主页配置数据
     */
    @RequestMapping("/saveVarConfigData")
    public ResultResponse saveVarConfigData(@RequestBody MapRequest request){
        varConfig.saveVarConfigData(request.getMap());
        return new ResultResponse();
    }


    /**
     * 获取主页配置数据
     */
    @RequestMapping("/getMainConfigData")
    public MapResponse getMainConfigData(){
        MapResponse response=new MapResponse();
        response.setMap(varConfig.getMainConfigData());
        return response;
    }
}
