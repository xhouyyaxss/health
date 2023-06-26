package com.hxy.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/10
 * @time: 23:41
 */
public class CheckDayUtils {

    //将日期转化成yyyy-mm-dd
    public static String getBeginAndEnd(Date date)
    {
        Calendar ca=Calendar.getInstance();
        ca.setTime(date);
        Integer imonth = ca.get(Calendar.MONTH) + 1;
        Integer iyear = ca.get(Calendar.YEAR);
        Integer iday=ca.get(Calendar.DAY_OF_MONTH);
        return iyear.toString()+"-"+imonth.toString()+"-"+iday.toString();
    }
    public static String getToday()
    {
        Calendar ca=Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
    }

    public static String getMonthFirstDay(Date date)
    {
        Calendar ca=Calendar.getInstance();
        ca.setTime(date);
        Integer imonth = ca.get(Calendar.MONTH) + 1;
        Integer iyear = ca.get(Calendar.YEAR);
        return iyear.toString()+"-"+imonth.toString()+"-1";
    }

    public static String getThisMonday()
    {
        Calendar ca=Calendar.getInstance();
        Integer day = ca.get(Calendar.DAY_OF_WEEK);
        ca.add(Calendar.DAY_OF_WEEK,-day);
        String format = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
        return format;
    }

    public static String getFirstDayOdThisMonth()
    {
        Calendar ca=Calendar.getInstance();
        Integer month = ca.get(Calendar.MONTH)+1;
        Integer year = ca.get(Calendar.YEAR);
        String smonth="";
        if(month<10)smonth="0"+month.toString();
        return year.toString()+"-"+smonth+"-"+"01";
    }

}
