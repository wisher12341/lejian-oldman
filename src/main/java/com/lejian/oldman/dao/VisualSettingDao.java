package com.lejian.oldman.dao;

import com.lejian.oldman.entity.VisualSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VisualSettingDao extends JpaRepository<VisualSettingEntity, Long>,JpaSpecificationExecutor<VisualSettingEntity> {


    @Modifying
    @Query(value = "update visual_setting set is_used=0 where user_id = ?1",nativeQuery = true)
    Integer clearUsed(Integer userId);

    VisualSettingEntity findByIsUsedAndUserId(Integer used,Integer userId);
}
