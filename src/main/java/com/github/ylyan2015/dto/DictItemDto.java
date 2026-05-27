package com.github.ylyan2015.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典项数据传输对象
 */
@Data
@Schema(description = "字典项信息")
public class DictItemDto {
    
    /**
     * 字典项ID
     */
    @Schema(description = "字典项ID", example = "1")
    private Long id;
    
    /**
     * 字典ID
     */
    @Schema(description = "字典ID", example = "1")
    private Long dictId;
    
    /**
     * 字典项标签
     */
    @Schema(description = "字典项标签（显示值）", example = "启用", required = true)
    private String itemLabel;
    
    /**
     * 字典项值
     */
    @Schema(description = "字典项值（存储值）", example = "1", required = true)
    private String itemValue;
    
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
     * 样式属性
     */
    @Schema(description = "样式属性（如颜色）", example = "success")
    private String cssClass;
    
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
