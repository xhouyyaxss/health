package com.hxy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hxy.constant.MessageConstant;
import com.hxy.constant.RedisMessageConstant;
import com.hxy.entity.Result;
import com.hxy.pojo.Order;
import com.hxy.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * 体检预约处理
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/10
 * @time: 22:43
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    JedisPool jedisPool;

    @Reference
    OrderService orderService;



    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        //拿到缓存中的key
        String key = (String) map.get("telephone") + RedisMessageConstant.SENDTYPE_ORDER;
        //拿到验证码
        String validateCode = (String) map.get("validateCode");
        //拿到redis中的验证码
        String value = jedisPool.getResource().get(key);
        if (validateCode != null && value != null && validateCode.equals(value)) {
            //设置预约类型 分为微信预约和电话预约
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result = null;
            try {

                result = orderService.addOrder(map);//通过远程服务调用
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            return result;//预约成功
//            if (result.isFlag()) {
//                try {
////                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, (String) map.get("telephone"), (String) map.get("orderDate"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        } else {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    //根据预约id查询预约的详细信息
    @RequestMapping("/findById")
    public Result findById(@RequestParam("orderId") Integer orderId){

        Map order=new HashMap();

        try{
            order=orderService.findById(orderId);
        }catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,order);

    }
}
