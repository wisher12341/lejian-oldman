package com.lejian.oldman.dao;


import com.lejian.oldman.entity.OldmanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OldmanDao extends JpaRepository<OldmanEntity, Long>,JpaSpecificationExecutor<OldmanEntity> {
    List<OldmanEntity> findByStatusIn(List<Integer> statusList);

    List<OldmanEntity> findByLocationId(Integer locationId);

    OldmanEntity findByOid(String oid);


    List<OldmanEntity> findByNameLike(String name);

    OldmanEntity findByName(String name);


    @Modifying
    @Query(value = "update oldman set status=?2 where care_gateway_id=?1",nativeQuery = true)
    Integer updateStatusByCareGatewayId(String gatewayId, Integer status);

    OldmanEntity findByCareGatewayId(String careGatewayId);

    Long countByStatus(Integer status);

    @Query(value = "(select count(care_gateway_id) from oldman where if(?1 !=null,area_custom_one=?1,1=1) and care_gateway_id!=0)\n" +
            "union all " +
            "(select count(camera_id)  from oldman where if(?1 !=null,area_custom_one=?1,1=1) and camera_id!=0)\n" +
            "union all " +
            "(select count(xjb_id)  from oldman  where if(?1 !=null,area_custom_one=?1,1=1) and xjb_id!=0)",nativeQuery = true)
    List<Long> getEquipCountByArea(String areaCustomOne);
}
