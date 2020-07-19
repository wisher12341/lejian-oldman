package com.lejian.oldman.dao;


import com.lejian.oldman.entity.OldmanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OldmanDao extends JpaRepository<OldmanEntity, Long>,JpaSpecificationExecutor<OldmanEntity> {
    List<OldmanEntity> findByStatusIn(List<Integer> statusList);

    List<OldmanEntity> findByLocationId(Integer locationId);

    OldmanEntity findByOid(String oid);


    List<OldmanEntity> findByNameLike(String name);

    OldmanEntity findByName(String name);


    @Modifying
    @Query(value = "update oldman set status=?2 where name=?1",nativeQuery = true)
    Integer updateStatusByName(String name, Integer status);
}
