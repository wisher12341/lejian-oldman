package com.lejian.oldman.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 长者关怀系统 枚举
 */
public interface CareSystemEnum extends BusinessEnum{

    /**
     * 报警类型
     */
    @Getter
    @AllArgsConstructor
    enum AlarmType implements CareSystemEnum{
        EMERGENCY_ALARM(1,"紧急报警"),
        ACTION_ALARM(2,"行为预警"),
        LIGHT_ALARM(3,"光强预警"),
        TEMPERATURE_ALARM(4,"温度预警"),
        OUT_ALARM(5,"未归预警"),
        LAW_ALARM(6,"规律异常预警"),
        ;
        private Integer value;
        private String desc;
    }

    @Getter
    @AllArgsConstructor
    enum HandleType implements CareSystemEnum{
        NO_HANDLE(0,"未处理"),
        HANDLE(1,"已处理"),
        ;
        private Integer value;
        private String desc;
    }

    @Getter
    @AllArgsConstructor
    enum ReadType implements CareSystemEnum{
        NO_READ(0,"未读"),
        READ(1,"已读"),
        ;
        private Integer value;
        private String desc;
    }
}
