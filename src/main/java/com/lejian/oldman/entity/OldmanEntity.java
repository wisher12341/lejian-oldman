package com.lejian.oldman.entity;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Data
@Entity
@Table(name = "oldman")
public class OldmanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String oid;
    @Column
    private Integer sex;
    @Column
    private String name;
    @Column
    private LocalDate birthday;
    @Column
    private String phone;
    @Column
    private String mary;
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
    @Column
    private Integer politics;
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
    @Column
    private String qq;
}
