package com.qrcodelogin.service.impl;

import com.qrcodelogin.Dao.RoleMapper;
import com.qrcodelogin.entity.Role;
import com.qrcodelogin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findRoleByUserId(Integer id) {
        return roleMapper.findRoleByUserId(id);
    }
}
