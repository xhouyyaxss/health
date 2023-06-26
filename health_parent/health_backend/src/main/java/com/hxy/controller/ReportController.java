package com.hxy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hxy.constant.MessageConstant;
import com.hxy.entity.Result;
import com.hxy.service.MemberService;
import com.hxy.service.OrderService;
import com.hxy.service.SetMealService;
import com.hxy.utils.DateUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/13
 * @time: 1:12
 */
@RestController
@RequestMapping("/report")
//报表处理器
public class ReportController {


    @Reference
    SetMealService setMealService;
    @Reference
    MemberService memberService;

    @Reference
    OrderService orderService;

    //会员数量折线图
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() throws ParseException {

      try{
          Map map = memberService.findCountOnPastYear();
          return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
      }catch(Exception e)
      {
          e.printStackTrace();
          return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
      }

    }

    //查询套餐报报表
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport()
    {
        try{
            Map map = setMealService.getSetmealReport();

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        }catch(Exception e)
        {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData()
    {
//        reportDate:null,
//                todayNewMember :0,
//            totalMember :0,
//            thisWeekNewMember :0,
//            thisMonthNewMember :0,
//            todayOrderNumber :0,
//            todayVisitsNumber :0,
//            thisWeekOrderNumber :0,
//            thisWeekVisitsNumber :0,
//            thisMonthOrderNumber :0,
//            thisMonthVisitsNumber :0,
//            hotSetmeal :[
//        {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
//        {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
        //member：今日新增 这周新增 本月新增
        Map<String, Integer> memberMap = memberService.getAddMemberCount();
        Map<String, Integer> orderMap = orderService.getAddOrderCount();
        List hotSetmeal = setMealService.getHotSetmeal();
        Map map=new HashMap();
        Set<String> memberKeys = memberMap.keySet();
        for(String key:memberKeys)
        {
            map.put(key,memberMap.get(key));
        }
        Set<String> orderKeys = orderMap.keySet();
        for(String key:orderKeys)
        {
            map.put(key,orderMap.get(key));
        }
        map.put("hotSetmeal",hotSetmeal);
        map.put("reportDate",new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.getToday()));
        return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){

        try{

            Map<String, Integer> memberMap = memberService.getAddMemberCount();
            Map<String, Integer> orderMap = orderService.getAddOrderCount();
            List hotSetmeal = setMealService.getHotSetmeal();
            Map map=new HashMap();
            Set<String> memberKeys = memberMap.keySet();
            for(String key:memberKeys)
            {
                map.put(key,memberMap.get(key));
            }
            Set<String> orderKeys = orderMap.keySet();
            for(String key:orderKeys)
            {
                map.put(key,orderMap.get(key));
            }
            map.put("hotSetmeal",hotSetmeal);


            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            map.put("reportDate", reportDate);
            Integer todayNewMember = (Integer) map.get("todayNewMember");
            Integer totalMember = (Integer) map.get("totalMember");
            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
            List<Map> hotsetmeal = (List<Map>) map.get("hotSetmeal");


            //拼出模板文件的动态的绝对路径
            String filePath = request.getSession().getServletContext().getRealPath("/template") + File.separator + "report_template.xlsx";

            XSSFWorkbook excel=new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //读取第一个工作表
            XSSFSheet sheet = excel.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map1 : hotsetmeal){//热门套餐
                String name = (String) map1.get("name");
                Long setmeal_count = (Long) map1.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map1.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //使用输出流进行表格下载,基于浏览器作为客户端下载
            OutputStream out=response.getOutputStream();
            //设置响应信息
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");//以附件形式下载

            excel.write(out);
            out.flush();
            out.close();
            return null;

        }catch(Exception e)
        {
            e.printStackTrace();
            return new Result(false,"导出失败");
        }



    }

    @RequestMapping("/exportBusinessReportOfPDF")
    public Result exportBusinessReportOfPDF(HttpServletRequest request,HttpServletResponse response)
    {
          try{
              Map<String, Integer> memberMap = memberService.getAddMemberCount();
              Map<String, Integer> orderMap = orderService.getAddOrderCount();
              List hotSetmeal = setMealService.getHotSetmeal();
              Map map=new HashMap();
              Set<String> memberKeys = memberMap.keySet();
              for(String key:memberKeys)
              {
                  map.put(key,memberMap.get(key));
              }
              Set<String> orderKeys = orderMap.keySet();
              for(String key:orderKeys)
              {
                  map.put(key,orderMap.get(key));
              }
              map.put("hotSetmeal",hotSetmeal);


              //取出返回结果数据，准备将报表数据写入到Excel文件中
              String reportDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
              map.put("reportDate", reportDate);
              Integer todayNewMember = (Integer) map.get("todayNewMember");
              Integer totalMember = (Integer) map.get("totalMember");
              Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
              Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
              Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
              Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
              Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
              Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
              Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
              List<Map> hotsetmeal =(List<Map>) map.get("hotSetmeal");
              Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
              //拼出模板文件的动态的绝对路径
              String modulePath=request.getSession().getServletContext().getRealPath("/template") + File.separator + "health_business3.jrxml";
              String jasperPath=request.getSession().getServletContext().getRealPath("/template") + File.separator + "health_business3.jasper";
              //编译模板
              JasperCompileManager.compileReportToFile(modulePath,jasperPath);
              //填充数据，使用javabean数据源的方式填充
              JasperPrint jasperPrint= JasperFillManager.fillReport(jasperPath,map,new JRBeanCollectionDataSource(hotsetmeal));
              //使用输出流进行表格下载,基于浏览器作为客户端下载
              ServletOutputStream out=response.getOutputStream();
              //设置响应信息
              response.setContentType("application/vnd.ms-pdf");
              response.setHeader("content-Disposition","attachment;filename=report.pdf");//以附件形式下载

              //输出文件
              JasperExportManager.exportReportToPdfStream(jasperPrint,out);
              out.flush();
              out.close();
              return null;

          }catch(Exception e)
          {
              e.printStackTrace();
              return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
          }


    }

}
