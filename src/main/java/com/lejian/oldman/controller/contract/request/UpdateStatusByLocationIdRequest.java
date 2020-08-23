package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class UpdateStatusByLocationIdRequest {
    private Integer locationId;
    private Integer status;
}
