package com.lejian.oldman.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.bo.OrganBo;
import com.lejian.oldman.bo.RzzBo;
import com.lejian.oldman.check.bo.CheckResultBo;
import com.lejian.oldman.controller.contract.request.OrganParam;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.enums.*;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.repository.OrganRepository;
import com.lejian.oldman.repository.ServiceRepository;
import com.lejian.oldman.repository.VisualSettingRepository;
import com.lejian.oldman.utils.LjReflectionUtils;
import com.lejian.oldman.vo.OldmanVo;
import com.lejian.oldman.vo.OrganVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.lejian.oldman.common.ComponentRespCode.REFLECTION_ERROR;

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

    private static final int PART_NUM=100;

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

    @Transactional
    public List<CheckResultBo> addByExcel(Pair<List<String>, List<List<String>>> excelData) {
        List<String> titleList=excelData.getFirst();
        List<List<String>> valueList=excelData.getSecond();

        //todo
//        List<CheckResultBo> checkResultBoList=checkRzzImport(excelData);
        List<CheckResultBo> checkResultBoList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(checkResultBoList)){
            return checkResultBoList;
        }


        List<OrganBo> boList = Lists.newArrayList();

        IntStream.range(0,valueList.size()).forEach(item->{
            OrganBo organBo=new OrganBo();
            boList.add(organBo);
        });

        Map<String,Field> fieldMap= LjReflectionUtils.getFieldToMap(OrganBo.class);

        try {
            for (int i = 0; i < titleList.size(); i++) {
                ExcelEnum excelEnum = ExcelEnum.findFieldName(titleList.get(i),OrganExcelEnum.class);
                Field field = fieldMap.get(excelEnum.getFieldName());
                field.setAccessible(true);
                //纵向 遍历每个对象，一个属性一个属性 纵向赋值
                for (int j = 0; j < valueList.size(); j++) {
                    Object value=valueList.get(j).get(i);
                    if(StringUtils.isNotBlank(String.valueOf(value))) {
                        //转换成枚举值
                        Class<? extends BusinessEnum> enumClass = excelEnum.getEnumType();
                        if (enumClass != null) {
                            //需要 枚举转换
                            for (BusinessEnum businessEnum : enumClass.getEnumConstants()) {
                                if (businessEnum.getDesc().equals(value)) {
                                    value = businessEnum.getValue();
                                    break;
                                }
                            }
                        }
                        if (field.getType() == Integer.class){
                            field.set(boList.get(j), Integer.valueOf(String.valueOf(value)));
                        }else {
                            field.set(boList.get(j), value);
                        }
                    }
                }
            }
        }catch (IllegalArgumentException | IllegalAccessException e){
            REFLECTION_ERROR.doThrowException("fail to addByExcel",e);
        }

        // left 添加 right更新
        Pair<List<OrganBo>,List<OrganBo>> pair = classifyDbType(boList);
        //todo 并非真正的 batch
        repository.batchAdd(pair.getFirst());
        repository.batchUpdate(pair.getSecond());

        return Lists.newArrayList();
    }


    private Pair<List<OrganBo>,List<OrganBo>> classifyDbType(List<OrganBo> boList) {
        List<OrganBo> addList=Lists.newArrayList();
        List<OrganBo> updateList=Lists.newArrayList();

        List<List<OrganBo>> parts = Lists.partition(boList, PART_NUM);
        parts.forEach(item->{
            List<String> nameList=item.stream().map(OrganBo::getName).collect(Collectors.toList());
            Map<String,OrganBo> existOrganMap=repository.getByNames(nameList).stream().collect(Collectors.toMap(OrganBo::getName, Function.identity()));
            item.forEach(organ->{
                if(existOrganMap.containsKey(organ.getName())){
                    organ.setId(existOrganMap.get(organ.getName()).getId());
                    updateList.add(organ);
                } else{
                    addList.add(organ);
                }
            });
        });
        return Pair.of(addList,updateList);
    }
}
