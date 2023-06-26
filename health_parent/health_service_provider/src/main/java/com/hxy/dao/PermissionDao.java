package com.hxy.dao;

import com.hxy.pojo.Permission;

import java.util.Set;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/12
 * @time: 16:13
 */
public interface PermissionDao {

    public Set<Permission> findByRoleId(Integer roleId);
}
