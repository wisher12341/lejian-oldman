package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetWorkerByPageRequest {
    private PageParam pageParam;
    private WorkerSearchParam workerSearchParam;

}
