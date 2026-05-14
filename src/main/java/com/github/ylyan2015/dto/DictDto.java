package com.github.ylyan2015.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据字典DTO
 */
@Data
public class DictDto {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典名称
     */
    private String dictName;

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

    /**
     * 字典项列表
     */
    private List<DictItemDto> items;
}
