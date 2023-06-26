package com.hxy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hxy.constant.MessageConstant;
import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.entity.Result;
import com.hxy.pojo.Member;
import com.hxy.service.MemberService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/17
 * @time: 17:15
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Reference
    MemberService memberService;

    @RequestMapping("/upload")
    public Result uploadFile()
    {



        return new Result(true, MessageConstant.UPLOAD_SUCCESS);
    }

    /**
     * 新增会员
     * @param member
     * @return
     */

    @RequestMapping("/add")
    public Result addMember(@RequestBody Member member)
    {
       try{
           memberService.add(member);
       }
       catch(Exception e)
       {
           e.printStackTrace();
           return new Result(false,MessageConstant.ADD_MEMBER_FAIL);
       }
        return new Result(true,MessageConstant.ADD_MEMBER_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean)
    {

        PageResult page = memberService.findPage(queryPageBean);


        return page;
    }

}
