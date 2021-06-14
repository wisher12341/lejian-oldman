package com.lejian.oldman.bo;

import com.lejian.oldman.enums.BusinessEnum;
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
    private String birthplace;
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
    private String beyond;
    private Integer type;
    private Integer addUserId;


    /**
     * 当前排单的 id
     */
    private Integer currentDispatchId;
}
