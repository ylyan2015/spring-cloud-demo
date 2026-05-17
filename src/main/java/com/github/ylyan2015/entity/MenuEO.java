package com.github.ylyan2015.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 菜单实体类
 */
@Data
@Entity
@Table(name = "t_menu")
public class MenuEO {

    /**
     * 菜单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 菜单名称
     */
    @Column(nullable = false, length = 50)
    private String menuName;

    /**
     * 菜单路径
     */
    @Column(length = 200)
    private String path;

    /**
     * 父菜单ID（0表示根菜单）
     */
    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    /**
     * 菜单类型（1-目录，2-菜单，3-按钮）
     */
    private Integer menuType;

    /**
     * 菜单图标
     */
    @Column(length = 100)
    private String icon;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 权限标识
     */
    @Column(length = 100)
    private String permission;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 是否外链（0-否，1-是）
     */
    private Integer isExternal;

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
