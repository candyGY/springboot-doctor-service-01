package com.ccunix.springbootsecurity.service;

import com.ccunix.springbootsecurity.domain.User;

public interface IUserService {
    User selectUserByUsername(String username);
}
