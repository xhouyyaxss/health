package com.hxy.service;

import com.hxy.pojo.User;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/12
 * @time: 15:47
 */
public interface UserService {

    public User findByUserName(String username);

}
