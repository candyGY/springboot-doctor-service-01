package com.ccunix.springbootsecurity.service;

import com.ccunix.springbootsecurity.domain.model.LoginUser;
import com.ccunix.springbootsecurity.utils.Constants;
import com.ccunix.springbootsecurity.utils.RedisCache;
import com.ccunix.springbootsecurity.utils.StringUtils;
import com.ccunix.springbootsecurity.utils.uuid.IdUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TokenService {
    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌秘钥  Authorization
    @Value("${token.header}")
    private String header;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    RedisCache redisCache;

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser)
    {
        // 不重复的字符串uuid 推荐分布式
        String token = IdUtils.fastUUID();
        System.out.println("uuid token="+token);
        loginUser.setToken(token);
        // 刷新用户信息   单点登录   admin     admin  存储redis
        refreshToken(loginUser);
        // 存储d获得的token信息
        Map<String, Object> claims = new HashMap<>();
        // 令牌前缀login_user_key  LOGIN_USER_KEY
        claims.put(Constants.LOGIN_USER_KEY,token);
        return createToken(claims);
    }
    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }
    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存   把用户信息缓存起来key:  login_tokens:fdsfdsfdstrtrtrtret
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }
    // 获得令牌的key
    private String getTokenKey(String uuid)
    {
        // LOGIN_TOKEN_KEY  login_tokens
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }
    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        // 从请求头中获得header变量的值 值为：Authorization
        String token = request.getHeader(header);
        // TOKEN_PREFIX 令牌前缀 "Bearer "+token
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            //判断令牌是不是空，是不是Bearer 开头，如果是，就用“”替换“Bearer ”
            //其实我们要的还是token，只是为了安全我在它前面添加一点东西，用的时候再把东西去掉
            //这样就不容易被破解，一定程度上提高了安全性
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }
    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     *  获得登录用户   携带令牌
     *  key:Authorization
     *  value:Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImFkZTQyYWQ4LTkwNTAtNGJmOC1iYmVmLTk2NzUwMzQxN2ZjZCJ9.h6EtJTIAJjycGMsQEJR17roiGnD3RqC_AVvwxq7VzqshnLmozzcKxVeZqU-jzVvv50piu756-3TfXCbpAPpMqw
     *  令牌都放到请求头
     * @param request
     * @return
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        // token:eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImFkZTQyYWQ4LTkwNTAtN........
        if (StringUtils.isNotEmpty(token)) {
            try {
                // 数据声明
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                LoginUser user = redisCache.getCacheObject(userKey);
                return user;
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }
}
