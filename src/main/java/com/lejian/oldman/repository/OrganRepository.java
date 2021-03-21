package com.lejian.oldman.repository;


import com.lejian.oldman.bo.OrganBo;
import com.lejian.oldman.dao.OrganDao;
import com.lejian.oldman.entity.OrganEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Repository
public class OrganRepository extends AbstractSpecificationRepository<OrganBo,OrganEntity> {

    @Autowired
    private OrganDao dao;

    @Override
    protected JpaRepository getDao() {
        return dao;
    }

    @Override
    protected OrganBo convertEntity(OrganEntity entity) {
        if(entity==null){
            return null;
        }
        OrganBo organBo = new OrganBo();
        BeanUtils.copyProperties(entity,organBo);
        return organBo;
    }



    @Override
    protected OrganEntity convertBo(OrganBo organBo) {
        OrganEntity organEntity = new OrganEntity();
        BeanUtils.copyProperties(organBo,organEntity);
        return organEntity;
    }

    public List<OrganBo> getByNames(List<String> nameList) {
        return dao.findByNameIn(nameList).stream().map(this::convertEntity).collect(Collectors.toList());
    }
}
