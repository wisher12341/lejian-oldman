package com.lejian.oldman.utils;

import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.entity.OldmanEntity;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.vo.OldmanVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilsTest {


    @Test
    void convertOldmanBoToOldmanVo() {

        OldmanBo oldmanBo=new OldmanBo();
        oldmanBo.setOid("oid");
        oldmanBo.setSexEnum(OldmanEnum.Sex.MAN);

        OldmanVo oldmanVo =ObjectUtils.convert(oldmanBo,OldmanVo.class);
        assert oldmanVo.getSex().equals("男");
    }


    @Test
    void convertOldmanBoToOldmanEntity() {

        OldmanBo oldmanBo=new OldmanBo();
        oldmanBo.setOid("oid");
        oldmanBo.setSexEnum(OldmanEnum.Sex.MAN);

        OldmanEntity oldmanEntity =ObjectUtils.convert(oldmanBo,OldmanEntity.class);
        assert oldmanEntity.getSex()==1;
    }
}