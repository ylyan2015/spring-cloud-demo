package com.github.ylyan2015.springbootdemo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 统一返回结果封装类
 *
 * @param <T> 数据类型
 */
@Data
@Schema(description = "统一返回结果")
public class Result<T> {

    @Schema(description = "响应码", example = "200")
    private Integer code;

    @Schema(description = "响应消息", example = "操作成功")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    public Result() {
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(SystemConstants.SUCCESS_CODE, SystemConstants.SUCCESS_MSG, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(SystemConstants.SUCCESS_CODE, SystemConstants.SUCCESS_MSG, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SystemConstants.SUCCESS_CODE, message, data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(SystemConstants.FAIL_CODE, message, null);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
