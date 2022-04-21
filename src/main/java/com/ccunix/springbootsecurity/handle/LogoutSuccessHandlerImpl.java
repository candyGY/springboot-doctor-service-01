package com.ccunix.springbootsecurity.handle;

import com.alibaba.fastjson.JSON;
import com.ccunix.springbootsecurity.domain.model.LoginUser;
import com.ccunix.springbootsecurity.service.TokenService;
import com.ccunix.springbootsecurity.utils.AjaxResult;
import com.ccunix.springbootsecurity.utils.HttpStatus;
import com.ccunix.springbootsecurity.utils.ServletUtils;
import com.ccunix.springbootsecurity.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 * 
 * @author 魏建波
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        String userName = "";
        if (StringUtils.isNotNull(loginUser))
        {
            userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS, userName+"退出成功")));
    }
}
