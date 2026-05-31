package com.github.ylyan2015.springbootdemo.dto;

import com.github.ylyan2015.springbootdemo.common.LogStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "操作日志")
public class OperationLogDto {

    @Schema(description = "日志ID", example = "1")
    private Long id;

    @Schema(description = "操作用户ID", example = "1")
    private Long userId;

    @Schema(description = "操作用户名", example = "admin")
    private String username;

    @Schema(description = "操作模块", example = "用户管理")
    private String module;

    @Schema(description = "操作类型：新增、修改、删除、查询等", example = "新增")
    private String operationType;

    @Schema(description = "操作描述", example = "新增用户：张三")
    private String description;

    @Schema(description = "请求方法", example = "POST")
    private String requestMethod;

    @Schema(description = "请求URL", example = "/user/add")
    private String requestUrl;

    @Schema(description = "请求参数（JSON格式）")
    private String requestParams;

    @Schema(description = "响应结果", example = "成功")
    private String responseResult;

    @Schema(description = "状态", example = "SUCCESS")
    private LogStatusEnum status;

    @Schema(description = "错误信息（操作失败时）")
    private String errorMsg;

    @Schema(description = "执行时间（毫秒）", example = "150")
    private Long executeTime;

    @Schema(description = "IP地址", example = "192.168.1.100")
    private String ipAddress;

    @Schema(description = "操作时间", example = "2024-01-01T10:00:00")
    private LocalDateTime operationTime;

    public Integer getStatusValue() {
        return status != null ? status.getCode() : null;
    }
}
