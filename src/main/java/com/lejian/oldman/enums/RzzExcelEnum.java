package com.lejian.oldman.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认知症excel表  表列名和 BO映射关系
 */
@Getter
@AllArgsConstructor
public enum RzzExcelEnum implements ExcelEnum{

    OID("老人身份证号","idCard"),
    TYPE("认知症类型","type"),
    ;

    private String columnName;
    private String fieldName;

}
