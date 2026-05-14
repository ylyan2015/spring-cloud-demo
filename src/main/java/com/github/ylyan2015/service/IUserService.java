package com.github.ylyan2015.service;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.UserDto;

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
     * 根据ID查询用户
     */
    Result<UserDto> getUserById(Long id);
    
    /**
     * 查询所有用户
     */
    Result<List<UserDto>> getAllUsers();
    
    /**
     * 为用户分配角色
     */
    Result<String> assignRolesToUser(Long userId, List<Long> roleIds);
}
