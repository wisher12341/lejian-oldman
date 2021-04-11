package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetRzzByPageRequest {
    private PageParam pageParam;
    private RzzSearchParam rzzSearchParam;
}
