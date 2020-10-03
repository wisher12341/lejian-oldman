package com.lejian.oldman.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WorkerVo {
    private Integer id;
    private String sex;
    private String name;
    private LocalDate birthday;
    private Integer age;
    private String nation;
    private String country;
    private String idCard;
    private String birthplace;
    private String address;
    private String picUrl;
    private String education;
    private String phone;
    private String mary;
    private String retiredStatus;
    private String reserve;
    private String housekeepingInsurance;
    private Double salary;
    private String organName;
    private List<Position> positionList;
    private String qq;
    private String type;
    private String beyond;


    @Data
    @AllArgsConstructor
    public static class Position{
        private String lng;
        private String lat;
        private String time;
    }
}
