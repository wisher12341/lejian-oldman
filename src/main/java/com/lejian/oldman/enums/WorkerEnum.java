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
}
