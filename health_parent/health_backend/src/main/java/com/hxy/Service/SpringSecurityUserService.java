package com.hxy.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hxy.pojo.Permission;
import com.hxy.pojo.Role;
import com.hxy.pojo.User;
import com.hxy.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/12
 * @time: 15:44
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {


    //通过dubbo调用远程接口
    @Reference
    UserService userService;
    //根据用户名查询数据库获取用户信息  该方法框架会自动调用
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user =userService.findByUserName(username);
        if(user==null)//用户名不存在
        {
            System.out.println("用户不存在");
            return null;
        }
        else
        {
            //创建用户的角色和权限的列表，该对象集合就是security框架内部用于存方角色和权限的
            List<GrantedAuthority>list=new ArrayList<>();
            //动态为当前用户授权
            Set<Role> roles=user.getRoles();
            //遍历角色
            for(Role role:roles)
            {
                //为用户授予角色
                 list.add(new SimpleGrantedAuthority(role.getKeyword()));
                System.out.println(role.getKeyword());
                 //获得该角色对用的权限
                 Set<Permission> permissions=role.getPermissions();
                 //便利权限集合，为用户授权
                for(Permission permission:permissions)
                {
                    list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    System.out.println(permission.getKeyword());
                }
            }
            //返回一个security框架中的User对昂，参数是用户名，数据库中的密码和角色权限列表
            //加noop表示不加密，否则验证加密的
            return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
        }
    }
}
