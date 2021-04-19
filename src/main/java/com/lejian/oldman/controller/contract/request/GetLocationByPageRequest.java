package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetLocationByPageRequest {
    private PageParam pageParam;
    private LocationSearchParam locationSearchParam;
    private Boolean needCount = false;
}
