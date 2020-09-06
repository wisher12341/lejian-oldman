package com.lejian.oldman.repository;

import com.lejian.oldman.bo.AreaBo;
import com.lejian.oldman.dao.AreaDao;
import com.lejian.oldman.entity.AreaEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class AreaRepository extends AbstractSpecificationRepository<AreaBo,AreaEntity> {

    @Autowired
    private AreaDao areaDao;

    @Override
    protected JpaRepository getDao() {
        return areaDao;
    }

    @Override
    protected AreaBo convertEntity(AreaEntity entity) {
        return null;
    }


    @Override
    protected AreaEntity convertBo(AreaBo areaBo) {
        return null;
    }

}
