package com.github.ylyan2015.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 数据字典主表实体类
 */
@Data
@Entity
@Table(name = "t_dict")
public class DictEO {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 字典编码
     */
    @Column(unique = true, nullable = false, length = 50)
    private String dictCode;

    /**
     * 字典名称
     */
    @Column(nullable = false, length = 100)
    private String dictName;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 描述
     */
    @Column(length = 200)
    private String description;

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
