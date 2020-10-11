package com.lejian.oldman.service;

import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.controller.contract.request.UserParam;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.enums.UserEnum;
import com.lejian.oldman.repository.UserRepository;
import com.lejian.oldman.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.ACCOUNT_REPEAT;
import static com.lejian.oldman.common.ComponentRespCode.ACCOUNT_UPDATE_ERROR;
import static com.lejian.oldman.common.ComponentRespCode.NOT_MATCH_DATA;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserVo> getUserByPage(PageParam pageParam) {
        return userRepository.findByPageWithSpec(pageParam.getPageNo(),pageParam.getPageSize(),null).stream().map(this::convert).collect(Collectors.toList());
    }

    private UserVo convert(UserBo userBo) {
        UserVo userVo=new UserVo();
        BeanUtils.copyProperties(userBo,userVo);
        userVo.setRole(BusinessEnum.find(userBo.getRole(), UserEnum.Role.class).getDesc());
        return userVo;
    }

    public Long getCount() {
        return userRepository.countWithSpec(null);
    }

    public UserVo getUserByUid(Integer uid) {
        return convert(userRepository.getByPkId(uid));
    }

    public void add(UserParam userParam) {
        UserBo userBo=convert(userParam);
        verify(userBo);
        userRepository.save(userBo);
    }

    private void verify(UserBo userBo) {
        if(BusinessEnum.find(userBo.getRole(),UserEnum.Role.class)==null){
            NOT_MATCH_DATA.doThrowException();
        }
        UserBo userDb=userRepository.getByUsername(userBo.getUsername());
        if(userBo.getId()!=null){
            //更新
            if (userDb==null || userDb.getId().intValue()!=userBo.getId()){
                ACCOUNT_UPDATE_ERROR.doThrowException();
            }
        }else {
            //新增
            if (userDb != null) {
                ACCOUNT_REPEAT.doThrowException();
            }
        }
    }

    public void edit(UserParam userParam) {
        UserBo userBo=convert(userParam);
        verify(userBo);
        userRepository.dynamicUpdateByPkId(userBo);
    }

    private UserBo convert(UserParam userParam) {
        UserBo userBo=new UserBo();
        BeanUtils.copyProperties(userParam,userBo);
        return userBo;
    }
}
