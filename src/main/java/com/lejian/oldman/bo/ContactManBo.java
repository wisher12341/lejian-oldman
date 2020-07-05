package com.lejian.oldman.bo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
public class ContactManBo {

    private String oid;
    private String name;
    private String phone;
    private String relation;
    private String address;
}
