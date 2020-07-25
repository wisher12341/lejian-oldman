package com.lejian.oldman.controller.contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseHead {
    private String code;
    private String errMsg;
    private StackTraceElement[] stackTraceElements;
}
