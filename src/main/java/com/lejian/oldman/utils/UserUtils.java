package com.lejian.oldman.utils;

import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.enums.UserEnum;
import com.lejian.oldman.repository.UserRepository;
import com.lejian.oldman.security.UserContext;
import org.springframework.security.core.userdetails.User;

public class UserUtils {

    public static UserBo getUser(){
        User user = UserContext.getLoginUser();

        UserRepository userRepository = BeanUtils.getBean(UserRepository.class);
        return userRepository.getByUsername(user.getUsername());
    }

    /**
     * 获取 USER角色的 账户id
     * @return
     */
    public static Integer getUserRoleId(){
        UserBo userBo = getUser();
        if (userBo !=null && userBo.getRole().intValue() == UserEnum.Role.USER.getValue()){
            return userBo.getId();
        }
        return 0;
    }
}
