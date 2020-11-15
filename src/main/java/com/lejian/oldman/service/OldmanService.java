package com.lejian.oldman.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Floats;
import com.lejian.oldman.bo.CareAlarmRecordBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.check.CheckProcessor;
import com.lejian.oldman.check.bo.CheckFieldBo;
import com.lejian.oldman.check.bo.CheckResultBo;
import com.lejian.oldman.check.bo.OldmanImportCheckBo;
import com.lejian.oldman.controller.contract.request.OldmanParam;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.enums.*;
import com.lejian.oldman.handler.BaiduMapHandler;
import com.lejian.oldman.repository.CareAlarmRecordRepository;
import com.lejian.oldman.repository.LocationRepository;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.utils.DateUtils;
import com.lejian.oldman.utils.LjReflectionUtils;
import com.lejian.oldman.utils.ObjectUtils;
import com.lejian.oldman.utils.tuple.Tuple3;
import com.lejian.oldman.vo.OldmanVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.lejian.oldman.common.ComponentRespCode.*;
import static com.lejian.oldman.utils.DateUtils.YYMMDD;

@Slf4j
@Service
public class OldmanService {

    @Autowired
    private OldmanRepository oldmanRepository;
    @Autowired
    private CareAlarmRecordRepository careAlarmRecordRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CheckProcessor checkProcessor;

    @Autowired
    private BaiduMapHandler baiduMapHandler;

    private static final int PART_NUM=100;

    /**
     * 分页获取老人信息
     * @param pageNo 页号
     * @param pageSize 大小
     * @param oldmanSearchParam 查询参数
     * @return
     */
    public List<OldmanVo> getOldmanByPage(Integer pageNo, Integer pageSize, OldmanSearchParam oldmanSearchParam) {
        List<OldmanBo> oldmanBoList = oldmanRepository.findByPageWithSpec(pageNo,pageSize,OldmanSearchParam.convert(oldmanSearchParam));
        return oldmanBoList.stream().map(OldmanBo::createVo).collect(Collectors.toList());
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
        return oldmanRepository.findByLocationId(locationId).stream().map(OldmanBo::createVo).collect(Collectors.toList());
    }


