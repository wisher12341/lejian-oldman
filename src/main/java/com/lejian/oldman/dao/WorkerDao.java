package com.lejian.oldman.dao;

import com.lejian.oldman.entity.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WorkerDao extends JpaRepository<WorkerEntity,Long>,JpaSpecificationExecutor<WorkerEntity> {
    WorkerEntity findByUserId(Integer userId);

    @Query(value = "select `type`,count(`type`) as count from worker where beyond=?1  and if(?2!=0,add_user_id=?2,1=1) group by `type`",nativeQuery = true)
    List<Map<String,Object>> getTypeCountByBeyond(String workerBeyond, Integer userId);

    List<WorkerEntity> findByIdCardIn(List<String> idCardList);
}
