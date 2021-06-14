package com.lejian.oldman.controller.contract.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DispatchRequest {
    private Integer workerId;
    private String oid;
    private Long startTime;
    private Long endTime;

}
