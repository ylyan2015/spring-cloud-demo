package com.github.ylyan2015.springbootdemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum MenuTypeEnum {

    DIRECTORY(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮");

    private static final Map<Integer, MenuTypeEnum> CODE_MAP = new HashMap<>();

    static {
        for (MenuTypeEnum type : values()) {
            CODE_MAP.put(type.code, type);
        }
    }

    private final int code;
    private final String description;

    public static MenuTypeEnum fromCode(int code) {
        MenuTypeEnum type = CODE_MAP.get(code);
        if (type == null) {
            throw new IllegalArgumentException("未知的菜单类型: " + code);
        }
        return type;
    }
}
