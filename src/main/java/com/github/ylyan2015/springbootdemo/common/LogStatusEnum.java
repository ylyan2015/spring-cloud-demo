package com.github.ylyan2015.springbootdemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum LogStatusEnum {

    FAILURE(0, "失败"),
    SUCCESS(1, "成功");

    private static final Map<Integer, LogStatusEnum> CODE_MAP = new HashMap<>();

    static {
        for (LogStatusEnum status : values()) {
            CODE_MAP.put(status.code, status);
        }
    }

    private final int code;
    private final String description;

    public static LogStatusEnum fromCode(int code) {
        LogStatusEnum status = CODE_MAP.get(code);
        if (status == null) {
            throw new IllegalArgumentException("未知的日志状态: " + code);
        }
        return status;
    }
}
