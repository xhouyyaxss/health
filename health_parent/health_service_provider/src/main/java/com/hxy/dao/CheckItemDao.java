package com.hxy.dao;

import com.github.pagehelper.Page;
import com.hxy.pojo.CheckItem;

import java.util.List;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/6
 * @time: 1:06
 */
public interface CheckItemDao {

    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(String queryString);

    public void deleteById(Integer id);

    public Long findCountByCheckItemId(Integer id);

    public void edit(CheckItem checkItem);

    public List<CheckItem> findAll();

    public CheckItem findById(Integer id);
}
