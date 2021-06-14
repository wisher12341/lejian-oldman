package com.lejian.oldman.entity;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@DynamicInsert
@DynamicUpdate
@Data
@Entity
@Table(name = "worker_dispatch")
public class WorkerDispatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String oid;
    @Column(name = "worker_id")
    private Integer workerId;
    @Column
    private Integer status;
    @Column(name = "start_time")
    private Timestamp startTime;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "end_time")
    private Timestamp endTime;
    @Column(name = "create_time")
    private Timestamp createTime;

}
