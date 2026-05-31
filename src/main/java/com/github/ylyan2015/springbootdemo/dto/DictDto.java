package com.github.ylyan2015.springbootdemo.dto;

import com.github.ylyan2015.springbootdemo.common.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "字典信息")
public class DictDto {

    @Schema(description = "字典ID", example = "1")
    private Long id;

    @Schema(description = "字典名称", example = "用户状态", required = true)
    private String dictName;

    @Schema(description = "字典编码", example = "user_status", required = true)
    private String dictCode;

    @Schema(description = "字典描述", example = "用户状态字典")
    private String description;

    @Schema(description = "状态", example = "ENABLED")
    private UserStatusEnum status;

    @Schema(description = "字典项列表")
    private List<DictItemDto> items;

    @Schema(description = "创建时间", example = "2024-01-01T10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2024-01-01T10:00:00")
    private LocalDateTime updateTime;
}
