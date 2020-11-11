package com.lejian.oldman.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 老人导出excel表  表列名和 BO映射关系
 */
@Getter
@AllArgsConstructor
public enum OldmanExcelExportEnum{

    NAME("姓名","name"),
    AGE("年龄","age"),
    ID_CARD("身份证号","idCard"),
    AREA_COUNTRY("区县级行政区","areaCountry"),
    AREA_TOWN("乡镇（街道）级行政区","areaTown"),
    AREA_VILLAGE("社区级行政区","areaVillage"),
    AREA_CUSTOM_ONE("自定义行政区","areaCustomOne"),
    ADDRESS("详细住址","address"),

    ;

    private String columnName;
    private String fieldName;

}
