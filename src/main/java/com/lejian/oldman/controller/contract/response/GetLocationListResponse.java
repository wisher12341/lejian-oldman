package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.LocationVo;
import lombok.Data;

import java.util.List;

@Data
public class GetLocationListResponse {

    private List<LocationVo> locationVoList;
    /**
     * 用于轮询接口， 数据的最新时间
     */
    private Long timestamp;
    /**
     * 黄灯老人的数量
     */
    private Long serviceOldmanCount;
}
