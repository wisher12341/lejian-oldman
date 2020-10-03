package com.lejian.oldman.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.lejian.oldman.common.ComponentRespCode.UN_KNOW_COLUMN;

/**
 * 服务人员excel表  表列名和 BO映射关系
 */
@Getter
@AllArgsConstructor
public enum WorkerExcelEnum implements ExcelEnum {

    NAME("姓名","name"),
    SEX("性别","sex"){
        @Override
        public Class<? extends BusinessEnum> getEnumType(){
            return OldmanEnum.Sex.class;
        }
    },
    ID_CARD("身份证号","idCard"),
    COUNTRY("国籍","country"),
    NATION("民族","nation"),
    BIRTHPLACE("籍贯","birthplace"),
    ADDRESS("地址","address"),
    QQ("qq号","qq"),
    PHONE("电话","phone"),
    SERVICE_TYPE("类型","type"){

        @Override
        public Class<? extends BusinessEnum> getEnumType(){
            return WorkerEnum.Type.class;
        }
    },
    BEYOND("所属区域","beyond")

    ;

    private String columnName;
    private String fieldName;


}
