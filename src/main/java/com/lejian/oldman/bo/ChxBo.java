package com.lejian.oldman.bo;


import com.lejian.oldman.entity.ChxEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class ChxBo {
    private Integer id;
    private String oid;
    private LocalDate pushDate;
    private LocalDate deadline;


}
