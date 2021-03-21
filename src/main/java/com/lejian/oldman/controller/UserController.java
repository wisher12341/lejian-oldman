package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.DeleteUserByIdRequest;
import com.lejian.oldman.controller.contract.request.GetUserByPageRequest;
import com.lejian.oldman.controller.contract.request.GetUserByUidRequest;
import com.lejian.oldman.controller.contract.request.SaveUserRequest;
import com.lejian.oldman.controller.contract.response.GetEnumResponse;
import com.lejian.oldman.controller.contract.response.GetUserByPageResponse;
import com.lejian.oldman.controller.contract.response.GetUserResponse;
import com.lejian.oldman.controller.contract.response.ResultResponse;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.enums.WorkerEnum;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.service.EnumService;
import com.lejian.oldman.service.UserService;
import com.lejian.oldman.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@BackAdminAuth
@Controller
@ResponseBody
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserByPage")
    public GetUserByPageResponse getUserByPage(@RequestBody GetUserByPageRequest request){
        GetUserByPageResponse response = new GetUserByPageResponse();
        response.setUserVoList(userService.getUserByPage(request.getPageParam()));
        response.setCount(userService.getCount());
        return response;
    }

    @RequestMapping("/getUserByUid")
    public GetUserResponse getUserByUid(@RequestBody GetUserByUidRequest request){
        GetUserResponse response = new GetUserResponse();
        response.setUserVo(userService.getUserByUid(request.getUid()));
        return response;
    }

    @RequestMapping("/add")
    public ResultResponse add(@RequestBody SaveUserRequest request){
        userService.add(request.getUserParam());
        return new ResultResponse();
    }

    //todo 服务人员变更，删除老的账号绑定的老人
    @RequestMapping("/edit")
    public ResultResponse edit(@RequestBody SaveUserRequest request){
        userService.edit(request.getUserParam());
        return new ResultResponse();
    }

    @RequestMapping("/delete")
    public ResultResponse delete(@RequestBody DeleteUserByIdRequest request){
        userService.delete(request.getId());
        return new ResultResponse();
    }

}
