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
import com.lejian.oldman.utils.ObjectUtils;
import com.lejian.oldman.utils.UserUtils;
import com.lejian.oldman.vo.LocationVo;
import org.apache.commons.collections4.MapUtils;
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
import java.util.Iterator;
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
        return ObjectUtils.convert(oldmanBo,OldmanEntity.class);
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
        oldmanBo.setServiceStatusEnum(BusinessEnum.find(oldmanBo.getServiceStatus(),OldmanEnum.ServiceStatus.class));
        oldmanBo.setServiceTypeEnum(BusinessEnum.find(oldmanBo.getServiceType(),OldmanEnum.ServiceType.class));
        return oldmanBo;
    }

    public List<OldmanBo> findByStatus(List<Integer> statusList, Integer userId) {
        if (userId!=null){
            return oldmanDao.findByStatusInAndIsDeleteAndUserId(statusList,0,userId).stream().map(this::convertEntity).collect(Collectors.toList());
        }
        return oldmanDao.findByStatusInAndIsDelete(statusList,0).stream().map(this::convertEntity).collect(Collectors.toList());
    }

    public List<OldmanBo> findByLocationId(Integer locationId) {
        return oldmanDao.findByLocationIdAndIsDelete(locationId,0).stream().map(this::convertEntity).collect(Collectors.toList());
    }

    public OldmanBo findByOid(String oid) {
        return convertEntity(oldmanDao.findByOidAndIsDelete(oid,0));
    }

    public List<OldmanBo> findByFuzzyName(String value) {
        return oldmanDao.findByNameLikeAndIsDelete(value,0).stream().map(this::convertEntity).collect(Collectors.toList());

    }

    public OldmanBo findByName(String name) {
        return convertEntity(oldmanDao.findByNameAndIsDelete(name,0));
    }

    @Transactional
    public void updateStatusByCareGatewayId(Integer gatewayId, Integer status) {
        oldmanDao.updateStatusByCareGatewayId(gatewayId,status);
    }

    public OldmanBo findByCareGatewayId(Integer gatewayId) {
        return convertEntity(oldmanDao.findByCareGatewayIdAndIsDelete(gatewayId,0));
    }

    public Long countByStatus(Integer status) {
        return oldmanDao.countByStatusAndIsDelete(status,0);
    }


    public Map<String,Long> getGroupCount(String groupFieldName, Map<String, String> where) {
        try {
            Map<String,Long> map= Maps.newHashMap();
            Integer userId = UserUtils.getUserRoleId();
            StringBuilder whereCase=new StringBuilder("where is_delete=0 ");
            if (userId!=null){
                whereCase.append(" and user_id=").append(userId).append(" ");
            }
            if(MapUtils.isNotEmpty(where)){
                Iterator iterator= where.keySet().iterator();
                String key= (String) iterator.next();
                whereCase.append("and ").append(key).append("='").append(where.get(key)).append("'");
                while (iterator.hasNext()){
                    whereCase.append(" and ");
                    key= (String) iterator.next();
                    whereCase.append(key).append("='").append(where.get(key)).append("'");
                }
            };
            String sql = String.format("select %s,count(%s) from oldman %s group by %s",
                    groupFieldName,groupFieldName,whereCase,groupFieldName);
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
     *
     * @param areaCustomOne
     * @param areaVillage
     *@param areaTown
     * @param areaCountry
     * @return
     */
    public List<Long> getEquipCountByArea(String areaCustomOne, String areaVillage, String areaTown, String areaCountry) {
        return oldmanDao.getEquipCountByArea(areaCustomOne,areaVillage,areaTown,areaCountry);
    }

    /**
     * 获取全部的老人（包括被删除的）
     * @param oidList
     * @return
     */
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

    public List<OldmanBo> getBirthdayOldman(String date,String whereCase) {
        try {
            List<OldmanEntity> oldmanEntityList= Lists.newArrayList();
            String sql = "select * from oldman where birthday like '%"+date+"%' and is_delete=0";
            if (StringUtils.isNotBlank(whereCase)){
                sql+=" and "+whereCase;
            }
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

    public List<Integer> getLocationIdByArea(String areaCountry, String areaTown, String areaVillage, String areaCustomOne) {
        return oldmanDao.getLocationIdByArea(areaCountry,areaTown,areaVillage,areaCustomOne);
    }

    public Long getBirthdayOldmanCount(String birthdayLike,String whereCase) {
        try {
            String sql = "select count(1) from oldman where birthday like '%"+birthdayLike+"%' and is_delete=0 ";
            if (StringUtils.isNotBlank(whereCase)){
                sql+=" and "+whereCase;
            }
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

    @Transactional
    public void deleteByOid(String oid) {
        oldmanDao.deleteByOid(oid);
    }

    public Long getRzzCount(String where) {
        try {
            String sql = "select count(1) from rzz_oldman r left join oldman o on r.oid=o.oid" +
                    " where 1=1 ";
            if (StringUtils.isNotBlank(where)){
                sql+=" and "+where;
            }
            Query query =entityManager.createNativeQuery(sql);
            return ((BigInteger) query.getResultList().get(0)).longValue();
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getRzzCount",e);
        }
        return 0L;
    }

    public Long getDbCount(String where) {
        try {
            String sql = "select count(1) from db d left join oldman o on d.oid=o.oid" +
                    " where 1=1 ";
            if (StringUtils.isNotBlank(where)){
                sql+=" and "+where;
            }
            Query query =entityManager.createNativeQuery(sql);
            return ((BigInteger) query.getResultList().get(0)).longValue();
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getDbCount",e);
        }
        return 0L;
    }
}
