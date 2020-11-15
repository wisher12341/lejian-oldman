package com.lejian.oldman.check.checker;

import com.lejian.oldman.check.annotation.CheckAnno;
import com.lejian.oldman.check.bo.CheckFieldBo;

import java.lang.annotation.Annotation;

public interface Checker {

    CheckFieldBo check(String name, Object value, Annotation annotation);
}
