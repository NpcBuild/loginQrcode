package com.qrcodelogin.service;

import com.qrcodelogin.entity.User;

public interface UserService {
    public User findByAccount(String account);
}
