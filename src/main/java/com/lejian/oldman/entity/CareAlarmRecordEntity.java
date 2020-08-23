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
@Table(name = "care_alarm_record")
public class CareAlarmRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String oid;
    @Column
    private Integer type;
    @Column
    private String content;
    @Column
    private String handle;
    @Column(name = "is_read")
    private Integer isRead;
    @Column(name = "is_handle")
    private Integer isHandle;
    @Column(name = "create_time")
    private Timestamp createTime;
}
