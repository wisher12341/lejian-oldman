package com.lejian.oldman.controller;

import com.google.common.collect.Lists;
import com.lejian.oldman.check.bo.CheckResultBo;
import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.controller.contract.response.GetChxByPageResponse;
import com.lejian.oldman.controller.contract.response.GetUserByPageResponse;
import com.lejian.oldman.controller.contract.response.GetUserResponse;
import com.lejian.oldman.controller.contract.response.ResultResponse;
import com.lejian.oldman.handler.ExcelHandler;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.security.annotation.BackUserAuth;
import com.lejian.oldman.service.ChxService;
import com.lejian.oldman.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@BackUserAuth
@Controller
@ResponseBody
@RequestMapping("/chx")
public class ChxController {

    @Autowired
    private ChxService service;
    @Autowired
    private ExcelHandler excelHandler;


    @RequestMapping("/getChxByPage")
    public GetChxByPageResponse getChxByPage(@RequestBody GetChxByPageRequest request){
        GetChxByPageResponse response = new GetChxByPageResponse();
        response.setChxVoList(service.getChxByPage(request.getChxParam(),request.getPageParam()));
        response.setCount(service.getCount(request.getChxParam()));
        return response;
    }

    @RequestMapping("/getChxById")
    public GetChxByIdResponse getChxByPage(@RequestBody GetByIdRequest request){
        GetChxByIdResponse response = new GetChxByIdResponse();
        response.setChxVo(service.getChxById(request.getId()));
        return response;
    }


    @RequestMapping("/edit")
    public ResultResponse edit(@RequestBody SaveChxRequest request){
        service.edit(request.getChxParam());
        return new ResultResponse();
    }

    @RequestMapping("/delete")
    public ResultResponse delete(@RequestBody DeleteByIdRequest request){
        service.delete(request.getId());
        return new ResultResponse();
    }


    @RequestMapping(value = "/importExcel",method = RequestMethod.POST)
    public ModelAndView importExcel(@RequestParam MultipartFile file) {
        Pair<List<String>,List<List<String>>> excelData=excelHandler.parse(file,2);
        List<CheckResultBo> checkResultBoList= Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(excelData.getSecond())) {
            checkResultBoList=service.addChxByExcel(excelData);
        }
        ModelAndView mv=new ModelAndView("/chx");
        mv.addObject("check",checkResultBoList);
        return mv;
    }

    /**
     * 获取 当前到 截止的老人
     * @return
     */
    @RequestMapping(value = "/deadlineCount")
    public GetChxByPageResponse deadlineCount(){
        GetChxByPageResponse response=new GetChxByPageResponse();
        response.setCount(service.getDeadLineCount());
        return response;
    }
}
