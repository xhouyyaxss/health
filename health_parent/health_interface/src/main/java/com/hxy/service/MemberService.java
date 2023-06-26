package com.hxy.service;

import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.pojo.Member;

import java.text.ParseException;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/11
 * @time: 0:10
 */
public interface MemberService {


    //通过电话查会员
    public Member findByTelephone(String telephone);

    public void add(Member member);

    public Map findCountOnPastYear() throws ParseException;

    public Map<String,Integer>getAddMemberCount();

    public PageResult findPage(QueryPageBean queryPageBean);

}
