package com.lejian.oldman.dao;

import com.lejian.oldman.entity.OrganServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrganServiceDao extends JpaRepository<OrganServiceEntity,Long>,JpaSpecificationExecutor<OrganServiceEntity> {



    @Query(value = "select count(1) from organ_service s left join organ o on o.id=s.organ_id where o.beyond=?1 and if(?2!=0,user_id=?2,1=1)",nativeQuery = true)
    Long countByOrganBeyond(String beyond, Integer userId);


    @Query(value = "select o.type,count(o.type) as count from organ_service s left join organ o on o.id=s.organ_id where o.beyond=?1 group by o.type",nativeQuery = true)
    List<Map<String,Object>> getServiceCountGroupByType(String beyond);


    @Query(value = "select o.name,count(o.name) as count from organ_service s left join organ o on o.id=s.organ_id where o.beyond=?2 and o.type=?1 group by o.name",nativeQuery = true)
    List<Map<String,Object>> getOrganServiceCountByType(Integer type, String beyond);

    @Query(value = "select s.service_type,count(s.service_type) as count from organ_service s left join organ o on o.id=s.organ_id where o.name=?1 group by s.service_type",nativeQuery = true)
    List<Map<String,Object>> getServiceCountByOrgan(String name);

    @Query(value = "select oid from organ_service where  (coalesce (?3 , null) is null or organ_id IN (?3))  and if(?4!=0,service_type=?4,1=1) limit ?1,?2",nativeQuery = true)
    List<String> getServiceOldmanByPage(Integer pageNo, Integer pageSize, List<Integer> organIdList, Integer serviceType);

}
