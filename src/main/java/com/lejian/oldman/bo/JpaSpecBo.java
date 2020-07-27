package com.lejian.oldman.bo;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于JPA 复杂查询， 归类where子句的各式条件
 * key: 字段名(entity的字段名 不是数据库的)  value:字段值
 */
@Getter
public class JpaSpecBo {
    /**
     * 等于
     */
    Map<String,Object> equalMap = new HashMap<>();
    /**
     * in
     */
    Map<String,List<Object>> inMap = new HashMap<>();
    /**
     * 小于等于
     */
    Map<String,Object> lessEMap = new HashMap<>();
    /**
     * 大于等于
     */
    Map<String,Object> greatEMap = new HashMap<>();
    /**
     * 大于
     */
    Map<String,Object> greatMap = new HashMap<>();

}
