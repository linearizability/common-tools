package com.linearizability.common.exception;

/**
 * 参数验证异常类
 * 用于处理参数验证相关的异常
 *
 * @author ZhangBoyuan
 * @since 2025-11-07
 */
public class ValidationException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 默认参数验证异常错误码
     */
    public static final String DEFAULT_CODE = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(DEFAULT_CODE, message);
    }

    public ValidationException(String code, String message) {
        super(code, message);
    }

    public ValidationException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ValidationException(String code, String message, Object data) {
        super(code, message, data);
    }

    public ValidationException(String code, String message, Object data, Throwable cause) {
        super(code, message, data, cause);
    }
}

