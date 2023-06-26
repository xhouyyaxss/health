package com.hxy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @email:1148666077@qq.com
 * @author:侯向阳
 * @date: 2023/6/6
 * @time: 15:12
 */
@Controller
public class indexController {

    @RequestMapping("/index")
    public String index()
    {
        return "login";
    }

    @RequestMapping("/loginAction")
    public String login()
    {
        return "main";
    }
}
