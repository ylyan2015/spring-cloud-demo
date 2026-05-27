package com.github.ylyan2015.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据传输对象
 */
@Data
@Schema(description = "用户信息")
public class UserDto {
    
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long id;
    
    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "admin", required = true)
    private String username;
    
    /**
     * 密码
     */
    @Schema(description = "密码（新增/修改时可选，不返回）", example = "123456")
    private String password;
    
    /**
     * 昵称
     */
    @Schema(description = "昵称", example = "管理员")
    private String nickname;
    
    /**
     * 手机号
     */
    @Schema(description = "手机号", example = "13800138000")
    private String phone;
    
    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;
    
    /**
     * 性别
     */
    @Schema(description = "性别：0-女，1-男", example = "1")
    private Integer gender;
    
    /**
     * 生日
     */
    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;
    
    /**
     * 年龄
     */
    @Schema(description = "年龄", example = "30")
    private Integer age;
    
    /**
     * 头像URL
     */
    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;
    
    /**
     * 身份证号
     */
    @Schema(description = "身份证号", example = "110101199001011234")
    private String idCard;
    
    /**
     * 状态
     */
    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;
    
    /**
     * 地址
     */
    @Schema(description = "地址", example = "北京市朝阳区")
    private String address;
    
    /**
     * 角色ID列表
     */
    @Schema(description = "角色ID列表")
    private List<Long> roleIds;
    
    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-01-01T10:00:00")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2024-01-01T10:00:00")
    private LocalDateTime updateTime;
}
