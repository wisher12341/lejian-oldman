package com.lejian.oldman.check.annotation;

import com.lejian.oldman.check.checker.EnumChecker;

import java.lang.annotation.*;

/**
 * 正则校验
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@CheckAnno(EnumChecker.class)
public @interface RegularCheck {
    int maxLength();
}
