package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.vo.VisualSettingVo;
import lombok.Data;

import java.util.List;

@Data
public class GetVisualSettingByPageResponse {
    private List<VisualSettingVo> visualSettingVoList;
    /**
     * 总数量
     */
    private Long count;
}
