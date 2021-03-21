package com.lejian.oldman.repository;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.bo.OrganBo;
import com.lejian.oldman.bo.RzzBo;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.dao.OrganDao;
import com.lejian.oldman.dao.RzzDao;
import com.lejian.oldman.entity.OrganEntity;
import com.lejian.oldman.entity.RzzEntity;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.enums.OrganEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.REPOSITORY_ERROR;

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

    public Map<Integer, Long> getMapCount(List<Integer> selectValue, String areaCountry, String areaTown, String areaVillage, String areaCustomOne) {
        Map<Integer, Long> map = Maps.newHashMap();
        List<Map<String, Object>> result = dao.getMapCount(selectValue,areaCountry,areaTown,areaVillage,areaCustomOne);
        result.forEach(item -> {
            Integer type = (Integer) item.get("type");
            Long b = ((BigInteger) item.get("count")).longValue();

            map.put(type, b);
        });
        return map;
    }

    public List<RzzBo> getByOids(List<String> oidList) {
        return dao.findByOidIn(oidList).stream().map(this::convertEntity).collect(Collectors.toList());
    }

    public List<String> getRzzOldmanByPage(Integer pageNo, Integer pageSize, OldmanSearchParam oldmanSearchParam,List<Integer> typeValue) {
        try {
            List<String> oidList = Lists.newArrayList();
            String where="";
            if(oldmanSearchParam!=null && StringUtils.isNotBlank(oldmanSearchParam.getSql("o."))){
                where+=oldmanSearchParam.getSql("o.");
            }
            String sql = String.format("select r.oid from rzz_oldman r left join oldman o on o.oid=r.oid where r.type in (%s) and %s limit %s,%s"
                    ,StringUtils.join(typeValue, ","),where,pageNo*pageSize,pageSize);
            Query query =entityManager.createNativeQuery(sql);
            query.getResultList().forEach(object->{
                oidList.add(String.valueOf(object));
            });
            return oidList;
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getRzzOldmanByPage",e);
        }
        return Lists.newArrayList();
    }

}
