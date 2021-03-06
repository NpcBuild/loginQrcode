package com.qrcodelogin.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统返回状态枚举与包装函数
 * 简易Shiro
 */
@AllArgsConstructor
@Getter
public enum ServerResponseEnum {
    SUCCESS(0, "成功"),
    ERROR(10, "失败"),

    ACCOUNT_NOT_EXIST(11, "账号不存在"),
    DUPLICATE_ACCOUNT(12, "账号重复"),
    ACCOUNT_IS_DISABLED(13, "账号被禁用"),
    ACCOUNT_IS_LOCKED(14, "账号被锁定,请稍后再试"),
    NOT_LOGIN_IN(15, "账号未登录"),
    UNAUTHORIZED(16, "没有权限"),
    INCORRECT_CREDENTIALS(17, "账号或密码错误");

    Integer code;
    String message;
}
