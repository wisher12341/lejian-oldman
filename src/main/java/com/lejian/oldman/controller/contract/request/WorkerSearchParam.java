package com.lejian.oldman.controller.contract.request;

import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.enums.UserEnum;
import com.lejian.oldman.utils.UserUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class WorkerSearchParam {
    private Integer type;
    private String beyond;
    private Integer organId;
    private String organName;
    private Boolean settingBeyond;
    private String name;

    public static JpaSpecBo convert(WorkerSearchParam param) {
        JpaSpecBo jpaSpecBo = new JpaSpecBo();


        UserBo userBo = UserUtils.getUser();
        if (userBo.getRole().intValue() == UserEnum.Role.USER.getValue()) {
            jpaSpecBo.getEqualMap().put("addUserId", userBo.getId());
        }
        if (param == null){
            return jpaSpecBo;
        }
        if(param.getType()!=null){
            jpaSpecBo.getEqualMap().put("type",param.getType());
        }
        if(StringUtils.isNotBlank(param.getBeyond())){
            jpaSpecBo.getEqualMap().put("beyond",param.getBeyond());

        }
        if(param.getOrganId()!=null){
            jpaSpecBo.getEqualMap().put("organId",param.getOrganId());

        }
        if (StringUtils.isNotBlank(param.getName())){
            jpaSpecBo.getLikeMap().put("name","%"+param.getName()+"%");
        }
        return jpaSpecBo;
    }
}
