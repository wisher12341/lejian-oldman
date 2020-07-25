package com.lejian.oldman.service;

import com.lejian.oldman.bo.CareAlarmRecordBo;
import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.CareSystemEnum;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.repository.CareAlarmRecordRepository;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.utils.DateUtils;
import com.lejian.oldman.vo.OldmanVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.NO_DATA_FOUND;

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
        List<OldmanBo> oldmanBoList = oldmanRepository.findByPageWithSpec(pageNo,pageSize,convert(oldmanSearchParam));
        return oldmanBoList.stream().map(this::convert).collect(Collectors.toList());
    }


    /**
     * 根据条件， 获取老人数量
     * @param oldmanSearchParam
     * @return
     */
    public Long getOldmanCount(OldmanSearchParam oldmanSearchParam) {
        return oldmanRepository.countWithSpec(convert(oldmanSearchParam));
    }


    private JpaSpecBo convert(OldmanSearchParam oldmanSearchParam) {
        if(oldmanSearchParam == null){
            return null;
        }
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        jpaSpecBo.getEqualMap().put("oid",oldmanSearchParam.getOid());
        return jpaSpecBo;
    }
    private OldmanVo convert(OldmanBo oldmanBo) {
        OldmanVo oldmanVo = new OldmanVo();
        BeanUtils.copyProperties(oldmanBo,oldmanVo);
        oldmanVo.setBirthday(oldmanBo.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        oldmanVo.setAge(DateUtils.birthdayToAge(oldmanBo.getBirthday()));
        oldmanVo.setZodiac(DateUtils.getZodiac(oldmanBo.getBirthday()));
        oldmanVo.setConstellation(DateUtils.getConstellation(oldmanBo.getBirthday()));
        oldmanVo.setStatus(oldmanBo.getStatus().getDesc());
        oldmanVo.setSex(oldmanBo.getSex().getDesc());
        oldmanVo.setPolitics(oldmanBo.getPolitics().getDesc());
        oldmanVo.setEducation(oldmanBo.getEducation().getDesc());
        oldmanVo.setHouseholdType(oldmanBo.getHouseholdType().getDesc());
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
     * 3. 前端页面展示
     */
    public void alarm(String gatewayId, String type, String content) throws UnsupportedEncodingException {
        type=new String(type.getBytes("ISO-8859-1"),"UTF-8");
        content=new String(content.getBytes("ISO-8859-1"),"UTF-8");

        OldmanBo oldmanBo = oldmanRepository.findByCareGatewayId(gatewayId);
        NO_DATA_FOUND.checkNotNull(oldmanBo);
        //step 1
        oldmanRepository.updateStatusByCareGatewayId(gatewayId, OldmanEnum.Status.RED.getValue());

        //step 2
        CareAlarmRecordBo careAlarmRecordBo=new CareAlarmRecordBo();
        careAlarmRecordBo.setType(BusinessEnum.find(type, CareSystemEnum.AlarmType.class).getValue());
        careAlarmRecordBo.setContent(content);
        careAlarmRecordBo.setOid(oldmanBo.getOid());
        careAlarmRecordRepository.save(careAlarmRecordBo);

        //step 3

    }
}
