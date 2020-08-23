package com.lejian.oldman.entity;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@DynamicInsert
@DynamicUpdate
@Data
@Entity
@Table(name = "worker")
public class WorkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer sex;
    @Column
    private String name;
    @Column
    private LocalDate birthday;
    @Column
    private String nation;
    @Column
    private String country;
    @Column(name = "id_card")
    private String idCard;
    @Column(name = "birthplace_province")
    private String birthplaceProvince;
    @Column(name = "birthplace_city")
    private String birthplaceCity;
    @Column
    private String address;
    @Column(name = "pic_url")
    private String picUrl;
    @Column
    private Integer education;
    @Column
    private String phone;
    @Column
    private String mary;
    @Column(name = "retired_status")
    private Integer retiredStatus;
    @Column
    private Integer reserve;
    @Column(name = "housekeeping_insurance")
    private String housekeepingInsurance;
    @Column
    private Double salary;
    @Column(name = "organ_id")
    private Integer organId;
    @Column
    private Integer star;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "datachange_time")
    private Timestamp datachangeTime;
    @Column
    private String qq;
    @Column
    private Integer type;
}
