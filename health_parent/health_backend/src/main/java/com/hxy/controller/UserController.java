package com.hxy.controller;

import com.hxy.constant.MessageConstant;
import com.hxy.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/13
 * @time: 0:04
 */
@RestController
@RequestMapping("/user")
public class UserController {

    //获得当前用户的登录名
    @RequestMapping("/getUsername")
    public Result getUsername()
    {
        //springsecurity帮我们完成认证之后，会将用户信息保存到框架提供的上下文中
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User user = (User)authentication.getPrincipal();//得到User对象，是security提供的User对象
        if(user!=null)
        {
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }
        else
        {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }

    }

}
