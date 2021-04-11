package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.OldmanVo;
import com.lejian.oldman.vo.RzzVo;
import lombok.Data;

import java.util.List;

@Data
public class GetRzzResponse {
    private List<RzzVo> rzzVoList;
    private Long count;
}
