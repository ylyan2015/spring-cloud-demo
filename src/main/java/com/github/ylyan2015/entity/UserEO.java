package com.github.ylyan2015.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Entity
@Table(name = "t_user")
public class UserEO {

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /**
     * 密码
     */
    @Column(nullable = false, length = 100)
    private String password;

    /**
     * 昵称
     */
    @Column(length = 50)
    private String nickname;

    /**
     * 手机号
     */
    @Column(length = 20)
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 性别（0-女，1-男）
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
    @Column(length = 255)
    private String avatar;

    /**
     * 身份证号
     */
    @Column(length = 20)
    private String idCard;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 地址
     */
    @Column(length = 500)
    private String address;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
