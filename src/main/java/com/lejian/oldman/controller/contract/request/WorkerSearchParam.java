package com.lejian.oldman.controller.contract.request;

import com.lejian.oldman.bo.JpaSpecBo;
import lombok.Data;

@Data
public class WorkerSearchParam {
    private Integer type;

    public JpaSpecBo convert() {
        JpaSpecBo jpaSpecBo=new JpaSpecBo();
        if(type!=null){
            jpaSpecBo.getEqualMap().put("type",type);
        }
        return jpaSpecBo;
    }
}
