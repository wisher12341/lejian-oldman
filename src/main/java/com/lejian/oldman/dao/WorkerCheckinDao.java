package com.lejian.oldman.dao;

import com.lejian.oldman.entity.WorkerCheckinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WorkerCheckinDao extends JpaRepository<WorkerCheckinEntity, Long>,JpaSpecificationExecutor<WorkerCheckinEntity> {
    WorkerCheckinEntity findFirstByWorkerIdOrderByIdDesc(Integer workerId);

    @Query(value = "select worker_id,max(create_time) as create_time from worker_checkin" +
            " where create_time>=?1 and create_time<=?2 group by worker_id",nativeQuery = true)
    List<Map<String,Object>> getAllLatestTimeByTime(String startTime, String endTime);
}