    public OldmanVo getOldmanByOid(String oid) {
        return OldmanBo.createVo(oldmanRepository.findByOid(oid));
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
    public Map<String, Long> getGroupCount(String groupFieldName,Map<String, String> where) {
        return oldmanRepository.getGroupCount(groupFieldName,where);
    }

    public void updateStatusByLocationId(Integer locationId,Integer status) {
        oldmanRepository.updateStatusByLocationId(locationId,status);
    }

    public Long getEquipCount(OldmanSearchParam oldmanSearchParam) {
        oldmanSearchParam.setEquip(true);
        return oldmanRepository.countWithSpec(OldmanSearchParam.convert(oldmanSearchParam));
    }

    public List<OldmanVo> getBirthdayOldman(String date) {
        return oldmanRepository.getBirthdayOldman(date).stream().map(OldmanBo::createVo).collect(Collectors.toList());
    }

    public Long getBirthdayOldmanCount(String birthdayLike, OldmanSearchParam oldmanSearchParam) {
        return oldmanRepository.getBirthdayOldmanCount(birthdayLike,oldmanSearchParam.getSql());
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

    /**
     * 老人excel表导入
     * @param excelData
     */
    @Transactional
    public List<CheckResultBo> addOldmanByExcel(Pair<List<String>, List<List<String>>> excelData) {
        List<String> titleList=excelData.getFirst();
        List<List<String>> valueList=excelData.getSecond();

        List<CheckResultBo> checkResultBoList=checkOldmanImport(excelData);
        if(CollectionUtils.isNotEmpty(checkResultBoList)){
            return checkResultBoList;
        }


        List<OldmanBo> oldmanBoList = Lists.newArrayList();

        IntStream.range(0,valueList.size()).forEach(item->{
            OldmanBo oldmanBo=new OldmanBo();
            oldmanBoList.add(oldmanBo);
        });

        Map<String,Field> fieldMap= LjReflectionUtils.getFieldToMap(OldmanBo.class);

        try {
            for (int i = 0; i < titleList.size(); i++) {
                ExcelEnum oldmanExcelEnum = ExcelEnum.findFieldName(titleList.get(i),OldmanExcelEnum.class);
                Field field = fieldMap.get(oldmanExcelEnum.getFieldName());
                field.setAccessible(true);
                //纵向 遍历每个对象，一个属性一个属性 纵向赋值
                for (int j = 0; j < valueList.size(); j++) {
                    Object value=valueList.get(j).get(i);
                    if(StringUtils.isNotBlank(String.valueOf(value))) {
                        //转换成枚举值
                        Class<? extends BusinessEnum> enumClass = oldmanExcelEnum.getEnumType();
                        if (enumClass != null) {
                            //需要 枚举转换
                            for (BusinessEnum businessEnum : enumClass.getEnumConstants()) {
                                if (businessEnum.getDesc().equals(value)) {
                                    value = businessEnum.getValue();
                                    break;
                                }
                            }
                        }
                        field.set(oldmanBoList.get(j), value);
                    }
                }
            }
        }catch (IllegalArgumentException | IllegalAccessException e){
            REFLECTION_ERROR.doThrowException("fail to addOldmanByExcel",e);
        }

        supplement(oldmanBoList);

        //todo 验证bo数据
        // left 添加 right更新
        Pair<List<OldmanBo>,List<OldmanBo>> pair = classifyDbType(oldmanBoList);
        //todo 并非真正的 batch
        oldmanRepository.batchAdd(pair.getFirst());
        oldmanRepository.batchUpdate(pair.getSecond());
        return Lists.newArrayList();
    }

    /**
     * Excel表格校验
     * @param excelData
     * @return
     */
    private List<CheckResultBo> checkOldmanImport(Pair<List<String>, List<List<String>>> excelData) {
        List<OldmanImportCheckBo> checkBoList=OldmanImportCheckBo.convert(excelData);
        List<CheckResultBo> checkResultBoList=checkProcessor.check(checkBoList);

        Map<Integer,CheckResultBo> checkResultBoMap=checkResultBoList.stream().collect(Collectors.toMap(CheckResultBo::getNumber,Function.identity()));

        /**
         * 校验坐标
         */
        checkBoList.forEach(bo->{
            if (StringUtils.isBlank(bo.getLocationAddress())){
                checkResultBoMap.get(bo.getNumCheck()).getCheckFieldBoList().add(new CheckFieldBo("坐标", LOCATION_CHECK.getDisplayMessage()));
                return;
            }
            if(StringUtils.isNotBlank(bo.getLng()) && StringUtils.isNotBlank(bo.getLat())){
                //todo 是否需要调用接口 验证 坐标正确？
                if(bo.getLng().startsWith("121.") && bo.getLat().startsWith("31.") && NumberUtils.isNumber(bo.getLng()) && NumberUtils.isNumber(bo.getLat())){

                }else {
                    checkResultBoMap.get(bo.getNumCheck()).getCheckFieldBoList().add(new CheckFieldBo("坐标", LOCATION_CHECK.getDisplayMessage()));
                }
                return;
            }
            if(StringUtils.isNotBlank(bo.getLocationAddress())){
                Pair<String,String> result=baiduMapHandler.geocoding(bo.getLocationAddress(),"上海市");
                if(result==null){
                    checkResultBoMap.get(bo.getNumCheck()).getCheckFieldBoList().add(new CheckFieldBo("坐标",LOCATION_CHECK.getDisplayMessage()));
                }
                return;
            }
            checkResultBoMap.get(bo.getNumCheck()).getCheckFieldBoList().add(new CheckFieldBo("坐标",LOCATION_CHECK.getDisplayMessage()));
        });

        // 属性名 改为 枚举中的表格文字
        checkResultBoList.forEach(a->{
            a.getCheckFieldBoList().forEach(b->{
                if (b.getName().equals("坐标")){
                    return;
                }
                b.setName(ExcelEnum.findColumnName(b.getName(),OldmanExcelEnum.class).getColumnName());
            });
        });
        return checkResultBoList;
    }

    /**
     * 区分 哪些老人 添加 哪些老人更新
     * @param oldmanBoList
     * @return
     */
    private Pair<List<OldmanBo>,List<OldmanBo>> classifyDbType(List<OldmanBo> oldmanBoList) {
        List<OldmanBo> addList=Lists.newArrayList();
        List<OldmanBo> updateList=Lists.newArrayList();

        List<List<OldmanBo>> parts = Lists.partition(oldmanBoList, PART_NUM);
        parts.forEach(item->{
            List<String> oidList=item.stream().map(OldmanBo::getOid).collect(Collectors.toList());
            Map<String,OldmanBo> existOldmanMap=oldmanRepository.getByOids(oidList).stream().collect(Collectors.toMap(OldmanBo::getOid, Function.identity()));
            item.forEach(oldman->{
                if(existOldmanMap.containsKey(oldman.getOid())){
                    oldman.setId(existOldmanMap.get(oldman.getOid()).getId());
                    updateList.add(oldman);
                } else{
                    addList.add(oldman);
                }
            });
        });
        return Pair.of(addList,updateList);
    }

    /**
     * 补全数据
     */
    private void supplement(List<OldmanBo> oldmanBoList){
        oldmanBoList.forEach(oldmanBo -> {
            oldmanBo.setBirthday(DateUtils.stringToLocalDate(oldmanBo.getIdCard().substring(6,14),YYMMDD));
            oldmanBo.setOid(oldmanBo.getIdCard().substring(oldmanBo.getIdCard().length()-10,oldmanBo.getIdCard().length()));
        });
        List<List<OldmanBo>> list= Lists.partition(oldmanBoList,200);
        list.forEach(item->{
            List<Tuple3<String,String,String>> tuple3List=Lists.newArrayList();
            item.forEach(bo-> tuple3List.add(Tuple3.of(bo.getLocationAddress(),bo.getLng(),bo.getLat())));
            // key 地址描述, value location id
            Map<String,Integer> map=locationRepository.getBatchByDescOrCreate(tuple3List);
            item.forEach(bo-> bo.setLocationId(map.get(bo.getLocationAddress())));
        });
    }

    public void editOldman(OldmanParam oldmanParam) {
        oldmanRepository.dynamicUpdate(convert(oldmanParam),"oid");
    }

    public Map<String,Long> getOldmanAreaGroupCount(String areaCountry, String areaTown, String areaVillage) {
        String groupField;
        Map<String,String> where=Maps.newHashMap();

        if (StringUtils.isNotBlank(areaVillage)){
            groupField="area_custom_one";
            where.put("area_village",areaVillage);
            where.put("area_town",areaTown);
            where.put("area_country",areaCountry);
        }else if (StringUtils.isNotBlank(areaTown)){
            groupField="area_village";
            where.put("area_town",areaTown);
            where.put("area_country",areaCountry);
        }else if (StringUtils.isNotBlank(areaCountry)){
            groupField="area_town";
            where.put("area_country",areaCountry);
        }else{
            groupField="area_country";
        }

        return this.getGroupCount(groupField,where);
    }

    public void deleteOldman(String oid) {
        oldmanRepository.deleteByOid(oid);
    }

    /**
     * 获取导出的数据
     * @param oldmanSearchParam
     * @return
     */
    public Pair<String[], String[][]> getExportOldmanInfo(OldmanSearchParam oldmanSearchParam) {
        try {
            OldmanExcelExportEnum[] oldmanExcelExportEnums = OldmanExcelExportEnum.values();
            String[] title = new String[oldmanExcelExportEnums.length];
            for(int i=0;i<oldmanExcelExportEnums.length;i++){
                title[i]=oldmanExcelExportEnums[i].getColumnName();
            }
            List<OldmanVo> oldmanVoList = oldmanRepository.findWithSpec(OldmanSearchParam.convert(oldmanSearchParam)).stream().map(OldmanBo::createVo).collect(Collectors.toList());
            String[][] content = new String[oldmanVoList.size()][title.length];
            for (int i = 0; i < oldmanVoList.size(); i++) {
                for (int j = 0; j < title.length; j++) {
                    content[i][j] = String.valueOf(ObjectUtils.getFieldValue(oldmanVoList.get(i), oldmanExcelExportEnums[j].getFieldName()));
                }
            }
            return Pair.of(title,content);
        } catch (IllegalAccessException | InvocationTargetException e) {
            UN_KNOWN.doThrowException("fail to getExportOldmanInfo",e);
        }
        return null;

    }
}
