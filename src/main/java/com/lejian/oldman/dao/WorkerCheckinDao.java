package com.lejian.oldman.dao;

import com.lejian.oldman.entity.ContactManEntity;
import com.lejian.oldman.entity.WorkerCheckinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerCheckinDao extends JpaRepository<WorkerCheckinEntity, Long>{
    WorkerCheckinEntity findFirstByWorkerIdOrderByIdDesc(Integer workerId);
}
