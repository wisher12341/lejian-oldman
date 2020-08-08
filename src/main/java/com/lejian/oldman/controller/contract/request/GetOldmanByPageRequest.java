package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetOldmanByPageRequest {
    private PageParam pageParam;
    private OldmanSearchParam oldmanSearchParam;
    /**
     * 是否需要获取 数量
     */
    private Boolean needCount=true;
}
