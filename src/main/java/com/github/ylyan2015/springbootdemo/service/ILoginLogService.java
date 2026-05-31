package com.github.ylyan2015.springbootdemo.service;

import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.dto.LoginLogDto;

import java.util.List;

/**
 * 登录日志服务接口
 */
public interface ILoginLogService {

    /**
     * 保存登录日志
     */
    void saveLoginLog(LoginLogDto loginLogDto);

    /**
     * 根据用户ID查询登录日志
     */
    Result<List<LoginLogDto>> getLogsByUserId(Long userId);

    /**
     * 根据用户名查询登录日志
     */
    Result<List<LoginLogDto>> getLogsByUsername(String username);

    /**
     * 查询所有登录日志
     */
    Result<List<LoginLogDto>> getAllLogs();
}
