package com.github.ylyan2015.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 统一返回结果封装类
 * @param <T> 数据类型
 */
@Data
@Schema(description = "统一返回结果")
public class Result<T> {
    
    /**
     * 响应码
     */
    @Schema(description = "响应码", example = "200")
    private int code;
    
    /**
     * 响应消息
     */
    @Schema(description = "响应消息", example = "成功")
    private String msg;
    
    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    /**
     * 成功响应
     */
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("成功");
        r.setData(data);
        return r;
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> fail(String msg) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }
}
