package com.lejian.oldman.service;

import com.google.common.collect.Maps;
import com.lejian.oldman.repository.OldmanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CountService {

    @Autowired
    private OldmanRepository oldmanRepository;

    /**
     * 智能设备安装人数
     * @return
     * @param areaCustomOne
     */
    public Map<String, Long> getOldmanEquip(String areaCustomOne) {
        Map<String, Long> map= Maps.newHashMap();
        /**
         * 1. 长者关怀系统
         * 2. 摄像头系统
         * 3. 想家宝系统
         */
        List<Long> countList=oldmanRepository.getEquipCountByArea(areaCustomOne);
        map.put("关怀系统",countList.get(0));
        map.put("摄像头",countList.get(1));
        map.put("想家宝",countList.get(2));
        return map;
    }
}
