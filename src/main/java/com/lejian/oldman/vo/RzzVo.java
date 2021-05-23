package com.lejian.oldman.vo;

import com.google.common.collect.Maps;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.bo.RzzBo;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

@Data
public class RzzVo {

    private Integer id;
    private String oid;
    private String oldmanName;
    private String type;
    private Integer typeRaw;

    public static RzzVo convert(RzzBo rzzBo, Map<String, OldmanBo> oldmanBoMap) {
        RzzVo rzzVo = new RzzVo();
        rzzVo.setId(rzzBo.getId());
        if(MapUtils.isNotEmpty(oldmanBoMap)) {
            rzzVo.setOldmanName(oldmanBoMap.get(rzzBo.getOid()).getName());
        }
        rzzVo.setOid(rzzBo.getOid());
        OldmanEnum.RzzType rzzType = (OldmanEnum.RzzType) BusinessEnum.find(rzzBo.getType(),OldmanEnum.RzzType.class);
        String type= rzzType.getDesc();
        rzzType = rzzType.getParent();
        while (rzzType!=null && rzzType!= OldmanEnum.RzzType.ROOT){
            type = rzzType.getDesc()+" - "+type;
            rzzType = rzzType.getParent();
        }
        rzzVo.setType(type);
        rzzVo.setTypeRaw(rzzBo.getType());
        return rzzVo;
    }

    public static RzzVo convert(RzzBo rzzBo) {
        return convert(rzzBo, Maps.newHashMap());
    }
}
