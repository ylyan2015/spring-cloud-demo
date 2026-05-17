package com.github.ylyan2015.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单数据传输对象
 */
@Data
public class MenuDto {

    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单类型（1-目录，2-菜单，3-按钮）
     */
    private Integer menuType;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 权限标识
     */
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
     * 子菜单列表
     */
    private List<MenuDto> children;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
