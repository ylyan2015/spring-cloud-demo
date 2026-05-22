package com.github.ylyan2015.dto;

import lombok.Data;

/**
 * 登录请求数据传输对象
 */
@Data
public class LoginRequestDto {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
}
