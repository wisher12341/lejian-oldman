package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.OldmanVo;
import lombok.Data;

@Data
public class GetOldmanResponse {
    private ResponseHead responseHead;
    private OldmanVo oldmanVo;
}
