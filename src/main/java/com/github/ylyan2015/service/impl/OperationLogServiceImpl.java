package com.github.ylyan2015.service.impl;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dao.OperationLogRepository;
import com.github.ylyan2015.dto.OperationLogDto;
import com.github.ylyan2015.entity.OperationLogEO;
import com.github.ylyan2015.service.IOperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志服务实现类
 */
@Service
@Slf4j
public class OperationLogServiceImpl implements IOperationLogService {

    @Resource
    private OperationLogRepository operationLogRepository;

    @Override
    public void saveOperationLog(OperationLogDto operationLogDto) {
        try {
            OperationLogEO logEO = new OperationLogEO();
            BeanUtils.copyProperties(operationLogDto, logEO);
            operationLogRepository.save(logEO);
        } catch (Exception e) {
            log.error("保存操作日志失败", e);
        }
    }

    @Override
    public Result<List<OperationLogDto>> getLogsByUserId(Long userId) {
        try {
            List<OperationLogEO> logs = operationLogRepository.findByUserIdOrderByOperationTimeDesc(userId);
            List<OperationLogDto> result = logs.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询用户操作日志失败", e);
            return Result.fail("查询操作日志失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<OperationLogDto>> getLogsByModule(String module) {
        try {
            List<OperationLogEO> logs = operationLogRepository.findByModuleOrderByOperationTimeDesc(module);
            List<OperationLogDto> result = logs.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询模块操作日志失败", e);
            return Result.fail("查询操作日志失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<OperationLogDto>> getAllLogs() {
        try {
            List<OperationLogEO> logs = operationLogRepository.findAll();
            List<OperationLogDto> result = logs.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询所有操作日志失败", e);
            return Result.fail("查询操作日志失败：" + e.getMessage());
        }
    }

    /**
     * Entity转Dto
     */
    private OperationLogDto convertToDto(OperationLogEO logEO) {
        OperationLogDto dto = new OperationLogDto();
        BeanUtils.copyProperties(logEO, dto);
        return dto;
    }
}
