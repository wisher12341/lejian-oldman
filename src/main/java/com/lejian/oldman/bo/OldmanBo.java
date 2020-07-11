package com.lejian.oldman.bo;

import com.lejian.oldman.enums.OldmanEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class OldmanBo {
    private Integer id;
    private String oid;
    private OldmanEnum sex;
    private String name;
    private LocalDate birthday;
    private String phone;
    private String mary;
    private String country;
    private String idCard;
    private String district;
    private String street;
    private String area;
    private String council;
    private String address;
    private String residenceAddress;
    private OldmanEnum politics;
    private OldmanEnum householdType;
    private String picUrl;
    private OldmanEnum education;
    private Integer nonelevatorFloor;
    private List<String> tags;
    private Integer locationId;
    private OldmanEnum status;
}
