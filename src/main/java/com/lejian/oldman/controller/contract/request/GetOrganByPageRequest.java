package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class GetOrganByPageRequest {
    private PageParam pageParam;
    private OrganParam organParam;
}
