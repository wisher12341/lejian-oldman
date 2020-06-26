package com.lejian.oldman.Dao;

import com.lejian.oldman.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationDao extends JpaRepository<LocationEntity, Long>{
}
