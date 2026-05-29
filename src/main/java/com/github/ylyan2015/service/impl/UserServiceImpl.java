package com.github.ylyan2015.service.impl;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dao.UserRepository;
import com.github.ylyan2015.dao.UserRoleRepository;
import com.github.ylyan2015.dto.LoginLogDto;
import com.github.ylyan2015.dto.LoginRequestDto;
import com.github.ylyan2015.dto.LoginResponseDto;
import com.github.ylyan2015.dto.UserDto;
import com.github.ylyan2015.entity.UserEO;
import com.github.ylyan2015.entity.UserRoleEO;
import com.github.ylyan2015.service.ILoginLogService;
import com.github.ylyan2015.service.IUserService;
import com.github.ylyan2015.util.IpUtil;
import com.github.ylyan2015.util.JwtUtil;
import com.github.ylyan2015.util.RedisUtil;
import com.github.ylyan2015.util.SmCryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private ILoginLogService loginLogService;

    @Resource
    private IpUtil ipUtil;

    @Resource
    private JwtUtil jwtUtil;

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
    public Result<LoginResponseDto> login(LoginRequestDto loginRequest, HttpServletRequest request) {
        try {
            // 参数校验
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                recordLoginLog(null, loginRequest.getUsername(), "login", 0, request, "用户名不能为空");
                return Result.fail("用户名不能为空");
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                recordLoginLog(null, loginRequest.getUsername(), "login", 0, request, "密码不能为空");
                return Result.fail("密码不能为空");
            }

            // 根据用户名查询用户
            Optional<UserEO> userOptional = userRepository.findByUsername(loginRequest.getUsername());
            if (!userOptional.isPresent()) {
                recordLoginLog(null, loginRequest.getUsername(), "login", 0, request, "用户名或密码错误");
                return Result.fail("用户名或密码错误");
            }

            UserEO user = userOptional.get();

            // 检查用户状态
            if (user.getStatus() != null && user.getStatus() == 0) {
                recordLoginLog(user.getId(), user.getUsername(), "login", 0, request, "用户已被禁用");
                return Result.fail("用户已被禁用");
            }

            // 使用用户名作为salt验证密码
            String inputPasswordHash = smCryptoUtil.sm3HashWithSalt(loginRequest.getPassword(), loginRequest.getUsername());
            
            if (!user.getPassword().equals(inputPasswordHash)) {
                recordLoginLog(user.getId(), user.getUsername(), "login", 0, request, "用户名或密码错误");
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

            // 生成JWT token
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            responseDto.setToken(token);

            // 将登录信息缓存到Redis（可选，用于快速查询和强制下线）
            String tokenKey = "login:token:" + token;
            redisUtil.set(tokenKey, responseDto, jwtUtil.getExpirationDateFromToken(token).getTime() / 1000 - System.currentTimeMillis() / 1000);

            // 记录登录成功日志
            recordLoginLog(user.getId(), user.getUsername(), "login", 1, request, null);

            log.info("用户登录成功，userId: {}, username: {}", user.getId(), user.getUsername());
            return Result.success(responseDto);

        } catch (Exception e) {
            log.error("用户登录失败", e);
            recordLoginLog(null, loginRequest.getUsername(), "login", 0, request, "登录异常：" + e.getMessage());
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

    @Override
    public Result<String> logout(String token, HttpServletRequest request) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return Result.fail("token不能为空");
            }

            // 从JWT中获取用户信息
            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);

            String tokenKey = "login:token:" + token;
            
            // 删除Redis中的token缓存
            if (redisUtil.hasKey(tokenKey)) {
                redisUtil.del(tokenKey);
            }
            
            // 清除用户相关缓存
            if (userId != null) {
                String userKey = "user:" + userId;
                String roleCacheKey = "user:role:" + userId;
                redisUtil.del(userKey, roleCacheKey);
            }
            
            // 记录登出成功日志
            if (username != null) {
                recordLoginLog(userId, username, "logout", 1, request, null);
                log.info("用户登出成功，userId: {}, username: {}", userId, username);
            } else {
                log.info("用户登出成功，token已清除");
            }
            
            return Result.success("登出成功");
        } catch (Exception e) {
            log.error("用户登出失败", e);
            return Result.fail("登出失败：" + e.getMessage());
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

    /**
     * 记录登录日志
     */
    private void recordLoginLog(Long userId, String username, String operationType, 
                                Integer status, HttpServletRequest request, String errorMsg) {
        try {
            LoginLogDto loginLog = new LoginLogDto();
            loginLog.setUserId(userId);
            loginLog.setUsername(username);
            loginLog.setOperationType(operationType);
            loginLog.setStatus(status);
            
            // 获取IP地址
            String ipAddress = getClientIp(request);
            loginLog.setIpAddress(ipAddress);
            
            // 解析IP地址
            if (ipAddress != null && !ipAddress.isEmpty()) {
                loginLog.setProvince(ipUtil.getProvince(ipAddress));
                loginLog.setCity(ipUtil.getCity(ipAddress));
                loginLog.setDistrict(ipUtil.getDistrict(ipAddress));
                loginLog.setAddress(ipUtil.getAddress(ipAddress));
            }
            
            // 获取浏览器和操作系统信息
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null) {
                loginLog.setBrowser(parseBrowser(userAgent));
                loginLog.setOs(parseOs(userAgent));
            }
            
            loginLog.setErrorMsg(errorMsg);
            
            loginLogService.saveLoginLog(loginLog);
        } catch (Exception e) {
            log.error("记录登录日志失败", e);
        }
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 对于通过多个代理的情况，第一个IP为客户端真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }

    /**
     * 解析浏览器信息
     */
    private String parseBrowser(String userAgent) {
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            return "IE";
        }
        return "Unknown";
    }

    /**
     * 解析操作系统信息
     */
    private String parseOs(String userAgent) {
        if (userAgent.contains("Windows NT 10.0")) {
            return "Windows 10";
        } else if (userAgent.contains("Windows NT 6.3")) {
            return "Windows 8.1";
        } else if (userAgent.contains("Windows NT 6.2")) {
            return "Windows 8";
        } else if (userAgent.contains("Windows NT 6.1")) {
            return "Windows 7";
        } else if (userAgent.contains("Mac OS X")) {
            return "Mac OS X";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            return "iOS";
        }
        return "Unknown";
    }
}
