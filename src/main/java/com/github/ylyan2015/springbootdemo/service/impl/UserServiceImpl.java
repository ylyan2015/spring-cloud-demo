package com.github.ylyan2015.springbootdemo.service.impl;

import com.github.ylyan2015.springbootdemo.common.LogStatusEnum;
import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.common.SystemConstants;
import com.github.ylyan2015.springbootdemo.common.UserStatusEnum;
import com.github.ylyan2015.springbootdemo.dto.LoginLogDto;
import com.github.ylyan2015.springbootdemo.dto.LoginRequestDto;
import com.github.ylyan2015.springbootdemo.dto.LoginResponseDto;
import com.github.ylyan2015.springbootdemo.dto.UserDto;
import com.github.ylyan2015.springbootdemo.entity.UserEO;
import com.github.ylyan2015.springbootdemo.entity.UserRoleEO;
import com.github.ylyan2015.springbootdemo.repository.UserRepository;
import com.github.ylyan2015.springbootdemo.repository.UserRoleRepository;
import com.github.ylyan2015.springbootdemo.service.ILoginLogService;
import com.github.ylyan2015.springbootdemo.service.IUserService;
import com.github.ylyan2015.springbootdemo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Resource
    private ILoginLogService loginLogService;

    @Resource
    private IpUtil ipUtil;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<UserDto> addUser(UserDto userDto) {
        try {
            if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
                return Result.fail(SystemConstants.MSG_USERNAME_ALREADY_EXISTS);
            }

            UserEO userEO = new UserEO();
            BeanUtils.copyProperties(userDto, userEO);

            String encryptedPassword = smCryptoUtil.sm3HashWithSalt(userDto.getPassword(), userDto.getUsername());
            userEO.setPassword(encryptedPassword);

            UserEO savedUser = userRepository.save(userEO);

            if (userDto.getRoleIds() != null && !userDto.getRoleIds().isEmpty()) {
                assignRoles(savedUser.getId(), userDto.getRoleIds());
            }

            UserDto result = toDto(savedUser);
            result.setPassword(null);
            return Result.success(result);
        } catch (Exception e) {
            log.error("新增用户失败", e);
            return Result.fail("新增用户失败，请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<UserDto> updateUser(UserDto userDto) {
        try {
            Optional<UserEO> optional = userRepository.findById(userDto.getId());
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_USER_NOT_FOUND);
            }

            if (userDto.getUsername() != null && !userDto.getUsername().equals(optional.get().getUsername())) {
                if (userRepository.findByUsernameAndIdNot(userDto.getUsername(), userDto.getId()).isPresent()) {
                    return Result.fail(SystemConstants.MSG_USERNAME_ALREADY_EXISTS);
                }
            }

            UserEO userEO = optional.get();
            BeanUtils.copyProperties(userDto, userEO, "id", "createTime");

            if (userDto.getPassword() != null && !userDto.getPassword().trim().isEmpty()) {
                String salt = userDto.getUsername() != null ? userDto.getUsername() : optional.get().getUsername();
                String encryptedPassword = smCryptoUtil.sm3HashWithSalt(userDto.getPassword(), salt);
                userEO.setPassword(encryptedPassword);
            } else {
                userEO.setPassword(optional.get().getPassword());
            }

            UserEO updatedUser = userRepository.save(userEO);

            if (userDto.getRoleIds() != null) {
                userRoleRepository.deleteByUserId(updatedUser.getId());
                if (!userDto.getRoleIds().isEmpty()) {
                    assignRoles(updatedUser.getId(), userDto.getRoleIds());
                }
            }

            String key = SystemConstants.CacheKey.USER_PREFIX + updatedUser.getId();
            redisUtil.del(key);

            UserDto result = toDto(updatedUser);
            result.setPassword(null);
            return Result.success(result);
        } catch (Exception e) {
            log.error("修改用户失败", e);
            return Result.fail("修改用户失败，请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<String> deleteUser(Long id) {
        try {
            Optional<UserEO> optional = userRepository.findById(id);
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_USER_NOT_FOUND);
            }

            userRoleRepository.deleteByUserId(id);
            userRepository.deleteById(id);

            String key = SystemConstants.CacheKey.USER_PREFIX + id;
            redisUtil.del(key);

            return Result.success(SystemConstants.MSG_DELETE_SUCCESS);
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return Result.fail("删除用户失败，请联系管理员");
        }
    }

    @Override
    public Result<UserDto> getUserById(Long id) {
        try {
            String userKey = SystemConstants.CacheKey.USER_PREFIX + id;
            if (redisUtil.hasKey(userKey)) {
                Object cached = redisUtil.get(userKey);
                if (cached instanceof UserDto) {
                    log.debug("从缓存获取用户信息，userId: {}", id);
                    return Result.success((UserDto) cached);
                }
            }

            Optional<UserEO> optional = userRepository.findById(id);
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_USER_NOT_FOUND);
            }

            UserDto userDto = toDto(optional.get());
            userDto.setPassword(null);

            String roleCacheKey = SystemConstants.CacheKey.USER_ROLE_PREFIX + id;
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
                    redisUtil.set(roleCacheKey, roleIds, SystemConstants.TimeSeconds.FIVE_MINUTES);
                    log.debug("缓存用户角色，userId: {}, 数量: {}", id, roleIds.size());
                }
            }

            redisUtil.set(userKey, userDto, SystemConstants.TimeSeconds.FIVE_MINUTES);

            return Result.success(userDto);
        } catch (Exception e) {
            log.error("查询用户失败", e);
            return Result.fail("查询用户失败，请联系管理员");
        }
    }

    @Override
    public Result<List<UserDto>> getAllUsers() {
        try {
            List<UserEO> users = userRepository.findAll();
            List<UserDto> userDtos = users.stream()
                    .map(this::toDto)
                    .peek(dto -> dto.setPassword(null))
                    .collect(Collectors.toList());
            return Result.success(userDtos);
        } catch (Exception e) {
            log.error("查询用户列表失败", e);
            return Result.fail("查询用户列表失败，请联系管理员");
        }
    }

    @Override
    public Result<LoginResponseDto> login(LoginRequestDto loginRequest, HttpServletRequest request) {
        try {
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                recordLoginLog(null, loginRequest.getUsername(), "login",
                        LogStatusEnum.FAILURE, request, SystemConstants.MSG_USERNAME_CANNOT_BE_EMPTY);
                return Result.fail(SystemConstants.MSG_USERNAME_CANNOT_BE_EMPTY);
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                recordLoginLog(null, loginRequest.getUsername(), "login",
                        LogStatusEnum.FAILURE, request, SystemConstants.MSG_PASSWORD_CANNOT_BE_EMPTY);
                return Result.fail(SystemConstants.MSG_PASSWORD_CANNOT_BE_EMPTY);
            }

            Optional<UserEO> userOptional = userRepository.findByUsername(loginRequest.getUsername());
            if (userOptional.isEmpty()) {
                recordLoginLog(null, loginRequest.getUsername(), "login",
                        LogStatusEnum.FAILURE, request, SystemConstants.MSG_INVALID_USERNAME_OR_PASSWORD);
                return Result.fail(SystemConstants.MSG_INVALID_USERNAME_OR_PASSWORD);
            }

            UserEO user = userOptional.get();

            if (user.getStatus() != null && user.getStatus() == UserStatusEnum.DISABLED) {
                recordLoginLog(user.getId(), user.getUsername(), "login",
                        LogStatusEnum.FAILURE, request, SystemConstants.MSG_USER_DISABLED);
                return Result.fail(SystemConstants.MSG_USER_DISABLED);
            }

            String inputPasswordHash = smCryptoUtil.sm3HashWithSalt(loginRequest.getPassword(), loginRequest.getUsername());

            if (!user.getPassword().equals(inputPasswordHash)) {
                recordLoginLog(user.getId(), user.getUsername(), "login",
                        LogStatusEnum.FAILURE, request, SystemConstants.MSG_INVALID_USERNAME_OR_PASSWORD);
                return Result.fail(SystemConstants.MSG_INVALID_USERNAME_OR_PASSWORD);
            }

            LoginResponseDto responseDto = new LoginResponseDto();
            responseDto.setUserId(user.getId());
            responseDto.setUsername(user.getUsername());
            responseDto.setNickname(user.getNickname());
            responseDto.setAvatar(user.getAvatar());

            List<UserRoleEO> userRoles = userRoleRepository.findByUserId(user.getId());
            if (!userRoles.isEmpty()) {
                List<Long> roleIds = userRoles.stream()
                        .map(UserRoleEO::getRoleId)
                        .collect(Collectors.toList());
                responseDto.setRoleIds(roleIds);
            }

            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            responseDto.setToken(token);

            String tokenKey = SystemConstants.CacheKey.LOGIN_TOKEN_PREFIX + token;
            Date expiration = jwtUtil.getExpirationDateFromToken(token);
            if (expiration != null) {
                long tokenTtlSeconds = (expiration.getTime() - System.currentTimeMillis()) / 1000;
                redisUtil.set(tokenKey, responseDto, Math.max(tokenTtlSeconds, 1));
            }
            recordLoginLog(user.getId(), user.getUsername(), "login",
                    LogStatusEnum.SUCCESS, request, null);

            log.info("用户登录成功，userId: {}, username: {}", user.getId(), user.getUsername());
            return Result.success(responseDto);

        } catch (Exception e) {
            log.error("用户登录失败", e);
            recordLoginLog(null, loginRequest.getUsername(), "login",
                    LogStatusEnum.FAILURE, request, "登录异常：" + e.getMessage());
            return Result.fail("登录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<String> assignRolesToUser(Long userId, List<Long> roleIds) {
        try {
            Optional<UserEO> optional = userRepository.findById(userId);
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_USER_NOT_FOUND);
            }

            userRoleRepository.deleteByUserId(userId);

            if (roleIds != null && !roleIds.isEmpty()) {
                assignRoles(userId, roleIds);
            }

            String userKey = SystemConstants.CacheKey.USER_PREFIX + userId;
            String roleCacheKey = SystemConstants.CacheKey.USER_ROLE_PREFIX + userId;
            redisUtil.del(userKey, roleCacheKey);

            return Result.success(SystemConstants.MSG_ASSIGN_ROLE_SUCCESS);
        } catch (Exception e) {
            log.error("分配角色失败", e);
            return Result.fail("分配角色失败，请联系管理员");
        }
    }

    @Override
    public Result<String> logout(String token, HttpServletRequest request) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return Result.fail(SystemConstants.MSG_TOKEN_CANNOT_BE_EMPTY);
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);

            String tokenKey = SystemConstants.CacheKey.LOGIN_TOKEN_PREFIX + token;

            if (redisUtil.hasKey(tokenKey)) {
                redisUtil.del(tokenKey);
            }

            if (userId != null) {
                String userKey = SystemConstants.CacheKey.USER_PREFIX + userId;
                String roleCacheKey = SystemConstants.CacheKey.USER_ROLE_PREFIX + userId;
                redisUtil.del(userKey, roleCacheKey);
            }

            if (username != null) {
                recordLoginLog(userId, username, "logout",
                        LogStatusEnum.SUCCESS, request, null);
                log.info("用户登出成功，userId: {}, username: {}", userId, username);
            } else {
                log.info("用户登出成功，token已清除");
            }

            return Result.success(SystemConstants.MSG_LOGOUT_SUCCESS);
        } catch (Exception e) {
            log.error("用户登出失败", e);
            return Result.fail("登出失败，请联系管理员");
        }
    }

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

    private UserDto toDto(UserEO userEO) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEO, userDto);
        return userDto;
    }

    private void recordLoginLog(Long userId, String username, String operationType,
                                LogStatusEnum status, HttpServletRequest request, String errorMsg) {
        try {
            LoginLogDto loginLog = new LoginLogDto();
            loginLog.setUserId(userId);
            loginLog.setUsername(username);
            loginLog.setOperationType(operationType);
            loginLog.setStatus(status);

            String ipAddress = IpUtil.getClientIp(request);
            loginLog.setIpAddress(ipAddress);

            if (ipAddress != null && !ipAddress.isEmpty()) {
                loginLog.setProvince(ipUtil.getProvince(ipAddress));
                loginLog.setCity(ipUtil.getCity(ipAddress));
                loginLog.setDistrict(ipUtil.getDistrict(ipAddress));
                loginLog.setAddress(ipUtil.getAddress(ipAddress));
            }

            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null) {
                loginLog.setBrowser(UserAgentUtil.parseBrowser(userAgent));
                loginLog.setOs(UserAgentUtil.parseOs(userAgent));
            }

            loginLog.setErrorMsg(errorMsg);

            loginLogService.saveLoginLog(loginLog);
        } catch (Exception e) {
            log.error("记录登录日志失败", e);
        }
    }
}
