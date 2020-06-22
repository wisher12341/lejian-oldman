package com.lejian.oldman.repository;


import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.exception.PendingException;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.MapUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            throw new PendingException("findByPageWithSpec",e);
        }
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
        if(!MapUtils.isEmpty(jpaSpecBo.getEqualMap())){
            jpaSpecBo.getEqualMap().forEach((k,v)->{
                if(!ObjectUtils.isEmpty(v)) {
                    predicateList.add(criteriaBuilder.equal(root.get(k), v));
                }
            });
        }
        return predicateList;
    }


    private JpaSpecificationExecutor getSpecDao(){
        return (JpaSpecificationExecutor)getDao();
    }

}
