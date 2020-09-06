package com.lejian.oldman.service;

import com.google.common.collect.Maps;
import com.lejian.oldman.bo.CareAlarmRecordBo;
import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.controller.contract.request.OldmanParam;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.CareSystemEnum;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.repository.CareAlarmRecordRepository;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.utils.DateUtils;
import com.lejian.oldman.vo.OldmanVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.NO_DATA_FOUND;
import static com.lejian.oldman.utils.DateUtils.YYMMDD;

@Slf4j
@Service
public class OldmanService {

    @Autowired
    private OldmanRepository oldmanRepository;
    @Autowired
    private CareAlarmRecordRepository careAlarmRecordRepository;

    /**
     * 分页获取老人信息
     * @param pageNo 页号
     * @param pageSize 大小
     * @param oldmanSearchParam 查询参数
     * @return
     */
    public List<OldmanVo> getOldmanByPage(Integer pageNo, Integer pageSize, OldmanSearchParam oldmanSearchParam) {
        List<OldmanBo> oldmanBoList = oldmanRepository.findByPageWithSpec(pageNo,pageSize,OldmanSearchParam.convert(oldmanSearchParam));
        return oldmanBoList.stream().map(this::convert).collect(Collectors.toList());
    }


    /**
     * 根据条件， 获取老人数量
     * @param oldmanSearchParam
     * @return
     */
    public Long getOldmanCount(OldmanSearchParam oldmanSearchParam) {
        return oldmanRepository.countWithSpec(OldmanSearchParam.convert(oldmanSearchParam));
    }

    public Long getOldmanCount() {
        return oldmanRepository.count();
    }

    private OldmanVo convert(OldmanBo oldmanBo) {
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

    /**
     * 只填充部分
     * @param oldmanBo
     * @return
     */
    private OldmanVo convertBrief(OldmanBo oldmanBo) {
        OldmanVo oldmanVo = new OldmanVo();
        oldmanVo.setOid(oldmanBo.getOid());
        oldmanVo.setName(oldmanBo.getName());
        return oldmanVo;
    }

    public List<OldmanVo> getOldmanByLocationId(Integer locationId) {
        return oldmanRepository.findByLocationId(locationId).stream().map(this::convert).collect(Collectors.toList());
    }


    public OldmanVo getOldmanByOid(String oid) {
        return convert(oldmanRepository.findByOid(oid));
    }

    /**
     * 模糊名称查询
     * @param value
     * @return
     */
    public List<OldmanVo> getOldmanByFuzzyName(String value) {
        return oldmanRepository.findByFuzzyName(value).stream().map(this::convertBrief).collect(Collectors.toList());

    }

    public OldmanVo getOldmanByName(String name) {
        return convertBrief(oldmanRepository.findByName(name));
    }


    /**
     * 长者关怀报警
     * 1. 老人状态变成红色
     * 2. 记录报警记录
     */
    public void alarm(Integer gatewayId, String type, String content) throws UnsupportedEncodingException {
        BusinessEnum alarmType=BusinessEnum.find(type, CareSystemEnum.AlarmType.class);
        if(alarmType==BusinessEnum.DefaultValue.NULL) {
            type = new String(type.getBytes("ISO-8859-1"), "UTF-8");
            alarmType=BusinessEnum.find(type, CareSystemEnum.AlarmType.class);
            content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
        }

        OldmanBo oldmanBo = oldmanRepository.findByCareGatewayId(gatewayId);
        NO_DATA_FOUND.checkNotNull(oldmanBo);
        //step 1
        oldmanRepository.updateStatusByCareGatewayId(gatewayId, OldmanEnum.Status.RED.getValue());

        //step 2
        CareAlarmRecordBo careAlarmRecordBo=new CareAlarmRecordBo();
        careAlarmRecordBo.setType(alarmType.getValue());
        careAlarmRecordBo.setContent(content);
        careAlarmRecordBo.setOid(oldmanBo.getOid());
        careAlarmRecordRepository.save(careAlarmRecordBo);


    }


    /**
     * 老人表， 根据 数据库字段 group 查数量
     * @param groupFieldName
     * @return
     */
    public Map<String, Long> getGroupCount(String groupFieldName) {
        return oldmanRepository.getGroupCount(groupFieldName);
    }

    public void updateStatusByLocationId(Integer locationId,Integer status) {
        oldmanRepository.updateStatusByLocationId(locationId,status);
    }

    public Long getEquipCount(OldmanSearchParam oldmanSearchParam) {
        oldmanSearchParam.setEquip(true);
        return oldmanRepository.countWithSpec(OldmanSearchParam.convert(oldmanSearchParam));
    }

    public List<OldmanVo> getBirthdayOldman(String date) {
        return oldmanRepository.getBirthdayOldman(date).stream().map(this::convert).collect(Collectors.toList());
    }

    public Long getBirthdayOldmanCount(String birthdayLike) {
        return oldmanRepository.getBirthdayOldmanCount(birthdayLike);
    }

    public Map<String, Long> getHomeServiceCount(OldmanSearchParam oldmanSearchParam) {
        Map<String, Long> map = Maps.newHashMap();
        for(OldmanEnum oldmanEnum:OldmanEnum.ServiceType.values()){
            if(oldmanEnum.getValue()<100) {
                map.put(oldmanEnum.getDesc(), 0L);
            }
        }
        Map<Integer, Long> mapInt =oldmanRepository.getOldmanGroup(oldmanSearchParam,"service_type");
        mapInt.forEach((k,v)->{
            BusinessEnum businessEnum=BusinessEnum.find(k,OldmanEnum.ServiceType.class);
            if(businessEnum!=BusinessEnum.DefaultValue.NULL) {
                List<OldmanEnum.ServiceType> serviceTypeList = ((OldmanEnum.ServiceType) businessEnum).map();
                serviceTypeList.forEach(item -> {
                    map.put(item.getDesc(), map.get(item.getDesc()) + v);
                });
            }

        });
        return map;
    }


    public void addOldman(OldmanParam oldmanParam) {
        oldmanRepository.save(convert(oldmanParam));
    }

    private OldmanBo convert(OldmanParam oldmanParam) {
        OldmanBo oldmanBo=new OldmanBo();
        BeanUtils.copyProperties(oldmanParam,oldmanBo);
        oldmanBo.setBirthday(DateUtils.stringToLocalDate(oldmanParam.getIdCard().substring(6,14),YYMMDD));
        oldmanBo.setOid(oldmanParam.getIdCard().substring(oldmanParam.getIdCard().length()-10,oldmanParam.getIdCard().length()));
        return oldmanBo;
    }
}
