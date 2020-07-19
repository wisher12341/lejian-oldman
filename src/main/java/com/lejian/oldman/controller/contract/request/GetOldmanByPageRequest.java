package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetOldmanByPageRequest {
    private PageParam pageParam;
    private OldmanSearchParam oldmanSearchParam;
}
