package com.lejian.oldman.check;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.check.annotation.CheckAnno;
import com.lejian.oldman.check.bo.AbstractCheckBo;
import com.lejian.oldman.check.bo.CheckFieldBo;
import com.lejian.oldman.check.bo.CheckResultBo;
import com.lejian.oldman.check.checker.Checker;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.lejian.oldman.common.ComponentRespCode.UN_KNOWN;

@Component
public class CheckProcessor implements ApplicationListener<ContextRefreshedEvent>{

    private static Map<String,Checker> CHECKER_MAP= Maps.newHashMap();


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext=contextRefreshedEvent.getApplicationContext();
        Map<String, Checker> map=applicationContext.getBeansOfType(Checker.class);
        map.forEach((k,v)->{
            CHECKER_MAP.put(v.getClass().getSimpleName(),v);
        });
    }

    public List<CheckResultBo> check(Collection<? extends AbstractCheckBo> collection){
        List<CheckResultBo> checkResultBoList =Lists.newArrayList();
        try {
            if (CollectionUtils.isEmpty(collection)) {
                return checkResultBoList;
            }
            for (AbstractCheckBo obj : collection) {
                CheckResultBo checkResultBo = checkObject(obj);
                if (checkResultBo != null && CollectionUtils.isNotEmpty(checkResultBo.getCheckFieldBoList())) {
                    checkResultBoList.add(checkResultBo);
                }
            }
        }catch (Exception e){
            UN_KNOWN.doThrowException("fail to check",e);
        }
        return checkResultBoList;
    }

    private CheckResultBo checkObject(AbstractCheckBo obj) throws IllegalAccessException {
        CheckResultBo checkResultBo=new CheckResultBo();
        List<CheckFieldBo> checkFieldBoList=Lists.newArrayList();
        checkResultBo.setCheckFieldBoList(checkFieldBoList);
        checkResultBo.setNumber(obj.getNumCheck());

        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            Object value=field.get(obj);
            Annotation[] annotations= field.getAnnotations();
            if (annotations.length==0){
                continue;
            }
            //快速失败
            for(Annotation annotation:annotations){
                CheckAnno checkAnno=annotation.annotationType().getAnnotation(CheckAnno.class);
                CheckFieldBo checkFieldBo=CHECKER_MAP.get(checkAnno.value().getSimpleName()).check(field.getName(),value,annotation);
                if(checkFieldBo!=null){
                    //校验失败
                    checkFieldBoList.add(checkFieldBo);
                    break;
                }
            }
        }
        return checkResultBo;
    }
}
