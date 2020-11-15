package com.lejian.oldman.check.checker;

import com.lejian.oldman.check.annotation.CheckAnno;
import com.lejian.oldman.check.bo.CheckFieldBo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

import static com.lejian.oldman.common.ComponentRespCode.NULL_CHECK;

/**
 * null值， 空值校验
 */
@Component
public class NullChecker implements Checker {

    @Override
    public CheckFieldBo check(String name, Object value, Annotation annotation) {
        if(value==null
                || (value instanceof String) && StringUtils.isBlank(String.valueOf(value))){
            return new CheckFieldBo(name,NULL_CHECK.getDisplayMessage());
        }

        return null;
    }
}
