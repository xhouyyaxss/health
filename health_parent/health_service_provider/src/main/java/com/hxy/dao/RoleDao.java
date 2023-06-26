package com.hxy.dao;

import com.hxy.pojo.Role;
import java.util.Set;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/12
 * @time: 16:10
 */
public interface RoleDao {

    public Set<Role> findByUserId(Integer userId);
}
