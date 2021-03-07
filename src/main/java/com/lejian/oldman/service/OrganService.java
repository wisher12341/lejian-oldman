package com.lejian.oldman.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.bo.OrganBo;
import com.lejian.oldman.controller.contract.request.OrganParam;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.enums.OrganEnum;
import com.lejian.oldman.enums.WorkerEnum;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.repository.OrganRepository;
import com.lejian.oldman.repository.ServiceRepository;
import com.lejian.oldman.repository.VisualSettingRepository;
import com.lejian.oldman.vo.OldmanVo;
import com.lejian.oldman.vo.OrganVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrganService {

    @Autowired
    private OrganRepository repository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private VisualSettingRepository visualSettingRepository;
    @Autowired
    private OldmanRepository oldmanRepository;


    public List<OrganVo> getByPage(PageParam pageParam) {
        return repository.findByPageWithSpec(pageParam.getPageNo(), pageParam.getPageSize(), null).stream().map(this::convert).collect(Collectors.toList());
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


    /**
     * 获取机构服务的总人数
     * 归属地取后台配置数据
     */
    public Long getServiceCount() {
        String beyond = visualSettingRepository.getWorkerBeyond();
        return serviceRepository.countByOrganBeyond(beyond);
    }

    public Map<String, String> getServiceCountGroupByType() {
        Map<String, String> map = Maps.newHashMap();
        for (OrganEnum.Type type : OrganEnum.Type.values()) {
            map.put(type.getDesc(), "0");

        }
        String beyond = visualSettingRepository.getWorkerBeyond();
        List<Map<String, Object>> count = serviceRepository.getServiceCountGroupByType(beyond);

        count.forEach(item -> {
            Integer type = (Integer) item.get("type");
            Long b = ((BigInteger) item.get("count")).longValue();

            map.put(BusinessEnum.find(type, OrganEnum.Type.class).getDesc(), String.valueOf(b));
        });
        return map;
    }

    public Map<String, String> getOrganServiceCountByType(OrganParam organParam) {
        Map<String, String> map = Maps.newHashMap();
        String beyond = visualSettingRepository.getWorkerBeyond();
        List<Map<String, Object>> count = serviceRepository.getOrganServiceCountByType(BusinessEnum.find(organParam.getTypeDesc(), OrganEnum.Type.class).getValue(), beyond);
        count.forEach(item -> {
            String name = (String) item.get("name");
            Long b = ((BigInteger) item.get("count")).longValue();

            map.put(name, String.valueOf(b));
        });
        return map;
    }

    public Map<String, String> getServiceCountByOrgan(String organName) {
        Map<String, String> map = Maps.newHashMap();
        for (OrganEnum.ServiceType serviceType : OrganEnum.ServiceType.values()) {
            map.put(serviceType.getDesc(), "0");

        }
        List<Map<String, Object>> count = serviceRepository.getServiceCountByOrgan(organName);
        count.forEach(item -> {
            Integer type = (Integer) item.get("service_type");
            Long b = ((BigInteger) item.get("count")).longValue();

            map.put(BusinessEnum.find(type, OrganEnum.ServiceType.class).getDesc(), String.valueOf(b));
        });
        return map;
    }

    public List<OldmanVo> getServiceOldmanByPage(Integer pageNo, Integer pageSize, OrganParam organParam) {
        List<Integer> organIdList = Lists.newArrayList();
        Integer serviceType = 0;
        if (organParam != null) {
            if (StringUtils.isNotBlank(organParam.getTypeDesc())) {
                JpaSpecBo jpaSpecBo = new JpaSpecBo();
                jpaSpecBo.getEqualMap().put("type", BusinessEnum.find(organParam.getTypeDesc(), OrganEnum.Type.class).getValue());
                organIdList = repository.findWithSpec(jpaSpecBo).stream().map(OrganBo::getId).collect(Collectors.toList());
            } else if (StringUtils.isNotBlank(organParam.getName())) {
                JpaSpecBo jpaSpecBo = new JpaSpecBo();
                jpaSpecBo.getEqualMap().put("name", organParam.getName());
                organIdList = repository.findWithSpec(jpaSpecBo).stream().map(OrganBo::getId).collect(Collectors.toList());
            }
            serviceType = BusinessEnum.find(organParam.getServiceTypeDesc(), OrganEnum.ServiceType.class).getValue();
        }
        List<String> oidList = serviceRepository.getServiceOldmanByPage(pageNo, pageSize, organIdList, serviceType);
        return oldmanRepository.getByOids(oidList).stream().map(OldmanBo::createVo).collect(Collectors.toList());
    }

}
