package com.lejian.oldman.Dao;


import com.lejian.oldman.entity.OldmanEntity;
import com.lejian.oldman.vo.OldmanVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OldmanDao extends JpaRepository<OldmanEntity, Long>,JpaSpecificationExecutor<OldmanEntity> {
    List<OldmanEntity> findByStatusIn(List<Integer> statusList);

    List<OldmanEntity> findByLocationId(Integer locationId);
}