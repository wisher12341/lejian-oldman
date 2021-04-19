package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class LocationParam {
    private Integer id;
    private String desc;
    private String positionX;
    private String positionY;
}
