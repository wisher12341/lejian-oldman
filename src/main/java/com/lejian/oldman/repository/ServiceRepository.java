package com.lejian.oldman.repository;


import com.lejian.oldman.bo.OrganBo;
import com.lejian.oldman.bo.ServiceBo;
import com.lejian.oldman.dao.OrganDao;
import com.lejian.oldman.dao.ServiceDao;
import com.lejian.oldman.entity.OrganEntity;
import com.lejian.oldman.entity.ServiceEntity;
import com.lejian.oldman.vo.OldmanVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public Long countByOrganBeyond(String beyond) {
        return dao.countByOrganBeyond(beyond);
    }

    public List<Map<String,Object>> getServiceCountGroupByType(String beyond) {
        return dao.getServiceCountGroupByType(beyond);
    }

    public List<Map<String,Object>> getOrganServiceCountByType(Integer type, String beyond) {
        return dao.getOrganServiceCountByType(type,beyond);
    }

    public List<Map<String,Object>> getServiceCountByOrgan(String name) {
        return dao.getServiceCountByOrgan(name);
    }

    public List<String> getServiceOldmanByPage(Integer pageNo, Integer pageSize, List<Integer> organIdList, Integer serviceType) {
        return dao.getServiceOldmanByPage(pageNo,pageSize,organIdList,serviceType);
    }
}
