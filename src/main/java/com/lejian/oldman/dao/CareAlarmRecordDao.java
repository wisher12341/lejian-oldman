package com.lejian.oldman.dao;

import com.lejian.oldman.bo.CareAlarmRecordBo;
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

    @Query(value = "select c.type,c.oid,c.content,c.handle,c.is_read,c.is_handle,c.create_time from care_alarm_record c left join oldman o on c.oid=o.oid where c.create_time>?1 and if(?2!=0,o.user_id=?2,1=1)" ,nativeQuery = true )
    List<CareAlarmRecordBo> getNewData(String time, Integer userRoleId);
}
