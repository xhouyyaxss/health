package com.hxy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hxy.constant.MessageConstant;
import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.entity.Result;
import com.hxy.pojo.CheckGroup;
import com.hxy.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/7
 * @time: 0:36
 */
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    CheckGroupService checkGroupService;


    //添加检查组
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds)
    {
        try{
//           新增成功
            checkGroupService.add(checkGroup,checkitemIds);
            System.out.println(checkitemIds);

            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        }catch(Exception e)
        {
            //失败
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }

    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean)
    {
          return checkGroupService.queryPage(queryPageBean);
    }

    //删除检查组
    @RequestMapping("/delete")
    public Result delete(Integer id)
    {

       try{

           checkGroupService.deleteById(id);

       }
       catch(Exception e)
       {
           return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);//删除失败
       }
       return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);//删除成功
    }

    @RequestMapping("/findCheckitemIds")
    public Result getCheckItemIds(Integer checkgroupId)
    {

        List<Integer> checkItemIds = checkGroupService.findCheckItemIds(checkgroupId);
        System.out.println(checkItemIds);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);

    }

    //编辑检查组
    @RequestMapping("/edit")
   public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds)
    {
        try{
            checkGroupService.edit(checkGroup,checkitemIds);

        }catch(Exception e)
        {
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findAll")
    public Result findAll()
    {
        List<CheckGroup>list=null;

        try{
            list=checkGroupService.findAll();

        }catch(Exception e)
        {
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);

    }

}
