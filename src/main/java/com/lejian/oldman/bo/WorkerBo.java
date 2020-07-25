package com.lejian.oldman.bo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkerBo {
    private Integer id;
    private Integer sex;
    private String name;
    private LocalDate birthday;
    private String nation;
    private String country;
    private String idCard;
    private String birthplaceProvince;
    private String birthplaceCity;
    private String address;
    private String picUrl;
    private Integer education;
    private String phone;
    private String mary;
    private Integer retiredStatus;
    private Integer reserve;
    private String housekeepingInsurance;
    private Double salary;
    private Integer organId;
    private Integer star;
    private Integer userId;
    private String qq;
}
