package com.lejian.oldman.check.bo;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 校验失败的值
 */
@Data
@AllArgsConstructor
public class CheckFieldBo {
    /**
     * 属性名
     */
    private String name;
    /**
     * 错误信息
     */
    private String msg;

}
