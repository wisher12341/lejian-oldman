package com.lejian.oldman.entity;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@DynamicInsert
@DynamicUpdate
@Data
@Entity
@Table(name = "chx")
public class ChxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String oid;
    @Column(name = "push_date")
    private LocalDate pushDate;
    @Column
    private LocalDate deadline;
    @Column(name = "is_delete")
    private Integer isDelete;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "datachange_time")
    private Timestamp datachangeTime;

}
