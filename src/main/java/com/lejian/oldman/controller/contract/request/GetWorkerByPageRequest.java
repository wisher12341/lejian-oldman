package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetWorkerByPageRequest {
    private PageParam pageParam;
    private WorkerSearchParam workerSearchParam;
    /**
     * 是否需要获取 数量
     */
    private Boolean needCount=false;

}
