package com.hxy.service;

import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.pojo.CheckGroup;

import java.util.List;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/7
 * @time: 0:32
 */
public interface CheckGroupService {

    public void add(CheckGroup checkGroup,Integer[] checkitemIds);

    public PageResult queryPage(QueryPageBean queryPageBean);

    public void deleteById(Integer id);

    public List<Integer> findCheckItemIds(Integer checkgroupId);

    public void edit(CheckGroup checkgroup,Integer[] checkitemIds);

    public List<CheckGroup> findAll();
}
