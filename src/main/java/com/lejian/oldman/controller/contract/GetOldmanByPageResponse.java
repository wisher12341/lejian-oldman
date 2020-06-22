package com.lejian.oldman.controller.contract;

import com.lejian.oldman.vo.OldmanVo;
import lombok.Data;

import java.util.List;

@Data
public class GetOldmanByPageResponse {
    private ResponseHead responseHead;
    private List<OldmanVo> oldmanVoList;
    /**
     * 老人总数量
     */
    private Long count;
}
