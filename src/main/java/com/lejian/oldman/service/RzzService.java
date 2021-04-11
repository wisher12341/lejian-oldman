package com.lejian.oldman.service;

import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.bo.RzzBo;
import com.lejian.oldman.controller.contract.request.OldmanSearchParam;
import com.lejian.oldman.controller.contract.request.RzzSearchParam;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.repository.RzzRepository;
import com.lejian.oldman.vo.RzzVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RzzService {


    @Autowired
    private RzzRepository rzzRepository;
    @Autowired
    private OldmanRepository oldmanRepository;


    public List<RzzVo> getByPage(Integer pageNo, Integer pageSize, RzzSearchParam rzzSearchParam) {
        List<RzzBo> rzzBoList = rzzRepository.findByPageWithSpec(pageNo,pageSize, null);
        Map<String,OldmanBo> oldmanBoMap = oldmanRepository.getByOids(rzzBoList.stream().map(RzzBo::getOid).collect(Collectors.toList())).stream().collect(Collectors.toMap(OldmanBo::getOid, Function.identity()));
        return rzzBoList.stream().map(item->RzzVo.convert(item,oldmanBoMap)).collect(Collectors.toList());
    }

    public Long getCount(RzzSearchParam rzzSearchParam) {
        return rzzRepository.countWithSpec(null);
    }
}
