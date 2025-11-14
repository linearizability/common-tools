package com.linearizability.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结果类 继承自Result，用于封装分页查询的响应结果
 *
 * @param  <T> 分页数据类型
 * @author     ZhangBoyuan
 * @since      2025-11-07
 */
public class PageResult<T> extends Result<List<T>> {

    private static final long serialVersionUID = 1L;

    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    public PageResult() {
        super();
    }

    public PageResult(List<T> data, PageInfo pageInfo) {
        super();
        this.setData(data);
        this.pageInfo = pageInfo;
    }

    /**
     * 创建成功的分页响应
     *
     * @param  data     数据列表
     * @param  pageInfo 分页信息
     * @param  <T>      数据类型
     * @return          分页响应结果
     */
    public static <T> PageResult<T> success(List<T> data, PageInfo pageInfo) {
        PageResult<T> result = new PageResult<>();
        result.setCode(SUCCESS_CODE);
        result.setMessage("查询成功");
        result.setData(data);
        result.setPageInfo(pageInfo);
        result.setSuccess(true);
        return result;
    }

    /**
     * 创建成功的分页响应（根据PageRequest自动计算分页信息）
     *
     * @param  data        数据列表
     * @param  total       总记录数
     * @param  pageRequest 分页请求
     * @param  <T>         数据类型
     * @return             分页响应结果
     */
    public static <T> PageResult<T> success(List<T> data, Long total, PageRequest pageRequest) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageRequest.getPageNum());
        pageInfo.setPageSize(pageRequest.getPageSize());
        pageInfo.setTotal(total);
        pageInfo.setTotalPages((long) Math.ceil((double) total / pageRequest.getPageSize()));
        return success(data, pageInfo);
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "PageResult{" + "pageInfo=" + pageInfo + "} " + super.toString();
    }

    /**
     * 分页信息内部类
     */
    public static class PageInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 当前页码
         */
        private Integer pageNum;

        /**
         * 每页大小
         */
        private Integer pageSize;

        /**
         * 总记录数
         */
        private Long total;

        /**
         * 总页数
         */
        private Long totalPages;

        /**
         * 是否有上一页
         */
        private Boolean hasPrevious;

        /**
         * 是否有下一页
         */
        private Boolean hasNext;

        public PageInfo() {
        }

        public PageInfo(Integer pageNum, Integer pageSize, Long total) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.total = total;
            this.totalPages = (long) Math.ceil((double) total / pageSize);
            this.hasPrevious = pageNum > 1;
            this.hasNext = pageNum < totalPages;
        }

        public Integer getPageNum() {
            return pageNum;
        }

        public void setPageNum(Integer pageNum) {
            this.pageNum = pageNum;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Long getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Long totalPages) {
            this.totalPages = totalPages;
        }

        public Boolean getHasPrevious() {
            return hasPrevious;
        }

        public void setHasPrevious(Boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
        }

        public Boolean getHasNext() {
            return hasNext;
        }

        public void setHasNext(Boolean hasNext) {
            this.hasNext = hasNext;
        }

        @Override
        public String toString() {
            return "PageInfo{" + "pageNum=" + pageNum + ", pageSize=" + pageSize + ", total=" + total + ", totalPages="
                    + totalPages + ", hasPrevious=" + hasPrevious + ", hasNext=" + hasNext + '}';
        }
    }
}
