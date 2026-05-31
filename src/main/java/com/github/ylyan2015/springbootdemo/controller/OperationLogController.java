package com.github.ylyan2015.springbootdemo.controller;

import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.dto.OperationLogDto;
import com.github.ylyan2015.springbootdemo.service.IOperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志控制器
 */
@Tag(name = "操作日志", description = "系统操作日志查询接口")
@RestController
@RequestMapping("/operation-log")
public class OperationLogController {

    @Resource
    private IOperationLogService operationLogService;

    /**
     * 查询用户的操作日志
     */
    @Operation(summary = "查询用户操作日志", description = "根据用户ID查询操作记录")
    @Parameter(name = "userId", description = "用户ID", required = true, example = "1")
    @GetMapping("/user/{userId}")
    public Result<List<OperationLogDto>> getLogsByUser(@PathVariable Long userId) {
        return operationLogService.getLogsByUserId(userId);
    }

    /**
     * 查询模块的操作日志
     */
    @Operation(summary = "查询模块操作日志", description = "根据模块名称查询操作记录")
    @Parameter(name = "module", description = "模块名称", required = true, example = "用户管理")
    @GetMapping("/module/{module}")
    public Result<List<OperationLogDto>> getLogsByModule(@PathVariable String module) {
        return operationLogService.getLogsByModule(module);
    }

    /**
     * 查询所有操作日志
     */
    @Operation(summary = "查询所有操作日志", description = "查询所有系统操作记录")
    @GetMapping("/list")
    public Result<List<OperationLogDto>> getAllLogs() {
        return operationLogService.getAllLogs();
    }
}
