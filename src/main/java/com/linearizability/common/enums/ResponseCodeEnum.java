package com.linearizability.common.enums;

import com.linearizability.common.base.BaseEnum;

/**
 * 响应状态码枚举示例 展示如何使用BaseEnum接口
 *
 * @author ZhangBoyuan
 * @since  2025-11-07
 */
public enum ResponseCodeEnum implements BaseEnum<Integer> {

    /**
     * 成功
     *
     */
    SUCCESS(200, "操作成功"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 服务器错误
     */
    SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR(600, "业务处理失败");

    private final Integer code;
    private final String message;

    ResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return message;
    }
}
