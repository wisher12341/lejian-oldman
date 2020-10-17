//package com.lejian.oldman.security;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//public class UserDetailsServiceImpl  implements UserDetailsService {
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if (username == null || "".equals(username)) {
//            throw new RuntimeException("用户不能为空");
//        }
//        //根据用户名查询用户
//        SysUser sysUser = sysUserService.selectByName(username);
//        if (sysUser == null) {
//            throw new RuntimeException("用户不存在");
//        }
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        if (sysUser != null) {
//            //获取该用户所拥有的权限
//            List<SysPermission> sysPermissions = sysPermissionService.selectListByUser(sysUser.getId());
//            // 声明用户授权
//            sysPermissions.forEach(sysPermission -> {
//                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysPermission.getPermissionCode());
//                grantedAuthorities.add(grantedAuthority);
//            });
//        }
//        return new User(sysUser.getAccount(), sysUser.getPassword(), sysUser.getEnabled(), sysUser.getAccountNonExpired(), sysUser.getCredentialsNonExpired(), sysUser.getAccountNonLocked(), grantedAuthorities);
//    }
//}