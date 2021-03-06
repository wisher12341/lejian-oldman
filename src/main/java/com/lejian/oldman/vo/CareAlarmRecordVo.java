package com.lejian.oldman.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CareAlarmRecordVo {

    private Integer id;
    private OldmanVo oldmanVo;
    private String type;
    private String content;
    private String handle;
    private String createTime;
    /**
     * 是否已处理
     */
    private Boolean isHandle;
    private Boolean isRead;
}
