package com.lejian.oldman.repository;

import com.lejian.oldman.bo.AreaBo;
import com.lejian.oldman.bo.VisualSettingBo;
import com.lejian.oldman.dao.AreaDao;
import com.lejian.oldman.dao.VisualSettingDao;
import com.lejian.oldman.entity.AreaEntity;
import com.lejian.oldman.entity.VisualSettingEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class VisualSettingRepository extends AbstractSpecificationRepository<VisualSettingBo,VisualSettingEntity> {

    @Autowired
    private VisualSettingDao visualSettingDao;

    @Override
    protected JpaRepository getDao() {
        return visualSettingDao;
    }

    @Override
    protected VisualSettingBo convertEntity(VisualSettingEntity entity) {
        VisualSettingBo bo =new VisualSettingBo();
        BeanUtils.copyProperties(entity,bo);
        return bo;
    }


    @Override
    protected VisualSettingEntity convertBo(VisualSettingBo bo) {
        return null;
    }

}
