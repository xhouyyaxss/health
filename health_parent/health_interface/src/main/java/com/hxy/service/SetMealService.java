package com.hxy.service;

import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/8
 * @time: 15:31
 */

public interface SetMealService {

    public void add(Setmeal setmeal,Integer[] checkgroupIds);

    public PageResult findPage(QueryPageBean queryPageBean);

    public List<Setmeal> findAll();

    public Setmeal findById(Integer id);

    public void generateHtml(String templateName, String pageHtml, Map map);

   public void generateMobileSetmealListHtml(List<Setmeal> list);

   public void generateMobileSetmealDetailHtml(List<Setmeal> list);

   public void generateMobileStaticHtml();

   public Map getSetmealReport();

   public List getHotSetmeal();


}
