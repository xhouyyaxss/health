package com.hxy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hxy.constant.RedisConstant;
import com.hxy.dao.CheckGroupDao;
import com.hxy.dao.CheckItemDao;
import com.hxy.dao.OrderDao;
import com.hxy.dao.SetMealDao;
import com.hxy.entity.PageResult;
import com.hxy.entity.QueryPageBean;
import com.hxy.pojo.Setmeal;
import com.hxy.service.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import redis.clients.jedis.JedisPool;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/8
 * @time: 15:33
 */
@Service(interfaceClass = SetMealService.class)
@Transactional
@org.springframework.stereotype.Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDaoDao;

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Autowired
    private CheckItemDao checkItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Value("${out_put_path}")
    String outPutPath;//从属性文件中读取属性

    //新增检查套餐
    public void add(Setmeal setmeal,Integer[] checkgroupIds) {

        setMealDaoDao.add(setmeal);
        Integer gid = setmeal.getId();
        //将存入数据库图片的名称存方到缓存
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

        if(checkgroupIds!=null)
        {
            for(Integer id:checkgroupIds)
            {
                Map map=new HashMap();
                map.put("setmealId",gid);
                map.put("checkgroupId",id);
                //将setmeal存入数据库
                setMealDaoDao.setMealAndGroup(map);
            }
        }
         //修改数据之后生成新的页面
        this.generateMobileStaticHtml();

    }

    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean) {

        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmeals = setMealDaoDao.selectByCondition(queryString);
        return new PageResult(setmeals.getTotal(),setmeals.getResult());
    }

    //查找所有的套餐,并且进行联合查询，将其中的检查组和检查项都查出来
    public List<Setmeal> findAll() {

        List<Setmeal> all = setMealDaoDao.findAll();
        List<Setmeal>list=new LinkedList<Setmeal>();
        if(all.size()>0)
        {
            for(Setmeal setmeal:all)
            {
                Setmeal byId = setMealDaoDao.findById(setmeal.getId());
                list.add(byId);
            }
        }

        return list;
    }

   //通过id查找setmeal并且还要查检查组和检查项
    public Setmeal findById(Integer id) {

//        Setmeal setmeal = setMealDaoDao.findById(id);
//        //查找出套餐中的检查组
//        List<CheckGroup> checkGroups = setMealDaoDao.findCheckGroup(id);
//        List<CheckGroup>trsansfer=null;
//        if(checkGroups.size()>0)
//        {
//            //将每个检查组中检查项也查询出来
//            for(CheckGroup checkGroup:checkGroups)
//            {
//                List<Integer> checkItemIds = checkGroupDao.findCheckItemIds(checkGroup.getId());
//
//                if(checkItemIds.size()<=0)
//                {
//                    checkGroup.setCheckItems(null);
//
//                }
//                else {
//                    List<CheckItem>checkItems=new ArrayList<CheckItem>();
//                    for(Integer itemId:checkItemIds)
//                    {
//                        CheckItem item = checkItemDao.findById(itemId);
//                        checkItems.add(item);
//                    }
//                    checkGroup.setCheckItems(checkItems);
//                }
//
//                trsansfer.add(checkGroup);
//            }
//        }
//        setmeal.setCheckGroups(trsansfer);
//
//        return setmeal;
//    }
        Setmeal setmeal = setMealDaoDao.findById(id);
        return setmeal;
    }

    //用于生成静态也买你
    //模板名字 生成出来的html名字 要传入的参数
    public void generateHtml(String templateName,String pageHtml,Map map){

        Configuration configuration = freeMarkerConfig.getConfiguration();
        Template template = null;
        try {
            template = configuration.getTemplate(templateName);
            Writer writer= writer = new FileWriter(outPutPath+"/"+pageHtml);;
            template.process(map,writer);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void generateMobileStaticHtml()
    {
        //生成静态页面之前先查询数据
        List<Setmeal> all = findAll();
        //生成套餐列表页面
        generateMobileSetmealListHtml(all);
        //生成套餐详情页面
        generateMobileSetmealDetailHtml(all);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> list)
    {
        //为模板提供数据
          Map map=new HashMap();
          map.put("setmealList",list);

          generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }
    public void generateMobileSetmealDetailHtml(List<Setmeal> all)
    {

        if(all.size()>0)
        {
            for(Setmeal  setmeal:all)
            {
                Map map=new HashMap();
                map.put("setmeal",setmeal);
               generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+Integer.toString(setmeal.getId())+".html",map);
            }
        }

    }

//    public Map getSetmealReport() {
//
//        //获得所有套餐名称以及预约该种套餐的会员数
//        List<Setmeal> setmeals = setMealDaoDao.findAll();
//        List<String>setmealNames=new ArrayList<String>();
//        List<Integer>setmealCount=new ArrayList<Integer>();
//        for(Setmeal setmeal:setmeals)
//        {
//            Integer count = orderDao.findCountBySetmealId(setmeal.getId());
//
//            if(count!=null&&count>0)
//            {
//                setmealNames.add(setmeal.getName());
//                setmealCount.add(count);
//            }
//
//        }
//        Map map =new HashMap();
//        map.put("setmealNames",setmealNames);
//        map.put("setmealCount",setmealCount);
//        return map;
//    }
    public Map getSetmealReport()
    {
        List<Map<String, Object>> setmeal = setMealDaoDao.findSetmealCount();
        List<String>setmealNames=new ArrayList<String>();
        List<Integer>setmealCount=new ArrayList<Integer>();
        for(Map i:setmeal)
        {
            String name = (String)i.get("name");
            Integer value= Integer.parseInt(i.get("value").toString());
            setmealNames.add(name);
            setmealCount.add(value);
        }
       Map map=new HashMap();
        map.put("setmealNames",setmealNames);
        map.put("setmealCount",setmealCount);
        return map;
    }


    //查询最火的两种套餐
    public List getHotSetmeal() {

        List<Map<String, Object>> setmeals = setMealDaoDao.findSetmealCountAndProportion();


        return setmeals;
    }
}
