package com.lejian.oldman.controller.contract.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChxParam {
    private Integer id;
    private String pushDate;
    private String deadline;
}
