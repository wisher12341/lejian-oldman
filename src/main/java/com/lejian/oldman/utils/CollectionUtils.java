package com.lejian.oldman.utils;

import com.google.common.collect.Maps;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.OldmanEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class CollectionUtils  {


    public static Map<Integer,String> enumToMap(BusinessEnum[] enums){
        Map<Integer,String> map= Maps.newHashMap();
        Arrays.stream(enums).forEach(item->{
            map.put(item.getValue(),item.getDesc());
        });
        return map;
    }
}
