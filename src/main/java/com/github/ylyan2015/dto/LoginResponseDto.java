package com.github.ylyan2015.dto;

import lombok.Data;
import java.util.List;

/**
 * 登录响应数据传输对象
 */
@Data
public class LoginResponseDto {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 角色ID列表
     */
    private List<Long> roleIds;
    
    /**
     * 登录令牌
     */
    private String token;
}
