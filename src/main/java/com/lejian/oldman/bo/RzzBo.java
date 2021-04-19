package com.lejian.oldman.bo;



import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
public class RzzBo {


    private Integer id;
    private String oid;
    private Integer type;
    private String idCard;

}
