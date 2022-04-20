package com.ccunix.springbootsecurity.service;

import com.ccunix.springbootsecurity.excepiton.ServiceException;
import com.ccunix.springbootsecurity.domain.User;
import com.ccunix.springbootsecurity.domain.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @description 这个接口实现  负责从数据库获得用户信息  封装成UserDetails对象 返回给
 *
 * authentication = authenticationManager
 *                 .authenticate(new UsernamePasswordAuthenticationToken(username, password));
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中去取出来
        User user = userService.selectUserByUsername(username);
        // 验证密码
        if (user == null) {
            System.out.println("没有该用户名");
            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        // 组装一个UserDetails对象
        LoginUser loginUser = new LoginUser();
        loginUser.setDbUser(user);
        System.out.println("UserDetailsService获得的对象：");
        System.out.println(loginUser);
        return loginUser;
    }
}
