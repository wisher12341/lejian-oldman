package com.lejian.oldman.common;

import lombok.*;

@AllArgsConstructor
@Getter
public enum ComponentRespCode implements IRespCode {

    /**
     * 5xx system bug
     */

    UN_KNOWN("500", "unknown error"),
    REPOSITORY_ERROR("501", "repository error"),
    ACCOUNT_ERROR("502","username or password error"),
    REFLECTION_ERROR("503","reflection error"),
    REMOTE_ERROR("504","remote call error"),

    /**
     * 2xx 服务人员业务中断
     */
    CHECKIN_SHORT_TIME("200","签到时间间隔太短"),
    CHECKIN_OVER_DISTANCE("201","签到超过最大距离"),

    /**
     * 3xx 数据错误
     */
    NO_DATA_FOUND("300","没找到数据"),
    PARSE_DATA_ERROR("301","数据解析错误"),
    NOT_MATCH_DATA("302","枚举数据不匹配"),


    /**
     * 6xx 业务错误
     */
    UN_KNOW_COLUMN("601","不能识别 excel表的列名"),
    ACCOUNT_REPEAT("602","账号重复"),
    ACCOUNT_UPDATE_ERROR("603","账号更新失败"),


    /**
     * 7xx check校验错误码
     */
    NULL_CHECK("701","不能为空"),
    ENUM_CHECK("702","枚举数据不匹配"),
    LOCATION_CHECK("703","不能识别坐标"),
    ;

    private String code;
    private String displayMessage;

}
