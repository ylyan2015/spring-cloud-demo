package com.github.ylyan2015.service.impl;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dao.LoginLogRepository;
import com.github.ylyan2015.dto.LoginLogDto;
import com.github.ylyan2015.entity.LoginLogEO;
import com.github.ylyan2015.service.ILoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录日志服务实现类
 */
@Service
@Slf4j
public class LoginLogServiceImpl implements ILoginLogService {

    @Resource
    private LoginLogRepository loginLogRepository;

    @Override
    public void saveLoginLog(LoginLogDto loginLogDto) {
        try {
            LoginLogEO logEO = new LoginLogEO();
            BeanUtils.copyProperties(loginLogDto, logEO);
            loginLogRepository.save(logEO);
        } catch (Exception e) {
            log.error("保存登录日志失败", e);
        }
    }

    @Override
    public Result<List<LoginLogDto>> getLogsByUserId(Long userId) {
        try {
            List<LoginLogEO> logs = loginLogRepository.findByUserIdOrderByOperationTimeDesc(userId);
            List<LoginLogDto> result = logs.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询用户登录日志失败", e);
            return Result.fail("查询登录日志失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<LoginLogDto>> getLogsByUsername(String username) {
        try {
            List<LoginLogEO> logs = loginLogRepository.findByUsernameOrderByOperationTimeDesc(username);
            List<LoginLogDto> result = logs.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询用户名登录日志失败", e);
            return Result.fail("查询登录日志失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<LoginLogDto>> getAllLogs() {
        try {
            List<LoginLogEO> logs = loginLogRepository.findAll();
            List<LoginLogDto> result = logs.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询所有登录日志失败", e);
            return Result.fail("查询登录日志失败：" + e.getMessage());
        }
    }

    /**
     * Entity转Dto
     */
    private LoginLogDto convertToDto(LoginLogEO logEO) {
        LoginLogDto dto = new LoginLogDto();
        BeanUtils.copyProperties(logEO, dto);
        return dto;
    }
}
