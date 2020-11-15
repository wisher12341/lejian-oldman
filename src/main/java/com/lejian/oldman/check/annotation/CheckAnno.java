package com.lejian.oldman.check.annotation;

import com.lejian.oldman.check.checker.Checker;

import java.lang.annotation.*;

@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAnno {
    Class<? extends Checker> value();
}
