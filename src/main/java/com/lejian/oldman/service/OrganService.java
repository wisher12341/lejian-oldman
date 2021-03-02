package com.lejian.oldman.service;

import com.google.common.collect.Maps;
import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.OrganBo;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OrganEnum;
import com.lejian.oldman.repository.OrganRepository;
import com.lejian.oldman.repository.ServiceRepository;
import com.lejian.oldman.vo.OrganVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrganService {

    @Autowired
    private OrganRepository repository;
    @Autowired
    private ServiceRepository serviceRepository;


    public List<OrganVo> getByPage(PageParam pageParam) {
        return repository.findByPageWithSpec(pageParam.getPageNo(),pageParam.getPageSize(),null).stream().map(this::convert).collect(Collectors.toList());
    }

    private OrganVo convert(OrganBo organBo) {
        OrganVo organVo = new OrganVo();
        organVo.setName(organBo.getName());
        organVo.setType(BusinessEnum.find(organBo.getType(), OrganEnum.class).getDesc());
        organVo.setTypeId(organBo.getType());
        return organVo;
    }

    public Long getCount() {
        return 0L;
    }


    public Map<String, String> getServiceTypeCount(Integer id) {
        Map<String,String> map = Maps.newHashMap();
        Map<String,String> count = serviceRepository.getServiceTypeCount(id);
        count.keySet().forEach(key-> map.put(BusinessEnum.find(key, OrganEnum.ServiceType.class).getDesc(),count.get(key)));
        return map;
    }

    public Long getServiceCount() {
        return serviceRepository.count();
    }
}
