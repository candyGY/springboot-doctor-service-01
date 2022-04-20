package com.ccunix.springbootsecurity.domain;

import com.ccunix.springbootsecurity.domain.entity.BaseEntity;
import lombok.Data;

@Data
public class User extends BaseEntity {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
