package com.ccunix.springbootsecurity.domain.model;

import com.ccunix.springbootsecurity.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class LoginUser implements UserDetails {
    private User dbUser;  // 查出来的用户
    private String token; //uuid生成的token
    private Long loginTime;  //登录时间
    private Long expireTime; //失效时间
    /**
     * 认证权限集合
     * */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * 密码
     * */
    @Override
    public String getPassword() {
        return dbUser.getPassword();
    }

    /**
     * 用户名
     * */
    @Override
    public String getUsername() {
        return dbUser.getUsername();
    }

    /**
     *账号是否过期
     * */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否被锁定
     * */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 用户凭证是过期
     * */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可见
     * */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getDbUser() {
        return dbUser;
    }

    public void setDbUser(User dbUser) {
        this.dbUser = dbUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
}
