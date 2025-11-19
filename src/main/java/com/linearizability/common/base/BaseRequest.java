package com.linearizability.common.base;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础请求类 所有请求类应继承此类，提供通用的请求字段
 *
 * @author ZhangBoyuan
 * @since  2025-11-07
 */
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求ID，用于链路追踪
     */
    private String requestId;

    /**
     * 请求时间戳
     */
    private Long timestamp;

    /**
     * 请求来源（如：web、app、api等）
     */
    private String source;

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 请求开始时间（用于性能统计）
     */
    private LocalDateTime startTime;

    /**
     * 扩展字段，用于存储额外的请求参数
     */
    private Map<String, Object> extParams;

    public BaseRequest() {
        this.timestamp = System.currentTimeMillis();
        this.startTime = LocalDateTime.now();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Map<String, Object> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, Object> extParams) {
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
            this.extParams = new HashMap<>();
        }
        this.extParams.put(key, value);
    }

    /**
     * 获取扩展参数
     *
     * @param  key 键
     * @return     值
     */
    public Object getExtParam(String key) {
        return extParams == null ? null : extParams.get(key);
    }

    @Override
    public String toString() {
        return "BaseRequest{" + "requestId='" + requestId + '\'' + ", timestamp=" + timestamp + ", source='" + source
                + '\'' + ", clientIp='" + clientIp + '\'' + ", userId=" + userId + ", username='" + username + '\''
                + ", startTime=" + startTime + ", extParams=" + extParams + '}';
    }
}
