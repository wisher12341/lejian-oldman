package com.lejian.oldman.bo;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
public class OrganBo {

    private String name;
    private Integer type;

}
