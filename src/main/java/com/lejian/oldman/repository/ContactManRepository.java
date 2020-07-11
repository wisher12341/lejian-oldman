package com.lejian.oldman.repository;

import com.lejian.oldman.bo.ContactManBo;
import com.lejian.oldman.dao.ContactManDao;
import com.lejian.oldman.entity.ContactManEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ContactManRepository extends AbstractRepository<ContactManBo,ContactManEntity>{

    @Autowired
    private ContactManDao dao;

    public List<ContactManBo> getContactManByOid(String oid) {
        return dao.findByOid(oid).stream().map(this::convertEntity).collect(Collectors.toList());
    }

    @Override
    protected JpaRepository getDao() {
        return dao;
    }

    @Override
    protected ContactManBo convertEntity(ContactManEntity contactManEntity) {
        if(contactManEntity == null){
            return null;
        }
        ContactManBo contactManBo=new ContactManBo();
        BeanUtils.copyProperties(contactManEntity,contactManBo);
        return contactManBo;
    }

    @Override
    protected ContactManEntity convertBo(ContactManBo contactManBo) {
        ContactManEntity contactManEntity= new ContactManEntity();
        BeanUtils.copyProperties(contactManBo,contactManEntity);
        return contactManEntity;
    }
}
