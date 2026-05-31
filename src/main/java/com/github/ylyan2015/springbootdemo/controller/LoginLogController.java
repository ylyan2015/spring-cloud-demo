package com.github.ylyan2015.springbootdemo.controller;

import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.dto.LoginLogDto;
import com.github.ylyan2015.springbootdemo.service.ILoginLogService;
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
 * 登录日志控制器
 */
@Tag(name = "登录日志", description = "用户登录登出日志查询接口")
@RestController
@RequestMapping("/login-log")
public class LoginLogController {

    @Resource
    private ILoginLogService loginLogService;

    /**
     * 根据用户ID查询登录日志
     */
    @Operation(summary = "查询用户登录日志", description = "根据用户ID查询登录登出记录")
    @Parameter(name = "userId", description = "用户ID", required = true, example = "1")
    @GetMapping("/user/{userId}")
    public Result<List<LoginLogDto>> getLogsByUserId(@PathVariable Long userId) {
        return loginLogService.getLogsByUserId(userId);
    }

    /**
     * 根据用户名查询登录日志
     */
    @Operation(summary = "根据用户名查询登录日志", description = "根据用户名查询登录登出记录")
    @Parameter(name = "username", description = "用户名", required = true, example = "admin")
    @GetMapping("/username/{username}")
    public Result<List<LoginLogDto>> getLogsByUsername(@PathVariable String username) {
        return loginLogService.getLogsByUsername(username);
    }

    /**
     * 查询所有登录日志
     */
    @Operation(summary = "查询所有登录日志", description = "查询所有用户的登录登出记录")
    @GetMapping("/list")
    public Result<List<LoginLogDto>> getAllLogs() {
        return loginLogService.getAllLogs();
    }
}
