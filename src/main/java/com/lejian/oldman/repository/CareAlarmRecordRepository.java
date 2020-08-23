package com.lejian.oldman.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.bo.CareAlarmRecordBo;
import com.lejian.oldman.bo.WorkerBo;
import com.lejian.oldman.controller.contract.request.GetAlarmByPageRequest;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.dao.CareAlarmRecordDao;
import com.lejian.oldman.dao.WorkerDao;
import com.lejian.oldman.entity.CareAlarmRecordEntity;
import com.lejian.oldman.entity.WorkerEntity;
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
public class CareAlarmRecordRepository extends AbstractSpecificationRepository<CareAlarmRecordBo,CareAlarmRecordEntity>{

    @Autowired
    private CareAlarmRecordDao careAlarmRecordDao;

    @Override
    protected JpaRepository getDao() {
        return careAlarmRecordDao;
    }

    @Override
    protected CareAlarmRecordBo convertEntity(CareAlarmRecordEntity careAlarmRecordEntity) {
        CareAlarmRecordBo careAlarmRecordBo = new CareAlarmRecordBo();
        BeanUtils.copyProperties(careAlarmRecordEntity,careAlarmRecordBo);
        return careAlarmRecordBo;
    }


    @Override
    protected CareAlarmRecordEntity convertBo(CareAlarmRecordBo careAlarmRecordBo) {
        CareAlarmRecordEntity careAlarmRecordEntity = new CareAlarmRecordEntity();
        BeanUtils.copyProperties(careAlarmRecordBo,careAlarmRecordEntity);
        return careAlarmRecordEntity;
    }

    public Map<Integer, Map<Integer,Long>> getWarnCountByArea(String areaCustomOne) {
        Map<Integer, Map<Integer,Long>> map=Maps.newHashMap();
        try {
            String sql ="";
            if(StringUtils.isNotBlank(areaCustomOne)){
                sql=String.format("select `type`,is_handle,count(`type`) from care_alarm_record " +
                        "where oid in (select oid from oldman where area_custom_one='"+areaCustomOne+"')"+
                        " group by `type`,is_handle",areaCustomOne);
            }else{
                sql="select `type`,is_handle,count(`type`) from care_alarm_record group by `type`,is_handle";
            }
            Query query =entityManager.createNativeQuery(sql);
            query.getResultList().forEach(obj->{
                Object[] cells = (Object[]) obj;
                if(!map.containsKey(cells[0])){
                    Map<Integer,Long> m=Maps.newHashMap();
                    map.put((Integer)cells[0],m);
                }
                map.get(cells[0]).put(Integer.valueOf(String.valueOf(cells[1])),Long.valueOf(String.valueOf(cells[2])));
            });
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getWarnCountByArea",e);
        }
        return map;
    }

    public List<CareAlarmRecordBo> getAlarmByPage(PageParam pageParam, GetAlarmByPageRequest.AlarmSearchParam param) {
        try {
            long start=pageParam.getPageNo()*pageParam.getPageSize();

            String where = "";
            if(param!=null) {
                String oldmanWhere = "";
                String alaramWhere = "";
                if (StringUtils.isNotBlank(param.getAreaCustomOne())) {
                    oldmanWhere = "area_custom_one='" + param.getAreaCustomOne() + "'";
                }
                if (param.getType() != null) {
                    alaramWhere = "type=" + param.getType();
                }
                if (StringUtils.isNotBlank(oldmanWhere)) {
                    oldmanWhere = "oid in (select oid from oldman where " + oldmanWhere + ")";
                    where = "where " + oldmanWhere + "";
                }
                if (StringUtils.isNotBlank(alaramWhere)) {
                    if (StringUtils.isNotBlank(where)) {
                        where += " and " + alaramWhere;
                    } else {
                        where = "where " + alaramWhere;
                    }
                }

            }

            String sql =String.format("select * from care_alarm_record %s order by is_handle asc, id desc limit %s,%s",where,start,pageParam.getPageSize());

            Query query =entityManager.createNativeQuery(sql);
            return (List<CareAlarmRecordBo>) query.getResultList().stream().map(obj->{
                CareAlarmRecordBo careAlarmRecordBo=new CareAlarmRecordBo();
                Object[] cells = (Object[]) obj;
                careAlarmRecordBo.setType(Integer.valueOf(String.valueOf(cells[1])));
                careAlarmRecordBo.setOid(String.valueOf(cells[2]));
                careAlarmRecordBo.setContent(String.valueOf(cells[3]));
                careAlarmRecordBo.setHandle(String.valueOf(cells[4]));
                careAlarmRecordBo.setIsRead(Integer.valueOf(String.valueOf(cells[5])));
                careAlarmRecordBo.setIsHandle(Integer.valueOf(String.valueOf(cells[6])));
                return careAlarmRecordBo;
                }).collect(Collectors.toList());
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getWarnCountByArea",e);
        }
        return Lists.newArrayList();
    }

    @Transactional
    public void updateIsHandleByLocationId(Integer locationId, Integer isHandle) {
        careAlarmRecordDao.updateIsHandleByLocationId(locationId,isHandle);
    }

    public Long count() {
        return careAlarmRecordDao.count();
    }

    public Long getCount(OldmanSearchParam oldmanSearchParam) {
        try {
            String sql = "select count(1) from care_alarm_record ";

            if(oldmanSearchParam!=null && StringUtils.isNotBlank(oldmanSearchParam.getSql())){
                sql+="where oid in (select oid from oldman where "+oldmanSearchParam.getSql()+")";
            }

            Query query =entityManager.createNativeQuery(sql);
            return ((BigInteger) query.getResultList().get(0)).longValue();

        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getCount",e);
        }
        return 0L;
    }
}
