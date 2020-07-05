package com.lejian.oldman.controller.contract;

import com.lejian.oldman.vo.ContactManVo;
import lombok.Data;

import java.util.List;

@Data
public class GetContactManByOidResponse {
    private ResponseHead responseHead;
    private List<ContactManVo> contactManVoList;
}
