package com.lejian.oldman.bo;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
public class VisualSettingBo {
    private Integer id;
    private String lat;
    private String lng;
    private String areaCountry;
    private String areaTown;
    private String areaVillage;
    private Boolean isUsed;

}
