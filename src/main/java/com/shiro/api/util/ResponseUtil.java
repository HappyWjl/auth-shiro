package com.shiro.api.util;

import com.shiro.api.enums.ErrorCode;

public class ResponseUtil {

    public ResponseUtil() {
    }

    public static Response makeFail(String message) {
        return makeResponse(1, message, (Object)null);
    }

    public static Response makeSuccess(Object obj) {
        return makeResponse(0, "", obj);
    }

    public static Response makeSuccess(Object obj, String msg) {
        return makeResponse(0, msg, obj);
    }

    public static Response makeFail(Object obj) {
        return makeResponse(1, "", obj);
    }

    public static Response makeError(ErrorCode errorCode) {
        return makeResponse(errorCode.getCode(), errorCode.getMsg(), (Object)null);
    }

    public static Response makeError(ErrorCode errorCode, Object obj) {
        return makeResponse(errorCode.getCode(), errorCode.getMsg(), obj);
    }

    public static Response makeAdminError(ErrorCode errorCode) {
        return makeResponse(errorCode.getCode(), errorCode.getMsg(), (Object)null);
    }

    public static Response makeAdminError(ErrorCode errorCode, Object obj) {
        return makeResponse(errorCode.getCode(), errorCode.getMsg(), obj);
    }

    public static Response makeResponse(int code, String msg, Object obj) {
        Response result = new Response();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(obj);
        return result;
    }

    public static boolean isOk(Response response) {
        return response != null && response.getCode() == 0;
    }
}
