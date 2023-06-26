package com.hxy.dao;

import com.github.pagehelper.Page;
import com.hxy.pojo.Member;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/11
 * @time: 0:09
 */
public interface MemberDao {

    public Member findByTelephone(String telephone);


    public void add(Member member);

    public Integer findMemberCountBeforeDate(String date);

    public Integer findMemberCountByDate(String date);

    public Integer findMemberTotalCount();

    public Integer findMemberCountAfterDate(String date);

    public Page<Member> findByCondition(String queryString);

}
