package com.lejian.oldman.controller;


import com.lejian.oldman.controller.contract.request.GetOldmanByPageRequest;
import com.lejian.oldman.controller.contract.request.GetRzzByPageRequest;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.controller.contract.response.GetOldmanByPageResponse;
import com.lejian.oldman.controller.contract.response.GetRzzResponse;
import com.lejian.oldman.handler.ExcelHandler;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.security.annotation.BackUserAuth;
import com.lejian.oldman.service.RzzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@BackAdminAuth
@Controller
@ResponseBody
@RequestMapping("/rzz")
public class RzzController {


    @Autowired
    private RzzService service;

    @BackUserAuth
    @ResponseBody
    @RequestMapping("/getByPage")
    public GetRzzResponse getByPage(@RequestBody GetRzzByPageRequest request){
        GetRzzResponse response = new GetRzzResponse();
        PageParam pageParam = request.getPageParam();
        response.setRzzVoList(service.getByPage(pageParam.getPageNo(),pageParam.getPageSize(),request.getRzzSearchParam()));
        response.setCount(service.getCount(request.getRzzSearchParam()));
        return response;
    }



}
