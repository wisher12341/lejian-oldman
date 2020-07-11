package com.lejian.oldman.repository;


import com.lejian.oldman.dao.LocationDao;
import com.lejian.oldman.bo.LocationBo;
import com.lejian.oldman.entity.LocationEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository extends AbstractRepository<LocationBo,LocationEntity>{

    @Autowired
    private LocationDao dao;


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
}
