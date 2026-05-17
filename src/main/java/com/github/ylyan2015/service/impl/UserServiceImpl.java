package com.github.ylyan2015.service.impl;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dao.UserRepository;
import com.github.ylyan2015.dao.UserRoleRepository;
import com.github.ylyan2015.dto.UserDto;
import com.github.ylyan2015.entity.UserEO;
import com.github.ylyan2015.entity.UserRoleEO;
import com.github.ylyan2015.service.IUserService;
import com.github.ylyan2015.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserRoleRepository userRoleRepository;

    @Resource
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<UserDto> addUser(UserDto userDto) {
        try {
            UserEO userEO = new UserEO();
            BeanUtils.copyProperties(userDto, userEO);
            
            UserEO savedUser = userRepository.save(userEO);
            
            if (userDto.getRoleIds() != null && !userDto.getRoleIds().isEmpty()) {
                assignRoles(savedUser.getId(), userDto.getRoleIds());
            }
            
            UserDto result = convertToDto(savedUser);
            return Result.success(result);
        } catch (Exception e) {
            log.error("新增用户失败", e);
            return Result.fail("新增用户失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<UserDto> updateUser(UserDto userDto) {
        try {
            Optional<UserEO> optional = userRepository.findById(userDto.getId());
            if (!optional.isPresent()) {
                return Result.fail("用户不存在");
            }
            
            UserEO userEO = optional.get();
            BeanUtils.copyProperties(userDto, userEO, "id", "createTime");
            
            UserEO updatedUser = userRepository.save(userEO);
            
            if (userDto.getRoleIds() != null) {
                userRoleRepository.deleteByUserId(updatedUser.getId());
                if (!userDto.getRoleIds().isEmpty()) {
                    assignRoles(updatedUser.getId(), userDto.getRoleIds());
                }
            }
            
            String key = "user:" + updatedUser.getId();
            redisUtil.del(key);
            
            UserDto result = convertToDto(updatedUser);
            return Result.success(result);
        } catch (Exception e) {
            log.error("修改用户失败", e);
            return Result.fail("修改用户失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteUser(Long id) {
        try {
            Optional<UserEO> optional = userRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("用户不存在");
            }
            
            userRoleRepository.deleteByUserId(id);
            userRepository.deleteById(id);
            
            String key = "user:" + id;
            redisUtil.del(key);
            
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return Result.fail("删除用户失败：" + e.getMessage());
        }
    }

    @Override
    public Result<UserDto> getUserById(Long id) {
        try {
            String key = "user:" + id;
            if (redisUtil.hasKey(key)) {
                Object cached = redisUtil.get(key);
                if (cached instanceof UserDto) {
                    return Result.success((UserDto) cached);
                }
            }
            
            Optional<UserEO> optional = userRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("用户不存在");
            }
            
            UserDto userDto = convertToDto(optional.get());
            
            List<UserRoleEO> userRoles = userRoleRepository.findByUserId(id);
            if (!userRoles.isEmpty()) {
                List<Long> roleIds = userRoles.stream()
                        .map(UserRoleEO::getRoleId)
                        .collect(Collectors.toList());
                userDto.setRoleIds(roleIds);
            }
            
            redisUtil.set(key, userDto, 300);
            
            return Result.success(userDto);
        } catch (Exception e) {
            log.error("查询用户失败", e);
            return Result.fail("查询用户失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<UserDto>> getAllUsers() {
        try {
            List<UserEO> users = userRepository.findAll();
            List<UserDto> userDtos = users.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(userDtos);
        } catch (Exception e) {
            log.error("查询用户列表失败", e);
            return Result.fail("查询用户列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> assignRolesToUser(Long userId, List<Long> roleIds) {
        try {
            Optional<UserEO> optional = userRepository.findById(userId);
            if (!optional.isPresent()) {
                return Result.fail("用户不存在");
            }
            
            userRoleRepository.deleteByUserId(userId);
            
            if (roleIds != null && !roleIds.isEmpty()) {
                assignRoles(userId, roleIds);
            }
            
            String key = "user:" + userId;
            redisUtil.del(key);
            
            return Result.success("分配角色成功");
        } catch (Exception e) {
            log.error("分配角色失败", e);
            return Result.fail("分配角色失败：" + e.getMessage());
        }
    }

    /**
     * 批量分配角色
     */
    private void assignRoles(Long userId, List<Long> roleIds) {
        List<UserRoleEO> userRoles = roleIds.stream()
                .map(roleId -> {
                    UserRoleEO userRole = new UserRoleEO();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    return userRole;
                })
                .collect(Collectors.toList());
        userRoleRepository.saveAll(userRoles);
    }

    /**
     * Entity转Dto
     */
    private UserDto convertToDto(UserEO userEO) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEO, userDto);
        return userDto;
    }
}
