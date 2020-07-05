package com.lejian.oldman.dao;

import com.lejian.oldman.bo.ContactManBo;
import com.lejian.oldman.entity.ContactManEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactManDao extends JpaRepository<ContactManEntity, Long>{
    List<ContactManEntity> findByOid(String oid);
}
