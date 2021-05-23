package com.lejian.oldman.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账号 枚举
 */
public interface UserEnum extends BusinessEnum{

    /**
     * 服务人员类型
     */
    @Getter
    @AllArgsConstructor
    enum Role implements UserEnum {
        ADMIN(1,"管理员"),
        WORKER(2,"服务人员"),
        USER(3,"使用人员")
        ;
        private Integer value;
        private String desc;
    }
}
