package com.ccunix.springbootsecurity.excepiton;

/**
 * 用户名或者密码不匹配异常
 */
public class UserPasswordNotMatchException extends RuntimeException {
    public UserPasswordNotMatchException(String message){
        super(message);
    }
}
