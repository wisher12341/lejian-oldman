package com.lejian.oldman.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "oldman")
public class OldmanEntity {

    @Id
    private Long id;
    @Column
    private String oid;
    @Column
    private Integer sex;
    @Column
    private String name;
    @Column
    private Date birthday;
    @Column
    private String zodiac;
    @Column
    private String constellation;
    @Column
    private String phone;
    @Column
    private Integer mary;
    @Column
    private String country;
    @Column
    private String idCard;
    @Column
    private String district;
    @Column
    private String street;
    @Column
    private String area;
    @Column
    private String council;
    @Column
    private String address;
    @Column
    private String residenceAddress;
    @Column
    private Integer politicsStatus;
    @Column
    private Integer householdType;
    @Column
    private String picUrl;
    @Column
    private Integer education;
    @Column
    private Integer nonelevatorFloor;
    @Column
    private String tags;
    @Column
    private Timestamp createTime;
    @Column
    private Timestamp datachangeTime;
}
