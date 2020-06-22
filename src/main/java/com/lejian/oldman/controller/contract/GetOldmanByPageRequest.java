package com.lejian.oldman.controller.contract;

import lombok.Data;

@Data
public class GetOldmanByPageRequest {
    private PageParam pageParam;
    private OldmanSearchParam oldmanSearchParam;
}
