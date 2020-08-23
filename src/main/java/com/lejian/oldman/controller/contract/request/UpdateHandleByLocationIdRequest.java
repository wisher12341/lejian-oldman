package com.lejian.oldman.controller.contract.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateHandleByLocationIdRequest {
    private Integer locationId;
    private Integer isHandle;
}
