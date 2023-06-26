package com.hxy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hxy.constant.MessageConstant;
import com.hxy.entity.Result;
import com.hxy.pojo.Setmeal;
import com.hxy.service.SetMealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/9
 * @time: 19:39
 */
@RestController
@RequestMapping("/setmeal")
public class Setmealcontroller {


    @Reference
    SetMealService setMealService;

    //得到全部的套餐传给用户看
    @RequestMapping("/getSetmeal")
    public Result getSetmeal()
    {
        List<Setmeal> list=null;
        try {
            list=setMealService.findAll();
        }catch(Exception e)
        {
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }

        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,list);
    }

    //根据setmealid查询信息
    @RequestMapping("/findById")
    public Result findSetmealById(@RequestParam("id") Integer id)
    {
        Setmeal setmeal=null;
        try{
            setmeal=setMealService.findById(id);
        }catch(Exception e) {

            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

}
