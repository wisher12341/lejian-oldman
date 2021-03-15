package com.lejian.oldman.dao;

import com.lejian.oldman.entity.OrganEntity;
import com.lejian.oldman.entity.RzzEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RzzDao extends JpaRepository<RzzEntity,Long>,JpaSpecificationExecutor<RzzEntity> {

    @Query(value = "select `type`,count(`type`) as count from rzz where `type` in (?1) group by `type`",nativeQuery = true)
    List<Map<String,Object>> getMapCount(List<Integer> selectValue);
}
