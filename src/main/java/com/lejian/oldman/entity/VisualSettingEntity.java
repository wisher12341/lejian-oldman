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
@Table(name = "visual_setting")
public class VisualSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String lat;
    @Column
    private String lng;
    @Column(name = "area_country")
    private String areaCountry;
    @Column(name = "area_town")
    private String areaTown;
    @Column(name = "area_village")
    private String areaVillage;
    @Column(name = "is_used")
    private Integer isUsed;
    @Column(name = "user_id")
    private Integer userId;
}
