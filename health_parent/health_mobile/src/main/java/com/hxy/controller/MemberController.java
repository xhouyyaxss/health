package com.hxy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hxy.constant.MessageConstant;
import com.hxy.constant.RedisMessageConstant;
import com.hxy.entity.Result;
import com.hxy.pojo.Member;
import com.hxy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/11
 * @time: 20:08
 */
@RestController
@RequestMapping("/member")
public class MemberController {


    @Autowired
    JedisPool jedisPool;

    @Reference
    MemberService memberService;

    //检验快速登录
    @RequestMapping("/login")
    public Result login(@RequestBody Map map, HttpServletResponse response)
    {
        //拿到电话和验证码
        String telephone=(String)map.get("telephone");
        String validateCode=(String)map.get("validateCode");
        //将验证码和redis中的验证码进行比较
        String key=telephone+ RedisMessageConstant.SENDTYPE_LOGIN;
        String value = jedisPool.getResource().get(key);
        if(value!=null&&validateCode!=null&&validateCode.equals(value))
        {
            //判断是否是会员
            Member member = memberService.findByTelephone(telephone);
            //不是的话注册一个
            if(member==null)
            {
                //当前只保存手机号和注册时间
                member=new Member();
                member.setRegTime(Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())));
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            //将手机号放入redis缓存用于登录免验证，向客户端写入cookie,内容是手机号
            Cookie cookie=new Cookie("login_member_telephone",telephone);
            //设置cookie的保存路径“/”为了保证任意路径都能访问到
            cookie.setPath("/");//设置路径
            //设置cookie的有效时间
            cookie.setMaxAge(60*60*24*30);//有效期30天
            response.addCookie(cookie);//将cookie添加到响应
            String json = JSON.toJSON(member).toString();
            //redis不能保存对象，要先转化成json对象
            //下次登录的时候可以先验证redis或者cookie中的信息，通过就直接登录，并且将信息保存到session中，不通过转到再转到登录界面
            jedisPool.getResource().setex(telephone,30*60,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);

        }
        else {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
