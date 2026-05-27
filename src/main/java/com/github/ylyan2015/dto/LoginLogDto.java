package com.github.ylyan2015.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录日志数据传输对象
 */
@Data
@Schema(description = "登录日志")
public class LoginLogDto {
    
    /**
     * 日志ID
     */
    @Schema(description = "日志ID", example = "1")
    private Long id;
    
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;
    
    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "admin")
    private String username;
    
    /**
     * 操作类型
     */
    @Schema(description = "操作类型：login-登录，logout-登出", example = "login")
    private String operationType;
    
    /**
     * 状态
     */
    @Schema(description = "状态：0-失败，1-成功", example = "1")
    private Integer status;
    
    /**
     * IP地址
     */
    @Schema(description = "IP地址", example = "192.168.1.100")
    private String ipAddress;
    
    /**
     * 省份
     */
    @Schema(description = "省份", example = "北京市")
    private String province;
    
    /**
     * 城市
     */
    @Schema(description = "城市", example = "北京市")
    private String city;
    
    /**
     * 区县
     */
    @Schema(description = "区县", example = "朝阳区")
    private String district;
    
    /**
     * 详细地址
     */
    @Schema(description = "详细地址", example = "中国|华北|北京市|北京市|朝阳区")
    private String address;
    
    /**
     * 浏览器
     */
    @Schema(description = "浏览器", example = "Chrome 120.0")
    private String browser;
    
    /**
     * 操作系统
     */
    @Schema(description = "操作系统", example = "Windows 11")
    private String os;
    
    /**
     * 错误信息
     */
    @Schema(description = "错误信息（登录失败时）", example = "用户名或密码错误")
    private String errorMsg;
    
    /**
     * 操作时间
     */
    @Schema(description = "操作时间", example = "2024-01-01T10:00:00")
    private LocalDateTime operationTime;
}
