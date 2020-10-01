package com.lejian.oldman.controller.contract.request;

import com.lejian.oldman.bo.JpaSpecBo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class WorkerSearchParam {
    private Integer type;
    private String beyond;

    public JpaSpecBo convert() {
        JpaSpecBo jpaSpecBo=new JpaSpecBo();
        if(type!=null){
            jpaSpecBo.getEqualMap().put("type",type);
        }
        if(StringUtils.isNotBlank(beyond)){
            jpaSpecBo.getEqualMap().put("beyond",beyond);

        }
        return jpaSpecBo;
    }
}
