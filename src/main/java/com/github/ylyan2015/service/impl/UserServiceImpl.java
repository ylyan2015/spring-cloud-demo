package com.github.ylyan2015.service.impl;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dao.UserRepository;
import com.github.ylyan2015.dao.UserRoleRepository;
import com.github.ylyan2015.dto.LoginRequestDto;
import com.github.ylyan2015.dto.LoginResponseDto;
import com.github.ylyan2015.dto.UserDto;
import com.github.ylyan2015.entity.UserEO;
import com.github.ylyan2015.entity.UserRoleEO;
import com.github.ylyan2015.service.IUserService;
import com.github.ylyan2015.util.RedisUtil;
import com.github.ylyan2015.util.SmCryptoUtil;
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

    @Resource
    private SmCryptoUtil smCryptoUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<UserDto> addUser(UserDto userDto) {
        try {
            // 校验用户名是否已存在
            if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
                return Result.fail("用户名已存在");
            }
            
            UserEO userEO = new UserEO();
            BeanUtils.copyProperties(userDto, userEO);
            
            // 使用用户名作为salt对密码进行加密处理
            String encryptedPassword = smCryptoUtil.sm3HashWithSalt(userDto.getPassword(), userDto.getUsername());
            userEO.setPassword(encryptedPassword);
            
            UserEO savedUser = userRepository.save(userEO);
            
            if (userDto.getRoleIds() != null && !userDto.getRoleIds().isEmpty()) {
                assignRoles(savedUser.getId(), userDto.getRoleIds());
            }
            
            UserDto result = convertToDto(savedUser);
            // 不返回密码信息
            result.setPassword(null);
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
            
            // 如果修改了用户名，需要校验新用户名是否已被其他用户使用
            if (userDto.getUsername() != null && !userDto.getUsername().equals(optional.get().getUsername())) {
                if (userRepository.findByUsernameAndIdNot(userDto.getUsername(), userDto.getId()).isPresent()) {
                    return Result.fail("用户名已存在");
                }
            }
            
            UserEO userEO = optional.get();
            BeanUtils.copyProperties(userDto, userEO, "id", "createTime");
            
            // 如果提供了新密码，则更新密码
            if (userDto.getPassword() != null && !userDto.getPassword().trim().isEmpty()) {
                // 使用当前用户名（可能是新的）作为salt
                String salt = userDto.getUsername() != null ? userDto.getUsername() : optional.get().getUsername();
                String encryptedPassword = smCryptoUtil.sm3HashWithSalt(userDto.getPassword(), salt);
                userEO.setPassword(encryptedPassword);
            } else {
                // 保持原密码不变
                userEO.setPassword(optional.get().getPassword());
            }
            
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
            // 不返回密码信息
            result.setPassword(null);
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
            String userKey = "user:" + id;
            if (redisUtil.hasKey(userKey)) {
                Object cached = redisUtil.get(userKey);
                if (cached instanceof UserDto) {
                    log.debug("从缓存获取用户信息，userId: {}", id);
                    return Result.success((UserDto) cached);
                }
            }
            
            Optional<UserEO> optional = userRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("用户不存在");
            }
            
            UserDto userDto = convertToDto(optional.get());
            // 不返回密码信息
            userDto.setPassword(null);
            
            String roleCacheKey = "user:role:" + id;
            if (redisUtil.hasKey(roleCacheKey)) {
                Object cachedRoles = redisUtil.get(roleCacheKey);
                if (cachedRoles instanceof List) {
                    userDto.setRoleIds((List<Long>) cachedRoles);
                    log.debug("从缓存获取用户角色，userId: {}", id);
                }
            } else {
                List<UserRoleEO> userRoles = userRoleRepository.findByUserId(id);
                if (!userRoles.isEmpty()) {
                    List<Long> roleIds = userRoles.stream()
                            .map(UserRoleEO::getRoleId)
                            .collect(Collectors.toList());
                    userDto.setRoleIds(roleIds);
                    redisUtil.set(roleCacheKey, roleIds, 60);
                    log.debug("缓存用户角色，userId: {}, 数量: {}", id, roleIds.size());
                }
            }
            
            redisUtil.set(userKey, userDto, 300);
            
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
                    .peek(dto -> dto.setPassword(null)) // 不返回密码信息
                    .collect(Collectors.toList());
            return Result.success(userDtos);
        } catch (Exception e) {
            log.error("查询用户列表失败", e);
            return Result.fail("查询用户列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<LoginResponseDto> login(LoginRequestDto loginRequest) {
        try {
            // 参数校验
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                return Result.fail("用户名不能为空");
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                return Result.fail("密码不能为空");
            }

            // 根据用户名查询用户
            Optional<UserEO> userOptional = userRepository.findByUsername(loginRequest.getUsername());
            if (!userOptional.isPresent()) {
                return Result.fail("用户名或密码错误");
            }

            UserEO user = userOptional.get();

            // 检查用户状态
            if (user.getStatus() != null && user.getStatus() == 0) {
                return Result.fail("用户已被禁用");
            }

            // 使用用户名作为salt验证密码
            String inputPasswordHash = smCryptoUtil.sm3HashWithSalt(loginRequest.getPassword(), loginRequest.getUsername());
            
            if (!user.getPassword().equals(inputPasswordHash)) {
                return Result.fail("用户名或密码错误");
            }

            // 构建登录响应
            LoginResponseDto responseDto = new LoginResponseDto();
            responseDto.setUserId(user.getId());
            responseDto.setUsername(user.getUsername());
            responseDto.setNickname(user.getNickname());
            responseDto.setAvatar(user.getAvatar());

            // 获取用户角色
            List<UserRoleEO> userRoles = userRoleRepository.findByUserId(user.getId());
            if (!userRoles.isEmpty()) {
                List<Long> roleIds = userRoles.stream()
                        .map(UserRoleEO::getRoleId)
                        .collect(Collectors.toList());
                responseDto.setRoleIds(roleIds);
            }

            // 生成简单的token
            String token = smCryptoUtil.sm3Hash(String.valueOf(user.getId()) + System.currentTimeMillis());
            responseDto.setToken(token);

            // 将登录信息缓存到Redis
            String tokenKey = "login:token:" + token;
            redisUtil.set(tokenKey, responseDto, 7200); // 2小时过期

            log.info("用户登录成功，userId: {}, username: {}", user.getId(), user.getUsername());
            return Result.success(responseDto);

        } catch (Exception e) {
            log.error("用户登录失败", e);
            return Result.fail("登录失败：" + e.getMessage());
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
            
            String userKey = "user:" + userId;
            String roleCacheKey = "user:role:" + userId;
            redisUtil.del(userKey, roleCacheKey);
            
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
