package com.github.ylyan2015.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单数据传输对象
 */
@Data
@Schema(description = "菜单信息")
public class MenuDto {
    
    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID", example = "1")
    private Long id;
    
    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID，0表示根菜单", example = "0")
    private Long parentId;
    
    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称", example = "用户管理", required = true)
    private String menuName;
    
    /**
     * 菜单编码
     */
    @Schema(description = "菜单编码", example = "user_management", required = true)
    private String menuCode;
    
    /**
     * 菜单类型
     */
    @Schema(description = "菜单类型：1-目录，2-菜单，3-按钮", example = "2")
    private Integer menuType;
    
    /**
     * 路由地址
     */
    @Schema(description = "路由地址", example = "/system/user")
    private String path;
    
    /**
     * 组件路径
     */
    @Schema(description = "组件路径", example = "system/user/index")
    private String component;
    
    /**
     * 权限标识
     */
    @Schema(description = "权限标识", example = "system:user:list")
    private String permission;
    
    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标", example = "user")
    private String icon;
    
    /**
     * 排序
     */
    @Schema(description = "排序号", example = "1")
    private Integer sort;
    
    /**
     * 状态
     */
    @Schema(description = "状态：0-隐藏，1-显示", example = "1")
    private Integer visible;
    
    /**
     * 子菜单列表
     */
    @Schema(description = "子菜单列表")
    private List<MenuDto> children;
    
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
