package com.lejian.oldman.controller.contract.response;

import lombok.Data;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

@Data
public class GetMainSencondAllCountResponse {
    private Map<String,Long> equipMap;
    private Long equipCount=0L;

    private Map<String,Long> homeServiceMap;
    private Long homeServiceCount=0L;
    private Map<String,Long> warnMap;
    private Long warnCount=0L;
    private Map<String,Long> workerMap;
    private Long workerCount=0L;


    public void sum() {
        if(MapUtils.isNotEmpty(equipMap)){
            equipMap.forEach((k,v)-> equipCount+=v);
        }
        if(MapUtils.isNotEmpty(homeServiceMap)){
            homeServiceMap.forEach((k,v)-> homeServiceCount+=v);
        }
        if(MapUtils.isNotEmpty(warnMap)){
            warnMap.forEach((k,v)-> warnCount+=v);
        }
        if(MapUtils.isNotEmpty(workerMap)){
            workerMap.forEach((k,v)-> workerCount+=v);
        }
    }

}
