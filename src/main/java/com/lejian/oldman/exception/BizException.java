package com.lejian.oldman.exception;

import com.lejian.oldman.common.IRespCode;

public class BizException extends RuntimeException{
    /**
     * resp code
     */
    IRespCode iRespCode;

    public BizException(String errMsg) {
        super(errMsg);
    }

    public BizException(IRespCode iRespCode) {
        super(iRespCode.getDisplayMessage());
        this.iRespCode = iRespCode;
    }

    public BizException(IRespCode iRespCode, String message) {
        super(message);
        this.iRespCode = iRespCode;
    }

    public BizException(IRespCode iRespCode, Throwable cause) {
        super(cause);
        this.iRespCode = iRespCode;
    }

    public BizException(IRespCode iRespCode, String message,Throwable cause) {
        super(message,cause);
        this.iRespCode = iRespCode;
    }
}
