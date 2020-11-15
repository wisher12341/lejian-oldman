package com.lejian.oldman.check.annotation;

import com.lejian.oldman.check.checker.EnumChecker;
import com.lejian.oldman.check.checker.NullChecker;
import com.lejian.oldman.enums.BusinessEnum;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@CheckAnno(EnumChecker.class)
public @interface EnumCheck {


    Class<? extends BusinessEnum> value();
}
