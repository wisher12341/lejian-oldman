package com.lejian.oldman.repository;

import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.bo.WorkerCheckinBo;
import com.lejian.oldman.exception.RepositoryException;
import com.lejian.oldman.utils.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractRepository<Bo,Entity> {


    public List<Bo> getAll() {
        List<Entity> entityList = getDao().findAll();
        return entityList.stream().map(this::convertEntity).collect(Collectors.toList());
    }

    protected abstract JpaRepository getDao();

    protected abstract Bo convertEntity(Entity entity);

    protected abstract Entity convertBo(Bo bo);

    public Bo getByPkId(Integer pkId) {
        Optional<Entity> optional =getDao().findById(pkId);
        if(optional.isPresent()){
            return convertEntity(optional.get());
        }
        throw new RepositoryException("getByPkId no data found,"+this.getClass().getSimpleName());
    }


    public void save(Bo bo) {
        Entity entity = convertBo(bo);
        getDao().save(entity);
    }


    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 动态根据主键id更新 过滤非null字段
     * jpa 拼接 update 语句
     * 要加事务  不然会报错
     * @param bo
     */
    @Transactional
    public void dynamicUpdateByPkId(Bo bo){
        try {

            String sqlFormat = "update %s set %s where id=%s";

            StringBuilder updateStr=new StringBuilder();
            Entity entity = convertBo(bo);
            Object pkId =null;
            Field[] fields = entity.getClass().getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
                Object fieldValue = field.get(entity);
                if(fieldValue!=null){
                    if(field.getName().equals("id")){
                        pkId = fieldValue;
                    }else {
                        updateStr.append(field.getName() + "='" + fieldValue + "'");
                        updateStr.append(",");
                    }
                }
            }
            updateStr.deleteCharAt(updateStr.length()-1);

            if(pkId ==null){
                throw new RepositoryException("dynamicUpdateByPkId, no pk id found,"+this.getClass().getSimpleName());
            }

            String sql = String.format(sqlFormat,
                    entity.getClass().getAnnotation(Table.class).name(),
                    StringUtils.camelToUnderline(updateStr.toString()),
                    pkId);

            Query query =entityManager.createNativeQuery(sql);
            query.executeUpdate();

        }catch (Exception e){
            throw new RepositoryException("dynamicUpdateByPkId,"+this.getClass().getSimpleName(),e);
        }
    }
}
