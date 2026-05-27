package com.github.ylyan2015.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录请求数据传输对象
 */
@Data
@Schema(description = "登录请求")
public class LoginRequestDto {
    
    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "admin", required = true)
    private String username;
    
    /**
     * 密码
     */
    @Schema(description = "密码", example = "123456", required = true)
    private String password;
}
