package com.lejian.oldman.controller;


import com.lejian.oldman.controller.contract.response.ResponseHead;
import com.lejian.oldman.exception.BizException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.lejian.oldman.common.ComponentRespCode.UN_KNOWN;

@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseHead handleException(Exception e){
        ResponseHead responseHead = new ResponseHead();
        if(e instanceof BizException){
            BizException bizException = (BizException) e;
            responseHead.setErrorCode(bizException.getIRespCode().getCode());
            responseHead.setErrMsg(bizException.getIRespCode().getDisplayMessage());
        }else{
            responseHead.setErrorCode(UN_KNOWN.getCode());
            responseHead.setErrMsg(e.getMessage());
            responseHead.setCause(e.getCause().getMessage());
        }
        return responseHead;
    }
}
