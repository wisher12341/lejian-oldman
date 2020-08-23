package com.lejian.oldman.controller.contract.response;

import lombok.Data;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

@Data
public class GetMainSecondAllCountResponse {
    private Map<String,Map<String,Long>> sMap;
    private Map<String,Long> fMap;
    private Long sum=0L;


    public void sum() {
        if(MapUtils.isNotEmpty(fMap)){
            fMap.forEach((k,v)-> sum+=v);
        }
        if(MapUtils.isNotEmpty(sMap)){
            sMap.forEach((k,v)-> v.forEach((k2, v2)->sum+=v2));
        }
    }

}
