package com.lejian.oldman.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.dao.OldmanDao;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.entity.OldmanEntity;
import com.lejian.oldman.entity.WorkerCheckinEntity;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.vo.LocationVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
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
        if(oldmanBo.getEducationEnum()!=null) {
            oldmanEntity.setEducation(oldmanBo.getEducationEnum().getValue());
        }
        if(oldmanBo.getStatusEnum()!=null) {
            oldmanEntity.setStatus(oldmanBo.getStatusEnum().getValue());
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
        oldmanBo.setStatusEnum(BusinessEnum.find(oldmanEntity.getStatus(),OldmanEnum.Status.class));
        oldmanBo.setEducationEnum(BusinessEnum.find(oldmanEntity.getEducation(),OldmanEnum.Education.class));
        oldmanBo.setHouseholdTypeEnum(BusinessEnum.find(oldmanEntity.getHouseholdType(),OldmanEnum.HouseholdType.class));
        oldmanBo.setPoliticsEnum(BusinessEnum.find(oldmanEntity.getPolitics(),OldmanEnum.Politics.class));
        oldmanBo.setSexEnum(BusinessEnum.find(oldmanEntity.getSex(),OldmanEnum.Sex.class));
        oldmanBo.setFamilyEnum(BusinessEnum.find(oldmanEntity.getFamily(),OldmanEnum.FamilyType.class));
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
    public void updateStatusByCareGatewayId(Integer gatewayId, Integer status) {
        oldmanDao.updateStatusByCareGatewayId(gatewayId,status);
    }

    public OldmanBo findByCareGatewayId(Integer gatewayId) {
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

    public List<OldmanBo> getByOids(List<String> oidList) {
        return oldmanDao.findByOidIn(oidList).stream().map(this::convertEntity).collect(Collectors.toList());
    }

    @Transactional
    public void updateStatusByLocationId(Integer locationId, Integer status) {
        oldmanDao.updateStatusByLocationId(locationId,status);
    }

    public Map<Integer, Long> getOldmanGroup(OldmanSearchParam oldmanSearchParam, String groupField) {
        try {
            Map<Integer,Long> map= Maps.newHashMap();
            String where="";
            if(oldmanSearchParam!=null && StringUtils.isNotBlank(oldmanSearchParam.getSql())){
                where+="where ";
                where+=oldmanSearchParam.getSql();
            }
            String sql = String.format("select %s,count(%s) from oldman %s group by %s",groupField,groupField,where,groupField);
            Query query =entityManager.createNativeQuery(sql);
            query.getResultList().forEach(object->{
                Object[] cells = (Object[]) object;
                map.put((Integer) cells[0],Long.valueOf(String.valueOf(cells[1])));
            });
            return map;
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getGroupCount",e);
        }
        return Maps.newHashMap();
    }

    public List<OldmanBo> getBirthdayOldman(String date) {
        try {
            List<OldmanEntity> oldmanEntityList= Lists.newArrayList();
            String sql = "select * from oldman where birthday like '%"+date+"%'";
            Query query =entityManager.createNativeQuery(sql);
            query.getResultList().forEach(object->{
                Object[] cells = (Object[]) object;
                OldmanEntity entity=new OldmanEntity();
                entity.setOid((String) cells[1]);
                entity.setSex((Integer) cells[2]);
                entity.setName((String) cells[3]);
                entity.setBirthday(((Date) cells[4]).toLocalDate());
                oldmanEntityList.add(entity);
            });
            return oldmanEntityList.stream().map(this::convertEntity).collect(Collectors.toList());
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getBirthdayOldman",e);
        }
        return Lists.newArrayList();
    }

    public List<Integer> getLocationIdByAreaCustomOne(String areaCustomOne) {
        return oldmanDao.getLocationIdByAreaCustomOne(areaCustomOne);
    }

    public Long getBirthdayOldmanCount(String birthdayLike) {
        try {
            String sql = "select count(1) from oldman where birthday like '%"+birthdayLike+"%'";
            Query query =entityManager.createNativeQuery(sql);
            return ((BigInteger) query.getResultList().get(0)).longValue();
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getBirthdayOldmanCount",e);
        }
        return 0L;
    }

    public Map<Integer, Long> getHomeServiceCount(OldmanSearchParam oldmanSearchParam) {
        return null;
    }

    /**
     * 老人状态 黄变绿
     */
    @Transactional
    public void updateStatusYtoG() {
        oldmanDao.updateStatusYtoG();
    }
}
