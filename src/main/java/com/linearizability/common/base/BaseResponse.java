package com.linearizability.common.base;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础响应类
 * 所有响应类应继承此类，提供通用的响应字段
 *
 * @author ZhangBoyuan
 * @since 2025-11-07
 */
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应ID，对应请求ID
     */
    private String responseId;

    /**
     * 响应时间戳
     */
    private Long timestamp;

    /**
     * 响应时间
     */
    private LocalDateTime responseTime;

    /**
     * 处理耗时（毫秒）
     */
    private Long costTime;

    /**
     * 扩展字段，用于存储额外的响应信息
     */
    private java.util.Map<String, Object> extParams;

    public BaseResponse() {
        this.timestamp = System.currentTimeMillis();
        this.responseTime = LocalDateTime.now();
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public java.util.Map<String, Object> getExtParams() {
        return extParams;
    }

    public void setExtParams(java.util.Map<String, Object> extParams) {
        this.extParams = extParams;
    }

    /**
     * 添加扩展参数
     *
     * @param key   键
     * @param value 值
     */
    public void addExtParam(String key, Object value) {
        if (this.extParams == null) {
            this.extParams = new java.util.HashMap<>();
        }
        this.extParams.put(key, value);
    }

    /**
     * 获取扩展参数
     *
     * @param key 键
     * @return 值
     */
    public Object getExtParam(String key) {
        return extParams == null ? null : extParams.get(key);
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
            "responseId='" + responseId + '\'' +
            ", timestamp=" + timestamp +
            ", responseTime=" + responseTime +
            ", costTime=" + costTime +
            ", extParams=" + extParams +
            '}';
    }
}
