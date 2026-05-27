package com.github.ylyan2015.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典数据传输对象
 */
@Data
@Schema(description = "字典信息")
public class DictDto {
    
    /**
     * 字典ID
     */
    @Schema(description = "字典ID", example = "1")
    private Long id;
    
    /**
     * 字典名称
     */
    @Schema(description = "字典名称", example = "用户状态", required = true)
    private String dictName;
    
    /**
     * 字典编码
     */
    @Schema(description = "字典编码", example = "user_status", required = true)
    private String dictCode;
    
    /**
     * 字典描述
     */
    @Schema(description = "字典描述", example = "用户状态字典")
    private String description;
    
    /**
     * 状态
     */
    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;
    
    /**
     * 字典项列表
     */
    @Schema(description = "字典项列表")
    private List<DictItemDto> items;
    
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
