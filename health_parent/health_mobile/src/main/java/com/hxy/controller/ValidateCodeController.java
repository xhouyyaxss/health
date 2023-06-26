package com.hxy.controller;

import com.hxy.constant.MessageConstant;
import com.hxy.constant.RedisMessageConstant;
import com.hxy.entity.Result;
import com.hxy.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/10
 * @time: 22:04
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {


    @Autowired
    JedisPool jedisPool;

    //用户体检预约的时候发送的验证码
    @RequestMapping("/send4Order")
    public Result send4OrderValidateCode(@RequestParam("telephone") String telephone)
    {
         //给用户发送验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);

        try{
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,tel,validateCode.toString());
        }catch(Exception e)
        {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        //将验证码保存到redis中,key是手机号吗加后缀表示预约的时候的验证码
        //setex表示将数据放在缓存中多少时间（key,senconds,value）
         jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());

        //暂时没开通发短信，将验证码发送到前台
        return new Result(true,validateCode.toString(),validateCode);
    }

    @RequestMapping("/send4Login")
    public Result sned4LoginValidateCode(@RequestParam("telephone") String telephone)
    {
        //给用户发送验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);

        //将验证码保存到redis中,key是手机号吗加后缀表示预约的时候的验证码
        //setex表示将数据放在缓存中多少时间（key,senconds,value）
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());

        //暂时没开通发短信，将验证码发送到前台
        return new Result(true,validateCode.toString(),validateCode);
    }

}
