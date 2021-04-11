package com.lejian.oldman.controller.contract.request;

import com.lejian.oldman.bo.JpaSpecBo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;


@Data
public class LocationSearchParam {
    private String desc;

    public static JpaSpecBo convert(LocationSearchParam locationSearchParam){
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        if(locationSearchParam == null){
            return jpaSpecBo;
        }
        if(StringUtils.isNotBlank(locationSearchParam.getDesc())){
            jpaSpecBo.getLikeMap().put("desc","%"+locationSearchParam.getDesc()+"%");
        }
        return jpaSpecBo;
    }
}
