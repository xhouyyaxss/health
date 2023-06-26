package com.hxy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hxy.constant.MessageConstant;
import com.hxy.entity.Result;
import com.hxy.pojo.OrderSetting;
import com.hxy.service.OrderSettingService;
import com.hxy.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/8
 * @time: 19:48
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {


    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            List<String[]> rows = POIUtils.readExcel(excelFile);
            List<OrderSetting> data = new ArrayList<>();
            for (String[] strings : rows) {

                String date = strings[0];
                String number = strings[1];
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(number));
                data.add(orderSetting);
            }
            //通过dubbo远程调用后台服务
            orderSettingService.add(data);

        } catch (Exception e) {
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/findCurrentMonthOrderSetting")
    public Result findCurrentMonthOrder(@RequestParam("year") String year,@RequestParam("month") String month)
    {

        List<Map>data=null;

        try{

            data=orderSettingService.findCurrentMonthOrderSetting(year,month);
        }catch(Exception e)
        {

            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,data);

    }

    @RequestMapping("/setNumber")
    public Result setNumber(@RequestBody OrderSetting orderSetting)
    {
//        System.out.println(orderSetting.getOrderDate()+" "+orderSetting.getNumber());
//        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
//        String format = formatter.format(orderSetting.getOrderDate());
//        将日期按照格式转化
//        Date date=java.sql.Date.valueOf(format);
//        System.out.println(date);
//        orderSetting.setOrderDate(date);
        try{

           orderSettingService.setNumber(orderSetting);

       }catch(Exception e)
       {
           return new Result(false,MessageConstant.ORDERSETTING_FAIL);
       }
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);




    }
}
