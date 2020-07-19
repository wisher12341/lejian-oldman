package com.lejian.oldman.common;

import com.lejian.oldman.exception.BizException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

public interface IRespCode {

    /**
     * SOA api return code, which correspond to ResponseHead's code.
     *
     * @return
     */
    String getCode();

    /**
     * SOA return display msg, which correspond to ResponseHead's DisplayMessage.
     *
     * @return
     */
    String getDisplayMessage();

    /**
     * check condition, if false throw BizException
     */
    default void check(boolean condition) {
        if (!condition) {
            throw new BizException(this);
        }
    }

    /**
     * check condition, if false throw BizException
     */
    default void check(boolean condition, String message) {
        if (!condition) {
            throw new BizException(this, message);
        }
    }

    /**
     * check not null, if false throw BizException
     */
    default void checkNotNull(Object target) {
        if (target == null) {
            throw new BizException(this);
        }
    }

    /**
     * check not null, if false throw BizException
     */
    default void checkNotNull(Object target, String message) {
        if (target == null) {
            throw new BizException(this, message);
        }
    }

    /**
     * check not null, if false throw BizException
     */
    default void checkNotBlank(String target, String message) {
        if (StringUtils.isEmpty(target)) {
            throw new BizException(this, message);
        }
    }

    /**
     * check not null, if false throw BizException
     */
    default void checkNotEmpty(Collection collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BizException(this, message);
        }
    }

    /**
     * throw new BizException with msg.
     *
     * @param msg message
     */
    default void throwExceptionWithMsg(String msg) {
        throw new BizException(this, msg);
    }

    /**
     * throw BizException
     */
    default void doThrowException() {
        throw new BizException(this);
    }

    /**
     * throw BizException with error msg
     *
     * @param errorMsg error msg
     */
    default void doThrowException(String errorMsg) {
        throw new BizException(this, errorMsg);
    }

    /**
     * throw BizException with Throwable
     *
     * @param cause Throwable
     */
    default void doThrowException(Throwable cause) {
        throw new BizException(this, cause);
    }

    /**
     * throw BizException with Throwable and msg
     *@param errorMsg
     * @param cause Throwable
     */
    default void doThrowException(String errorMsg,Throwable cause) {
        throw new BizException(this, errorMsg,cause);
    }
}
