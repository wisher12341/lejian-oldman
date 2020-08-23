package com.lejian.oldman.controller.contract.response;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
}
