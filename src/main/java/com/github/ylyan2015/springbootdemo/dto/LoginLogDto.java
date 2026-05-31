package com.github.ylyan2015.springbootdemo.dto;

import com.github.ylyan2015.springbootdemo.common.LogStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "登录日志")
public class LoginLogDto {

    @Schema(description = "日志ID", example = "1")
    private Long id;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "操作类型：login-登录，logout-登出", example = "login")
    private String operationType;

    @Schema(description = "状态", example = "SUCCESS")
    private LogStatusEnum status;

    @Schema(description = "IP地址", example = "192.168.1.100")
    private String ipAddress;

    @Schema(description = "省份", example = "北京市")
    private String province;

    @Schema(description = "城市", example = "北京市")
    private String city;

    @Schema(description = "区县", example = "朝阳区")
    private String district;

    @Schema(description = "详细地址", example = "中国|华北|北京市|北京市|朝阳区")
    private String address;

    @Schema(description = "浏览器", example = "Chrome 120.0")
    private String browser;

    @Schema(description = "操作系统", example = "Windows 11")
    private String os;

    @Schema(description = "错误信息（登录失败时）", example = "用户名或密码错误")
    private String errorMsg;

    @Schema(description = "操作时间", example = "2024-01-01T10:00:00")
    private LocalDateTime operationTime;

    public Integer getStatusValue() {
        return status != null ? status.getCode() : null;
    }
}
