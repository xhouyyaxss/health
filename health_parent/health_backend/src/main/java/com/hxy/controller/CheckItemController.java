package com.hxy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hxy.constant.MessageConstant;
import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.entity.Result;
import com.hxy.pojo.CheckItem;
import com.hxy.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/6
 * @time: 0:48
 * 检查项管理
 */
@RestController
@RequestMapping("/checkItem")
public class CheckItemController {


    @Reference//查找服务
    private CheckItemService checkItemService;


    //新增检查项
    @RequestMapping("/add")
    public Result checkItemAdd(@RequestBody CheckItem checkItem)//@RequestBody从前台将json数据转化成对象
    {
           try {
               checkItemService.add(checkItem);
           }catch(Exception e){
               //e.printStackTrace();
               return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
    }
           return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean)
    {
        return checkItemService.pageQuery(queryPageBean);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/delete")
    public Result delete(Integer id)
    {

        try{
            checkItemService.deleteById(id);
        }catch(Exception e)
        {
            //调用失败
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //编辑checkitem
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem)
    {

        try{
            checkItemService.edit(checkItem);
        }catch(Exception e)
        {//调用失败
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);

    }

    @RequestMapping("/findAll")
    public Result findAll()
    {
        try{
            List<CheckItem> list=checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        }catch(Exception e)
        {//调用失败
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }



}
