package com.linearizability.common.dto;

import com.linearizability.common.base.BaseRequest;

/**
 * 分页请求类
 * 继承自BaseRequest，提供分页相关的通用字段
 *
 * @author ZhangBoyuan
 * @since 2025-11-07
 */
public class PageRequest extends BaseRequest {

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    /**
     * 默认每页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 最大每页大小
     */
    public static final int MAX_PAGE_SIZE = 1000;
    private static final long serialVersionUID = 1L;
    /**
     * 页码，从1开始
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向：ASC 或 DESC
     */
    private String orderDirection;

    /**
     * 是否需要总数
     */
    private Boolean needTotal = true;

    public PageRequest() {
        super();
        this.pageNum = DEFAULT_PAGE_NUM;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public PageRequest(Integer pageNum, Integer pageSize) {
        super();
        this.pageNum = pageNum != null && pageNum > 0 ? pageNum : DEFAULT_PAGE_NUM;
        this.pageSize = pageSize != null && pageSize > 0 ? Math.min(pageSize, MAX_PAGE_SIZE) : DEFAULT_PAGE_SIZE;
    }

    /**
     * 获取页码，确保返回有效值
     *
     * @return 页码
     */
    public Integer getPageNum() {
        if (pageNum == null || pageNum < 1) {
            return DEFAULT_PAGE_NUM;
        }
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * 获取每页大小，确保返回有效值
     *
     * @return 每页大小
     */
    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取排序字段
     *
     * @return 排序字段
     */
    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 获取排序方向
     *
     * @return 排序方向
     */
    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    public Boolean getNeedTotal() {
        return needTotal;
    }

    public void setNeedTotal(Boolean needTotal) {
        this.needTotal = needTotal;
    }

    /**
     * 获取偏移量（用于数据库查询）
     *
     * @return 偏移量
     */
    public Integer getOffset() {
        return (getPageNum() - 1) * getPageSize();
    }

    /**
     * 获取限制数（用于数据库查询）
     *
     * @return 限制数
     */
    public Integer getLimit() {
        return getPageSize();
    }

    /**
     * 判断是否为升序
     *
     * @return true表示升序，false表示降序
     */
    public boolean isAsc() {
        return orderDirection == null || "ASC".equalsIgnoreCase(orderDirection);
    }

    @Override
    public String toString() {
        return "PageRequest{" +
            "pageNum=" + pageNum +
            ", pageSize=" + pageSize +
            ", orderBy='" + orderBy + '\'' +
            ", orderDirection='" + orderDirection + '\'' +
            ", needTotal=" + needTotal +
            "} " + super.toString();
    }
}

