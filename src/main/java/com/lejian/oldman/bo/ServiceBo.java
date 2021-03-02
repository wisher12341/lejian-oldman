package com.lejian.oldman.bo;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
public class ServiceBo {


    private Integer organId;
    private Integer worker_id;
    private Integer oid;
    private Integer serviceType;

}
