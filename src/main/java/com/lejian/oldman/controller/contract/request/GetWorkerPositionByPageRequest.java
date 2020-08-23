package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetWorkerPositionByPageRequest {
    private PageParam pageParam;
    private String startTime;
    private String endTime;
}
