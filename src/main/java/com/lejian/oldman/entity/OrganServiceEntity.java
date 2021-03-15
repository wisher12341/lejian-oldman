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
@Table(name = "organ_service")
public class OrganServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "organ_id")
    private Integer organId;
    @Column(name = "worker_id")
    private Integer worker_id;
    @Column(name = "oid")
    private Integer oid;
    @Column(name = "service_type")
    private Integer serviceType;
    @Column(name = "create_time")
    private Timestamp createTime;

}
