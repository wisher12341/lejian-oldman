package com.lejian.oldman.check.annotation;

import com.lejian.oldman.check.checker.NullChecker;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@CheckAnno(NullChecker.class)
public @interface NullCheck {

}
