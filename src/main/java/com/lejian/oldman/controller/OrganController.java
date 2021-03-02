package com.lejian.oldman.controller;

import com.google.common.collect.Maps;
import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.controller.contract.response.*;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.service.OrganService;
import com.lejian.oldman.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@BackAdminAuth
@Controller
@ResponseBody
@RequestMapping("/organ")
public class OrganController {

    @Autowired
    private OrganService organService;

    @RequestMapping("/getByPage")
    public GetOrganByPageResponse getUserByPage(@RequestBody GetOrganByPageRequest request){
        GetOrganByPageResponse response = new GetOrganByPageResponse();
        response.setOrganVoList(organService.getByPage(request.getPageParam()));
        if (request.getPageParam().getNeedCount()) {
            response.setCount(organService.getCount());
        }
        return response;
    }


    /**
     * 获取各服务类型的统计数据
     */
    @RequestMapping("/getServiceTypeCount")
    public MapResponse getServiceTypeCount(@RequestBody IdRequest request){
        MapResponse response = new MapResponse();
        response.setMap(organService.getServiceTypeCount(request.getId()));
        return response;
    }

    /**
     * 获取各服务类型的统计数据
     */
    @RequestMapping("/getServiceCount")
    public MapResponse getServiceCount(){
        MapResponse response = new MapResponse();
        Map<String,String> map = Maps.newHashMap();
        map.put("count", String.valueOf(organService.getServiceCount()));
        response.setMap(map);
        return response;
    }

//    @RequestMapping("/add")
//    public ResultResponse add(@RequestBody SaveUserRequest request){
//        userService.add(request.getUserParam());
//        return new ResultResponse();
//    }
//
//    @RequestMapping("/edit")
//    public ResultResponse edit(@RequestBody SaveUserRequest request){
//        userService.edit(request.getUserParam());
//        return new ResultResponse();
//    }
//
//    @RequestMapping("/delete")
//    public ResultResponse delete(@RequestBody DeleteUserByIdRequest request){
//        userService.delete(request.getId());
//        return new ResultResponse();
//    }

}
