package com.lejian.oldman.bo;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
public class WorkerDispatchBo {
    private Integer id;
    private String oid;
    private Integer workerId;
    private Integer status;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer userId;
}
