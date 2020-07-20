package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetWorkerByPageRequest {
    private PageParam pageParam;
    private String startTime;
    private String endTime;
    private Boolean location;
}
