package com.github.ylyan2015.springbootdemo.exception;

/**
 * 权限异常类
 */
public class AuthorizationException extends BusinessException {

    public AuthorizationException(String message) {
        super(403, message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(403, message, cause);
    }
}
