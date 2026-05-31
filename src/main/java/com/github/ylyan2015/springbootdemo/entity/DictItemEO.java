package com.github.ylyan2015.springbootdemo.entity;

import com.github.ylyan2015.springbootdemo.common.UserStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 数据字典子表实体类
 */
@Data
@Entity
@Table(name = "t_dict_item")
public class DictItemEO {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 字典ID（关联主表）
     */
    @Column(name = "dict_id", nullable = false)
    private Long dictId;

    /**
     * 字典项标签（显示名称）
     */
    @Column(nullable = false, length = 100)
    private String itemLabel;

    /**
     * 字典项值（实际值）
     */
    @Column(nullable = false, length = 100)
    private String itemValue;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Enumerated(EnumType.ORDINAL)
    private UserStatusEnum status = UserStatusEnum.ENABLED;

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
