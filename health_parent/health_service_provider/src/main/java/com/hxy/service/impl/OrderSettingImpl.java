package com.hxy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hxy.dao.OrderSettingDao;
import com.hxy.pojo.OrderSetting;
import com.hxy.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/8
 * @time: 20:14
 */
@Service(interfaceClass = OrderSettingService.class)
@org.springframework.stereotype.Service
@Transactional
public class OrderSettingImpl implements OrderSettingService {


    @Autowired
    OrderSettingDao orderSettingDao;

    //批量导入预约设置
    public void add(List<OrderSetting> orderSettings) {

         if(orderSettings!=null&&orderSettings.size()>0)
         {
             for(OrderSetting orderSetting:orderSettings)
             {

                 Date orderDate = orderSetting.getOrderDate();
                 Calendar ca=Calendar.getInstance();
                 ca.setTime(orderDate);
                 Integer iyear = ca.get(Calendar.YEAR);
                 Integer im=ca.get(Calendar.MONTH)+1;
                 Integer id=ca.get(Calendar.DAY_OF_MONTH);
                 String beginDate=Integer.toString(iyear)+"-"+Integer.toString(im)+"-"+Integer.toString(id);
                 String endDate=Integer.toString(iyear)+"-"+Integer.toString(im)+"-"+Integer.toString(id);
                 Map d=new HashMap();
                 d.put("beginDate",beginDate);
                 d.put("endDate",endDate);
                 List<OrderSetting> currentMonthOrderSetting = orderSettingDao.findCurrentMonthOrderSetting(d);
                 Integer count=currentMonthOrderSetting.size();
//                 Integer count=orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                 System.out.println(count+"条 "+orderSetting.getOrderDate()+"导入的时候");
                 //先判断对应日期有没有预约
                 if(count>0)
                 {
                     currentMonthOrderSetting.get(0).setNumber(orderSetting.getNumber());
                     //有的话修改
                     orderSettingDao.editNumByorderOrderDate(currentMonthOrderSetting.get(0));
                 }
                 else
                 {
                     //没有预约就插入
                     orderSettingDao.add(orderSetting);
                 }
             }
         }

    }

    //查询当月的预约数目
    public List<Map> findCurrentMonthOrderSetting(String year, String month) {
        //判断闰年
        String day="31";
        if(month.equals("2")||month.equals("4")||month.equals("6")||month.equals("9")||month.equals("11"))day="30";
        Integer iyear=Integer.parseInt(year);
        if(((iyear%4==0)&&iyear%100!=0)||(iyear%400==0))
        {
            if(iyear==2)day="28";
        }

           //数据库格式yyyy-MM-dd
        String endDate=year+"-"+month+"-"+day;
        String beginDate=year+"-"+month+"-01";
//        System.out.println(beginDate+""+endDate);
        Map d=new HashMap();
        d.put("beginDate",beginDate);
        d.put("endDate",endDate);
        //查询所在日期之间的预约
        List<OrderSetting> list=orderSettingDao.findCurrentMonthOrderSetting(d);
        List<Map>maps=new ArrayList<Map>();
        if(list.size()!=0)
        {
            //将每一条数据放在map中传到前台
            for(OrderSetting orderSetting:list)
            {
                Map map=new HashMap();
                //老版的Date中的getMonth方法已经过时，要用Calendar中的方法
                Date date=orderSetting.getOrderDate();
                Calendar ca=Calendar.getInstance();
                ca.setTime(date);
                int dayofmonth = ca.get(Calendar.DAY_OF_MONTH);
                map.put("date",dayofmonth);
                map.put("number",orderSetting.getNumber());
                Integer reservations = orderSetting.getReservations();
                if(reservations==null)reservations=0;
                map.put("reservations", reservations);
                maps.add(map);
            }
        }

        return maps;
    }

    //根据日期设置人数
    public void setNumber(OrderSetting orderSetting) {

        Date orderDate = orderSetting.getOrderDate();
        //创建Calendar对象
        Calendar ca=Calendar.getInstance();
        //设置时间参数
        ca.setTime(orderDate);
        //得到年份
        Integer iyear = ca.get(Calendar.YEAR);
        //得到月份要加1
        Integer im=ca.get(Calendar.MONTH)+1;
        //得到天数
        Integer id=ca.get(Calendar.DAY_OF_MONTH);
        //将开始日期和结束日期设置成一样的便于查找某一固定时间的设置
        String beginDate=Integer.toString(iyear)+"-"+Integer.toString(im)+"-"+Integer.toString(id);
        String endDate=Integer.toString(iyear)+"-"+Integer.toString(im)+"-"+Integer.toString(id);
        Map d=new HashMap();
        d.put("beginDate",beginDate);
        d.put("endDate",endDate);
        List<OrderSetting> currentMonthOrderSetting = orderSettingDao.findCurrentMonthOrderSetting(d);
        Integer count=currentMonthOrderSetting.size();

//        Integer count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
//        System.out.println(count+"->>>>");
//        System.out.println(orderSetting.getOrderDate()+" "+orderSetting.getNumber()+" "+orderSetting.getId());
//        System.out.println(beginDate+" "+endDate);

        if(count>0)//这个日期有预约就调用编辑方法
        {
            currentMonthOrderSetting.get(0).setNumber(orderSetting.getNumber());
            orderSettingDao.editNumByorderOrderDate(currentMonthOrderSetting.get(0));
        }
        else orderSettingDao.add(orderSetting);//否则就添加

    }
}
