package com.lejian.oldman.repository;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.bo.DbBo;
import com.lejian.oldman.bo.RzzBo;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.dao.DbDao;
import com.lejian.oldman.dao.RzzDao;
import com.lejian.oldman.entity.DbEntity;
import com.lejian.oldman.entity.RzzEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.REPOSITORY_ERROR;

@Repository
public class DbRepository extends AbstractSpecificationRepository<DbBo,DbEntity> {

    @Autowired
    private DbDao dao;

    @Override
    protected JpaRepository getDao() {
        return dao;
    }

    @Override
    protected DbBo convertEntity(DbEntity entity) {
        if(entity==null){
            return null;
        }
        DbBo bo = new DbBo();
        BeanUtils.copyProperties(entity,bo);
        return bo;
    }



    @Override
    protected DbEntity convertBo(DbBo bo) {
        DbEntity entity = new DbEntity();
        BeanUtils.copyProperties(bo,entity);
        return entity;
    }

    public List<DbBo> getByOids(List<String> oidList) {
        return dao.findByOidIn(oidList).stream().map(this::convertEntity).collect(Collectors.toList());
    }



}
