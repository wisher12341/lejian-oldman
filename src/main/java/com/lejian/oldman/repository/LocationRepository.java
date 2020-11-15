package com.lejian.oldman.repository;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.dao.LocationDao;
import com.lejian.oldman.bo.LocationBo;
import com.lejian.oldman.entity.LocationEntity;
import com.lejian.oldman.handler.BaiduMapHandler;
import com.lejian.oldman.utils.tuple.Tuple3;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class LocationRepository extends AbstractRepository<LocationBo,LocationEntity>{

    @Autowired
    private LocationDao dao;
    @Autowired
    private BaiduMapHandler baiduMapHandler;


    @Override
    protected JpaRepository getDao() {
        return dao;
    }


    @Override
    protected LocationBo convertEntity(LocationEntity locationEntity) {
        if(locationEntity == null){
            return null;
        }
        LocationBo locationBo = new LocationBo();
        BeanUtils.copyProperties(locationEntity,locationBo);
        return locationBo;
    }

    @Override
    protected LocationEntity convertBo(LocationBo locationBo) {
        LocationEntity locationEntity = new LocationEntity();
        BeanUtils.copyProperties(locationBo,locationEntity);
        return locationEntity;
    }

    /**
     * 根据desc 获取id
     *
     * @param desc
     */
    //todo desc 不一样  坐标一样的 也要解决
    @Transactional
    public Integer getByDesc(String desc) {
        if(StringUtils.isNotBlank(desc)) {
            LocationEntity locationEntity = dao.findByDesc(desc);
            if (locationEntity != null) {
                return locationEntity.getId();
            }
        }
        return null;
    }

    /**
     * 如果填写了坐标 则直接创建
     * 反之，则调用百度api获取坐标
     * @param desc
     * @param lng
     * @param lat
     * @return
     */
    @Transactional
    public Integer create(String desc, String lng, String lat) {
        String positionX= null;
        String positionY=null;
        if(StringUtils.isNotBlank(lng) && StringUtils.isNotBlank(lat)){
            positionX=lng;
            positionY=lat;
        }else if(StringUtils.isNotBlank(desc)) {
            //todo 后面 city 传入
            Pair<String, String> location = baiduMapHandler.geocoding(desc, "上海市");
            if (location != null) {
                positionX=location.getFirst();
                positionY=location.getSecond();
            }
        }

        if(StringUtils.isNotBlank(positionX) && StringUtils.isNotBlank(positionY)){
            LocationEntity locationEntitySave = new LocationEntity();
            locationEntitySave.setDesc(desc);
            locationEntitySave.setPositionX(positionX);
            locationEntitySave.setPositionY(positionY);
            LocationEntity result = dao.save(locationEntitySave);
            return result.getId();
        }

        return null;
    }

    public List<LocationBo> getAllLocationByConfig(String areaCountry, String areaTown, String areaVillage) {
        return dao.getAllLocationByConfig(areaCountry,areaTown,areaVillage).stream().map(this::convertEntity).collect(Collectors.toList());
    }

    /**
     * 批量获取location id，
     * 如果不存在则添加
     * @param tuple3List
     * @return key 地址描述, value location id
     */
    public Map<String,Integer> getBatchByDescOrCreate(List<Tuple3<String,String,String>> tuple3List) {
        Map<String, Integer> map=Maps.newHashMap();
        Map<String, Tuple3<String,String,String>> tupleMap=Maps.newHashMap();
        tuple3List.forEach(item-> {
            map.put(item.getA(), null);
            tupleMap.put(item.getA(),item);
        });


        //先从数据库查询已有的
        Map<String,Integer> locationEntityMap=dao.findByDescIn(Lists.newArrayList(map.keySet())).stream().collect(Collectors.toMap(LocationEntity::getDesc, LocationEntity::getId));
        map.putAll(locationEntityMap);

        //新增
        map.forEach((k,v)->{
            if (v==null){
                map.put(k,create(k,tupleMap.get(k).getB(),tupleMap.get(k).getC()));
            }
        });
        return map;
    }
}
