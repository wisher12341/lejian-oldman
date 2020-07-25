package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.LocationVo;
import lombok.Data;

import java.util.List;

@Data
public class GetLocationListResponse {

    private ResponseHead responseHead;
    private List<LocationVo> locationVoList;
}
