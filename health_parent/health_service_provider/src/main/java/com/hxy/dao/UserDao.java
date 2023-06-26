package com.hxy.dao;

import com.hxy.pojo.User;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/12
 * @time: 16:05
 */
public interface UserDao {

    public User findByUserName(String username);

}
