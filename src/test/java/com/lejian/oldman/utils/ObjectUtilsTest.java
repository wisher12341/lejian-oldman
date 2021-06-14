package com.lejian.oldman.utils;

import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.entity.OldmanEntity;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.vo.OldmanVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilsTest {


    @Test
    void convertOldmanBoToOldmanVo() {

        String val ="1622986680000";
        Timestamp timestamp = new Timestamp(Long.valueOf(val));

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

    @Test
    void test(){
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123"));
        System.out.println(encoder.encode("123"));
        System.out.println(encoder.encode("123"));
        System.out.println(encoder.encode("123"));
    }
}