package com.hxy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hxy.constant.MessageConstant;
import com.hxy.constant.RedisConstant;
import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.entity.Result;
import com.hxy.pojo.Setmeal;
import com.hxy.service.SetMealService;
import com.hxy.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/8
 * @time: 14:56
 */
@RestController
@RequestMapping("/setMeal")
public class SetMealController {


    @Reference
    SetMealService setMealService;
    //jedispool注入
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        String fileName= UUID.randomUUID().toString();//
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf('.');
        String extention = originalFilename.substring(index - 1);
        try {
            //使用七牛云工具上传文件
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName+extention);
            Jedis jedis = jedisPool.getResource();
            //将上传的每一张图片名字加到缓存
            //set String hash list Zset
            jedis.sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName+extention);
        } catch (IOException e) {
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName+extention);
    }


    //添加检查套餐
    //get方法不能用@requestbody，要使用post
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds)
    {

        try{

            setMealService.add(setmeal,checkgroupIds);
        }
        catch(Exception e)
        {
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean)
    {


           return  setMealService.findPage(queryPageBean);


    }



}
