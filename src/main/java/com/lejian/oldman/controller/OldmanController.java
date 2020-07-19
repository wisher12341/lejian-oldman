package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.controller.contract.response.GetOldmanByPageResponse;
import com.lejian.oldman.controller.contract.response.GetOldmanListResponse;
import com.lejian.oldman.controller.contract.response.GetOldmanResponse;
import com.lejian.oldman.controller.contract.response.ResultResponse;
import com.lejian.oldman.service.OldmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@RequestMapping("/oldman")
public class OldmanController {


    @Autowired
    private OldmanService oldmanService;

    /**
     * 老人列表分页查询
     * @param request
     * @return
     */
    @RequestMapping("/getOldmanByPage")
    public GetOldmanByPageResponse getOldmanByPage(@RequestBody GetOldmanByPageRequest request){
        GetOldmanByPageResponse response = new GetOldmanByPageResponse();
        PageParam pageParam = request.getPageParam();
        response.setOldmanVoList(oldmanService.getOldmanByPage(pageParam.getPageNo(),pageParam.getPageSize(),request.getOldmanSearchParam()));
        response.setCount(oldmanService.getOldmanCount(request.getOldmanSearchParam()));
        return response;
    }


    /**
     * 根据locationid 获取全都老人
     * @param request
     * @return
     */
    @RequestMapping("/getOldmanByLocationId")
    public GetOldmanListResponse getOldmanByLocationId(@RequestBody GetOldmanByLocationIdRequest request){
        GetOldmanListResponse response = new GetOldmanListResponse();
        response.setOldmanVoList(oldmanService.getOldmanByLocationId(request.getLocationId()));
        return response;
    }

    /**
     * oid
     * @param request
     * @return
     */
    @RequestMapping("/getOldmanByOid")
    public GetOldmanResponse getOldmanByOid(@RequestBody GetOldmanByOidRequest request){
        GetOldmanResponse response = new GetOldmanResponse();
        response.setOldmanVo(oldmanService.getOldmanByOid(request.getOid()));
        return response;
    }

    /**
     * 模糊姓名查询
     * @param request
     * @return
     */
    @RequestMapping("/getOldmanByFuzzyName")
    public GetOldmanListResponse getOldmanByFuzzyName(@RequestBody GetOldmanByFuzzNameRequest request){
        GetOldmanListResponse response = new GetOldmanListResponse();
        response.setOldmanVoList(oldmanService.getOldmanByFuzzyName(request.getValue()));
        return response;
    }

    /**
     * 根据姓名查询
     * @param request
     * @return
     */
    @RequestMapping("/getOldmanByName")
    public GetOldmanResponse getOldmanByName(@RequestBody GetOldmanByNameRequest request){
        GetOldmanResponse response = new GetOldmanResponse();
        response.setOldmanVo(oldmanService.getOldmanByName(request.getName()));
        return response;
    }

    /**
     * 关怀系统紧急报警 根据老人姓名（最好录入不能重名）
     */
    @GetMapping("/sensorUrgency")
    public ResultResponse sensorUrgency(@RequestParam("name") String oldmanName){
        oldmanService.sensorUrgency(oldmanName);
        return new ResultResponse();
    }

}
