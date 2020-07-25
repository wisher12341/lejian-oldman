package com.lejian.oldman.bo;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class CareAlarmRecordBo {

    private Integer id;
    private String oid;
    private Integer type;
    private String content;
    private String handle;
    private Timestamp createTime;
}
