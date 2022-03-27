package com.qrcodelogin.service;

import com.qrcodelogin.entity.Role;

import java.util.List;

public interface RoleService {
    public List<Role> findRoleByUserId(Integer id);
}
