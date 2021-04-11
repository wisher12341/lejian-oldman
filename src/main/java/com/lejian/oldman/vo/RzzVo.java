package com.lejian.oldman.vo;

import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.bo.RzzBo;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import lombok.Data;

import java.util.Map;

@Data
public class RzzVo {

    private Integer id;
    private String oid;
    private String oldmanName;
    private String type;

    public static RzzVo convert(RzzBo rzzBo, Map<String, OldmanBo> oldmanBoMap) {
        RzzVo rzzVo = new RzzVo();
        rzzVo.setId(rzzBo.getId());
        rzzVo.setOldmanName(oldmanBoMap.get(rzzBo.getOid()).getName());
        rzzVo.setOid(rzzBo.getOid());
        OldmanEnum.RzzType rzzType = (OldmanEnum.RzzType) BusinessEnum.find(rzzBo.getType(),OldmanEnum.RzzType.class);
        String type= rzzType.getDesc();
        rzzType = rzzType.getParent();
        while (rzzType!=null && rzzType!= OldmanEnum.RzzType.ROOT){
            type = rzzType.getDesc()+" - "+type;
            rzzType = rzzType.getParent();
        }
        rzzVo.setType(type);
        return rzzVo;
    }
}
