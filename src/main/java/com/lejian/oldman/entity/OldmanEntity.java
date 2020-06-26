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
    @Column(name = "id_card")
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
    @Column(name = "residence_address")
    private String residenceAddress;
    @Column(name = "politics_status")
    private Integer politicsStatus;
    @Column(name = "household_type")
    private Integer householdType;
    @Column(name = "pic_url")
    private String picUrl;
    @Column
    private Integer education;
    @Column(name = "nonelevator_floor")
    private Integer nonelevatorFloor;
    @Column
    private String tags;
    @Column(name = "location_id")
    private Integer locationId;
    @Column
    private Integer status;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "datachange_time")
    private Timestamp datachangeTime;
}
