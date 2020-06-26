package com.lejian.oldman.service;


import com.google.common.collect.Lists;
import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.LocationBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.enums.OldmanStatusEnum;
import com.lejian.oldman.repository.LocationRepository;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.vo.LocationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.lejian.oldman.enums.OldmanStatusEnum.GREEN;
import static com.lejian.oldman.enums.OldmanStatusEnum.RED;
import static com.lejian.oldman.enums.OldmanStatusEnum.YELLOW;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private OldmanRepository oldmanRepository;

    /**
     * 获取全部位置
     * 同时查询红灯和黄灯老人
     * @return
     */
    public List<LocationVo> getAllLocation() {
        List<LocationBo> locationBoList = locationRepository.getAll();
        Map<Integer,List<OldmanBo>> locationOldmanMap = getRedAndYellowLocation();
        return classifyLocation(locationBoList,locationOldmanMap);
    }


    /**
     * 对Location 根据老人状态进行分类
     * @param locationBoList
     * @param locationOldmanMap
     * @return
     */
    private List<LocationVo> classifyLocation(List<LocationBo> locationBoList, Map<Integer, List<OldmanBo>> locationOldmanMap) {
        return locationBoList.stream().map(bo->{
                LocationVo vo = new LocationVo();
                BeanUtils.copyProperties(bo,vo);
                vo.setLocationTypeEnum(LocationVo.LocationTypeEnum.GREEN);
                if(locationOldmanMap.containsKey(bo.getId())){
                    for(OldmanBo oldmanBo : locationOldmanMap.get(bo.getId())){
                        if(vo.getLocationTypeEnum()== LocationVo.LocationTypeEnum.RANDY){
                            break;
                        }
                        vo.setLocationTypeEnum(vo.getLocationTypeEnum().convert(oldmanBo.getStatus()));
                    }
                }
                return vo;
              }).collect(Collectors.toList());
    }

    /**
     * 获取 红灯，黄灯老人对应的 位置
     * @return
     */
    private Map<Integer, List<OldmanBo>> getRedAndYellowLocation() {
        List<OldmanBo> oldmanBoList = oldmanRepository.findByStatus(Lists.newArrayList(RED.getStatus(),YELLOW.getStatus()));
        return oldmanBoList.stream().collect(Collectors.groupingBy(OldmanBo::getLocationId));
    }
}
