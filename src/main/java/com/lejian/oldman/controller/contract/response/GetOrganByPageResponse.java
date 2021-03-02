package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.OrganVo;
import com.lejian.oldman.vo.UserVo;
import lombok.Data;

import java.util.List;

@Data
public class GetOrganByPageResponse {
    private List<OrganVo> organVoList;
    /**
     * 总数量
     */
    private Long count;
}
