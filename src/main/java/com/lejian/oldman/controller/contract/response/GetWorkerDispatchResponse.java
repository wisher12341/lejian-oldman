package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.WorkerDispatchVo;
import com.lejian.oldman.vo.WorkerVo;
import lombok.Data;

import java.util.List;

@Data
public class GetWorkerDispatchResponse {
    private List<WorkerDispatchVo> workerDispatchVoList;
    /**
     * 总数量
     */
    private Long count;
}
