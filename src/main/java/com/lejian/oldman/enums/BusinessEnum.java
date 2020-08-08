package com.lejian.oldman.enums;

public interface BusinessEnum {

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

    /**
     * 默认值
     */
    enum DefaultValue implements BusinessEnum{
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



    static BusinessEnum find(Integer value, Class<? extends BusinessEnum> enumClass){
        for(BusinessEnum businessEnum: enumClass.getEnumConstants()){
            if(businessEnum.getValue().intValue()==value){
                return businessEnum;
            }
        }
        return BusinessEnum.DefaultValue.NULL;
    }

    static BusinessEnum find(String desc, Class<? extends BusinessEnum> enumClass){
        for(BusinessEnum businessEnum: enumClass.getEnumConstants()){
            if(businessEnum.getValue().equals(desc)){
                return businessEnum;
            }
        }
        return BusinessEnum.DefaultValue.NULL;
    }
}
