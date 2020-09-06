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
@Table(name = "area")
public class AreaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column
    private Integer type;
    @Column
    private String value;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "datachange_time")
    private Timestamp datachangeTime;

}
