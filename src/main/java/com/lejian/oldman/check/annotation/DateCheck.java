package com.lejian.oldman.check.annotation;

import com.lejian.oldman.check.checker.DateChecker;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@CheckAnno(DateChecker.class)
public @interface DateCheck {


    String value();
}