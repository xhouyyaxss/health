package com.hxy.dao;

import com.github.pagehelper.Page;
import com.hxy.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/7
 * @time: 0:34
 */
public interface CheckGroupDao {

    public void add(CheckGroup checkGroup);

    public void setGroupAndItem(Map map);

    public Page<CheckGroup> selectByCondition(String querystring);

    public void deleteById(Integer id);

    public void deleteGroupAndItem(Integer id);

    public List<Integer> findCheckItemIds(Integer checkgroupId);

    public void edit(CheckGroup checkGroup);

    public List<CheckGroup>findAll();

    public CheckGroup findById(Integer id);
}
