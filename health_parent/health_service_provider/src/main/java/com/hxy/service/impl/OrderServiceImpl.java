package com.hxy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hxy.constant.MessageConstant;
import com.hxy.dao.MemberDao;
import com.hxy.dao.OrderDao;
import com.hxy.dao.OrderSettingDao;
import com.hxy.entity.Result;
import com.hxy.pojo.Member;
import com.hxy.pojo.Order;
import com.hxy.pojo.OrderSetting;
import com.hxy.service.OrderService;
import com.hxy.utils.CheckDayUtils;
import com.hxy.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/10
 * @time: 22:54
 */
@Transactional
@Service(interfaceClass = OrderService.class)
@org.springframework.stereotype.Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    //预约服务实现类
    public Result addOrder(Map map) throws Exception {

        if(map.get("orderDate")==null)return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        String sdate = (String)map.get("orderDate");
        //将日期的格式转化成yyyy-mm-dd（要和数据路中的日期格式保持一致）
        Date date = DateUtils.parseString2Date(sdate, "yyyy-MM-dd");
        //检查预约日期是否有预约设置并且还有余额
        String beginDate= CheckDayUtils.getBeginAndEnd(date);
        String endDate= CheckDayUtils.getBeginAndEnd(date);
        Map dateMap=new HashMap();
        dateMap.put("beginDate",beginDate);
        dateMap.put("endDate",endDate);
        List<OrderSetting> orderSettings = orderSettingDao.findCurrentMonthOrderSetting(dateMap);
        //判断指定日期是否能预约
        if(orderSettings==null&&orderSettings.size()==0)return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        OrderSetting orderSetting=orderSettings.get(0);
        //判断当天的预约数目是否满了
        if(orderSetting.getReservations()>=orderSetting.getNumber()) return new Result(false,MessageConstant.ORDER_FULL);
        //一个用户一天是否重复预约 通过手机号判断一个用户是否在这一天预约过了
        String telephone=(String)map.get("telephone");
        //判断是不是会员
        Member ismember = memberDao.findByTelephone(telephone);
        if(ismember!=null)
        {
            //判断是否重复预约
            Integer memberId = ismember.getId();
           Integer setmealId=Integer.parseInt((String)map.get("setmealId"));
           Order order=new Order(memberId,date,setmealId);
           //根据条件进行查询
            List<Order> orders = orderDao.findByCondition(order);
            if(orders!=null&&orders.size()>0)
            {
                //已经预约过了
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        else
        {
            //不是会员就注册一个，
            ismember=new Member();
            ismember.setPhoneNumber(telephone);
            ismember.setIdCard((String)map.get("idCard"));
            ismember.setName((String)map.get("name"));
            ismember.setSex((String)map.get("sex"));
            ismember.setRegTime(new Date());
            memberDao.add(ismember);
        }
        //预约成功，更新预约的人数
        Integer reservation=orderSetting.getReservations();
        orderSetting.setReservations(reservation+1);
        orderSettingDao.updateReservations(orderSetting);

        Order order=new Order();
        order.setOrderDate(date);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
        order.setMemberId(ismember.getId());
        order.setOrderType((String)map.get("orderType"));
        orderDao.add(order);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //通过id查找order(体检人姓名，套餐名称，预约状态，预约日期)
    public Map findById(Integer orderId) throws Exception {

       Map order = orderDao.findById4Details(orderId);
       Date sdate=(Date)order.get("orderDate");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(sdate);
        order.put("orderDate",format);
        return order;
    }

    //获得今日 这周 这月 的order数量以及visit
    public Map<String, Integer> getAddOrderCount() {

        Integer todayOrderNumber=orderDao.findOrderCountByDate(CheckDayUtils.getToday());
        Integer todayVisitNumber=orderDao.findVisitCountByDate(CheckDayUtils.getToday());
        Integer thisWeekOrderNumber=orderDao.findOrderCountAfterDate(CheckDayUtils.getThisMonday());
        Integer thisWeekVisitNumber=orderDao.findVisitCountAfterDate(CheckDayUtils.getThisMonday());
        Integer thisMonthOrderNumber=orderDao.findOrderCountAfterDate(CheckDayUtils.getFirstDayOdThisMonth());
        Integer thisMonthVisitNumber=orderDao.findVisitCountAfterDate(CheckDayUtils.getFirstDayOdThisMonth());
        Map map=new HashMap();
        map.put("todayOrderNumber",todayOrderNumber);
        map.put("todayVisitsNumber",todayVisitNumber);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        map.put("thisWeekVisitsNumber",thisWeekVisitNumber);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        map.put("thisMonthVisitsNumber",thisMonthVisitNumber);

        return map;
    }
}
