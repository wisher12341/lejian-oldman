package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.OldmanVo;
import lombok.Data;

import java.util.List;

@Data
public class GetOldmanByPageResponse {
    private List<OldmanVo> oldmanVoList;
    /**
     * 老人总数量
     */
    private Long count;
}
