package com.lejian.oldman.controller.contract.response;

import lombok.Data;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

@Data
public class GetMainOldmanDataResponse {

    private Map<String,Long> sexMap;
    private Map<String,Long> ageMap;
    private Map<String,Long> hjMap;
    private Map<String,Long> jtMap;
}
