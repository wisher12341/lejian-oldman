package com.lejian.oldman.controller.contract.request;

import com.lejian.oldman.bo.JpaSpecBo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class WorkerSearchParam {
    private Integer type;
    private String beyond;
    private Integer organId;
    private String organName;
    private Boolean settingBeyond;

    public static JpaSpecBo convert(WorkerSearchParam param) {
        if(param==null){
            return null;
        }
        JpaSpecBo jpaSpecBo=new JpaSpecBo();
        if(param.getType()!=null){
            jpaSpecBo.getEqualMap().put("type",param.getType());
        }
        if(StringUtils.isNotBlank(param.getBeyond())){
            jpaSpecBo.getEqualMap().put("beyond",param.getBeyond());

        }
        if(param.getOrganId()!=null){
            jpaSpecBo.getEqualMap().put("organId",param.getOrganId());

        }
        return jpaSpecBo;
    }
}
