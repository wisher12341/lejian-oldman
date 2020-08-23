package com.lejian.oldman.dao;

import com.lejian.oldman.entity.CareAlarmRecordEntity;
import com.lejian.oldman.entity.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CareAlarmRecordDao extends JpaRepository<CareAlarmRecordEntity,Long>,JpaSpecificationExecutor<CareAlarmRecordEntity> {

    @Modifying
    @Query(value = "update care_alarm_record set is_handle=?2 where oid in (select oid from oldman where location_id=?1)",nativeQuery = true)
    void updateIsHandleByLocationId(Integer locationId, Integer isHandle);
}
