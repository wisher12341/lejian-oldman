package com.lejian.oldman.controller.contract.response;

import lombok.Data;

import java.util.Map;

@Data
public class GetEnumResponse {
    Map<Integer,String> sex;
    Map<Integer,String> politics;
    Map<Integer,String> education;
    Map<Integer,String> householdType;
    Map<Integer,String> family;
    Map<Integer,String> workerType;
    Map<Integer,String> roleType;
}
