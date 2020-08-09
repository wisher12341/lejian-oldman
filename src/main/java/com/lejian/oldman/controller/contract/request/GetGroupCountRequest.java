package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetGroupCountRequest {
    /**
     * 分组的  数据库字段名
     */
    private String groupFieldName;
}
