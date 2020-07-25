package com.lejian.oldman.repository;


import com.google.common.collect.Lists;
import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.exception.BizException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.REPOSITORY_ERROR;

/**
 * 动态查询
 * @param <Bo>
 * @param <Entity>
 */
public abstract class AbstractSpecificationRepository<Bo,Entity> extends AbstractRepository<Bo,Entity>{


    /**
     * 根据bo查询equal条件 分页获取数据
     * @param pageNo
     * @param pageSize
     * @param jpaSpecBo
     * @return
     */
    public List<Bo> findByPageWithSpec(Integer pageNo, Integer pageSize, JpaSpecBo jpaSpecBo) {
        try {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<Entity> page = getSpecDao().findAll(createSpec(jpaSpecBo), pageable);
            return page.get().map(this::convertEntity).collect(Collectors.toList());
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("findByPageWithSpec",e);
        }
        return Lists.newArrayList();
    }

    /**
     * 根据bo查询equal条件 获取数据
     * @param jpaSpecBo
     * @return
     */
    public List<Bo> findWithSpec(JpaSpecBo jpaSpecBo) {
        try {
            List<Entity> entityList = getSpecDao().findAll(createSpec(jpaSpecBo));
            return entityList.stream().map(this::convertEntity).collect(Collectors.toList());
        }catch (Exception e){
            REPOSITORY_ERROR.doThrowException("findWithSpec",e);
        }
        return Lists.newArrayList();
    }


    /**
     * 根据equal条件获取数量
     * @param jpaSpecBo
     * @return
     */
    public long countWithSpec(JpaSpecBo jpaSpecBo) {
        return getSpecDao().count(createSpec(jpaSpecBo));
    }

    /**
     * 生成Specification
     * @param jpaSpecBo
     * @return
     */
    private Specification createSpec(JpaSpecBo jpaSpecBo){
        if(jpaSpecBo == null){
            return null;
        }
        return (Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = createPredicate(jpaSpecBo, criteriaBuilder, root);
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }


    /**
     * 生成 查询条件
     * @param jpaSpecBo
     * @param criteriaBuilder
     * @param root
     * @return
     */
    private List<Predicate> createPredicate(JpaSpecBo jpaSpecBo, CriteriaBuilder criteriaBuilder, Root<Entity> root){
        List<Predicate> predicateList = new ArrayList<>();
        jpaSpecBo.getEqualMap().forEach((k,v)->{
            if(!ObjectUtils.isEmpty(v)) {
                predicateList.add(criteriaBuilder.equal(root.get(k), v));
            }
        });
        jpaSpecBo.getInMap().forEach((k,v)->{
            if (CollectionUtils.isNotEmpty(v)){
                CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get(k));
                predicateList.add(in);
            }
        });
        jpaSpecBo.getGreatEMap().forEach((k,v)->{
            if(!ObjectUtils.isEmpty(v)) {
                if(v instanceof Timestamp) {
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get(k), (Timestamp) v));
                }else{
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get(k), (String) v));
                }
            }
        });
        jpaSpecBo.getLessEMap().forEach((k,v)->{
            if(!ObjectUtils.isEmpty(v)) {
                if(v instanceof Timestamp) {
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get(k), (Timestamp) v));
                }else{
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get(k), (String) v));
                }
            }
        });
        return predicateList;
    }


    private JpaSpecificationExecutor getSpecDao(){
        return (JpaSpecificationExecutor)getDao();
    }

}
