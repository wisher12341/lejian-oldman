package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.OldmanVo;
import lombok.Data;

import java.util.List;

@Data
public class GetOldmanListResponse {
    private List<OldmanVo> oldmanVoList;
}
