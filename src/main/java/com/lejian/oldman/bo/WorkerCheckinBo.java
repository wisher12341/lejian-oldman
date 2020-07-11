package com.lejian.oldman.bo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkerCheckinBo {
    private String lng;
    private String lat;
    private String oid;
    private Integer workerId;
    private LocalDateTime createTime;
}
