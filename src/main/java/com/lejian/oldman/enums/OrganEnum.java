package com.lejian.oldman.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 机构 枚举
 */
public interface OrganEnum extends BusinessEnum{

    /**
     * 机构类型
     */
    @Getter
    @AllArgsConstructor
    enum Type implements BusinessEnum {
        ZZZGZJ(1,"长者照顾之家"),
        WLFWZX(2,"为老服务中心")
        ;
        private Integer value;
        private String desc;
    }

    /**
     * 机构服务类型类型
     */
    @Getter
    @AllArgsConstructor
    enum ServiceType implements BusinessEnum {
        QT(1,"全托服务"),
        RT(2,"日托服务")
        ;
        private Integer value;
        private String desc;
    }
}
