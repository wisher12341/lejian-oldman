package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetAlarmByPageRequest {
    private PageParam pageParam;
    /**
     * 是否需要老人信息
     */
    private Boolean needOldmanInfo;
    /**
     * 是否需要获取总数
     */
    private Boolean needCount;
    /**
     * 搜索条件
     */
    private AlarmSearchParam alarmSearchParam;


    @Data
    public class AlarmSearchParam{
        private Integer type;
        private String areaCustomOne;
    }
}
