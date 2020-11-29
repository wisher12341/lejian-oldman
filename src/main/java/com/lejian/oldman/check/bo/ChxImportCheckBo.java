package com.lejian.oldman.check.bo;

import com.google.common.collect.Lists;
import com.lejian.oldman.check.annotation.DateCheck;
import com.lejian.oldman.check.annotation.EnumCheck;
import com.lejian.oldman.check.annotation.NullCheck;
import com.lejian.oldman.enums.ChxExcelEnum;
import com.lejian.oldman.enums.ExcelEnum;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.enums.OldmanExcelEnum;
import com.lejian.oldman.utils.LjReflectionUtils;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.lejian.oldman.common.ComponentRespCode.UN_KNOWN;

@Data
public class ChxImportCheckBo extends AbstractCheckBo {


    @NullCheck
    private String oid;
    @NullCheck
    @DateCheck("yyyy-MM-dd")
    private String pushDate;

    public static List<ChxImportCheckBo> convert(Pair<List<String>, List<List<String>>> data) {
        List<ChxImportCheckBo> list = Lists.newArrayList();
        try {
            IntStream.range(0, data.getSecond().size()).forEach(item -> {
                ChxImportCheckBo checkBo = new ChxImportCheckBo();
                checkBo.setNumCheck(item+1);
                list.add(checkBo);
            });
            Map<String, Field> fieldMap = LjReflectionUtils.getFieldToMap(ChxImportCheckBo.class);
            for (int i = 0; i < data.getFirst().size(); i++) {
                ExcelEnum chxExcelEnum = ExcelEnum.findFieldName(data.getFirst().get(i), ChxExcelEnum.class);
                Field field = fieldMap.get(chxExcelEnum.getFieldName());
                if(field==null){
                    continue;
                }
                field.setAccessible(true);
                for (int j = 0; j < list.size(); j++) {
                    field.set(list.get(j), data.getSecond().get(j).get(i));
                }
            }
        }catch (Exception e){
            UN_KNOWN.doThrowException("fail to convert",e);
        }
        return list;
    }
}
