package com.lejian.oldman.exception;

public class PendingException extends RuntimeException{
    public PendingException(String msg, Exception e) {
        super(msg,e);
    }
}
