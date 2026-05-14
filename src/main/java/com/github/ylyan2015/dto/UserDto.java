package com.github.ylyan2015.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据传输对象
 */
@Data
public class UserDto {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 性别
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private LocalDate birthday;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 身份证号
     */
    private String idCard;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 角色ID列表
     */
    private List<Long> roleIds;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
