package com.github.ylyan2015.springbootdemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum YesNoEnum {

    NO(0, "否"),
    YES(1, "是");

    private static final Map<Integer, YesNoEnum> CODE_MAP = new HashMap<>();

    static {
        for (YesNoEnum value : values()) {
            CODE_MAP.put(value.code, value);
        }
    }

    private final int code;
    private final String description;

    public static YesNoEnum fromCode(int code) {
        YesNoEnum value = CODE_MAP.get(code);
        if (value == null) {
            throw new IllegalArgumentException("未知的值: " + code);
        }
        return value;
    }
}
