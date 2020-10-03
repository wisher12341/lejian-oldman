package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.response.GetEnumResponse;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.enums.WorkerEnum;
import com.lejian.oldman.service.EnumService;
import com.lejian.oldman.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 获取系统定义的枚举值
 */

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

}
