package com.lejian.oldman.controller.contract.request;

import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
public class OldmanSearchParam {
    private String name;
    private String idCard;
    private Integer serviceStatus;
    private String createTimeStart;
    private String createTimeEnd;
    private String oid;
    private String areaCustomOne;
    private String areaCountry;
    private String areaTown;
    private String areaVillage;
    private Integer status;
    private String birthdayLike;
    private String sex;
    private String age;
    private String householdType;
    private String familyType;
    /**
     * 是否过滤 有智能设备
     */
    private Boolean equip;
    /**
     * 是否过滤 有养老服务
     */
    private Boolean homeService;
    /**
     * 居家养老服务类型
     */
    private Integer serviceType;

    /**
     * 智能设备类型
     * 传entity 属性 名
     */
    private String equipType;

    public static JpaSpecBo convert(OldmanSearchParam oldmanSearchParam){
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        jpaSpecBo.getEqualMap().put("isDelete",0);
        if(oldmanSearchParam == null){
            return jpaSpecBo;
        }

        if(StringUtils.isNotBlank(oldmanSearchParam.getOid())) {
            jpaSpecBo.getEqualMap().put("oid", oldmanSearchParam.getOid());
        }
        if(StringUtils.isNotBlank(oldmanSearchParam.getAreaCustomOne())){
            jpaSpecBo.getEqualMap().put("areaCustomOne", oldmanSearchParam.getAreaCustomOne());
        }
        if (StringUtils.isNotBlank(oldmanSearchParam.getAreaVillage())){
            jpaSpecBo.getEqualMap().put("areaVillage", oldmanSearchParam.getAreaVillage());
        }
        if (StringUtils.isNotBlank(oldmanSearchParam.getAreaTown())){
            jpaSpecBo.getEqualMap().put("areaTown", oldmanSearchParam.getAreaTown());
        }
        if (StringUtils.isNotBlank(oldmanSearchParam.getAreaCountry())){
            jpaSpecBo.getEqualMap().put("areaCountry", oldmanSearchParam.getAreaCountry());
        }
        if (StringUtils.isNotBlank(oldmanSearchParam.getName())){
            jpaSpecBo.getEqualMap().put("name", oldmanSearchParam.getName());
        }

        if (StringUtils.isNotBlank(oldmanSearchParam.getIdCard())){
            jpaSpecBo.getEqualMap().put("idCard", oldmanSearchParam.getIdCard());
        }

        if (oldmanSearchParam.getServiceStatus()!=null){
            jpaSpecBo.getEqualMap().put("serviceStatus", oldmanSearchParam.getServiceStatus());
        }

        if (StringUtils.isNotBlank(oldmanSearchParam.getCreateTimeStart())){
            jpaSpecBo.getGreatEMap().put("createTime", Timestamp.valueOf(oldmanSearchParam.getCreateTimeStart()));
        }

        if (StringUtils.isNotBlank(oldmanSearchParam.getCreateTimeEnd())){
            jpaSpecBo.getLessEMap().put("createTime", Timestamp.valueOf(oldmanSearchParam.getCreateTimeEnd()));
        }


        if(StringUtils.isNotBlank(oldmanSearchParam.getBirthdayLike())){
            jpaSpecBo.getLikeMap().put("birthday","%"+oldmanSearchParam.getBirthdayLike()+"%s");
        }
        if(oldmanSearchParam.getStatus()!=null){
            jpaSpecBo.getEqualMap().put("status",oldmanSearchParam.getStatus());
        }
        if(oldmanSearchParam.getEquip()!=null && oldmanSearchParam.getEquip()){
            jpaSpecBo.getOrNotEquipMap().put("careGatewayId",0);
            jpaSpecBo.getOrNotEquipMap().put("cameraId",0);
            jpaSpecBo.getOrNotEquipMap().put("xjbId",0);
        }
        if (oldmanSearchParam.getHomeService()!=null && oldmanSearchParam.getHomeService()){
            jpaSpecBo.getGreatMap().put("serviceType",0);
        }
        if (oldmanSearchParam.getServiceType()!=null){
            jpaSpecBo.getInMap().put("serviceType",((OldmanEnum.ServiceType)BusinessEnum.find(oldmanSearchParam.getServiceType(),OldmanEnum.ServiceType.class)).getSearchValue().stream().map(item-> (Object)item).collect(Collectors.toList()));
        }
        if(StringUtils.isNotBlank(oldmanSearchParam.getEquipType())){
            jpaSpecBo.getOrNotEquipMap().put(oldmanSearchParam.getEquipType(),0);
        }
        if(StringUtils.isNotBlank(oldmanSearchParam.getSex())){
            jpaSpecBo.getEqualMap().put("sex", BusinessEnum.find(oldmanSearchParam.getSex(), OldmanEnum.Sex.class).getValue());
        }
        if(StringUtils.isNotBlank(oldmanSearchParam.getHouseholdType())){
            jpaSpecBo.getEqualMap().put("householdType", BusinessEnum.find(oldmanSearchParam.getHouseholdType(), OldmanEnum.HouseholdType.class).getValue());
        }
        if(StringUtils.isNotBlank(oldmanSearchParam.getFamilyType())){
            jpaSpecBo.getEqualMap().put("family", BusinessEnum.find(oldmanSearchParam.getFamilyType(), OldmanEnum.FamilyType.class).getValue());
        }
        if(StringUtils.isNotBlank(oldmanSearchParam.getAge())){
            String start=oldmanSearchParam.getAge().split("-")[0];
            String end="";
            if(oldmanSearchParam.getAge().split("-").length>1) {
                end = oldmanSearchParam.getAge().split("-")[1];
            }
            jpaSpecBo.getLessEMap().put("birthday", LocalDateTime.now().minusYears(Integer.valueOf(start)).toLocalDate());
            if(StringUtils.isNotBlank(end)) {
                jpaSpecBo.getGreatEMap().put("birthday", LocalDateTime.now().minusYears(Integer.valueOf(end)).toLocalDate());
            }
        }
        return jpaSpecBo;
    }

    public String getSql() {
        return getSql("");
    }

    public String getSql(String type) {
        String where="";
        if(StringUtils.isNotBlank(getAreaCustomOne())){
            where+=" "+type+"area_custom_one='"+getAreaCustomOne()+"' and ";
        }
        if (StringUtils.isNotBlank(getAreaVillage())){
            where+=" "+type+"area_village='"+getAreaVillage()+"' and ";
        }
        if (StringUtils.isNotBlank(getAreaTown())){
            where+=" "+type+"area_town='"+getAreaTown()+"' and ";
        }
        if (StringUtils.isNotBlank(getAreaCountry())){
            where+=" "+type+"area_country='"+getAreaCountry()+"' and ";
        }

        where+=" "+type+"is_delete=0 ";

        return where;
    }
}
