package com.github.ylyan2015.common;

/**
 * 系统常量类
 */
public class SystemConstants {

    /**
     * 成功响应码
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 失败响应码
     */
    public static final int FAIL_CODE = 500;

    /**
     * 未授权响应码
     */
    public static final int UNAUTHORIZED_CODE = 401;

    /**
     * 禁止访问响应码
     */
    public static final int FORBIDDEN_CODE = 403;

    /**
     * 资源不存在响应码
     */
    public static final int NOT_FOUND_CODE = 404;

    /**
     * 参数校验失败响应码
     */
    public static final int VALIDATION_FAILED_CODE = 400;

    /**
     * 成功消息
     */
    public static final String SUCCESS_MSG = "成功";

    /**
     * 失败消息
     */
    public static final String FAIL_MSG = "失败";

    /**
     * 用户相关常量
     */
    public static class User {
        public static final String USERNAME_ALREADY_EXISTS = "用户名已存在";
        public static final String USER_NOT_FOUND = "用户不存在";
        public static final String USER_DISABLED = "用户已被禁用";
        public static final String INVALID_USERNAME_OR_PASSWORD = "用户名或密码错误";
        public static final String USERNAME_CANNOT_BE_EMPTY = "用户名不能为空";
        public static final String PASSWORD_CANNOT_BE_EMPTY = "密码不能为空";
    }

    /**
     * 角色相关常量
     */
    public static class Role {
        public static final String ROLE_NOT_FOUND = "角色不存在";
        public static final String ROLE_ALREADY_EXISTS = "角色已存在";
    }

    /**
     * 菜单相关常量
     */
    public static class Menu {
        public static final String MENU_NOT_FOUND = "菜单不存在";
        public static final String MENU_ALREADY_EXISTS = "菜单已存在";
    }

    /**
     * 字典相关常量
     */
    public static class Dict {
        public static final String DICT_NOT_FOUND = "字典不存在";
        public static final String DICT_ITEM_NOT_FOUND = "字典项不存在";
    }

    /**
     * 登录相关常量
     */
    public static class Login {
        public static final String TOKEN_EXPIRED = "令牌已过期或无效";
        public static final String LOGIN_FAILED = "登录失败";
        public static final String LOGOUT_SUCCESS = "登出成功";
    }
}
