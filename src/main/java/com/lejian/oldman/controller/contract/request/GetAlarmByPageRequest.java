package com.lejian.oldman.controller.contract.request;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

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
        private String areaCountry;
        private String areaTown;
        private String areaVillage;

        public String getOldManSql(){
            String where="";
            if(StringUtils.isNotBlank(areaCustomOne)){
                where+=" area_custom_one='"+getAreaCustomOne()+"' and ";
            }
            if (StringUtils.isNotBlank(areaVillage)){
                where+=" area_village='"+areaVillage+"' and ";
            }
            if (StringUtils.isNotBlank(areaTown)){
                where+=" area_town='"+areaTown+"' and ";
            }
            if (StringUtils.isNotBlank(areaCountry)){
                where+=" area_country='"+areaCountry+"' and ";
            }
            if(StringUtils.isNotBlank(where)) {
                where += " 1=1 ";
            }

            return where;
        }
    }
}
