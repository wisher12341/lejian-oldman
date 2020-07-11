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
@Table(name = "worker_checkin")
public class WorkerCheckinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "worker_id")
    private Integer workerId;
    @Column
    private String oid;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column
    private String lng;
    @Column
    private String lat;

}
