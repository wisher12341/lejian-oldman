package com.lejian.oldman.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 老人属性 枚举
 */
public interface OldmanEnum {


    /**
     * 默认值
     */
    enum DefaultValue implements OldmanEnum{
        NULL;

        @Override
        public Integer getValue() {
            return null;
        }

        @Override
        public String getDesc() {
            return null;
        }
    }


    /**
     * 老人服务状态（颜色）
     */
    @Getter
    @AllArgsConstructor
    enum Status implements OldmanEnum{
        GREEN(1,"正常"),
        YELLOW(2,"服务中"),
        RED(3,"异常");
        private Integer value;
        private String desc;
    }

    /**
     * 性别
     */
    @Getter
    @AllArgsConstructor
    enum Sex implements OldmanEnum{
        MAN(1,"男"),
        WOMAN(2,"女");
        private Integer value;
        private String desc;
    }


    /**
     * 政治面貌
     */
    @Getter
    @AllArgsConstructor
    enum Politics implements OldmanEnum{
        MASSES(1,"群众"),
        MEMBER(2,"中共党员"),
        OTHER(3,"其他");
        private Integer value;
        private String desc;
    }


    /**
     * 学历
     */
    @Getter
    @AllArgsConstructor
    enum Education implements OldmanEnum{
        PRIMARY(1,"小学"),
        JUNIOR(2,"初中"),
        HIGH(3,"高中"),
        SECONDARY(4,"中专"),
        JUNIOR_COLLEGE(5,"大专"),
        BACHELOR(6,"本科"),
        MASTER(7,"硕士"),
        DOCTOR(8,"博士"),
        ILLITERACY(9,"文盲");
        private Integer value;
        private String desc;
    }


    /**
     * 户口性质
     */
    @Getter
    @AllArgsConstructor
    enum HouseholdType implements OldmanEnum{
        LOCAL(1,"本地"),
        NO_LOCAL(2,"非本地"),
        SEPARATION(3,"人户分离");
        private Integer value;
        private String desc;
    }



    /**
     * 获取 枚举 值（DB存的数字）
     * @return
     */
    Integer getValue();
    /**
     * 枚举描述
     * @return
     */
    String getDesc();


    static OldmanEnum find(Integer value, Class<? extends OldmanEnum> enumClass){
        for(OldmanEnum oldmanEnum: enumClass.getEnumConstants()){
            if(oldmanEnum.getValue().intValue()==value){
                return oldmanEnum;
            }
        }
        return DefaultValue.NULL;
    }


}
