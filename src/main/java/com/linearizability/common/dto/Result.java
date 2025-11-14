package com.linearizability.common.dto;

import com.linearizability.common.base.BaseResponse;
import com.linearizability.common.exception.BaseException;

/**
 * 统一响应结果类 用于封装所有API的响应结果，提供统一的响应格式
 *
 * @param  <T> 响应数据类型
 * @author     ZhangBoyuan
 * @since      2025-11-07
 */
public class Result<T> extends BaseResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 成功状态码
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 失败状态码
     */
    public static final int FAIL_CODE = 500;

    /**
     * HTTP响应状态码（用于HTTP协议）
     */
    private Integer code;

    /**
     * 业务错误码（用于业务逻辑，可选）
     */
    private String errorCode;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 是否成功
     */
    private Boolean success;

    public Result() {
        super();
    }

    public Result(Integer code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = SUCCESS_CODE == code;
    }

    public Result(Integer code, String errorCode, String message, T data) {
        super();
        this.code = code;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
        this.success = SUCCESS_CODE == code;
    }

    /**
     * 成功响应（无数据）
     *
     * @param  <T> 数据类型
     * @return     响应结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功响应（带数据）
     *
     * @param  data 响应数据
     * @param  <T>  数据类型
     * @return      响应结果
     */
    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }

    /**
     * 成功响应（带消息和数据）
     *
     * @param  message 响应消息
     * @param  data    响应数据
     * @param  <T>     数据类型
     * @return         响应结果
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        result.setMessage(message);
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    /**
     * 失败响应（带消息）
     *
     * @param  message 错误消息
     * @param  <T>     数据类型
     * @return         响应结果
     */
    public static <T> Result<T> fail(String message) {
        return fail(FAIL_CODE, message);
    }

    /**
     * 失败响应（带业务错误码和消息）
     *
     * @param  errorCode 业务错误码
     * @param  message   错误消息
     * @param  <T>       数据类型
     * @return           响应结果
     */
    public static <T> Result<T> fail(String errorCode, String message) {
        return fail(FAIL_CODE, errorCode, message);
    }

    /**
     * 失败响应（带HTTP状态码和消息）
     *
     * @param  code    HTTP状态码
     * @param  message 错误消息
     * @param  <T>     数据类型
     * @return         响应结果
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return fail(code, null, message, null);
    }

    /**
     * 失败响应（带HTTP状态码、业务错误码和消息）
     *
     * @param  code      HTTP状态码
     * @param  errorCode 业务错误码
     * @param  message   错误消息
     * @param  <T>       数据类型
     * @return           响应结果
     */
    public static <T> Result<T> fail(Integer code, String errorCode, String message) {
        return fail(code, errorCode, message, null);
    }

    /**
     * 失败响应（带HTTP状态码、消息和数据）
     *
     * @param  code    HTTP状态码
     * @param  message 错误消息
     * @param  data    响应数据
     * @param  <T>     数据类型
     * @return         响应结果
     */
    public static <T> Result<T> fail(Integer code, String message, T data) {
        return fail(code, null, message, data);
    }

    /**
     * 失败响应（带HTTP状态码、业务错误码、消息和数据）
     *
     * @param  code      HTTP状态码
     * @param  errorCode 业务错误码
     * @param  message   错误消息
     * @param  data      响应数据
     * @param  <T>       数据类型
     * @return           响应结果
     */
    public static <T> Result<T> fail(Integer code, String errorCode, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setErrorCode(errorCode);
        result.setMessage(message);
        result.setData(data);
        result.setSuccess(false);
        return result;
    }

    /**
     * 从异常创建失败响应
     *
     * @param  exception 异常对象
     * @param  <T>       数据类型
     * @return           响应结果
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> fail(BaseException exception) {
        return fail(FAIL_CODE, exception.getCode(), exception.getMessage(), (T) exception.getData());
    }

    /**
     * 从异常创建失败响应（指定HTTP状态码）
     *
     * @param  code      HTTP状态码
     * @param  exception 异常对象
     * @param  <T>       数据类型
     * @return           响应结果
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> fail(Integer code, BaseException exception) {
        return fail(code, exception.getCode(), exception.getMessage(), (T) exception.getData());
    }

    /**
     * 判断是否成功
     *
     * @return true表示成功，false表示失败
     */
    public boolean isSuccess() {
        return success != null && success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
        this.success = SUCCESS_CODE == code;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Result{" + "code=" + code + ", errorCode='" + errorCode + '\'' + ", message='" + message + '\''
                + ", data=" + data + ", success=" + success + "} " + super.toString();
    }
}
