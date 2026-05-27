package com.github.ylyan2015.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志数据传输对象
 */
@Data
@Schema(description = "操作日志")
public class OperationLogDto {
    
    /**
     * 日志ID
     */
    @Schema(description = "日志ID", example = "1")
    private Long id;
    
    /**
     * 用户ID
     */
    @Schema(description = "操作用户ID", example = "1")
    private Long userId;
    
    /**
     * 用户名
     */
    @Schema(description = "操作用户名", example = "admin")
    private String username;
    
    /**
     * 操作模块
     */
    @Schema(description = "操作模块", example = "用户管理")
    private String module;
    
    /**
     * 操作类型
     */
    @Schema(description = "操作类型：新增、修改、删除、查询等", example = "新增")
    private String operationType;
    
    /**
     * 操作描述
     */
    @Schema(description = "操作描述", example = "新增用户：张三")
    private String description;
    
    /**
     * 请求方法
     */
    @Schema(description = "请求方法", example = "POST")
    private String requestMethod;
    
    /**
     * 请求URL
     */
    @Schema(description = "请求URL", example = "/user/add")
    private String requestUrl;
    
    /**
     * 请求参数
     */
    @Schema(description = "请求参数（JSON格式）")
    private String requestParams;
    
    /**
     * 响应结果
     */
    @Schema(description = "响应结果", example = "成功")
    private String responseResult;
    
    /**
     * 状态
     */
    @Schema(description = "状态：0-失败，1-成功", example = "1")
    private Integer status;
    
    /**
     * 错误信息
     */
    @Schema(description = "错误信息（操作失败时）")
    private String errorMsg;
    
    /**
     * 执行时间（毫秒）
     */
    @Schema(description = "执行时间（毫秒）", example = "150")
    private Long executeTime;
    
    /**
     * IP地址
     */
    @Schema(description = "IP地址", example = "192.168.1.100")
    private String ipAddress;
    
    /**
     * 操作时间
     */
    @Schema(description = "操作时间", example = "2024-01-01T10:00:00")
    private LocalDateTime operationTime;
}
