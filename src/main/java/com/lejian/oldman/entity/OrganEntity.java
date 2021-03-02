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
@Table(name = "organ")
public class OrganEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private Integer type;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "datachange_time")
    private Timestamp datachangeTime;

}
