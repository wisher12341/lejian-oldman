package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.ChxVo;
import com.lejian.oldman.vo.UserVo;
import lombok.Data;

import java.util.List;

@Data
public class GetChxByPageResponse {
    private List<ChxVo> chxVoList;
    /**
     * 总数量
     */
    private Long count;
}
