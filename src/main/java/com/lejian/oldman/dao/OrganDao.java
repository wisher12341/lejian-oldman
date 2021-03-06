package com.lejian.oldman.dao;

import com.lejian.oldman.bo.OrganBo;
import com.lejian.oldman.entity.OrganEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganDao extends JpaRepository<OrganEntity,Long>,JpaSpecificationExecutor<OrganEntity> {

    List<OrganEntity> findByNameIn(List<String> nameList);
}
