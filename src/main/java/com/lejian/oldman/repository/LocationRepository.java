package com.lejian.oldman.repository;


import com.lejian.oldman.dao.LocationDao;
import com.lejian.oldman.bo.LocationBo;
import com.lejian.oldman.entity.LocationEntity;
import com.lejian.oldman.handler.BaiduMapHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.xml.stream.Location;
import java.util.List;
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
     * 如果不存在 则调用百度api获取坐标
     * @param desc
     * @return
     */
    @Transactional
    public Integer getByDesc(String desc) {
        LocationEntity locationEntity=dao.findByDesc(desc);
        if(locationEntity!=null){
            return locationEntity.getId();
        }
        //todo 后面 city 传入
        Pair<String,String> location= baiduMapHandler.geocoding(desc,"上海市");
        if (location!=null){
            LocationEntity locationEntitySave=new LocationEntity();
            locationEntitySave.setDesc(desc);
            locationEntitySave.setPositionX(location.getFirst());
            locationEntitySave.setPositionY(location.getSecond());
            LocationEntity result=dao.save(locationEntitySave);
            return result.getId();
        }
        return null;
    }

    public List<LocationBo> getAllLocationByConfig(String areaCountry, String areaTown, String areaVillage) {
        return dao.getAllLocationByConfig(areaCountry,areaTown,areaVillage).stream().map(this::convertEntity).collect(Collectors.toList());
    }
}
