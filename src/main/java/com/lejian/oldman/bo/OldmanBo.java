package com.lejian.oldman.bo;

import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.utils.DateUtils;
import com.lejian.oldman.vo.OldmanVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static com.lejian.oldman.utils.DateUtils.YYMMDD;

@Data
public class OldmanBo {
    private Integer id;
    private String oid;
    private BusinessEnum sexEnum;
    private Integer sex;
    private String name;
    private LocalDate birthday;
    private String phone;
    private String mary;
    private String country;
    private String idCard;
    private String address;
    private String residenceAddress;
    private BusinessEnum politicsEnum;
    private Integer politics;
    private BusinessEnum householdTypeEnum;
    private Integer householdType;
    private String picUrl;
    private BusinessEnum educationEnum;
    private Integer education;
    private Integer nonelevatorFloor;
    private List<String> tags;
    private Integer locationId;
    private BusinessEnum statusEnum;
    private Integer status;
    private Integer careGatewayId;
    private Integer cameraId;
    private Integer xjbId;
    private Timestamp datachangeTime;
    private BusinessEnum familyEnum;
    private Integer family;
    private String areaCountry;
    private String areaTown;
    private String areaVillage;
    private String areaCustomOne;
    private BusinessEnum serviceTypeEnum;
    private Integer serviceType;

    private String locationAddress;

    private String lng;
    private String lat;

    public static OldmanVo createVo(OldmanBo oldmanBo){
        OldmanVo oldmanVo = new OldmanVo();
        BeanUtils.copyProperties(oldmanBo,oldmanVo);
        oldmanVo.setBirthday(oldmanBo.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        oldmanVo.setAge(DateUtils.birthdayToAge(oldmanBo.getBirthday()));
        oldmanVo.setZodiac(DateUtils.getZodiac(oldmanBo.getBirthday()));
        oldmanVo.setConstellation(DateUtils.getConstellation(oldmanBo.getBirthday()));
        oldmanVo.setStatus(oldmanBo.getStatusEnum().getDesc());
        oldmanVo.setSex(oldmanBo.getSexEnum().getDesc());
        oldmanVo.setPolitics(oldmanBo.getPoliticsEnum().getDesc());
        oldmanVo.setEducation(oldmanBo.getEducationEnum().getDesc());
        oldmanVo.setHouseholdType(oldmanBo.getHouseholdTypeEnum().getDesc());
        oldmanVo.setFamily(oldmanBo.getFamilyEnum().getDesc());
        return oldmanVo;
    }
}
