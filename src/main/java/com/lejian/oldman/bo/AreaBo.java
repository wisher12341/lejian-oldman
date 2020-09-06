package com.lejian.oldman.bo;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
public class AreaBo {

    private Integer userId;
    private Integer type;
    private String value;
    private Integer parentId;

}
