package com.lejian.oldman.bo;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于JPA 复杂查询， 归类where子句的各式条件
 * key: 字段名  value:字段值
 */
@Getter
public class JpaSpecBo {
    Map<String,Object> equalMap = new HashMap<>();

}
