package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetAllWorkerPositionByTimeRequest {
    private String startTime;
    private String endTime;

}
