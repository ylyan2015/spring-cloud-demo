package com.github.ylyan2015.controller;


import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.LoginRequestDto;
import com.github.ylyan2015.dto.LoginResponseDto;
import com.github.ylyan2015.dto.UserDto;
import com.github.ylyan2015.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public Result<UserDto> addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    /**
     * 修改用户
     */
    @PutMapping("/update")
    public Result<UserDto> updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /**
     * 查询单个用户
     */
    @GetMapping("/get/{id}")
    public Result<UserDto> getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/list")
    public Result<List<UserDto>> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 给用户分配角色
     */
    @PostMapping("/assign-roles")
    public Result<String> assignRoles(@RequestParam Long userId, @RequestBody List<Long> roleIds) {
        return userService.assignRolesToUser(userId, roleIds);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        return userService.login(loginRequest);
    }
}