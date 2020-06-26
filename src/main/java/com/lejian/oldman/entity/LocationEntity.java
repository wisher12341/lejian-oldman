package com.lejian.oldman.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "location")
public class LocationEntity {

    @Id
    private Integer id;
    @Column
    private String desc;
    @Column(name = "position_x")
    private String positionX;
    @Column(name = "position_y")
    private String positionY;
}
