package com.hxy.jobs;

import com.hxy.constant.RedisConstant;
import com.hxy.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/8
 * @time: 19:05
 */
//自定义job定时清理垃圾图片
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;
    public void clearImg()
    {
        Jedis jedis = jedisPool.getResource();
        //根据Redis中保存的两个set对象进行差值计算，获得垃圾图片的集合
        Set<String> sdiff = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(sdiff!=null)
        {
            for(String name:sdiff)
            {
                //删除七牛云服务器上的垃圾图片
                QiniuUtils.deleteFileFromQiniu(name);
                //从redis集合中删除
                jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES,name);
            }
        }
    }
}
