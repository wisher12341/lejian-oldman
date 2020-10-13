package com.lejian.oldman.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.lejian.oldman.common.ComponentRespCode.UN_KNOW_COLUMN;

/**
 * 老人excel表  表列名和 BO映射关系
 */
@Getter
@AllArgsConstructor
public enum OldmanExcelEnum implements ExcelEnum{

    NAME("姓名","name"),
    SEX("性别","sex"){
        @Override
        public Class<? extends BusinessEnum> getEnumType(){
            return OldmanEnum.Sex.class;
        }
    },
    ID_CARD("身份证号","idCard"),
    AREA_COUNTRY("区县级行政区","areaCountry"),
    AREA_TOWN("乡镇（街道）级行政区","areaTown"),
    AREA_VILLAGE("社区级行政区","areaVillage"),
    AREA_CUSTOM_ONE("自定义行政区","areaCustomOne"),
    LOCATION_ADDRESS("坐标地址","locationAddress"),
    Family("家庭结构","family"){

        @Override
        public Class<? extends BusinessEnum> getEnumType(){
            return OldmanEnum.FamilyType.class;
        }
    },
    HOUSEHOLD_TYPE("户口性质","householdType"){

        @Override
        public Class<? extends BusinessEnum> getEnumType(){
            return OldmanEnum.HouseholdType.class;
        }
    },
    SERVICE_TYPE("居家养老服务类型","serviceType"){

        @Override
        public Class<? extends BusinessEnum> getEnumType(){
            return OldmanEnum.ServiceType.class;
        }
    },
    ADDRESS("详细住址","address"),
    LNG("经度","lng"),
    LAT("纬度","lat")

    ;

    private String columnName;
    private String fieldName;

}
