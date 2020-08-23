package com.lejian.oldman.service;


import com.google.common.collect.Lists;
import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.LocationBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.repository.LocationRepository;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.vo.LocationVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
                        OldmanEnum.Status oldmanStatus = (OldmanEnum.Status) oldmanBo.getStatus();
                        vo.setLocationTypeEnum(vo.getLocationTypeEnum().convert(oldmanStatus));
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
        List<OldmanBo> oldmanBoList = oldmanRepository.findByStatus(Lists.newArrayList(OldmanEnum.Status.RED.getValue(),OldmanEnum.Status.YELLOW.getValue()));
        return oldmanBoList.stream().collect(Collectors.groupingBy(OldmanBo::getLocationId));
    }

    public Pair<Long, List<LocationVo>> pollStatus(long timestamp) {
        List<LocationVo> locationVoList = Lists.newArrayList();
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        jpaSpecBo.getGreatMap().put("datachangeTime", new Timestamp(timestamp));
        List<OldmanBo> oldmanBoList = oldmanRepository.findWithSpec(jpaSpecBo);
        if(CollectionUtils.isNotEmpty(oldmanBoList)){
            Map<Integer,List<OldmanBo>> map = oldmanBoList.stream()
                    .collect(Collectors.groupingBy(OldmanBo::getLocationId));
            map.forEach((k,v)->{
                LocationVo locationVo=new LocationVo();
                locationVo.setId(k);
                locationVo.setLocationTypeEnum(LocationVo.LocationTypeEnum.GREEN);
                for(OldmanBo oldmanBo:v){
                    if(locationVo.getLocationTypeEnum()== LocationVo.LocationTypeEnum.RANDY){
                        break;
                    }
                    OldmanEnum.Status oldmanStatus = (OldmanEnum.Status) oldmanBo.getStatus();
                    locationVo.setLocationTypeEnum(locationVo.getLocationTypeEnum().convert(oldmanStatus));
                }
                locationVoList.add(locationVo);
            });
            OldmanBo oldmanBo=oldmanBoList.stream().max(Comparator.comparing(OldmanBo::getDatachangeTime)).get();
            return Pair.of(oldmanBo.getDatachangeTime().getTime(),locationVoList);

        }
        return null;
    }

    public List<LocationVo> getLocationByAreaCustomOne(String areaCustomOne) {
        return oldmanRepository.getLocationIdByAreaCustomOne(areaCustomOne).stream().map(id->{
            LocationVo locationVo=new LocationVo();
            locationVo.setId(id);
            return locationVo;
        }).collect(Collectors.toList());
    }

}
