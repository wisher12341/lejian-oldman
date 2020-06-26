package com.lejian.oldman.controller.contract;

import com.lejian.oldman.vo.LocationVo;
import lombok.Data;

import java.util.List;

@Data
public class GetAllLocationResponse {

    private ResponseHead responseHead;
    private List<LocationVo> locationVoList;
}
