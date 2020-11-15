package com.lejian.oldman.enums;

import static com.lejian.oldman.common.ComponentRespCode.UN_KNOW_COLUMN;

public interface ExcelEnum {

    String getColumnName();
    String getFieldName();

    static ExcelEnum findFieldName(String columnName, Class<? extends ExcelEnum> enumClass){
        columnName = columnName.split("【")[0].trim();
        for(ExcelEnum excelEnum: enumClass.getEnumConstants()){
            if(excelEnum.getColumnName().equals(columnName)){
                return excelEnum;
            }
        }
        UN_KNOW_COLUMN.doThrowException();
        return null;
    }

    static ExcelEnum findColumnName(String filedName, Class<? extends ExcelEnum> enumClass){
        for(ExcelEnum excelEnum: enumClass.getEnumConstants()){
            if(excelEnum.getFieldName().equals(filedName)){
                return excelEnum;
            }
        }
        UN_KNOW_COLUMN.doThrowException();
        return null;
    }

    /**
     * 获取 枚举值类型
     * @return
     */
    default Class<? extends BusinessEnum> getEnumType(){
        return null;
    }
}
