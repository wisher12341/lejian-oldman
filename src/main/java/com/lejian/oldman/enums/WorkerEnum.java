package com.lejian.oldman.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工作人员 枚举
 */
public interface WorkerEnum extends BusinessEnum{

    /**
     * 服务人员类型
     */
    @Getter
    @AllArgsConstructor
    enum Type implements WorkerEnum {
        SC(1,"送餐"),
        CHX(2,"长护险"),
        YL(3,"医疗"),
        JJYL(4,"居家养老"),
        ;
        private Integer value;
        private String desc;
    }


    /**
     * 服务人员类型
     */
    @Getter
    @AllArgsConstructor
    enum WorkerStatus implements WorkerEnum {
        NOT_START(0,"未开始"),
        FINISH(1,"已完成"),
        DOING(2,"正在进行"),
        NOT_FINISH(3,"未完成"),
        ;
        private Integer value;
        private String desc;
    }
}
