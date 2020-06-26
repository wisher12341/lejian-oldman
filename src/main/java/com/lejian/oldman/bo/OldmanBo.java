package com.lejian.oldman.bo;

import com.lejian.oldman.enums.OldmanStatusEnum;
import lombok.Data;
import java.util.List;

@Data
public class OldmanBo {
    private String oid;
    private Integer sex;
    private String name;
    private String birthday;
    private String zodiac;
    private String constellation;
    private String phone;
    private Integer mary;
    private String country;
    private String idCard;
    private String district;
    private String street;
    private String area;
    private String council;
    private String address;
    private String residenceAddress;
    private Integer politicsStatus;
    private Integer householdType;
    private String picUrl;
    private Integer education;
    private Integer nonelevatorFloor;
    private List<String> tags;
    private Integer locationId;
    private OldmanStatusEnum status;
}
