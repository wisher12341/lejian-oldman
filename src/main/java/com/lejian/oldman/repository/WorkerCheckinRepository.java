package com.lejian.oldman.repository;

import com.lejian.oldman.bo.WorkerCheckinBo;
import com.lejian.oldman.dao.WorkerCheckinDao;
import com.lejian.oldman.entity.WorkerCheckinEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class WorkerCheckinRepository extends AbstractRepository<WorkerCheckinBo,WorkerCheckinEntity> {

    @Autowired
    private WorkerCheckinDao workerCheckinDao;

    @Override
    protected JpaRepository getDao() {
        return workerCheckinDao;
    }

    @Override
    protected WorkerCheckinBo convertEntity(WorkerCheckinEntity workerCheckinEntity) {
        if(workerCheckinEntity==null){
            return null;
        }
        WorkerCheckinBo workerCheckinBo=new WorkerCheckinBo();
        BeanUtils.copyProperties(workerCheckinEntity,workerCheckinBo);
        workerCheckinBo.setCreateTime(workerCheckinEntity.getCreateTime().toLocalDateTime());
        return workerCheckinBo;
    }

    public WorkerCheckinBo getLatestRecordByWid(Integer workerId) {
        return convertEntity(workerCheckinDao.findFirstByWorkerIdOrderByIdDesc(workerId));
    }

    @Override
    protected WorkerCheckinEntity convertBo(WorkerCheckinBo workerCheckinBo) {
        WorkerCheckinEntity workerCheckinEntity=new WorkerCheckinEntity();
        BeanUtils.copyProperties(workerCheckinBo,workerCheckinEntity);
        return workerCheckinEntity;
    }
}
