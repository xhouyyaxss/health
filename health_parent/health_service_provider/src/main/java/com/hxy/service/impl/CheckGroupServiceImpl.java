package com.hxy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hxy.dao.CheckGroupDao;
import com.hxy.dao.CheckItemDao;
import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.pojo.CheckGroup;
import com.hxy.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/7
 * @time: 0:33
 */
@Service(interfaceClass = CheckGroupService.class)
@org.springframework.stereotype.Service
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {


    @Autowired
    CheckGroupDao checkGroupDao;

    @Autowired
    CheckItemDao checkItemDao;

    //新增检查组，也要关联检查项
    public void add(CheckGroup checkGroup,Integer[] checkitemIds) {
        //新增检查组
        checkGroupDao.add(checkGroup);
        //获得自增的id的值
        Integer id = checkGroup.getId();

        //设置检查组和检查项多对多关系，操作t_checkgroup_checkitem表
        if(checkitemIds!=null)
        {
            for(Integer ids:checkitemIds)
            {
               Map map=new HashMap();
               map.put("checkgroupId",id);
               map.put("checkitemId",ids);
               checkGroupDao.setGroupAndItem(map);
            }
        }
    }

    //分页查询
    public PageResult queryPage(QueryPageBean queryPageBean) {

        Integer currentPage = queryPageBean.getCurrentPage();

        Integer pageSize = queryPageBean.getPageSize();

        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);

        List<CheckGroup> result = page.getResult();
        long total = page.getTotal();

        return new PageResult(total,result);
    }

    //删除检查组
    public void deleteById(Integer id) {

        //注意先删除关联的表中的数据，再删除检查组中的数据
        //将该检查组内的检查项也删除了
        checkGroupDao.deleteGroupAndItem(id);
        //删除检查组
        checkGroupDao.deleteById(id);

    }

    //查询对应检查组的检查项
    public List<Integer> findCheckItemIds(Integer checkgroupId) {

        List<Integer> checkItemIds = checkGroupDao.findCheckItemIds(checkgroupId);
        return checkItemIds;
    }

    //编辑信息
    public void edit(CheckGroup checkgroup, Integer[] checkitemIds) {

        //修改检查组的信息
        checkGroupDao.edit(checkgroup);
        Integer gid=checkgroup.getId();
        //删除之前检查组所对应的检查项
       checkGroupDao.deleteGroupAndItem(gid);

       //为空的话不操作
       if(checkitemIds==null)return ;
       //新增新的检查项
        for(Integer id:checkitemIds)
        {
            Map map=new HashMap();
            map.put("checkgroupId",gid);
            map.put("checkitemId",id);
            checkGroupDao.setGroupAndItem(map);
        }

    }

    //查询所有检查组
    public List<CheckGroup> findAll() {

       return  checkGroupDao.findAll();

    }
}
