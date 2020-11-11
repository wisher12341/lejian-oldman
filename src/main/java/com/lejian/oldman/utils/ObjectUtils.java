package com.lejian.oldman.utils;

import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.exception.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ObjectUtils {


    /**
     * 对象转换
     * 取的source的属性
     * BeanUtils.copy 实现
     * 添加了枚举的映射， 要以“Enum”结尾
     * @param source
     * @param <T>
     * @return
     */
    public static <T> T convert(Object source,Class<T> targetClazz){
        T target;
        try {
            target = targetClazz.newInstance();
            PropertyDescriptor[] sourcePds = BeanUtils.getPropertyDescriptors(source.getClass());
            for (PropertyDescriptor sourcePd:sourcePds) {
                Method readMethod = sourcePd.getReadMethod();
                if (readMethod != null) {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source);
                    if(value!=null) {
                        PropertyDescriptor targetPd = null;
                        if (value instanceof BusinessEnum) {
                            targetPd = BeanUtils.getPropertyDescriptor(target.getClass(), sourcePd.getName().split("Enum")[0]);
                            Method writeMethod = targetPd.getWriteMethod();
                            if (writeMethod.getParameterTypes()[0] == String.class) {
                                writeMethod.invoke(target, ((BusinessEnum) value).getDesc());
                            } else {
                                writeMethod.invoke(target, ((BusinessEnum) value).getValue());
                            }
                        } else {
                            targetPd = BeanUtils.getPropertyDescriptor(target.getClass(), sourcePd.getName());
                            if (targetPd != null) {
                                Method writeMethod = targetPd.getWriteMethod();
                                if (writeMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                        writeMethod.setAccessible(true);
                                    }
                                    writeMethod.invoke(target, value);
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            throw new BizException("fail to convert object");
        }
        return target;
    }

    /**
     * 获取属性值
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object obj, String fieldName) throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor pd=BeanUtils.getPropertyDescriptor(obj.getClass(), fieldName);
        return pd.getReadMethod().invoke(obj);
    }
}
