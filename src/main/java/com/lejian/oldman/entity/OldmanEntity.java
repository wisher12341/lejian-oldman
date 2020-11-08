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
    @Column
    private Integer family;
    @Column(name = "id_card")
    private String idCard;
    @Column(name = "area_country")
    private String areaCountry;
    @Column(name = "area_town")
    private String areaTown;
    @Column(name = "area_village")
    private String areaVillage;
    @Column(name = "area_custom_one")
    private String areaCustomOne;
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
    @Column(name = "care_gateway_id")
    private Integer careGatewayId;
    @Column(name = "camera_id")
    private Integer cameraId;
    @Column(name = "xjb_id")
    private Integer xjbId;
    @Column(name = "service_type")
    private Integer serviceType;
    @Column(name = "service_status")
    private Integer serviceStatus;
}
