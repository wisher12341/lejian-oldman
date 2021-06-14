package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetWorkerDispatchByPageRequest {
    private PageParam pageParam;
    private WorkerDispatchSearchParam workerDispatchSearchParam;
    /**
     * 是否需要获取 数量
     */
    private Boolean needCount=false;
}
