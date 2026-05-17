package com.github.ylyan2015.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色菜单关联实体类
 */
@Data
@Entity
@Table(name = "t_role_menu")
public class RoleMenuEO {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色ID
     */
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    /**
     * 菜单ID
     */
    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
