package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.UserVo;
import lombok.Data;

import java.util.List;

@Data
public class GetUserByPageResponse {
    private List<UserVo> userVoList;
    /**
     * 总数量
     */
    private Long count;
}
