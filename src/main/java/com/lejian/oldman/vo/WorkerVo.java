package com.lejian.oldman.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WorkerVo {
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
    private List<Position> positionList;


    @Data
    @AllArgsConstructor
    public static class Position{
        private String lng;
        private String lat;
        private String time;
    }
}
