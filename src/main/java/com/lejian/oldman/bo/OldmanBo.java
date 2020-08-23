package com.lejian.oldman.bo;

import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class OldmanBo {
    private Integer id;
    private String oid;
    private BusinessEnum sex;
    private String name;
    private LocalDate birthday;
    private String phone;
    private String mary;
    private String country;
    private String idCard;
    private String address;
    private String residenceAddress;
    private BusinessEnum politics;
    private BusinessEnum householdType;
    private String picUrl;
    private BusinessEnum education;
    private Integer nonelevatorFloor;
    private List<String> tags;
    private Integer locationId;
    private BusinessEnum status;
    private Integer careGatewayId;
    private Integer cameraId;
    private Integer xjbId;
    private Timestamp datachangeTime;
    private BusinessEnum family;
    private String areaCountry;
    private String areaTown;
    private String areaVillage;
    private String areaCustomOne;
    private BusinessEnum serviceType;
}
