package com.github.ylyan2015.springbootdemo.entity;

import com.github.ylyan2015.springbootdemo.common.MenuTypeEnum;
import com.github.ylyan2015.springbootdemo.common.UserStatusEnum;
import com.github.ylyan2015.springbootdemo.common.YesNoEnum;
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
    private Long parentId = 0L;

    /**
     * 菜单类型（1-目录，2-菜单，3-按钮）
     */
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private MenuTypeEnum menuType = MenuTypeEnum.MENU;

    /**
     * 菜单图标
     */
    @Column(length = 100)
    private String icon;

    /**
     * 排序号
     */
    private Integer sortOrder = 0;

    /**
     * 权限标识
     */
    @Column(length = 100)
    private String permission;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserStatusEnum status = UserStatusEnum.ENABLED;

    /**
     * 是否外链（0-否，1-是）
     */
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private YesNoEnum isExternal = YesNoEnum.NO;

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
