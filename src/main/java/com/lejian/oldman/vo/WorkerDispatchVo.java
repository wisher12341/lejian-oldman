package com.lejian.oldman.vo;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class WorkerDispatchVo {

    private String oid;
    private String oldmanName;
    private Integer workerId;
    private String workerName;
    private String status;
    private String startTime;
    private String endTime;
}
