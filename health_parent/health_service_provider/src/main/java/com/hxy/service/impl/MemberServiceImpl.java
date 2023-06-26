package com.hxy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hxy.dao.MemberDao;
import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.pojo.Member;
import com.hxy.service.MemberService;
import com.hxy.utils.CheckDayUtils;
import com.hxy.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/11
 * @time: 0:11
 */
@Service(interfaceClass = MemberService.class)
@org.springframework.stereotype.Service
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;
    public Member findByTelephone(String telephone) {

        Member member = memberDao.findByTelephone(telephone);
        return member;
    }

    public void add(Member member) {
        String password = member.getPassword();
        member.setRegTime(new Date());
        if(password!=null)
        {
            String enPassword = MD5Utils.md5(password);
            member.setPassword(enPassword);
        }
        memberDao.add(member);
    }

    //获得过去一年每个月的会员数量
    public Map findCountOnPastYear() throws ParseException {
//      Date nowDate=new Date();
        Calendar ca=Calendar.getInstance();//获得日历对象
        //将月份向前推12个月
        ca.add(Calendar.MONTH,-12);
        List<String> months=new ArrayList<String>();
        List<Integer>memberCount=new ArrayList<Integer>();
        Map map=new HashMap();
        for(int i=0;i<12;i++)
        {
            String beginDate = CheckDayUtils.getMonthFirstDay(ca.getTime());
//            Date bdate = new SimpleDateFormat("yyyy-MM-dd").parse(beginDate);
            Integer beginCount = memberDao.findMemberCountBeforeDate(beginDate);
            System.out.print(beginDate);
            Integer year=ca.get(Calendar.YEAR);
            ca.add(Calendar.MONTH,1);
            String endDate=CheckDayUtils.getMonthFirstDay(ca.getTime());
            System.out.println("----->"+endDate);
//            Date edate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);---------------
            Integer endCount = memberDao.findMemberCountBeforeDate(endDate);
            Integer bCount = memberDao.findMemberCountByDate(beginDate);
            Integer eCount = memberDao.findMemberCountByDate(endDate);
            Integer count = endCount - beginCount - eCount + bCount;
            Integer imonth=ca.get(Calendar.MONTH);
            String smonth="";
            if(imonth==0)smonth="12";
            else {
                if(imonth<10)smonth="0"+imonth.toString();
                else {smonth=imonth.toString();}
            }

            String month=year.toString()+"."+smonth;
            months.add(month);
            memberCount.add(count);
        }
        map.put("months",months);
        map.put("memberCount",memberCount);
        return map;
    }

    //获取近期会员新增数据
    public Map<String, Integer> getAddMemberCount() {

        Integer todayNewMember = memberDao.findMemberCountByDate(CheckDayUtils.getToday());
        Integer totalMember = memberDao.findMemberTotalCount();
        Integer thisWeek = memberDao.findMemberCountAfterDate(CheckDayUtils.getThisMonday());

        Integer thisMonth = memberDao.findMemberCountAfterDate(CheckDayUtils.getFirstDayOdThisMonth());
        Map map=new HashMap();
        map.put("todayNewMember",todayNewMember);
        map.put("totalMember",totalMember);
        map.put("thisWeekNewMember",thisWeek);
        map.put("thisMonthNewMember",thisMonth);
        return map;
    }

    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Member> pages = memberDao.findByCondition(queryString);
        long total = pages.getTotal();
        List<Member> result = pages.getResult();
        return new PageResult(total,result);
    }
}
