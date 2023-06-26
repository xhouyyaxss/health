package com.hxy.dao;

import com.hxy.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/10
 * @time: 22:57
 */
public interface OrderDao {

    public void add(Order order);

    public List<Order> findByCondition(Order order);

    public Map findById4Details(Integer orderId);

    public Integer findCountBySetmealId(Integer id);

    public Integer findOrderCountByDate(String date);

    public Integer findOrderCountAfterDate(String date);

    public Integer findVisitCountByDate(String date);

    public Integer findVisitCountAfterDate(String date);
}
