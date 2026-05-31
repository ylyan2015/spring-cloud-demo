package com.github.ylyan2015.springbootdemo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面控制器
 */
@Controller
public class PageController {

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 默认首页重定向到登录页
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
}