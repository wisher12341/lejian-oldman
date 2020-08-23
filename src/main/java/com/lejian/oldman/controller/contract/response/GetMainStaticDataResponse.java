package com.lejian.oldman.controller.contract.response;

import lombok.Data;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

@Data
public class GetMainStaticDataResponse {

    private Long workerCount=0L;
    private Long homeServiceCount=0L;
    private Long equipCount=0L;
    private Long birthdayCount=0L;
    private Map<String,Long> workerMap;
    private Map<String,Long> equipMap;
    private Map<String,Long> homeServiceMap;


    public void sum() {
        if(MapUtils.isNotEmpty(workerMap)){
            workerMap.forEach((k,v)-> workerCount+=v);
        }
        if(MapUtils.isNotEmpty(homeServiceMap)){
            homeServiceMap.forEach((k,v)-> homeServiceCount+=v);
        }
    }
}
