package com.lejian.oldman.controller;

import com.google.common.collect.Maps;
import com.lejian.oldman.controller.contract.response.GetEnumResponse;
import com.lejian.oldman.controller.contract.response.MapResponse;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.enums.UserEnum;
import com.lejian.oldman.enums.WorkerEnum;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.security.annotation.BackUserAuth;
import com.lejian.oldman.service.EnumService;
import com.lejian.oldman.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.stream.Stream;

/**
 * 获取系统定义的枚举值
 */
@BackUserAuth
@Controller
@ResponseBody
@RequestMapping("/enum")
public class EnumController {

    @Autowired
    private EnumService service;

    @RequestMapping("/oldmanAdd")
    public GetEnumResponse oldmanAdd(){
        GetEnumResponse response=new GetEnumResponse();
        response.setSex(CollectionUtils.enumToMap(OldmanEnum.Sex.values()));
        response.setPolitics(CollectionUtils.enumToMap(OldmanEnum.Politics.values()));
        response.setEducation(CollectionUtils.enumToMap(OldmanEnum.Education.values()));
        response.setFamily(CollectionUtils.enumToMap(OldmanEnum.FamilyType.values()));
        response.setHouseholdType(CollectionUtils.enumToMap(OldmanEnum.HouseholdType.values()));
        response.setServiceStatus(CollectionUtils.enumToMap(OldmanEnum.ServiceStatus.values()));
        return response;
    }

    @RequestMapping("/workerAdd")
    public GetEnumResponse workerAdd(){
        GetEnumResponse response=new GetEnumResponse();
        response.setWorkerType(CollectionUtils.enumToMap(WorkerEnum.Type.values()));
        response.setSex(CollectionUtils.enumToMap(OldmanEnum.Sex.values()));
        response.setEducation(CollectionUtils.enumToMap(OldmanEnum.Education.values()));
        return response;
    }

    @RequestMapping("/rzzAdd")
    public MapResponse rzzAdd(){
        MapResponse response=new MapResponse();
        Map<Integer,String> map = CollectionUtils.enumToMap(OldmanEnum.RzzType.values());
        Map<String,String> result = Maps.newHashMap();
        map.forEach((k,v)->{
            if (k!=0){
                result.put(String.valueOf(k),v);
            }
        });
        response.setMap(result);
        return response;
    }

    @RequestMapping("/user")
    public GetEnumResponse user(){
        GetEnumResponse response=new GetEnumResponse();
        response.setRoleType(CollectionUtils.enumToMap(UserEnum.Role.values()));
        return response;
    }

}
