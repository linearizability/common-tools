package com.linearizability.common.enums;

/**
 * @author ZhangBoyuan
 * @since 2025-11-07
 */

import com.linearizability.common.base.BaseEnum;

/**
 * 响应状态码枚举示例
 * 展示如何使用BaseEnum接口
 *
 * @author ZhangBoyuan
 * @since 2025-11-07
 */
public enum ResponseCodeEnum implements BaseEnum<Integer> {

    /**
     * 成功
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

    /**
     * 根据编码查找枚举
     *
     * @param code 编码
     * @return 枚举，未找到返回Optional.empty()
     */
    public static java.util.Optional<ResponseCodeEnum> findByCode(Integer code) {
        return BaseEnum.findByCode(ResponseCodeEnum.class, code);
    }

    /**
     * 根据描述查找枚举
     *
     * @param desc 描述
     * @return 枚举，未找到返回Optional.empty()
     */
    public static java.util.Optional<ResponseCodeEnum> findByDesc(String desc) {
        return BaseEnum.findByDesc(ResponseCodeEnum.class, desc);
    }

    /**
     * 根据编码查找枚举，未找到时抛出异常
     *
     * @param code 编码
     * @return 枚举
     * @throws IllegalArgumentException 未找到匹配的枚举时抛出
     */
    public static ResponseCodeEnum findByCodeOrThrow(Integer code) {
        return BaseEnum.findByCodeOrThrow(ResponseCodeEnum.class, code);
    }

    /**
     * 判断编码是否存在
     *
     * @param code 编码
     * @return 存在返回true，否则返回false
     */
    public static boolean exists(Integer code) {
        return BaseEnum.exists(ResponseCodeEnum.class, code);
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


