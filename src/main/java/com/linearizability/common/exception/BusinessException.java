package com.linearizability.common.exception;

/**
 * 业务异常类 用于处理业务逻辑相关的异常
 *
 * @author ZhangBoyuan
 * @since  2025-11-07
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 默认业务异常错误码
     */
    public static final String DEFAULT_CODE = "BUSINESS_ERROR";

    public BusinessException(String message) {
        super(DEFAULT_CODE, message);
    }

    public BusinessException(String code, String message) {
        super(code, message);
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BusinessException(String code, String message, Object data) {
        super(code, message, data);
    }

    public BusinessException(String code, String message, Object data, Throwable cause) {
        super(code, message, data, cause);
    }
}
