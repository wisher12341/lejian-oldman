package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetWorkerPositionByTimeAndIdRequest {
    private String startTime;
    private String endTime;
    private Integer workerId;
}
