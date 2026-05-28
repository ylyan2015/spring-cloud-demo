package com.github.ylyan2015.exception;

/**
 * 参数校验异常类
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(400, message);
    }

    public ValidationException(String message, Throwable cause) {
        super(400, message, cause);
    }
}
