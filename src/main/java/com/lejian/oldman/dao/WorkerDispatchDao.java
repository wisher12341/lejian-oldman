package com.lejian.oldman.dao;

import com.lejian.oldman.entity.RzzEntity;
import com.lejian.oldman.entity.WorkerDispatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WorkerDispatchDao extends JpaRepository<WorkerDispatchEntity,Long>,JpaSpecificationExecutor<WorkerDispatchEntity> {

    @Modifying
    @Query(value ="update worker_dispatch set status=3 where worker_id=?1 and end_time<?2 and status=0 ",nativeQuery = true)
    void notFinishUpdate(Integer workerId, String currentTime);
}
