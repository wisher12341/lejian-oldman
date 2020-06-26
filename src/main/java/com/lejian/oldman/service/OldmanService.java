package com.lejian.oldman.service;

import com.lejian.oldman.bo.JpaSpecBo;
import com.lejian.oldman.bo.OldmanBo;
import com.lejian.oldman.controller.contract.OldmanSearchParam;
import com.lejian.oldman.repository.OldmanRepository;
import com.lejian.oldman.vo.OldmanVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OldmanService {

    @Autowired
    private OldmanRepository oldmanRepository;

    /**
     * 分页获取老人信息
     * @param pageNo 页号
     * @param pageSize 大小
     * @param oldmanSearchParam 查询参数
     * @return
     */
    public List<OldmanVo> getOldmanByPage(Integer pageNo, Integer pageSize, OldmanSearchParam oldmanSearchParam) {
        List<OldmanBo> oldmanBoList = oldmanRepository.findByPageWithSpec(pageNo,pageSize,convert(oldmanSearchParam));
        return oldmanBoList.stream().map(this::convert).collect(Collectors.toList());
    }


    /**
     * 根据条件， 获取老人数量
     * @param oldmanSearchParam
     * @return
     */
    public Long getOldmanCount(OldmanSearchParam oldmanSearchParam) {
        return oldmanRepository.countWithSpec(convert(oldmanSearchParam));
    }


    private JpaSpecBo convert(OldmanSearchParam oldmanSearchParam) {
        if(oldmanSearchParam == null){
            return null;
        }
        JpaSpecBo jpaSpecBo = new JpaSpecBo();
        jpaSpecBo.getEqualMap().put("oid",oldmanSearchParam.getOid());
        return jpaSpecBo;
    }
    private OldmanVo convert(OldmanBo oldmanBo) {
        OldmanVo oldmanVo = new OldmanVo();
        BeanUtils.copyProperties(oldmanBo,oldmanVo);
        return oldmanVo;
    }

    public List<OldmanVo> getOldmanByLocationId(Integer locationId) {
        return oldmanRepository.findByLocationId(locationId).stream().map(this::convert).collect(Collectors.toList());
    }
}
