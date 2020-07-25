package com.lejian.oldman.repository;

import com.lejian.oldman.bo.CareAlarmRecordBo;
import com.lejian.oldman.bo.WorkerBo;
import com.lejian.oldman.dao.CareAlarmRecordDao;
import com.lejian.oldman.dao.WorkerDao;
import com.lejian.oldman.entity.CareAlarmRecordEntity;
import com.lejian.oldman.entity.WorkerEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CareAlarmRecordRepository extends AbstractSpecificationRepository<CareAlarmRecordBo,CareAlarmRecordEntity>{

    @Autowired
    private CareAlarmRecordDao careAlarmRecordDao;

    @Override
    protected JpaRepository getDao() {
        return careAlarmRecordDao;
    }

    @Override
    protected CareAlarmRecordBo convertEntity(CareAlarmRecordEntity careAlarmRecordEntity) {
        CareAlarmRecordBo careAlarmRecordBo = new CareAlarmRecordBo();
        BeanUtils.copyProperties(careAlarmRecordEntity,careAlarmRecordBo);
        return careAlarmRecordBo;
    }


    @Override
    protected CareAlarmRecordEntity convertBo(CareAlarmRecordBo careAlarmRecordBo) {
        CareAlarmRecordEntity careAlarmRecordEntity = new CareAlarmRecordEntity();
        BeanUtils.copyProperties(careAlarmRecordBo,careAlarmRecordEntity);
        return careAlarmRecordEntity;
    }
}
