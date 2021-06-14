package com.lejian.oldman.repository;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.bo.RzzBo;
import com.lejian.oldman.bo.WorkerDispatchBo;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.dao.RzzDao;
import com.lejian.oldman.dao.WorkerDispatchDao;
import com.lejian.oldman.entity.RzzEntity;
import com.lejian.oldman.entity.WorkerDispatchEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.REPOSITORY_ERROR;

@Repository
public class WorkerDispatchRepository extends AbstractSpecificationRepository<WorkerDispatchBo,WorkerDispatchEntity> {

    @Autowired
    private WorkerDispatchDao dao;

    @Override
    protected JpaRepository getDao() {
        return dao;
    }

    @Override
    protected WorkerDispatchBo convertEntity(WorkerDispatchEntity entity) {
        if(entity==null){
            return null;
        }
        WorkerDispatchBo bo = new WorkerDispatchBo();
        BeanUtils.copyProperties(entity,bo);
        return bo;
    }



    @Override
    protected WorkerDispatchEntity convertBo(WorkerDispatchBo bo) {
        WorkerDispatchEntity entity = new WorkerDispatchEntity();
        BeanUtils.copyProperties(bo,entity);
        return entity;
    }

    @Transactional
    public void notFinishUpdate(Integer workerId, String currentTime) {
        dao.notFinishUpdate(workerId,currentTime);
    }
}
