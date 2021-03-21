package com.lejian.oldman.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认知症excel表  表列名和 BO映射关系
 */
@Getter
@AllArgsConstructor
public enum OrganExcelEnum implements ExcelEnum{

    NAME("名称","name"),
    TYPE("类型","type"),
    BEYOND("归属","beyond"),
    ;

    private String columnName;
    private String fieldName;

}
