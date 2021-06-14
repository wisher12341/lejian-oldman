package com.lejian.oldman.controller.contract.request;

import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.enums.UserEnum;
import com.lejian.oldman.utils.UserUtils;
import lombok.Data;

@Data
public class RzzSearchParam {
    private String oid;
    private Integer id;

    public JpaSpecBo getJpaSpecBo() {
        JpaSpecBo jpaSpecBo = new JpaSpecBo();

        UserBo userBo = UserUtils.getUser();
        if (userBo.getRole().intValue() == UserEnum.Role.USER.getValue()){
            jpaSpecBo.getEqualMap().put("userId",userBo.getId());
        }
        return jpaSpecBo;
    }
}
