package com.lejian.oldman.repository;


import com.lejian.oldman.bo.OrganServiceBo;
import com.lejian.oldman.dao.OrganServiceDao;
import com.lejian.oldman.entity.OrganServiceEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ServiceRepository extends AbstractSpecificationRepository<OrganServiceBo,OrganServiceEntity> {

    @Autowired
    private OrganServiceDao dao;

    @Override
    protected JpaRepository getDao() {
        return dao;
    }

    @Override
    protected OrganServiceBo convertEntity(OrganServiceEntity entity) {
        if(entity==null){
            return null;
        }
        OrganServiceBo serviceBo = new OrganServiceBo();
        BeanUtils.copyProperties(entity,serviceBo);
        return serviceBo;
    }



    @Override
    protected OrganServiceEntity convertBo(OrganServiceBo serviceBo) {
        OrganServiceEntity serviceEntity = new OrganServiceEntity();
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
