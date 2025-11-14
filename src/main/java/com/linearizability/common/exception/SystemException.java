package com.linearizability.common.exception;

/**
 * 系统异常类 用于处理系统级别的异常（如数据库连接失败、外部服务调用失败等）
 *
 * @author ZhangBoyuan
 * @since  2025-11-07
 */
public class SystemException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 默认系统异常错误码
     */
    public static final String DEFAULT_CODE = "SYSTEM_ERROR";

    public SystemException(String message) {
        super(DEFAULT_CODE, message);
    }

    public SystemException(String code, String message) {
        super(code, message);
    }

    public SystemException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public SystemException(String code, String message, Object data) {
        super(code, message, data);
    }

    public SystemException(String code, String message, Object data, Throwable cause) {
        super(code, message, data, cause);
    }
}
