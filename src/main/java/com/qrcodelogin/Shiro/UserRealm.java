package com.qrcodelogin.Shiro;

import com.qrcodelogin.entity.Role;
import com.qrcodelogin.entity.User;
import com.qrcodelogin.service.PermissionService;
import com.qrcodelogin.service.RoleService;
import com.qrcodelogin.service.UserService;
import com.qrcodelogin.service.impl.UserServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 负责认证用户身份和对用户进行授权
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    //用户授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){
        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<Role> roleList = roleService.findRoleByUserId(user.getId());
        Set<String> roleSet = new HashSet<>();
        List<Integer> roleIds = new ArrayList<>();
        for (Role role : roleList){
            roleSet.add(role.getRole());
            roleIds.add(role.getId());
        }
        //放入角色信息
        authorizationInfo.setRoles(roleSet);
        //放入权限信息
        List<String> permissionList = permissionService.findByRoleId(roleIds);
        authorizationInfo.setStringPermissions(new HashSet<>(permissionList));

        return authorizationInfo;
    }

    //用户认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException{
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;
        User user = userService.findByAccount(token.getUsername());
        if (user == null){
            return null;
        }
        return new SimpleAuthenticationInfo(user, user.getUserPwd(), getName());
    }
}
