package com.lejian.oldman.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 老人excel表  表列名和 BO映射关系
 */
@Getter
@AllArgsConstructor
public enum ChxExcelEnum implements ExcelEnum{

    OID("老人编号","oid"),
    NAME("老人姓名","name"),
    PUSH_DATE("推送日","pushDate"),
    DEADLINE("截止日","deadline")
    ;

    private String columnName;
    private String fieldName;

}
