package com.lejian.oldman.repository;

import com.lejian.oldman.Dao.OldmanDao;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.entity.OldmanEntity;
import com.lejian.oldman.enums.OldmanStatusEnum;
import com.lejian.oldman.vo.OldmanVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return null;
    }



    @Override
    protected OldmanBo convertEntity(OldmanEntity oldmanEntity) {
        OldmanBo oldmanBo = new OldmanBo();
        BeanUtils.copyProperties(oldmanEntity,oldmanBo);
        oldmanBo.setStatus(OldmanStatusEnum.find(oldmanEntity.getStatus()));
        return oldmanBo;
    }

    public List<OldmanBo> findByStatus(List<Integer> statusList) {
        return oldmanDao.findByStatusIn(statusList).stream().map(this::convertEntity).collect(Collectors.toList());
    }

    public List<OldmanBo> findByLocationId(Integer locationId) {
        return oldmanDao.findByLocationId(locationId).stream().map(this::convertEntity).collect(Collectors.toList());
    }
}
