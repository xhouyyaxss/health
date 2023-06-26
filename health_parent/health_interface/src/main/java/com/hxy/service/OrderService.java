package com.hxy.service;

import com.hxy.entity.Result;

import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/10
 * @time: 22:54
 */
public interface OrderService {

    //预约服务
    public Result addOrder(Map map) throws Exception;

    //通过id查找订单
    public Map findById(Integer orderId) throws Exception;

    public Map<String,Integer> getAddOrderCount();

}
