package com.lejian.oldman.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "contact_man")
public class ContactManEntity {

    @Id
    private Integer id;
    @Column
    private String oid;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String relation;
    @Column
    private String address;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "datachange_time")
    private Timestamp datachangeTime;
}
