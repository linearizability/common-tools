package com.linearizability.common.base;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类基类 所有实体类应继承此类，提供通用的实体字段
 *
 * @author ZhangBoyuan
 * @since  2025-11-07
 */
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 是否删除（逻辑删除标记）
     */
    private Boolean deleted = false;

    /**
     * 版本号（乐观锁）
     */
    private Integer version;

    /**
     * 备注
     */
    private String remark;

    public BaseEntity() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 判断是否已删除
     *
     * @return true表示已删除，false表示未删除
     */
    public boolean isDeleted() {
        return deleted != null && deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * 标记为已删除
     */
    public void markAsDeleted() {
        this.deleted = true;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 更新前处理（设置更新时间）
     */
    public void beforeUpdate() {
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 创建前处理（设置创建时间和更新时间）
     */
    public void beforeInsert() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
    }

    @Override
    public String toString() {
        return "BaseEntity{" + "id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime + ", createBy="
                + createBy + ", updateBy=" + updateBy + ", deleted=" + deleted + ", version=" + version + ", remark='"
                + remark + '\'' + '}';
    }
}
