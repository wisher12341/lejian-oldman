package com.lejian.oldman.repository;

import com.google.common.collect.Maps;
import com.lejian.oldman.dao.OldmanDao;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.entity.OldmanEntity;
import com.lejian.oldman.entity.WorkerCheckinEntity;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.REPOSITORY_ERROR;

@Repository
public class OldmanRepository extends AbstractSpecificationRepository<OldmanBo,OldmanEntity> {

    @Autowired
    private OldmanDao oldmanDao;


    @Override
    protected JpaRepository getDao() {
        return oldmanDao;
    }

    @Override
    protected OldmanEntity convertBo(OldmanBo oldmanBo) {
        OldmanEntity oldmanEntity= new OldmanEntity();
        BeanUtils.copyProperties(oldmanBo,oldmanEntity);
        if(oldmanBo.getEducation()!=null) {
            oldmanEntity.setEducation(oldmanBo.getEducation().getValue());
        }
        if(oldmanBo.getStatus()!=null) {
            oldmanEntity.setStatus(oldmanBo.getStatus().getValue());
        }
        return oldmanEntity;
    }

    @Override
    protected OldmanBo convertEntity(OldmanEntity oldmanEntity) {
        OldmanBo oldmanBo = new OldmanBo();
        if(oldmanEntity == null){
            return null;
        }
        BeanUtils.copyProperties(oldmanEntity,oldmanBo);
        oldmanBo.setStatus(BusinessEnum.find(oldmanEntity.getStatus(),OldmanEnum.Status.class));
        oldmanBo.setEducation(BusinessEnum.find(oldmanEntity.getEducation(),OldmanEnum.Education.class));
        oldmanBo.setHouseholdType(BusinessEnum.find(oldmanEntity.getHouseholdType(),OldmanEnum.HouseholdType.class));
        oldmanBo.setPolitics(BusinessEnum.find(oldmanEntity.getPolitics(),OldmanEnum.Politics.class));
        oldmanBo.setSex(BusinessEnum.find(oldmanEntity.getSex(),OldmanEnum.Sex.class));
        oldmanBo.setFamily(BusinessEnum.find(oldmanEntity.getFamily(),OldmanEnum.FamilyType.class));
        return oldmanBo;
    }

    public List<OldmanBo> findByStatus(List<Integer> statusList) {
        return oldmanDao.findByStatusIn(statusList).stream().map(this::convertEntity).collect(Collectors.toList());
    }

    public List<OldmanBo> findByLocationId(Integer locationId) {
        return oldmanDao.findByLocationId(locationId).stream().map(this::convertEntity).collect(Collectors.toList());
    }

    public OldmanBo findByOid(String oid) {
        return convertEntity(oldmanDao.findByOid(oid));
    }

    public List<OldmanBo> findByFuzzyName(String value) {
        return oldmanDao.findByNameLike(value).stream().map(this::convertEntity).collect(Collectors.toList());

    }

    public OldmanBo findByName(String name) {
        return convertEntity(oldmanDao.findByName(name));
    }

    @Transactional
    public void updateStatusByCareGatewayId(String gatewayId, Integer status) {
        oldmanDao.updateStatusByCareGatewayId(gatewayId,status);
    }

    public OldmanBo findByCareGatewayId(String gatewayId) {
        return convertEntity(oldmanDao.findByCareGatewayId(gatewayId));
    }

    public Long countByStatus(Integer status) {
        return oldmanDao.countByStatus(status);
    }


    public Map<String,Long> getGroupCount(String groupFieldName) {
        try {
            Map<String,Long> map= Maps.newHashMap();
            String sql = String.format("select %s,count(%s) from oldman group by %s",groupFieldName,groupFieldName,groupFieldName);
            Query query =entityManager.createNativeQuery(sql);
            query.getResultList().forEach(object->{
                Object[] cells = (Object[]) object;
                map.put((String) cells[0],Long.valueOf(String.valueOf(cells[1])));
            });
            return map;
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getGroupCount",e);
        }
        return Maps.newHashMap();
    }

    /**
     * 1. 长者关怀系统
     * 2. 摄像头系统
     * 3. 想家宝系统
     * @param areaCustomOne
     * @return
     */
    public List<Long> getEquipCountByArea(String areaCustomOne) {
        return oldmanDao.getEquipCountByArea(areaCustomOne);
    }
}
