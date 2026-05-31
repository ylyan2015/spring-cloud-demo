package com.github.ylyan2015.springbootdemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    private static final Map<Integer, UserStatusEnum> CODE_MAP = new HashMap<>();

    static {
        for (UserStatusEnum status : values()) {
            CODE_MAP.put(status.code, status);
        }
    }

    private final int code;
    private final String description;

    public static UserStatusEnum fromCode(int code) {
        UserStatusEnum status = CODE_MAP.get(code);
        if (status == null) {
            throw new IllegalArgumentException("未知的状态: " + code);
        }
        return status;
    }
}
