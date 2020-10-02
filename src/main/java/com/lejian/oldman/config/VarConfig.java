package com.lejian.oldman.config;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

import static com.lejian.oldman.common.ComponentRespCode.REFLECTION_ERROR;

/**
 * 全局 变量值 配置
 */
@Component
public class VarConfig {


    /**
     * 主页的可视化 配置
     */
    public static String areaCountry;
    public static String areaTown;
    public static String areaVillage;

    public VarConfig() {
        areaCountry="浦东新区";
        areaTown="老港镇";
        areaVillage="大河村";
    }

    public Map<String, String> getMainConfigData() {
        Map<String, String> map= Maps.newHashMap();
        map.put("areaVillage", areaVillage);
        map.put("areaTown", areaTown);
        map.put("areaCountry", areaCountry);
        return map;
    }

    public void saveVarConfigData(Map<String, String> map) {
        try {
            Field[] fields=VarConfig.class.getDeclaredFields();
            for(Field field:fields){
                if(map.containsKey(field.getName())){
                    field.setAccessible(true);
                    field.set(this,map.get(field.getName()));
                }

            }
        } catch (IllegalAccessException e) {
            REFLECTION_ERROR.doThrowException();
        }
    }

    public static String getWorkerBeyond() {
        StringBuilder str=new StringBuilder();
        if(StringUtils.isNotBlank(VarConfig.areaCountry)){
            str.append(VarConfig.areaCountry).append("-");
        }
        if(StringUtils.isNotBlank(VarConfig.areaTown)){
            str.append(VarConfig.areaTown).append("-");
        }
        if(StringUtils.isNotBlank(VarConfig.areaVillage)){
            str.append(VarConfig.areaVillage).append("-");
        }
        return str.toString().substring(0,str.length()-1);

    }
}
