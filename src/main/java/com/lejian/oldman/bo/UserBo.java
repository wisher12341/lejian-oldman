package com.lejian.oldman.bo;

import lombok.Data;

@Data
public class UserBo {
    private Integer id;
    private String username;
    private String password;
    private Integer type;

}
