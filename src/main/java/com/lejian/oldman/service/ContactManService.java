package com.lejian.oldman.service;

import com.lejian.oldman.bo.ContactManBo;
import com.lejian.oldman.repository.ContactManRepository;
import com.lejian.oldman.vo.ContactManVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactManService {


    @Autowired
    private ContactManRepository contactManRepository;


    public List<ContactManVo> getContactManByOid(String oid) {
        return contactManRepository.getContactManByOid(oid).stream().map(this::convert).collect(Collectors.toList());
    }

    private ContactManVo convert(ContactManBo contactManBo) {
        ContactManVo contactManVo=new ContactManVo();
        BeanUtils.copyProperties(contactManBo,contactManVo);
        return contactManVo;
    }
}
