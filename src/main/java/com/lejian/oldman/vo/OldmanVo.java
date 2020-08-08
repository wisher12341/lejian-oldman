package com.lejian.oldman.vo;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class OldmanVo {
    private String oid;
    private String sex;
    private String name;
    private String birthday;
    private Integer age;
    private String zodiac;
    private String constellation;
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
    private String politics;
    private String householdType;
    private String picUrl;
    private String education;
    private Integer nonelevatorFloor;
    private String tags;
    private Integer locationId;
    private String status;
    private String careGatewayId;
    private Timestamp datachangeTime;
    private String family;

}
