package com.lejian.oldman.service;

import com.lejian.oldman.bo.VisualSettingBo;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.repository.VisualSettingRepository;
import com.lejian.oldman.vo.UserVo;
import com.lejian.oldman.vo.VisualSettingVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisualSettingService {

    @Autowired
    private VisualSettingRepository repository;


    public List<VisualSettingVo> getByPage(PageParam pageParam) {
        return repository.findByPageWithSpec(pageParam.getPageNo(),pageParam.getPageSize(),null).stream().map(this::convert).collect(Collectors.toList());
    }

    private VisualSettingVo convert(VisualSettingBo bo){
        VisualSettingVo vo = new VisualSettingVo();
        BeanUtils.copyProperties(bo,vo);
        return vo;
    }
}
