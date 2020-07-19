package com.lejian.oldman.controller.contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseHead {
    private String code;
    private String errMsg;
}
