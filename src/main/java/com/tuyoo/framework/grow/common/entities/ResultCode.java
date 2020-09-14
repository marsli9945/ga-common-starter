package com.tuyoo.framework.grow.common.entities;

/**
 * 枚举了一些常用API操作码
 * Created by macro on 2019/4/19.
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(0, "操作成功"),

    UNAUTHORIZED(1, "暂未登录或token已经过期"),
    FORBIDDEN(3, "没有相关权限"),
    NULL_POINT(5, "空指针异常"),
    VALIDATE_FAILED(6, "参数检验失败"),
    HTTP_CLIENT_ERROR(8, "服务请求错误"),

    FAILED(10, "操作失败");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
