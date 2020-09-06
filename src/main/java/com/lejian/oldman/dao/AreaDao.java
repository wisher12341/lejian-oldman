package com.lejian.oldman.dao;

import com.lejian.oldman.entity.AreaEntity;
import com.lejian.oldman.entity.WorkerCheckinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaDao extends JpaRepository<AreaEntity, Long>,JpaSpecificationExecutor<AreaEntity> {

}
