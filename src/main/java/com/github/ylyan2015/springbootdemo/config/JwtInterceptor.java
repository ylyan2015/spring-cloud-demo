package com.github.ylyan2015.springbootdemo.config;

import com.alibaba.fastjson.JSON;
import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.common.SystemConstants;
import com.github.ylyan2015.springbootdemo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT认证拦截器
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader(jwtConfig.getHeader());

        if (authHeader != null && authHeader.startsWith(jwtConfig.getTokenPrefix())) {
            String token = authHeader.substring(jwtConfig.getTokenPrefix().length());

            if (jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.getUserIdFromToken(token);
                String username = jwtUtil.getUsernameFromToken(token);

                request.setAttribute("userId", userId);
                request.setAttribute("username", username);

                log.debug("JWT认证成功，userId: {}, username: {}", userId, username);
                return true;
            } else {
                log.warn("JWT token无效或已过期");
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Result<String> result = Result.fail(SystemConstants.UNAUTHORIZED_CODE, SystemConstants.MSG_TOKEN_EXPIRED);
        response.getWriter().write(JSON.toJSONString(result));
        return false;
    }
}
