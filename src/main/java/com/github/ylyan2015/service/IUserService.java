package com.github.ylyan2015.service;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.LoginRequestDto;
import com.github.ylyan2015.dto.LoginResponseDto;
import com.github.ylyan2015.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务接口
 */
public interface IUserService {

    /**
     * 新增用户
     */
    Result<UserDto> addUser(UserDto userDto);

    /**
     * 修改用户
     */
    Result<UserDto> updateUser(UserDto userDto);

    /**
     * 删除用户
     */
    Result<String> deleteUser(Long id);

    /**
     * 查询单个用户
     */
    Result<UserDto> getUserById(Long id);

    /**
     * 查询所有用户
     */
    Result<List<UserDto>> getAllUsers();

    /**
     * 给用户分配角色
     */
    Result<String> assignRolesToUser(Long userId, List<Long> roleIds);

    /**
     * 用户登录
     */
    Result<LoginResponseDto> login(LoginRequestDto loginRequest, HttpServletRequest request);

    /**
     * 用户登出
     */
    Result<String> logout(String token, HttpServletRequest request);
}
