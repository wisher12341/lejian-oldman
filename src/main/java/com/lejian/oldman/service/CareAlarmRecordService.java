package com.lejian.oldman.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.bo.CareAlarmRecordBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.controller.contract.request.GetAlarmByPageRequest;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.CareSystemEnum;
import com.lejian.oldman.repository.CareAlarmRecordRepository;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.utils.DateUtils;
import com.lejian.oldman.vo.CareAlarmRecordVo;
import com.lejian.oldman.vo.OldmanVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CareAlarmRecordService {

    @Autowired
    private CareAlarmRecordRepository careAlarmRecordRepository;
    @Autowired
    private OldmanRepository oldmanRepository;

    /**
     * 获取所有类型的数量
     * @param areaCustomOne
     * @return
     */
    public Map<String, Map<String,Long>> getAllTypeCount(String areaCustomOne) {
        Map<String, Map<String,Long>> map= Maps.newHashMap();
        Map<String,Long> map2=Maps.newHashMap();
        for(BusinessEnum businessEnum:CareSystemEnum.HandleType.values()){
            map2.put(businessEnum.getDesc(),0L);
        }
        for(BusinessEnum businessEnum:CareSystemEnum.AlarmType.values()){
            map.put(businessEnum.getDesc(),map2);
        }
        Map<Integer, Map<Integer,Long>> countMap= careAlarmRecordRepository.getWarnCountByArea(areaCustomOne);

        if(MapUtils.isNotEmpty(countMap)){
            countMap.forEach((k,v)->{
                BusinessEnum careSystemEnum= BusinessEnum.find(k,CareSystemEnum.AlarmType.class);
                if(careSystemEnum!=BusinessEnum.DefaultValue.NULL){
                    Map<String,Long> secondMap=Maps.newHashMap(map2);
                    v.forEach((k2,v2)-> secondMap.put(BusinessEnum.find(k2,CareSystemEnum.HandleType.class).getDesc(),v2));
                    map.put(careSystemEnum.getDesc(),secondMap);
                }
            });
        }
        return map;
    }

    public List<CareAlarmRecordVo> getAlarmByPage(Boolean needOldmanInfo,
                                                  PageParam pageParam,
                                                  GetAlarmByPageRequest.AlarmSearchParam param) {
        List<CareAlarmRecordBo> careAlarmRecordBoList=
                careAlarmRecordRepository.getAlarmByPage(pageParam,param);
        if(CollectionUtils.isNotEmpty(careAlarmRecordBoList)) {
            if (needOldmanInfo) {
                Map<String,OldmanBo> oldmanBoMap=oldmanRepository.getByOids(careAlarmRecordBoList.stream().map(CareAlarmRecordBo::getOid).distinct().collect(Collectors.toList())).stream().collect(Collectors.toMap(OldmanBo::getOid, Function.identity()));
                return careAlarmRecordBoList.stream().map(item->convert(item,oldmanBoMap)).collect(Collectors.toList());
            }
            return careAlarmRecordBoList.stream().map(this::convert).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    private CareAlarmRecordVo convert(CareAlarmRecordBo careAlarmRecordBo, Map<String, OldmanBo> oldmanBoMap) {
        CareAlarmRecordVo careAlarmRecordVo=new CareAlarmRecordVo();
        BeanUtils.copyProperties(careAlarmRecordBo,careAlarmRecordVo);
        careAlarmRecordVo.setIsHandle(BusinessEnum.find(careAlarmRecordBo.getIsHandle(),CareSystemEnum.HandleType.class)==CareSystemEnum.HandleType.HANDLE);
        careAlarmRecordVo.setIsRead(BusinessEnum.find(careAlarmRecordBo.getIsRead(),CareSystemEnum.ReadType.class)==CareSystemEnum.ReadType.READ);
        if(MapUtils.isNotEmpty(oldmanBoMap)){
            OldmanBo oldmanBo=oldmanBoMap.get(careAlarmRecordBo.getOid());
            careAlarmRecordVo.setOldmanVo(convertOldman(oldmanBo));
        }
        return careAlarmRecordVo;
    }

    private CareAlarmRecordVo convert(CareAlarmRecordBo careAlarmRecordBo) {
        return convert(careAlarmRecordBo,null);
    }

    private OldmanVo convertOldman(OldmanBo oldmanBo) {
        OldmanVo oldmanVo = new OldmanVo();
        BeanUtils.copyProperties(oldmanBo,oldmanVo);
        oldmanVo.setBirthday(oldmanBo.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        oldmanVo.setAge(DateUtils.birthdayToAge(oldmanBo.getBirthday()));
        oldmanVo.setZodiac(DateUtils.getZodiac(oldmanBo.getBirthday()));
        oldmanVo.setConstellation(DateUtils.getConstellation(oldmanBo.getBirthday()));
        oldmanVo.setStatus(oldmanBo.getStatusEnum().getDesc());
        oldmanVo.setSex(oldmanBo.getSexEnum().getDesc());
        oldmanVo.setPolitics(oldmanBo.getPoliticsEnum().getDesc());
        oldmanVo.setEducation(oldmanBo.getEducationEnum().getDesc());
        oldmanVo.setHouseholdType(oldmanBo.getHouseholdTypeEnum().getDesc());
        oldmanVo.setFamily(oldmanBo.getFamilyEnum().getDesc());
        return oldmanVo;
    }

    public void updateHandleByLocationId(Integer locationId, Integer isHandle) {
        careAlarmRecordRepository.updateIsHandleByLocationId(locationId, isHandle);
    }


    public Long getCount(OldmanSearchParam oldmanSearchParam) {
        return careAlarmRecordRepository.getCount(oldmanSearchParam);
    }
}
