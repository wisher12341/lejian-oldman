package com.lejian.oldman.dao;

import com.lejian.oldman.bo.ChxBo;
import com.lejian.oldman.entity.ChxEntity;
import com.lejian.oldman.entity.WorkerCheckinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChxDao extends JpaRepository<ChxEntity, Long>,JpaSpecificationExecutor<ChxEntity> {


    List<ChxEntity> findByOidIn(List<String> oidList);
}
