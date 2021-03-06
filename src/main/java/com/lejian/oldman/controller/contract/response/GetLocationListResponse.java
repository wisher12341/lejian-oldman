package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.LocationVo;
import lombok.Data;

import java.util.List;

@Data
public class GetLocationListResponse {

    private List<LocationVo> locationVoList;
    private Long count;
    /**
     * 用于轮询接口， 数据的最新时间
     */
    private Long timestamp;
}
