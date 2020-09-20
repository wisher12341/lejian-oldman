package com.lejian.oldman.controller.contract.request;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class OldmanParam {
    private String oid;
    private Integer sex;
    private String name;
    private String phone;
    private String mary;
    private String country;
    private String idCard;
    private String areaCountry;
    private String areaTown;
    private String areaVillage;
    private String areaCustomOne;
    private String address;
    private String residenceAddress;
    private Integer politics;
    private Integer householdType;
    private String picUrl;
    private Integer education;
    private Integer nonelevatorFloor;
    private Integer locationId;
    private Integer careGatewayId;
    private Integer cameraId;
    private Integer xjbId;
    private Integer family;
//    private String serviceType;

}
