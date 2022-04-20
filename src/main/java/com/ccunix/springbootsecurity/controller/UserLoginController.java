package com.ccunix.springbootsecurity.controller;

import com.ccunix.springbootsecurity.domain.Goods;
import com.ccunix.springbootsecurity.domain.model.LoginBody;
import com.ccunix.springbootsecurity.utils.AjaxResult;
import com.ccunix.springbootsecurity.service.UserLoginService;
import com.ccunix.springbootsecurity.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserLoginController {
    @Autowired
    UserLoginService userLoginService;
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody){
        AjaxResult ajax = AjaxResult.success();
        String token =  userLoginService.login(loginBody.getUsername(), loginBody.getPassword());
        //token
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @GetMapping("/goodsList")
    public AjaxResult list(){
        List<Goods> goodsList = new ArrayList<>();
        AjaxResult ajax = AjaxResult.success();
        Goods g1 = new Goods("1","苹果",10.5);
        Goods g2 = new Goods("2","香蕉",8.5);
        Goods g3 = new Goods("3","橘子",12.5);
        Goods g4 = new Goods("4","梨子",15.5);
        Goods g5 = new Goods("5","草莓",30.5);
        goodsList.add(g1);
        goodsList.add(g2);
        goodsList.add(g3);
        goodsList.add(g4);
        goodsList.add(g5);
        ajax.put("data",goodsList);
        return ajax;
    }

    @PostMapping("/register")
    public AjaxResult register(String username, String password){
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", "success");
        return ajax;
    }
}
