package com.github.ylyan2015.springbootdemo.service;

import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.dto.OperationLogDto;

import java.util.List;

/**
 * 操作日志服务接口
 */
public interface IOperationLogService {

    /**
     * 保存操作日志
     */
    void saveOperationLog(OperationLogDto operationLogDto);

    /**
     * 根据用户ID查询操作日志
     */
    Result<List<OperationLogDto>> getLogsByUserId(Long userId);

    /**
     * 根据模块查询操作日志
     */
    Result<List<OperationLogDto>> getLogsByModule(String module);

    /**
     * 查询所有操作日志
     */
    Result<List<OperationLogDto>> getAllLogs();
}
