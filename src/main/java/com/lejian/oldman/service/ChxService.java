package com.lejian.oldman.service;

import com.google.common.collect.Lists;
import com.lejian.oldman.bo.ChxBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.check.CheckProcessor;
import com.lejian.oldman.check.bo.CheckResultBo;
import com.lejian.oldman.check.bo.ChxImportCheckBo;
import com.lejian.oldman.check.bo.OldmanImportCheckBo;
import com.lejian.oldman.controller.contract.request.ChxParam;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.ChxExcelEnum;
import com.lejian.oldman.enums.ExcelEnum;
import com.lejian.oldman.enums.OldmanExcelEnum;
import com.lejian.oldman.repository.ChxRepository;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.utils.DateUtils;
import com.lejian.oldman.utils.LjReflectionUtils;
import com.lejian.oldman.vo.ChxVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.lejian.oldman.common.ComponentRespCode.REFLECTION_ERROR;
import static com.lejian.oldman.utils.DateUtils.YYMMDD;
import static com.lejian.oldman.utils.DateUtils.YY_MM_DD;

@Service
public class ChxService {

    @Autowired
    private ChxRepository repository;
    @Autowired
    private OldmanRepository oldmanRepository;
    @Autowired
    private CheckProcessor checkProcessor;


    public Long getCount() {
        return repository.countWithSpec(null);
    }

    public List<ChxVo> getChxByPage(PageParam pageParam) {
        List<ChxBo> chxBoList= repository.findByPageWithSpec(pageParam.getPageNo(),pageParam.getPageSize(),null);
        List<String> oidList=chxBoList.stream().map(ChxBo::getOid).collect(Collectors.toList());
        Map<String,OldmanBo> oldmanBoMap= oldmanRepository.getByOids(oidList).stream().collect(Collectors.toMap(OldmanBo::getOid, Function.identity()));
        return chxBoList.stream().map(item->convert(item,oldmanBoMap.get(item.getOid()))).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private ChxVo convert(ChxBo chxBo,OldmanBo oldmanBo) {
        if(oldmanBo == null){
            return null;
        }
        ChxVo chxVo=new ChxVo();
        chxVo.setId(chxBo.getId());
        chxVo.setPushDate(chxBo.getPushDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        chxVo.setDeadline(chxBo.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        chxVo.setOid(chxBo.getOid());
        chxVo.setOldmanName(oldmanBo.getName());
        return chxVo;
    }

    public void edit(ChxParam chxParam) {
        ChxBo chxBo=new ChxBo();
        chxBo.setPushDate(DateUtils.stringToLocalDate(chxParam.getPushDate(),YY_MM_DD));
        chxBo.setDeadline(chxBo.getPushDate().plusYears(2));
        chxBo.setId(chxParam.getId());
        repository.dynamicUpdateByPkId(chxBo);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Transactional
    public List<CheckResultBo> addChxByExcel(Pair<List<String>, List<List<String>>> excelData) {
        List<String> titleList=excelData.getFirst();
        List<List<String>> valueList=excelData.getSecond();


        List<ChxImportCheckBo> chxImportCheckBoList= ChxImportCheckBo.convert(excelData);
        List<CheckResultBo> checkResultBoList=checkImport(chxImportCheckBoList);


        if(CollectionUtils.isNotEmpty(checkResultBoList)){
            return checkResultBoList;
        }

        List<ChxBo> chxBoList = Lists.newArrayList();

        IntStream.range(0,valueList.size()).forEach(item->{
            ChxBo chxBo=new ChxBo();
            chxBoList.add(chxBo);
        });

        Map<String,Field> fieldMap= LjReflectionUtils.getFieldToMap(ChxBo.class);

        try {
            for (int i = 0; i < titleList.size(); i++) {
                ExcelEnum excelEnum = ExcelEnum.findFieldName(titleList.get(i),ChxExcelEnum.class);
                Field field = fieldMap.get(excelEnum.getFieldName());
                if(field==null){
                    continue;
                }
                field.setAccessible(true);
                //纵向 遍历每个对象，一个属性一个属性 纵向赋值
                for (int j = 0; j < valueList.size(); j++) {
                    Object value=valueList.get(j).get(i);
                    if(StringUtils.isNotBlank(String.valueOf(value))) {
                        if(field.getType()==LocalDate.class){
                            field.set(chxBoList.get(j), DateUtils.stringToLocalDate(String.valueOf(value),YY_MM_DD));
                        }else{
                            field.set(chxBoList.get(j), value);
                        }
                    }
                }
            }
        }catch (IllegalArgumentException | IllegalAccessException e){
            REFLECTION_ERROR.doThrowException("fail to addChxByExcel",e);
        }


        supplement(chxBoList);

        //left 添加 right更新
        Pair<List<ChxBo>,List<ChxBo>> pair = classifyDbType(chxBoList);
        //todo 并非真正的 batch
        repository.batchAdd(pair.getFirst());
        repository.batchUpdate(pair.getSecond());
        return Lists.newArrayList();
    }

    private void supplement(List<ChxBo> checkBoList) {
        checkBoList.forEach(bo->{
            bo.setDeadline(bo.getPushDate().plusYears(2));
        });
    }

    private List<CheckResultBo> checkImport(List<ChxImportCheckBo> chxImportCheckBoList) {
        List<CheckResultBo> checkResultBoList=checkProcessor.check(chxImportCheckBoList);
        //todo 校验 oid是否正确
        return checkResultBoList;
    }

    private Pair<List<ChxBo>,List<ChxBo>> classifyDbType(List<ChxBo> chxBoList) {
        List<ChxBo> update=Lists.newArrayList();
        List<ChxBo> add=Lists.newArrayList();

        List<String> oidList=chxBoList.stream().map(ChxBo::getOid).collect(Collectors.toList());
        Map<String,ChxBo> existChxList=repository.getByOids(oidList).stream().collect(Collectors.toMap(ChxBo::getOid,Function.identity()));

        chxBoList.forEach(bo->{
            if(existChxList.containsKey(bo.getOid())){
                bo.setId(existChxList.get(bo.getOid()).getId());
                update.add(bo);
            }else{
                add.add(bo);
            }
        });

        return Pair.of(add,update);
    }

    public ChxVo getChxById(Integer id) {
        return convert(repository.getByPkId(id));
    }

    private ChxVo convert(ChxBo chxBo) {
        ChxVo chxVo=new ChxVo();
        chxVo.setId(chxBo.getId());
        chxVo.setPushDate(chxBo.getPushDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        chxVo.setDeadline(chxBo.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        chxVo.setOid(chxBo.getOid());
        return chxVo;
    }
}
