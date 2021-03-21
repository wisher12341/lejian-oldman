package com.lejian.oldman.dao;

import com.lejian.oldman.entity.DbEntity;
import com.lejian.oldman.entity.RzzEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RzzServiceDao extends JpaRepository<RzzEntity,Long>,JpaSpecificationExecutor<RzzEntity> {

}
