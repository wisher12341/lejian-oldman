package com.lejian.oldman.dao;

import com.lejian.oldman.entity.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerDao extends JpaRepository<WorkerEntity,Long>,JpaSpecificationExecutor<WorkerEntity> {
    WorkerEntity findByUserId(Integer userId);
}
