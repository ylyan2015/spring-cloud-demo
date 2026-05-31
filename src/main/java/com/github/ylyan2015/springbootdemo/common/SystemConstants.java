package com.github.ylyan2015.springbootdemo.common;

public class SystemConstants {

    public static final int SUCCESS_CODE = 200;

    public static final int FAIL_CODE = 500;

    public static final int UNAUTHORIZED_CODE = 401;

    public static final int FORBIDDEN_CODE = 403;

    public static final int NOT_FOUND_CODE = 404;

    public static final int VALIDATION_FAILED_CODE = 400;

    public static final String SUCCESS_MSG = "操作成功";

    public static final String FAIL_MSG = "操作失败";

    public static final String MSG_USERNAME_ALREADY_EXISTS = "用户名已存在";

    public static final String MSG_USER_NOT_FOUND = "用户不存在";

    public static final String MSG_USER_DISABLED = "用户已被禁用";

    public static final String MSG_INVALID_USERNAME_OR_PASSWORD = "用户名或密码错误";

    public static final String MSG_USERNAME_CANNOT_BE_EMPTY = "用户名不能为空";

    public static final String MSG_PASSWORD_CANNOT_BE_EMPTY = "密码不能为空";

    public static final String MSG_ROLE_NOT_FOUND = "角色不存在";

    public static final String MSG_ROLE_ALREADY_EXISTS = "角色已存在";

    public static final String MSG_MENU_NOT_FOUND = "菜单不存在";

    public static final String MSG_MENU_ALREADY_EXISTS = "菜单已存在";

    public static final String MSG_DICT_NOT_FOUND = "字典不存在";

    public static final String MSG_DICT_ALREADY_EXISTS = "字典编码已存在";

    public static final String MSG_DICT_ITEM_NOT_FOUND = "字典项不存在";

    public static final String MSG_TOKEN_EXPIRED = "令牌已过期或无效";

    public static final String MSG_LOGIN_FAILED = "登录失败";

    public static final String MSG_LOGOUT_SUCCESS = "登出成功";

    public static final String MSG_DELETE_SUCCESS = "删除成功";

    public static final String MSG_ASSIGN_ROLE_SUCCESS = "分配角色成功";

    public static final String MSG_ASSIGN_MENU_SUCCESS = "分配菜单成功";

    public static final String MSG_TOKEN_CANNOT_BE_EMPTY = "token不能为空";

    /**
     * 缓存键常量
     */
    public static class CacheKey {
        /**
         * 用户信息缓存前缀
         */
        public static final String USER_PREFIX = "user:";
        /**
         * 用户角色缓存前缀
         */
        public static final String USER_ROLE_PREFIX = "user:role:";
        /**
         * 登录令牌缓存前缀
         */
        public static final String LOGIN_TOKEN_PREFIX = "login:token:";
        /**
         * 角色菜单缓存前缀
         */
        public static final String ROLE_MENU_PREFIX = "role:menu:";

    }

    /**
     * 业务常量
     */
    public static class Business {
        /**
         * 根菜单ID
         */
        public static final Long ROOT_MENU_ID = 0L;
        /**
         * 系统用户ID(用于操作日志)
         */
        public static final Long SYSTEM_USER_ID = 0L;
        /**
         * 系统用户名(用于操作日志)
         */
        public static final String SYSTEM_USERNAME = "system";
    }

    /**
     * 时间常量(单位:秒)
     */
    public static class TimeSeconds {
        /**
         * 1分钟 = 60秒
         */
        public static final int ONE_MINUTE = 60;
        /**
         * 5分钟 = 300秒，用于短期缓存
         */
        public static final int FIVE_MINUTES = 300;
        /**
         * 2小时 = 7200秒，用于长期缓存
         */
        public static final int TWO_HOURS = 7200;
    }
}
