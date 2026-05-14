package com.github.ylyan2015.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据字典子项DTO
 */
@Data
public class DictItemDto {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 字典ID
     */
    private Long dictId;

    /**
     * 字典项标签
     */
    private String itemLabel;

    /**
     * 字典项值
     */
    private String itemValue;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
