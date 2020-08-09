package com.lejian.oldman.controller.contract.response;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

@Data
public class GetCountResponse {
    private Map<String,Long> countMap;
    private Long sumCount=0L;

    public void sum() {
        if(MapUtils.isNotEmpty(countMap)){
            countMap.forEach((k,v)-> sumCount+=v);
        }
    }
}
