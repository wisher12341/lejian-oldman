package com.lejian.oldman.dao;

import com.lejian.oldman.entity.OrganEntity;
import com.lejian.oldman.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ServiceDao extends JpaRepository<ServiceEntity,Long>,JpaSpecificationExecutor<ServiceEntity> {

    @Query(value = "select `type`,count(`service_type`) as count from service where organ_id =?1 group by service_type",nativeQuery = true)
    Map<String,String> getServiceTypeCount(Integer id);
}
