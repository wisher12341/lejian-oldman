package com.lejian.oldman.security;

import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || "".equals(username)) {
            throw new RuntimeException("用户不能为空");
        }
        //根据用户名查询用户
        UserBo userBo = userService.getUserByUsername(username);
        if (userBo == null) {
            throw new RuntimeException("用户不存在");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(String.valueOf(userBo.getRole()));
        grantedAuthorities.add(grantedAuthority);


        return new User(userBo.getUsername(), userBo.getPassword(),AuthorityUtils.commaSeparatedStringToAuthorityList(userBo.getRole().toString()));
    }
}