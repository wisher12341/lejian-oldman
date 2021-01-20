package com.lejian.oldman.dao;

import com.lejian.oldman.entity.VisualSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VisualSettingDao extends JpaRepository<VisualSettingEntity, Long>,JpaSpecificationExecutor<VisualSettingEntity> {

}
