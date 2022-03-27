package com.qrcodelogin.service.impl;

import com.qrcodelogin.Dao.UserMapper;
import com.qrcodelogin.entity.User;
import com.qrcodelogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByAccount(String account) {
        return userMapper.findByAccount(account);
    }
}