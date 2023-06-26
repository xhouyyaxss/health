package com.hxy.dao;

import com.hxy.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/8
 * @time: 20:18
 */
public interface OrderSettingDao {

    //添加预约设置
    public void add(OrderSetting orderSetting);
    //查找指定日期预约设置
    public Integer findCountByOrderDate(Date orderDate);
    //编辑预约人数
    public void editNumByorderOrderDate(OrderSetting orderSetting);

    public List<OrderSetting> findCurrentMonthOrderSetting(Map map);
    //编辑已预约人数
    public void updateReservations(OrderSetting orderSetting);
}
