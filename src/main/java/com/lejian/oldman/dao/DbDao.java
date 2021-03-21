package com.lejian.oldman.dao;

import com.lejian.oldman.entity.DbEntity;
import com.lejian.oldman.entity.RzzEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DbDao extends JpaRepository<DbEntity,Long>,JpaSpecificationExecutor<DbEntity> {

    List<DbEntity> findByOidIn(List<String> oidList);
}
