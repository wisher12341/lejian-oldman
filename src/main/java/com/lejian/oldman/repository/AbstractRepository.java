package com.lejian.oldman.repository;

import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.utils.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.REPOSITORY_ERROR;

public abstract class AbstractRepository<Bo,Entity> {



    @PersistenceContext
    protected EntityManager entityManager;


    public List<Bo> getAll() {
        List<Entity> entityList = getDao().findAll();
        return entityList.stream().map(this::convertEntity).collect(Collectors.toList());
    }

    protected abstract JpaRepository getDao();

    protected abstract Bo convertEntity(Entity entity);

    protected abstract Entity convertBo(Bo bo);

    public Bo getByPkId(Integer pkId) {
        Optional<Entity> optional =getDao().findById(pkId);
        REPOSITORY_ERROR.check(optional.isPresent(),
                "getByPkId no data found,id:"+pkId+","+this.getClass().getSimpleName());
        return convertEntity(optional.get());
    }


    public void save(Bo bo) {
        Entity entity = convertBo(bo);
        getDao().save(entity);
    }

    @Transactional
    public void dynamicUpdateByPkId(Bo bo){
        dynamicUpdate(bo,"id");
    }

    /**
     * 动态根据某个值更新 过滤非null字段
     * jpa 拼接 update 语句
     * 要加事务  不然会报错
     * @param bo
     */
    @Transactional
    public void dynamicUpdate(Bo bo,String fieldName){
        try {
            String sqlFormat = "update %s set %s where %s='%s'";

            StringBuilder updateStr=new StringBuilder();
            Entity entity = convertBo(bo);
            Object searchValue =null;
            Field[] fields = entity.getClass().getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
                Object fieldValue = field.get(entity);
                if(fieldValue!=null){
                    if(field.getName().equals(fieldName)){
                        searchValue = fieldValue;
                    }else {
                        updateStr.append(field.getName() + "='" + fieldValue + "'");
                        updateStr.append(",");
                    }
                }
            }
            updateStr.deleteCharAt(updateStr.length()-1);


            REPOSITORY_ERROR.checkNotNull(searchValue,
                    "dynamicUpdate, no searchValue id found,"+this.getClass().getSimpleName());

            String sql = String.format(sqlFormat,
                    entity.getClass().getAnnotation(Table.class).name(),
                    StringUtils.camelToUnderline(updateStr.toString()),
                    fieldName,searchValue);

            Query query =entityManager.createNativeQuery(sql);
            query.executeUpdate();

        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("dynamicUpdate,"+this.getClass().getSimpleName(),e);
        }
    }

    public List<Bo> getByPkIds(List<Integer> pkIds) {
        List<Entity> entityList =getDao().findAllById(pkIds);
        return entityList.stream().map(this::convertEntity).collect(Collectors.toList());
    }

    public Long count() {
        return getDao().count();
    }

    /**
     * 批量插入
     * @param boList
     */
    public void batchAdd(List<Bo> boList) {
        List<Entity> entityList = boList.stream().map(this::convertBo).collect(Collectors.toList());
        //saveAll  也可以用于更新， 但是更新是 null值的属性  相应字段会更新成null
        getDao().saveAll(entityList);
    }

    /**
     * 批量更新
     */
    public void batchUpdate(List<Bo> boList){
        boList.forEach(this::dynamicUpdateByPkId);
    }

}
