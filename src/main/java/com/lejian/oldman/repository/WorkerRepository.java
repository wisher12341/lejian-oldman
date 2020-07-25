package com.lejian.oldman.repository;

import com.lejian.oldman.bo.WorkerBo;
import com.lejian.oldman.dao.WorkerDao;
import com.lejian.oldman.entity.WorkerEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class WorkerRepository extends AbstractSpecificationRepository<WorkerBo,WorkerEntity>{

    @Autowired
    private WorkerDao workerDao;

    @Override
    protected JpaRepository getDao() {
        return workerDao;
    }

    @Override
    protected WorkerBo convertEntity(WorkerEntity workerEntity) {
        if(workerEntity ==null){
            return null;
        }
        WorkerBo workerBo = new WorkerBo();
        BeanUtils.copyProperties(workerEntity,workerBo);
        return workerBo;
    }

    public WorkerBo getWorkerByUid(Integer userId) {
        return convertEntity(workerDao.findByUserId(userId));
    }

    @Override
    protected WorkerEntity convertBo(WorkerBo workerBo) {
        WorkerEntity workerEntity = new WorkerEntity();
        BeanUtils.copyProperties(workerBo,workerEntity);
        return workerEntity;
    }
}
