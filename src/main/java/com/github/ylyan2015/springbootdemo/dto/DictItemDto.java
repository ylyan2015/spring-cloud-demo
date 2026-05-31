package com.github.ylyan2015.springbootdemo.dto;

import com.github.ylyan2015.springbootdemo.common.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "字典项信息")
public class DictItemDto {

    @Schema(description = "字典项ID", example = "1")
    private Long id;

    @Schema(description = "字典ID", example = "1")
    private Long dictId;

    @Schema(description = "字典项标签（显示值）", example = "启用", required = true)
    private String itemLabel;

    @Schema(description = "字典项值（存储值）", example = "1", required = true)
    private String itemValue;

    @Schema(description = "排序号", example = "1")
    private Integer sort;

    @Schema(description = "状态", example = "ENABLED")
    private UserStatusEnum status;

    @Schema(description = "样式属性（如颜色）", example = "success")
    private String cssClass;

    @Schema(description = "创建时间", example = "2024-01-01T10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2024-01-01T10:00:00")
    private LocalDateTime updateTime;
}
