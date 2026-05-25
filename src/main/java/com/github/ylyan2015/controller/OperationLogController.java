package com.github.ylyan2015.controller;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.OperationLogDto;
import com.github.ylyan2015.service.IOperationLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志控制器
 */
@RestController
@RequestMapping("/operation-log")
public class OperationLogController {

    @Resource
    private IOperationLogService operationLogService;

    /**
     * 查询用户的操作日志
     */
    @GetMapping("/user/{userId}")
    public Result<List<OperationLogDto>> getLogsByUser(@PathVariable Long userId) {
        return operationLogService.getLogsByUserId(userId);
    }

    /**
     * 查询模块的操作日志
     */
    @GetMapping("/module/{module}")
    public Result<List<OperationLogDto>> getLogsByModule(@PathVariable String module) {
        return operationLogService.getLogsByModule(module);
    }

    /**
     * 查询所有操作日志
     */
    @GetMapping("/list")
    public Result<List<OperationLogDto>> getAllLogs() {
        return operationLogService.getAllLogs();
    }
}
