package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetOldmanAreaGroupCountRequest {
    private String areaCountry;
    private String areaTown;
    private String areaVillage;
}
