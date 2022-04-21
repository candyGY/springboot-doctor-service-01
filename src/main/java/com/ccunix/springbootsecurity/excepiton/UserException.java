package com.ccunix.springbootsecurity.excepiton;

/**
 * 用户信息异常类
 * 
 * @author ruoyi
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;
    // code user.password.not.match
    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
