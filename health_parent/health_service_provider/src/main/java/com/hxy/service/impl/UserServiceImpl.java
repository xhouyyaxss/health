package com.hxy.service.impl;

import com.hxy.dao.PermissionDao;
import com.hxy.dao.RoleDao;
import com.hxy.dao.UserDao;
import com.hxy.pojo.Permission;
import com.hxy.pojo.Role;
import com.hxy.pojo.User;
import com.hxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/12
 * @time: 16:04
 */
@Transactional
@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    //查询用户信息和对应的角色信息,同时需要查询关联的权限信息
    public User findByUserName(String username) {
        User user = userDao.findByUserName(username);
        if(user==null)
        {
            return null;

        }
        Integer userId=user.getId();
        Set<Role> roles =roleDao.findByUserId(userId);
        for(Role role:roles)
        {
            Set<Permission> permissions = permissionDao.findByRoleId(role.getId());
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }
}
