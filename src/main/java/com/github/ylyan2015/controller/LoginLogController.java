package com.github.ylyan2015.controller;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.LoginLogDto;
import com.github.ylyan2015.service.ILoginLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 登录日志控制器
 */
@RestController
@RequestMapping("/login-log")
public class LoginLogController {

    @Resource
    private ILoginLogService loginLogService;

    /**
     * 根据用户ID查询登录日志
     */
    @GetMapping("/user/{userId}")
    public Result<List<LoginLogDto>> getLogsByUserId(@PathVariable Long userId) {
        return loginLogService.getLogsByUserId(userId);
    }

    /**
     * 根据用户名查询登录日志
     */
    @GetMapping("/username/{username}")
    public Result<List<LoginLogDto>> getLogsByUsername(@PathVariable String username) {
        return loginLogService.getLogsByUsername(username);
    }

    /**
     * 查询所有登录日志
     */
    @GetMapping("/list")
    public Result<List<LoginLogDto>> getAllLogs() {
        return loginLogService.getAllLogs();
    }
}
