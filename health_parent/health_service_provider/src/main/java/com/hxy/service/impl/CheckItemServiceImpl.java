package com.hxy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hxy.dao.CheckItemDao;
import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.pojo.CheckItem;
import com.hxy.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/6
 * @time: 1:04
 */
@Transactional
//添加事务注解之后要指定接口
@Service(interfaceClass = CheckItemService.class)
@org.springframework.stereotype.Service
public class CheckItemServiceImpl implements CheckItemService{

    @Autowired
    private CheckItemDao checkItemDao;

    public void add(CheckItem checkItem)
    {
        checkItemDao.add(checkItem);
    }

    public PageResult pageQuery(QueryPageBean queryPageBean)
    {
        //获取当前页码
        Integer currentPage=queryPageBean.getCurrentPage();
        //每页的数据条数
        Integer pageSize = queryPageBean.getPageSize();
        //查询条件
        String queryString = queryPageBean.getQueryString();
        //完成分页查询基于mybatis的分页助手插件
        PageHelper.startPage(currentPage,pageSize);//基于threadlocal实现的，将limit的数据绑定到sql语句里面
//        select * from t_checkitem limit 0,10
        Page<CheckItem> page= checkItemDao.selectByCondition(queryString);
        Long total = page.getTotal();
//        System.out.println(total);
//        System.out.println(page.get);
        List result = page.getResult();
        return new PageResult(total, result);
    }

    //根据id进行删除
    public void deleteById(Integer id) {
        //判断当前检查项是否已经关联到检查组
        Long count = checkItemDao.findCountByCheckItemId(id);
        if(count>0)
        {
            //当前删除项有关联
            new RuntimeException();
        }
        checkItemDao.deleteById(id);
    }

    //修改checkitem
    public void edit(CheckItem checkItem) {

        checkItemDao.edit(checkItem);
    }

    public List<CheckItem> findAll()
    {
        List<CheckItem> all = checkItemDao.findAll();
        return all;
    }

    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }
}
