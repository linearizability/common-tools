package com.linearizability.common.exception;

/**
 * 基础异常类 所有自定义异常应继承此类，提供统一的异常处理规范
 *
 * @author ZhangBoyuan
 * @since  2025-11-07
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误详情（可选）
     */
    private Object data;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BaseException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseException(String code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseException{" + "code=" + code + ", message='" + message + '\'' + ", data=" + data + '}';
    }
}
