package com.lejian.oldman.repository;


import com.lejian.oldman.bo.OrganBo;
import com.lejian.oldman.bo.ServiceBo;
import com.lejian.oldman.dao.OrganDao;
import com.lejian.oldman.dao.ServiceDao;
import com.lejian.oldman.entity.OrganEntity;
import com.lejian.oldman.entity.ServiceEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ServiceRepository extends AbstractSpecificationRepository<ServiceBo,ServiceEntity> {

    @Autowired
    private ServiceDao dao;

    @Override
    protected JpaRepository getDao() {
        return dao;
    }

    @Override
    protected ServiceBo convertEntity(ServiceEntity entity) {
        if(entity==null){
            return null;
        }
        ServiceBo serviceBo = new ServiceBo();
        BeanUtils.copyProperties(entity,serviceBo);
        return serviceBo;
    }



    @Override
    protected ServiceEntity convertBo(ServiceBo serviceBo) {
        ServiceEntity serviceEntity = new ServiceEntity();
        BeanUtils.copyProperties(serviceBo,serviceEntity);
        return serviceEntity;
    }

    public Map<String,String> getServiceTypeCount(Integer id) {
        return dao.getServiceTypeCount(id);
    }
}
