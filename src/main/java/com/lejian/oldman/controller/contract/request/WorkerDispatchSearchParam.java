package com.lejian.oldman.controller.contract.request;

import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.enums.UserEnum;
import com.lejian.oldman.utils.UserUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;

@Data
public class WorkerDispatchSearchParam {
    private Integer workerId;
    private Long startTime;
    private Long endTime;

    public JpaSpecBo getJpaSpecBo() {
        JpaSpecBo jpaSpecBo=new JpaSpecBo();
        if (workerId!=null){
            jpaSpecBo.getEqualMap().put("workerId",workerId);
        }
        if (startTime!=null){
            jpaSpecBo.getGreatEMap().put("startTime",new Timestamp(startTime));
        }
        if (endTime!=null){
            jpaSpecBo.getLessEMap().put("endTime",new Timestamp(endTime));
        }
        UserBo userBo = UserUtils.getUser();
        if (userBo.getRole().intValue() == UserEnum.Role.USER.getValue()){
            jpaSpecBo.getEqualMap().put("userId",userBo.getId());
        }

        return jpaSpecBo;
    }
}
