package com.lejian.oldman.controller.contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseHead {
    private String errorCode;
    private String errMsg;
    private String cause;
    private StackTraceElement[] stackTraceElements;
}
