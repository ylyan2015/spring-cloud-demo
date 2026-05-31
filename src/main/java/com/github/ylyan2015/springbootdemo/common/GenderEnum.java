package com.github.ylyan2015.springbootdemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum GenderEnum {

    FEMALE(0, "女"),
    MALE(1, "男");

    private static final Map<Integer, GenderEnum> CODE_MAP = new HashMap<>();

    static {
        for (GenderEnum gender : values()) {
            CODE_MAP.put(gender.code, gender);
        }
    }

    private final int code;
    private final String description;

    public static GenderEnum fromCode(int code) {
        GenderEnum gender = CODE_MAP.get(code);
        if (gender == null) {
            throw new IllegalArgumentException("未知的性别: " + code);
        }
        return gender;
    }
}
