package com.lejian.oldman.repository;


import com.google.common.collect.Maps;
import com.lejian.oldman.bo.OrganBo;
import com.lejian.oldman.bo.RzzBo;
import com.lejian.oldman.dao.OrganDao;
import com.lejian.oldman.dao.RzzDao;
import com.lejian.oldman.entity.OrganEntity;
import com.lejian.oldman.entity.RzzEntity;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.enums.OrganEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
public class RzzRepository extends AbstractSpecificationRepository<RzzBo,RzzEntity> {

    @Autowired
    private RzzDao dao;

    @Override
    protected JpaRepository getDao() {
        return dao;
    }

    @Override
    protected RzzBo convertEntity(RzzEntity entity) {
        if(entity==null){
            return null;
        }
        RzzBo rzzBo = new RzzBo();
        BeanUtils.copyProperties(entity,rzzBo);
        return rzzBo;
    }



    @Override
    protected RzzEntity convertBo(RzzBo rzzBo) {
        RzzEntity rzzEntity = new RzzEntity();
        BeanUtils.copyProperties(rzzBo,rzzEntity);
        return rzzEntity;
    }

    public Map<Integer, Long> getMapCount(List<Integer> selectValue) {
        Map<Integer, Long> map = Maps.newHashMap();
        List<Map<String, Object>> result = dao.getMapCount(selectValue);
        result.forEach(item -> {
            Integer type = (Integer) item.get("type");
            Long b = ((BigInteger) item.get("count")).longValue();

            map.put(type, b);
        });
        return map;
    }
}
