package com.ccunix.springbootsecurity.excepiton;

/**
 * 用户名或者密码不匹配异常
 */
public class UserPasswordNotMatchException extends UserException {
    /*public UserPasswordNotMatchException(String message){
        super(message);
    }*/
    private static final long serialVersionUID = 1L;
    public UserPasswordNotMatchException()
    {
        super("user.password.not.match", null);
    }
}
