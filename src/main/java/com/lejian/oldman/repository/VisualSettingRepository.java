package com.lejian.oldman.repository;

import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.bo.VisualSettingBo;
import com.lejian.oldman.dao.VisualSettingDao;
import com.lejian.oldman.entity.VisualSettingEntity;
import com.lejian.oldman.enums.UserEnum;
import com.lejian.oldman.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class VisualSettingRepository extends AbstractSpecificationRepository<VisualSettingBo, VisualSettingEntity> {

    @Autowired
    private VisualSettingDao visualSettingDao;

    private static final Integer USED = 1;

    @Override
    protected JpaRepository getDao() {
        return visualSettingDao;
    }

    @Override
    protected VisualSettingBo convertEntity(VisualSettingEntity entity) {
        VisualSettingBo bo = new VisualSettingBo();
        BeanUtils.copyProperties(entity, bo);
        bo.setIsUsed(entity.getIsUsed() != 0);
        return bo;
    }


    @Override
    protected VisualSettingEntity convertBo(VisualSettingBo bo) {
        VisualSettingEntity entity = new VisualSettingEntity();
        BeanUtils.copyProperties(bo, entity);
        if (bo.getIsUsed() != null) {
            entity.setIsUsed(bo.getIsUsed() ? 1 : 0);
        }
        return entity;
    }

    public void clearUsed() {
        UserBo userBo = UserUtils.getUser();
        int searchUserId= 0;
        if (userBo.getRole().intValue() == UserEnum.Role.USER.getValue()){
            searchUserId = userBo.getId();
        }
        visualSettingDao.clearUsed(searchUserId);
    }

    public String getWorkerBeyond() {
        UserBo userBo = UserUtils.getUser();
        int searchUserId= 0;
        if (userBo.getRole().intValue() == UserEnum.Role.USER.getValue()){
            searchUserId = userBo.getId();
        }
        VisualSettingEntity entity = visualSettingDao.findByIsUsedAndUserId(USED,searchUserId);
        StringBuilder str = new StringBuilder();
        if (StringUtils.isNotBlank(entity.getAreaCountry())) {
            str.append(entity.getAreaCountry()).append("-");
        }
        if (StringUtils.isNotBlank(entity.getAreaTown())) {
            str.append(entity.getAreaTown()).append("-");
        }
        if (StringUtils.isNotBlank(entity.getAreaVillage())) {
            str.append(entity.getAreaVillage()).append("-");
        }
        return str.toString().substring(0, str.length() - 1);
    }

    public VisualSettingBo getUsed() {
        UserBo userBo = UserUtils.getUser();
        int searchUserId= 0;
        if (userBo.getRole().intValue() == UserEnum.Role.USER.getValue()){
            searchUserId = userBo.getId();
        }
        return convertEntity(visualSettingDao.findByIsUsedAndUserId(USED,searchUserId));
    }
}
