package com.lejian.oldman.controller.contract.request;

import lombok.Data;

import java.util.Map;

@Data
public class MapRequest {
    private Map<String,String> map;
}
