package com.lejian.oldman.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认知症excel表  表列名和 BO映射关系
 */
@Getter
@AllArgsConstructor
public enum DbExcelEnum implements ExcelEnum{

    OID("老人编号","oid"),
    ;

    private String columnName;
    private String fieldName;

}
