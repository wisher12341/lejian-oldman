package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.controller.contract.response.*;
import com.lejian.oldman.service.OldmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

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
        if(request.getNeedCount()) {
            response.setCount(oldmanService.getOldmanCount(request.getOldmanSearchParam()));
        }
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
     * 关怀系统紧急报警 根据长者关怀系统 的老人oid（最好录入不能重名）
     * @param gatewayId 长者关怀系统 的老人 网关id
     * @param type
     * @param content
     */
    @GetMapping("/alarm")
    public ResultResponse sensorUrgency(Integer gatewayId,String type,String content) throws UnsupportedEncodingException {
        oldmanService.alarm(gatewayId,type,content);
        return new ResultResponse();
    }

    /**
     * 获取老人表 符合某个属性的数量
     * @param request
     * @return
     */
    @RequestMapping("/getCountOfOldmanField")
    public GetCountResponse getCountOfOldmanField(GetCountOfOldmanFieldRequest request){
        GetCountResponse response = new GetCountResponse();
        return response;
    }


    /**
     * 获取 根据区域分组的 各数量
     * @return
     */
    @RequestMapping("/getOldmanGroupCount")
    public GetCountResponse getOldmanGroupCount(@RequestBody GetGroupCountRequest request){
        GetCountResponse response= new GetCountResponse();
        response.setCountMap(oldmanService.getGroupCount(request.getGroupFieldName()));
        response.sum();
        response.sortByKey(true);
        return response;
    }

    /**
     * 根据location id更新老人状态
     */
    @RequestMapping("/updateStatusByLocationId")
    public ResultResponse updateStatusByLocationId(@RequestBody UpdateStatusByLocationIdRequest request){
        oldmanService.updateStatusByLocationId(request.getLocationId(),request.getStatus());
        return new ResultResponse();
    }

    @RequestMapping("/getBirthdayOldman")
    public GetOldmanListResponse getBirthdayOldman(@RequestBody GetBirthdayOldmanRequest request){
        GetOldmanListResponse response=new GetOldmanListResponse();
        response.setOldmanVoList(oldmanService.getBirthdayOldman(request.getDate()));
        return response;
    }

    /**
     * 添加老人
     * @return
     */
    @RequestMapping("/add")
    public ResultResponse add(@RequestBody AddOldmanRequest request){
        ResultResponse response=new ResultResponse();
        oldmanService.addOldman(request.getOldmanParam());
        return response;
    }
}
