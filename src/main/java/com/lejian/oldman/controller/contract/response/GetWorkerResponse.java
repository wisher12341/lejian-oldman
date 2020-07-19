package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.WorkerVo;
import lombok.Data;

import java.util.List;

@Data
public class GetWorkerResponse {
    private ResponseHead responseHead;
    private WorkerVo workerVo;
}
