package com.lejian.oldman.repository;

import com.google.common.collect.Lists;
import com.lejian.oldman.bo.WorkerCheckinBo;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.dao.WorkerCheckinDao;
import com.lejian.oldman.entity.WorkerCheckinEntity;
import com.lejian.oldman.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.REPOSITORY_ERROR;
import static com.lejian.oldman.utils.DateUtils.YYMMDDHHMMSS;

@Slf4j
@Repository
public class WorkerCheckinRepository extends AbstractSpecificationRepository<WorkerCheckinBo,WorkerCheckinEntity> {

    @Autowired
    private WorkerCheckinDao workerCheckinDao;

    @Override
    protected JpaRepository getDao() {
        return workerCheckinDao;
    }

    @Override
    protected WorkerCheckinBo convertEntity(WorkerCheckinEntity workerCheckinEntity) {
        if(workerCheckinEntity==null){
            return null;
        }
        WorkerCheckinBo workerCheckinBo=new WorkerCheckinBo();
        BeanUtils.copyProperties(workerCheckinEntity,workerCheckinBo);
        return workerCheckinBo;
    }

    public WorkerCheckinBo getLatestRecordByWid(Integer workerId) {
        return convertEntity(workerCheckinDao.findFirstByWorkerIdOrderByIdDesc(workerId));
    }

    @Override
    protected WorkerCheckinEntity convertBo(WorkerCheckinBo workerCheckinBo) {
        WorkerCheckinEntity workerCheckinEntity=new WorkerCheckinEntity();
        BeanUtils.copyProperties(workerCheckinBo,workerCheckinEntity);
        return workerCheckinEntity;
    }


    /**
     * 分页获取时间范围内，服务人员签到最近时间
     * @param startTime
     * @param endTime
     * @param pageParam
     * @return
     */
    public List<WorkerCheckinBo> getLatestTimeByTimeAndPage(String startTime, String endTime,PageParam pageParam) {
        try {
            String sql = String.format("select worker_id,max(create_time) as create_time from worker_checkin" +
                    " where create_time>='%s' and create_time<='%s' group by worker_id order by worker_id limit %s,%s",startTime,endTime,pageParam.getPageNo(),pageParam.getPageSize());
            Query query =entityManager.createNativeQuery(sql);
            return (List<WorkerCheckinBo>) query.getResultList().stream().map(object->{
                WorkerCheckinBo workerCheckinBo=new WorkerCheckinBo();
                Object[] cells = (Object[]) object;
                workerCheckinBo.setWorkerId((Integer) cells[0]);
                workerCheckinBo.setCreateTime(Timestamp.valueOf(String.valueOf(cells[1])));
                return workerCheckinBo;
            }).collect(Collectors.toList());
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getLatestTimeByTimeAndPageFromDb",e);
        }
        return Lists.newArrayList();
    }

    /**
     * 根据worker_id 和  create_time 获取多条记录
     * @param latestWorkerList
     * @return
     */
    public List<WorkerCheckinBo> getAllPositionByTime(List<WorkerCheckinBo> latestWorkerList) {
        try {
            StringBuilder sql = new StringBuilder("select * from worker_checkin where  ");

            latestWorkerList.forEach(bo->{
                WorkerCheckinEntity entity = convertBo(bo);
                sql.append(" (worker_id="+entity.getWorkerId()+" and create_time='"+ DateUtils.format(entity.getCreateTime(),YYMMDDHHMMSS)+"')");
                sql.append(" or");

            });
            sql.deleteCharAt(sql.length()-1).deleteCharAt(sql.length()-1);
            log.info("sql:"+sql);
            Query query =entityManager.createNativeQuery(sql.toString());
            log.info("result:"+query.getResultList().size());
            return (List<WorkerCheckinBo>) (query.getResultList()).stream().map(item->convertEntity(convertQuery(item))).collect(Collectors.toList());

        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getAllPositonByTime",e);
        }
        return Lists.newArrayList();
    }

    private WorkerCheckinEntity convertQuery(Object object) {
        Object[] cells = (Object[]) object;
        WorkerCheckinEntity workerCheckinEntity = new WorkerCheckinEntity();
        workerCheckinEntity.setId((Integer) cells[0]);
        workerCheckinEntity.setWorkerId((Integer) cells[1]);
        workerCheckinEntity.setOid((String) cells[2]);
        workerCheckinEntity.setLng((String) cells[3]);
        workerCheckinEntity.setLat((String) cells[4]);
        workerCheckinEntity.setCreateTime((Timestamp) cells[5]);
        return workerCheckinEntity;
    }

    public Long getCheckinCount(OldmanSearchParam oldmanSearchParam) {
        try {
            String sql = "select count(1) from worker_checkin ";

            if(oldmanSearchParam!=null && StringUtils.isNotBlank(oldmanSearchParam.getSql())){
                sql+="where oid in (select oid from oldman where "+oldmanSearchParam.getSql()+")";
            }

            Query query =entityManager.createNativeQuery(sql);
            return ((BigInteger) query.getResultList().get(0)).longValue();
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("getAllPositonByTime",e);
        }
        return 0L;
    }
}
