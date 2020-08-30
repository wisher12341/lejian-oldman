package com.lejian.oldman.controller.contract.response;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.CharUtils;
import org.springframework.data.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class GetCountResponse {
    private Map<String,Long> countMap;
    private Long sumCount=0L;
    private List<Pair<String,Long>> sortData;

    public void sum() {
        if(MapUtils.isNotEmpty(countMap)){
            countMap.forEach((k,v)-> sumCount+=v);
        }
    }

    public void sort(boolean asc) {
        sortData= Lists.newArrayList();
        if(MapUtils.isNotEmpty(countMap)){
            countMap.forEach((k,v)-> {
                Pair<String,Long> pair=Pair.of(k,v);
                sortData.add(pair);
            });
        }
        sortData=sortData.stream().sorted(Comparator.comparing(Pair::getSecond)).collect(Collectors.toList());
    }

    public void sortByKey(boolean asc) {
        sortData= Lists.newArrayList();
        if(MapUtils.isNotEmpty(countMap)){
            countMap.forEach((k,v)-> {
                Pair<String,Long> pair=Pair.of(k,v);
                sortData.add(pair);
            });
        }
        sortData.sort(this::compareTo);
//        sortData=sortData.stream().sort(Comparator.comparing((o1,o2)->o1.))
//                .collect(Collectors.toList());
        int a=1;
    }

    /**
     * 重新string的compareTo 是数字的char大于字母文字,并且如果都是数字则比较长度， 为了给行政单位排序
     * @param arg1
     * @param arg2
     * @return
     */
    public int compareTo(Pair<String,Long> arg1,Pair<String,Long> arg2) {
        int len1 = arg1.getFirst().length();
        int len2 = arg2.getFirst().length();
        int lim = Math.min(len1, len2);
        char v1[] = arg1.getFirst().toCharArray();
        char v2[] = arg2.getFirst().toCharArray();

        int k = 0;
        while (k < lim) {
            char c1 = v1[k];
            char c2 = v2[k];
            if (CharUtils.isAsciiNumeric(c1) && CharUtils.isAsciiNumeric(c1)
                    && len1!=len2){
                return len1-len2;
            }
            if (CharUtils.isAsciiNumeric(c1)) {
                c1 += 99999;
            }
            if (CharUtils.isAsciiNumeric(c2)) {
                c2 += 99999;
            }
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
    }
}
