package com.lejian.oldman.controller.contract.request;

import lombok.Data;

import java.util.List;

@Data
public class GetCountOfOldmanFieldRequest {
    List<String> oldmanFieldList;
}
