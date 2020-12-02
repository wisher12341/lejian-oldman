package com.lejian.oldman.dao;


import com.lejian.oldman.bo.OldmanBo;
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
    List<OldmanEntity> findByStatusInAndIsDelete(List<Integer> statusList,Integer delete);

    List<OldmanEntity> findByLocationIdAndIsDelete(Integer locationId,Integer delete);

    OldmanEntity findByOidAndIsDelete(String oid,Integer delete);


    List<OldmanEntity> findByNameLikeAndIsDelete(String name,Integer delete);

    OldmanEntity findByNameAndIsDelete(String name,Integer delete);


    @Modifying
    @Query(value = "update oldman set status=?2 where care_gateway_id=?1 and is_delete=0",nativeQuery = true)
    Integer updateStatusByCareGatewayId(Integer gatewayId, Integer status);

    OldmanEntity findByCareGatewayIdAndIsDelete(Integer careGatewayId,Integer delete);

    Long countByStatusAndIsDelete(Integer status,Integer delete);

    @Query(value = "(select count(care_gateway_id) from oldman where if(LENGTH(?1)>0,area_custom_one=?1,if(LENGTH(?2)>0,area_village=?2,if(LENGTH(?3)>0,area_town=?3,if(LENGTH(?4)>0,area_country=?4,1=1)))) and care_gateway_id!=0 and is_delete=0)\n" +
            "union all " +
            "(select count(camera_id)  from oldman where if(LENGTH(?1)>0,area_custom_one=?1,if(LENGTH(?2)>0,area_village=?2,if(LENGTH(?3)>0,area_town=?3,if(LENGTH(?4)>0,area_country=?4,1=1)))) and camera_id!=0 and is_delete=0)\n" +
            "union all " +
            "(select count(xjb_id)  from oldman  where if(LENGTH(?1)>0,area_custom_one=?1,if(LENGTH(?2)>0,area_village=?2,if(LENGTH(?3)>0,area_town=?3,if(LENGTH(?4)>0,area_country=?4,1=1)))) and xjb_id!=0 and is_delete=0)",nativeQuery = true)
    List<Long> getEquipCountByArea(String areaCustomOne,String areaVillage,String areaTown,String areaCountry);

    List<OldmanEntity> findByOidIn(List<String> oidList);

    @Modifying
    @Query(value = "update oldman set status=?2 where location_id=?1 and is_delete=0",nativeQuery = true)
    void updateStatusByLocationId(Integer locationId, Integer status);


    @Query(value = "select DISTINCT location_id from oldman where is_delete=0 and if(LENGTH(?1)>0,area_country=?1,1=1) and if(LENGTH(?2)>0,area_town=?2,1=1) and if(LENGTH(?3)>0,area_village=?3,1=1)  and if(LENGTH(?4)>0,area_custom_one=?4,1=1)",nativeQuery = true)
    List<Integer> getLocationIdByArea(String areaCountry, String areaTown, String areaVillage, String areaCustomOne);

    @Modifying
    @Query(value = "update oldman set status=1 where status=2 and is_delete=0",nativeQuery = true)
    void updateStatusYtoG();

    @Modifying
    @Query(value = "update oldman set is_delete=1 where oid=?1",nativeQuery = true)
    void deleteByOid(String oid);
}
