package com.ccunix.springbootsecurity.service.impl;

import com.ccunix.springbootsecurity.security.SecurityUtil;
import com.ccunix.springbootsecurity.service.IUserService;
import com.ccunix.springbootsecurity.domain.User;
import org.springframework.stereotype.Service;

import java.util.Hashtable;

@Service
public class UserService implements IUserService {
    private Hashtable<String,User> db = new Hashtable<>();

    public UserService(){
        System.out.println("11111初始化UserService1111");
        // BCryptPasswordEncoder这个类型去加密
        String encPass1 = SecurityUtil.encryptPassword("admin123");
        String encPass2 = SecurityUtil.encryptPassword("tom123");
        String encPass3 = SecurityUtil.encryptPassword("jack123");
        db.put("admin",new User("admin",encPass1));
        db.put("tom",new User("tom",encPass2));
        db.put("jack",new User("jack",encPass3));
    }
    @Override
    public User selectUserByUsername(String username) {
        return db.get(username);
    }
}
