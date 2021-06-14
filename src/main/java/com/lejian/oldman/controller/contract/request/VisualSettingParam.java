package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class VisualSettingParam {
    private Integer id;
    private String lat;
    private String lng;
    private String areaCountry="";
    private String areaTown="";
    private String areaVillage="";
    private Boolean isUsed;
}
