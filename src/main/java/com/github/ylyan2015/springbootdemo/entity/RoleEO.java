package com.github.ylyan2015.springbootdemo.entity;

import com.github.ylyan2015.springbootdemo.common.UserStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色实体类
 */
@Data
@Entity
@Table(name = "t_role")
public class RoleEO {

    /**
     * 角色ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色编码
     */
    @Column(unique = true, nullable = false, length = 50)
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(nullable = false, length = 50)
    private String roleName;

    /**
     * 角色描述
     */
    @Column(length = 200)
    private String description;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserStatusEnum status = UserStatusEnum.ENABLED;

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
