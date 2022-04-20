package com.ccunix.springbootsecurity.service;

import com.ccunix.springbootsecurity.excepiton.ServiceException;
import com.ccunix.springbootsecurity.excepiton.UserPasswordNotMatchException;
import com.ccunix.springbootsecurity.domain.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户输入用户名和密码进行验证类
 */
@Service
public class UserLoginService {
    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    /**
     * @param username 用户输入的用户名
     * @param password 已经输入的密码
     * @return
     */
    // 2
    public String login(String username, String password) {
        // 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                throw new UserPasswordNotMatchException("用户名/密码错误");
            }
            else
            {
                throw new ServiceException(e.getMessage());
            }
        }
        // 3
        System.out.println("AuthenticationManager获得的信息");
        System.out.println(authentication.getPrincipal());
        // 获得loadUserByUsername方法返回的UserDetails
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //LoginUser信息进行加密处理
        String token = tokenService.createToken(loginUser);
        System.out.println("jwt生成的令牌token=" + token);
        return token;
    }
}
