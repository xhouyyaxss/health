package com.hxy.service;

import com.hxy.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/8
 * @time: 20:07
 */
public interface OrderSettingService {

    public void add(List<OrderSetting> orderSettings);

    public List<Map> findCurrentMonthOrderSetting(String year, String month);

    public void setNumber(OrderSetting orderSetting);


}
