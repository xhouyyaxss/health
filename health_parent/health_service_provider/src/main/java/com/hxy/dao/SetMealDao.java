package com.hxy.dao;

import com.github.pagehelper.Page;
import com.hxy.pojo.CheckGroup;
import com.hxy.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/8
 * @time: 15:37
 */
public interface SetMealDao {

    //增加套餐
    public void add(Setmeal setMeal);

    //增加套餐和检查组
    public void setMealAndGroup(Map map);

    //条件查询
    public Page<Setmeal> selectByCondition(String queryString);

    //查找所有的setmeal
    public List<Setmeal> findAll();

    //通过id查找setmeal
    public Setmeal findById(Integer id);

    public List<CheckGroup> findCheckGroup(Integer id);

    public List<Map<String,Object>> findSetmealCount();

   public List<Map<String,Object>> findSetmealCountAndProportion();
}
