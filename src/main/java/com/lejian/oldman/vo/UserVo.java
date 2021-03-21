package com.lejian.oldman.vo;

import lombok.Data;

@Data
public class UserVo {
    private Integer id;
    private String username;
    private String password;
    private String role;
    /**
     * 绑定的服务人员id
     */
    private Integer wid;
    private String workerName;
}
