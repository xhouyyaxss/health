package com.hxy.service;

import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.pojo.CheckItem;

import java.util.List;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/6
 * @time: 0:54
 */
//服务接口
public interface CheckItemService {


     public void add(CheckItem  checkItem);

     public PageResult pageQuery(QueryPageBean queryPageBean);

    public void  deleteById(Integer id);

    public void edit(CheckItem checkItem);

    public List<CheckItem> findAll();

    public CheckItem findById(Integer id);
}
