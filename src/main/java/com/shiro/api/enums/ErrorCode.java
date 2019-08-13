package com.shiro.api.enums;

/**
 * 错误码
 */
public enum ErrorCode {

    UNAUTHENTIC(100401, "未认证，请先登录！"),
    TOKENERROR(100402, "未认证，Token错误！"),
    UNAUTHORIZED(100403, "未授权，权限不足！"),
    ERROR(100500, "服务器发生错误！"),
    PARAM_ERROR(100600, "账号密码有误！"),
    LOGIN_ERROR(100700, "登录失败，请重试！"),
    LAY_OUT_SUCCESS(100800, "退出成功！"),
    CAPTCHA_ERROR(100900, "验证码生成失败，请重试！"),
    CAPTCHA_CHECK_ERROR(101000, "验证码错误，请重试！");

    private Integer code;

    private String msg;

    ErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
