package com.qrcodelogin.service;

import com.qrcodelogin.entity.User;
import org.springframework.stereotype.Service;

public interface UserService {
    public User findByAccount(String account);
}
