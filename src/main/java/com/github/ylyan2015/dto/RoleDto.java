package com.github.ylyan2015.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色数据传输对象
 */
@Data
@Schema(description = "角色信息")
public class RoleDto {
    
    /**
     * 角色ID
     */
    @Schema(description = "角色ID", example = "1")
    private Long id;
    
    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "管理员", required = true)
    private String roleName;
    
    /**
     * 角色编码
     */
    @Schema(description = "角色编码", example = "ADMIN", required = true)
    private String roleCode;
    
    /**
     * 角色描述
     */
    @Schema(description = "角色描述", example = "系统管理员角色")
    private String description;
    
    /**
     * 排序
     */
    @Schema(description = "排序号", example = "1")
    private Integer sort;
    
    /**
     * 状态
     */
    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;
    
    /**
     * 菜单ID列表
     */
    @Schema(description = "菜单ID列表")
    private List<Long> menuIds;
    
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