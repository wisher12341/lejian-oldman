package com.lejian.oldman.dao;

import com.lejian.oldman.entity.LocationEntity;
import com.lejian.oldman.entity.OldmanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationDao extends JpaRepository<LocationEntity, Long>{

    LocationEntity findByDesc(String desc);
}
