package com.github.ylyan2015.controller;


import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.LoginRequestDto;
import com.github.ylyan2015.dto.LoginResponseDto;
import com.github.ylyan2015.dto.UserDto;
import com.github.ylyan2015.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户控制器
 */
@Tag(name = "用户管理", description = "用户登录、注册、信息管理等相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户", description = "创建新用户，用户名不能重复")
    @PostMapping("/add")
    public Result<UserDto> addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改用户", description = "更新用户信息，如果提供密码则更新密码")
    @PutMapping("/update")
    public Result<UserDto> updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "根据ID删除用户，同时删除用户角色关联")
    @Parameter(name = "id", description = "用户ID", required = true, example = "1")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /**
     * 查询单个用户
     */
    @Operation(summary = "查询用户详情", description = "根据ID查询用户详细信息，包含角色列表")
    @Parameter(name = "id", description = "用户ID", required = true, example = "1")
    @GetMapping("/get/{id}")
    public Result<UserDto> getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 查询所有用户
     */
    @Operation(summary = "查询用户列表", description = "查询所有用户信息")
    @GetMapping("/list")
    public Result<List<UserDto>> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 给用户分配角色
     */
    @Operation(summary = "分配角色", description = "为用户分配一个或多个角色")
    @Parameter(name = "userId", description = "用户ID", required = true, example = "1")
    @PostMapping("/assign-roles")
    public Result<String> assignRoles(@RequestParam Long userId, @RequestBody List<Long> roleIds) {
        return userService.assignRolesToUser(userId, roleIds);
    }

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户使用用户名和密码登录系统")
    @PostMapping("/login")
    public Result<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest, HttpServletRequest request) {
        return userService.login(loginRequest, request);
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出", description = "退出登录，清除token和相关缓存")
    @Parameter(name = "token", description = "登录令牌", required = true, example = "abc123...")
    @PostMapping("/logout")
    public Result<String> logout(@RequestParam String token, HttpServletRequest request) {
        return userService.logout(token, request);
    }
}