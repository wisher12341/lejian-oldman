package com.lejian.oldman.controller.contract.response;

import lombok.Data;

@Data
public class WorkerCheckInResponse {
    private ResponseHead responseHead;
    //todo 告诉签到还是签出， 签出 告诉服务多久
}
