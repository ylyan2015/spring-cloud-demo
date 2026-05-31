package com.github.ylyan2015.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 应用启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SpringBootDemoApplication {

    /**
     * 主方法入口
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }
}