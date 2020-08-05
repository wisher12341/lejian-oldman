package com.lejian.oldman.controller.contract.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
