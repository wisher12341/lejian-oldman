package com.lejian.oldman.repository;

import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.bo.WorkerBo;
import com.lejian.oldman.dao.WorkerDao;
import com.lejian.oldman.entity.WorkerEntity;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.WorkerEnum;
import com.lejian.oldman.utils.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    public List<Map<String,Object>> getTypeCountByBeyond(String workerBeyond) {
        Integer userId = UserUtils.getUserRoleId();
        return workerDao.getTypeCountByBeyond(workerBeyond,userId);
    }

    public List<WorkerBo> getByIdCards(List<String> idCardList) {
        return workerDao.findByIdCardIn(idCardList).stream().map(this::convertEntity).collect(Collectors.toList());
    }
}
