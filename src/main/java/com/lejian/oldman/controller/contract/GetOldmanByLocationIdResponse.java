package com.lejian.oldman.controller.contract;

import com.lejian.oldman.vo.OldmanVo;
import lombok.Data;

import java.util.List;

@Data
public class GetOldmanByLocationIdResponse {
    private ResponseHead responseHead;
    private List<OldmanVo> oldmanVoList;
}
