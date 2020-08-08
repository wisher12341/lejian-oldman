package com.lejian.oldman.controller.contract.request;

import com.lejian.oldman.bo.JpaSpecBo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class OldmanSearchParam {
    private String oid;
    private String areaCustomOne;
    private String birthdayLike;
    /**
     * 是否过滤 有智能设备
     */
    private Boolean equip;


    public static JpaSpecBo convert(OldmanSearchParam oldmanSearchParam){
        if(oldmanSearchParam == null){
            return null;
        }
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        if(StringUtils.isNotBlank(oldmanSearchParam.getOid())) {
            jpaSpecBo.getEqualMap().put("oid", oldmanSearchParam.getOid());
        }
        if(StringUtils.isNotBlank(oldmanSearchParam.getAreaCustomOne())){
            jpaSpecBo.getEqualMap().put("areaCustomOne", oldmanSearchParam.getAreaCustomOne());
        }
        if(StringUtils.isNotBlank(oldmanSearchParam.getBirthdayLike())){
            jpaSpecBo.getLikeMap().put("birthday",oldmanSearchParam.getBirthdayLike());
        }
        if(oldmanSearchParam.getEquip()!=null && oldmanSearchParam.getEquip()){
            jpaSpecBo.getOrNotEquipMap().put("careGatewayId",0);
            jpaSpecBo.getOrNotEquipMap().put("cameraId",0);
        }

        return jpaSpecBo;
    }

}
