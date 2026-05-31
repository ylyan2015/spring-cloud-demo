package com.github.ylyan2015.springbootdemo.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 登录日志实体类
 */
@Data
@Entity
@Table(name = "t_login_log")
public class LoginLogEO {

    /**
     * 日志ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    @Column(length = 50, nullable = false)
    private String username;

    /**
     * 操作类型（login-登录，logout-登出）
     */
    @Column(length = 20, nullable = false)
    private String operationType;

    /**
     * 登录状态（0-失败，1-成功）
     */
    private Integer status;

    /**
     * IP地址
     */
    @Column(length = 50)
    private String ipAddress;

    /**
     * 省份
     */
    @Column(length = 50)
    private String province;

    /**
     * 城市
     */
    @Column(length = 50)
    private String city;

    /**
     * 区县
     */
    @Column(length = 50)
    private String district;

    /**
     * 详细地址
     */
    @Column(length = 200)
    private String address;

    /**
     * 浏览器信息
     */
    @Column(length = 200)
    private String browser;

    /**
     * 操作系统
     */
    @Column(length = 100)
    private String os;

    /**
     * 错误信息
     */
    @Column(columnDefinition = "TEXT")
    private String errorMsg;

    /**
     * 操作时间
     */
    @Column(updatable = false)
    private LocalDateTime operationTime;

    @PrePersist
    protected void onCreate() {
        operationTime = LocalDateTime.now();
    }
}
