package com.ccunix.springbootsecurity.domain.model;

import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
public class LoginBody {
    private String username;
    private String password;
}
