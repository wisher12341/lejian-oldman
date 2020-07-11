package com.lejian.oldman.exception;

public class RepositoryException extends BusinessException {

    public RepositoryException(String msg) {
        super(msg);
    }

    public RepositoryException(String msg,Exception e) {
        super(msg,e);
    }
}
