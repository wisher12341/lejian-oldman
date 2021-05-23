package com.lejian.oldman.controller;


import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.controller.contract.response.GetOldmanByPageResponse;
import com.lejian.oldman.controller.contract.response.GetRzzResponse;
import com.lejian.oldman.controller.contract.response.ResultResponse;
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

    @BackUserAuth
    @ResponseBody
    @RequestMapping("/get")
    public GetRzzResponse get(@RequestBody RzzSearchParam rzzSearchParam){
        GetRzzResponse response = new GetRzzResponse();
        response.setRzzVoList(service.get(rzzSearchParam));
        return response;
    }


    /**
     * 添加
     * @return
     */
    @BackUserAuth
    @ResponseBody
    @RequestMapping("/add")
    public ResultResponse add(@RequestBody SaveRzzRequest request){
        ResultResponse response=new ResultResponse();
        service.add(request.getRzzParam());
        return response;
    }

    /**
     * 编辑
     * @return
     */
    @BackUserAuth
    @ResponseBody
    @RequestMapping("/edit")
    public ResultResponse edit(@RequestBody SaveRzzRequest request){
        ResultResponse response=new ResultResponse();
        service.edit(request.getRzzParam());
        return response;
    }
}
