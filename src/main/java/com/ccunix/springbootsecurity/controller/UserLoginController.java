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
        AjaxResult ajax = AjaxResult.success();
        List<Goods> goodList = new ArrayList<>();
        Goods g1 = new Goods("1","苹果",10.5);
        Goods g2 = new Goods("1","香蕉",18.5);
        Goods g3 = new Goods("1","橘子",15.5);
        Goods g4 = new Goods("1","柚子",30.5);
        goodList.add(g1);
        goodList.add(g2);
        goodList.add(g3);
        goodList.add(g4);
        ajax.put("data",goodList);
        return ajax;
    }

    @PostMapping("/register")
    public AjaxResult register(String username, String password){
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", "success");
        return ajax;
    }
}
