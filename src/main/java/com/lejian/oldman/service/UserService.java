package com.lejian.oldman.service;

import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.bo.WorkerBo;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.controller.contract.request.UserParam;
import com.lejian.oldman.enums.BusinessEnum;
import com.lejian.oldman.repository.WorkerRepository;
import com.lejian.oldman.vo.UserVo;
import com.lejian.oldman.enums.UserEnum;
import com.lejian.oldman.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.ACCOUNT_REPEAT;
import static com.lejian.oldman.common.ComponentRespCode.ACCOUNT_UPDATE_ERROR;
import static com.lejian.oldman.common.ComponentRespCode.NOT_MATCH_DATA;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkerRepository workerRepository;


    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    public List<UserVo> getUserByPage(PageParam pageParam) {
        return userRepository.findByPageWithSpec(pageParam.getPageNo(),pageParam.getPageSize(),null).stream().map(this::convert).collect(Collectors.toList());
    }

    private UserVo convert(UserBo userBo) {
        UserVo userVo=new UserVo();
        BeanUtils.copyProperties(userBo,userVo);
        userVo.setPassword("********");
        userVo.setRole(BusinessEnum.find(userBo.getRole(), UserEnum.Role.class).getDesc());
        return userVo;
    }

    public Long getCount() {
        return userRepository.countWithSpec(null);
    }

    public UserVo getUserByUid(Integer uid) {
        UserVo userVo= convert(userRepository.getByPkId(uid));
        if(userVo.getRole().equals(UserEnum.Role.WORKER.getDesc())){
            WorkerBo workerBo=workerRepository.getWorkerByUid(userVo.getId());
            if(workerBo!=null) {
                userVo.setWid(workerBo.getId());
                userVo.setWorkerName(workerBo.getName());
            }
        }
        return userVo;
    }

    @Transactional
    public void add(UserParam userParam) {
        UserBo userBo=convert(userParam);
        verify(userBo);

        UserBo resultBo=userRepository.saveAndReturn(userBo);
        if(userBo.getRole().intValue()==UserEnum.Role.WORKER.getValue()) {
            updateWorker(resultBo, userParam.getWid());
        }
    }

    private void updateWorker(UserBo userBo,Integer wid) {
        WorkerBo workerBo=new WorkerBo();
        workerBo.setUserId(userBo.getId());
        workerBo.setId(wid);
        workerRepository.dynamicUpdateByPkId(workerBo);
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

    @Transactional
    public void edit(UserParam userParam) {
        UserBo userBo=convert(userParam);
        verify(userBo);
        userRepository.dynamicUpdateByPkId(userBo);
        if(userBo.getRole().intValue()==UserEnum.Role.WORKER.getValue()) {
            updateWorker(userBo, userParam.getWid());
        }
    }

    private UserBo convert(UserParam userParam) {
        UserBo userBo=new UserBo();
        BeanUtils.copyProperties(userParam,userBo);
        userBo.setPassword(encoder.encode(userBo.getPassword()));
        return userBo;
    }

    public UserBo getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
