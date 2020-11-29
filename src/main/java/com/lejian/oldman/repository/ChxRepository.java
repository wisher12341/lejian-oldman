package com.lejian.oldman.repository;

import com.lejian.oldman.bo.ChxBo;
import com.lejian.oldman.bo.WorkerBo;
import com.lejian.oldman.dao.ChxDao;
import com.lejian.oldman.dao.WorkerDao;
import com.lejian.oldman.entity.ChxEntity;
import com.lejian.oldman.entity.WorkerEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ChxRepository extends AbstractSpecificationRepository<ChxBo,ChxEntity>{

    @Autowired
    private ChxDao chxDao;

    @Override
    protected JpaRepository getDao() {
        return chxDao;
    }

    @Override
    protected ChxBo convertEntity(ChxEntity entity) {
        ChxBo bo = new ChxBo();
        BeanUtils.copyProperties(entity,bo);
        return bo;
    }


    @Override
    protected ChxEntity convertBo(ChxBo chxBo) {
        ChxEntity entity = new ChxEntity();
        BeanUtils.copyProperties(chxBo,entity);
        return entity;
    }


    public List<ChxBo> getByOids(List<String> oidList) {
        return chxDao.findByOidIn(oidList).stream().map(this::convertEntity).collect(Collectors.toList());
    }
}
