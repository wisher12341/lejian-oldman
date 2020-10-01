package com.lejian.oldman.service;

import com.google.common.collect.Maps;
import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.config.VarConfig;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.repository.OldmanRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class MainService {

    @Autowired
    private OldmanRepository oldmanRepository;

    /**
     * 智能设备安装人数
     * @return
     * @param oldmanSearchParam
     */
    public Map<String, Long> getOldmanEquipCount(OldmanSearchParam oldmanSearchParam) {
        Map<String, Long> map= Maps.newHashMap();
        /**
         * 1. 长者关怀系统
         * 2. 摄像头系统
         * 3. 想家宝系统
         */
        List<Long> countList=oldmanRepository.getEquipCountByArea(
                oldmanSearchParam.getAreaCustomOne(),oldmanSearchParam.getAreaVillage(),oldmanSearchParam.getAreaTown(),oldmanSearchParam.getAreaCountry());
        map.put("关怀系统",countList.get(0));
        map.put("摄像头",countList.get(1));
        map.put("想家宝",countList.get(2));
        return map;
    }

    public Map<String, Long> getOldmanGroup(OldmanSearchParam oldmanSearchParam, Class<? extends BusinessEnum> businessEnumClass,String groupFieldName) {
        Map<String, Long> result=Maps.newHashMap();
        Map<Integer, Long> map= oldmanRepository.getOldmanGroup(oldmanSearchParam,groupFieldName);
        map.forEach((k,v)->{
            BusinessEnum businessEnum=BusinessEnum.find(k,businessEnumClass);
            if(businessEnum!=BusinessEnum.DefaultValue.NULL) {
                result.put(businessEnum.getDesc(), v);
            }
        });
        return result;
    }

    public Map<String, Long> getOldmanAge(OldmanSearchParam oldmanSearchParam) {
        Map<String, Long> map= Maps.newHashMap();
        JpaSpecBo jpaSpecBo1=OldmanSearchParam.convert(oldmanSearchParam);
        jpaSpecBo1.getGreatEMap().put("birthday", LocalDateTime.now().minusYears(69).toLocalDate());
        jpaSpecBo1.getLessEMap().put("birthday",LocalDateTime.now().minusYears(60).toLocalDate());
        map.put("60-69",oldmanRepository.countWithSpec(jpaSpecBo1));

        JpaSpecBo jpaSpecBo2=OldmanSearchParam.convert(oldmanSearchParam);
        jpaSpecBo2.getGreatEMap().put("birthday",LocalDateTime.now().minusYears(79).toLocalDate());
        jpaSpecBo2.getLessEMap().put("birthday",LocalDateTime.now().minusYears(70).toLocalDate());
        map.put("70-79",oldmanRepository.countWithSpec(jpaSpecBo2));

        JpaSpecBo jpaSpecBo3=OldmanSearchParam.convert(oldmanSearchParam);
        jpaSpecBo3.getGreatEMap().put("birthday",LocalDateTime.now().minusYears(89).toLocalDate());
        jpaSpecBo3.getLessEMap().put("birthday",LocalDateTime.now().minusYears(80).toLocalDate());
        map.put("80-89",oldmanRepository.countWithSpec(jpaSpecBo3));

        JpaSpecBo jpaSpecBo4=OldmanSearchParam.convert(oldmanSearchParam);
        jpaSpecBo4.getLessEMap().put("birthday",LocalDateTime.now().minusYears(90).toLocalDate());
        map.put("90-",oldmanRepository.countWithSpec(jpaSpecBo4));

        return map;
    }

}
