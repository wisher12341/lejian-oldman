package com.lejian.oldman.service;

import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.bo.VisualSettingBo;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.controller.contract.request.VisualSettingParam;
import com.lejian.oldman.enums.UserEnum;
import com.lejian.oldman.repository.VisualSettingRepository;
import com.lejian.oldman.utils.UserUtils;
import com.lejian.oldman.vo.UserVo;
import com.lejian.oldman.vo.VisualSettingVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisualSettingService {

    @Autowired
    private VisualSettingRepository repository;


    public List<VisualSettingVo> getByPage(PageParam pageParam) {
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        UserBo userBo = UserUtils.getUser();
        if (userBo.getRole().intValue() == UserEnum.Role.USER.getValue()) {
            jpaSpecBo.getEqualMap().put("userId", userBo.getId());
        }else {
            jpaSpecBo.getEqualMap().put("userId", 0);
        }
        List<VisualSettingVo> visualSettingVoList= repository.findByPageWithSpec(pageParam.getPageNo(),pageParam.getPageSize(),jpaSpecBo)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
        if (pageParam.getSort()==null || !pageParam.getSort()){
            return visualSettingVoList;
        }
        VisualSettingVo visualSettingVo = visualSettingVoList.stream().filter(item->item.getIsUsed().equals("是")).findFirst().orElse(null);
        if (visualSettingVo !=null){
            visualSettingVoList.remove(visualSettingVo);
            visualSettingVoList.add(0,visualSettingVo);
        }
        return visualSettingVoList;
    }

    private VisualSettingVo convert(VisualSettingBo bo){
        VisualSettingVo vo = new VisualSettingVo();
        BeanUtils.copyProperties(bo,vo);
        vo.setIsUsed(bo.getIsUsed()?"是":"否");
        return vo;
    }

    private VisualSettingBo convert(VisualSettingParam visualSettingParam){
        VisualSettingBo bo = new VisualSettingBo();
        BeanUtils.copyProperties(visualSettingParam,bo);
        return bo;
    }

    public Long getCount() {
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        UserBo userBo = UserUtils.getUser();
        if (userBo.getRole().intValue() == UserEnum.Role.USER.getValue()) {
            jpaSpecBo.getEqualMap().put("userId", userBo.getId());
        }else {
            jpaSpecBo.getEqualMap().put("userId", 0);
        }
        return repository.countWithSpec(jpaSpecBo);
    }

    @Transactional
    public void add(VisualSettingParam visualSettingParam) {
        VisualSettingBo bo=convert(visualSettingParam);
        UserBo userBo = UserUtils.getUser();
        bo.setUserId(userBo.getId());
        repository.save(bo);
    }

    @Transactional
    public void edit(VisualSettingParam visualSettingParam) {
        VisualSettingBo bo=convert(visualSettingParam);
        repository.dynamicUpdateByPkId(bo);
    }


    public void delete(Integer id) {
        repository.deleteById(id);
    }


    public VisualSettingVo getById(Integer id) {
        VisualSettingBo visualSettingBo=repository.getByPkId(id);
        return convert(visualSettingBo);
    }

    @Transactional
    public void select(Integer id) {
        repository.clearUsed();
        VisualSettingBo visualSettingBo = new VisualSettingBo();
        visualSettingBo.setId(id);
        visualSettingBo.setIsUsed(true);
        UserBo userBo = UserUtils.getUser();
        visualSettingBo.setUserId(userBo.getId());
        repository.dynamicUpdateByPkId(visualSettingBo);

    }


}
