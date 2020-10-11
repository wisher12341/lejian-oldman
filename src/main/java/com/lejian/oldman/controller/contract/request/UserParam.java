package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class UserParam {
    private Integer id;
    private String username;
    private String password;
    private Integer role;

}
