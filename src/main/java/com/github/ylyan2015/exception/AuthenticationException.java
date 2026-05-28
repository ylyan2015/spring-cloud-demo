package com.github.ylyan2015.exception;

/**
 * 认证异常类
 */
public class AuthenticationException extends BusinessException {

    public AuthenticationException(String message) {
        super(401, message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(401, message, cause);
    }
}
