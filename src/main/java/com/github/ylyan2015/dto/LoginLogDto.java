package com.github.ylyan2015.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录日志数据传输对象
 */
@Data
public class LoginLogDto {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 操作类型（login-登录，logout-登出）
     */
    private String operationType;

    /**
     * 登录状态（0-失败，1-成功）
     */
    private Integer status;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 浏览器信息
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;
}
