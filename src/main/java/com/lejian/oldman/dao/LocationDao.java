package com.lejian.oldman.dao;

import com.lejian.oldman.entity.LocationEntity;
import com.lejian.oldman.entity.OldmanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationDao extends JpaRepository<LocationEntity, Long>{

    LocationEntity findByDesc(String desc);


    @Query(value = "select * from location where id in (select location_id from oldman where " +
            "if(LENGTH(?1)>0,area_country=?1,1=1) and if(LENGTH(?2)>0,area_town=?2,1=1) and if(LENGTH(?3)>0,area_village=?3,1=1)) ",nativeQuery = true)
    List<LocationEntity> getAllLocationByConfig(String areaCountry, String areaTown, String areaVillage);

    List<LocationEntity> findByDescIn(List<String> descList);
}
