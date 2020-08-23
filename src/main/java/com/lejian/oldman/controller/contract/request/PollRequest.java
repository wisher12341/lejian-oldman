package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class PollRequest {
    private OldmanSearchParam oldmanSearchParam;
    private long timestamp;
    private String birthdayLike;
}
