package com.lejian.oldman.check.bo;

import lombok.Data;

@Data
public abstract class AbstractCheckBo {
    /**
     * 当前校验对象的 顺序
     */
    private Integer numCheck;
}
