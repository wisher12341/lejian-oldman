package com.lejian.oldman.controller.contract;

import lombok.Data;

@Data
public class WorkerCheckInRequest {
    private String lng;
    private String lat;
    private String oid;
}
