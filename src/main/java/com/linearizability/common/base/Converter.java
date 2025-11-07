package com.linearizability.common.base;

/**
 * 转换器接口
 * 定义对象转换规范，用于不同对象类型之间的转换
 *
 * @param <S> 源类型
 * @param <T> 目标类型
 * @author ZhangBoyuan
 * @since 2025-11-07
 */
@FunctionalInterface
public interface Converter<S, T> {

    /**
     * 将源对象转换为目标对象
     *
     * @param source 源对象
     * @return 目标对象
     */
    T convert(S source);

    /**
     * 批量转换
     *
     * @param sources 源对象列表
     * @return 目标对象列表
     */
    default java.util.List<T> convertList(java.util.List<S> sources) {
        if (sources == null || sources.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return sources.stream()
            .map(this::convert)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 批量转换（去空）
     *
     * @param sources 源对象列表
     * @return 目标对象列表（过滤掉null值）
     */
    default java.util.List<T> convertListNonNull(java.util.List<S> sources) {
        if (sources == null || sources.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return sources.stream()
            .map(this::convert)
            .filter(java.util.Objects::nonNull)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 反向转换（可选实现）
     * 默认抛出UnsupportedOperationException
     *
     * @param target 目标对象
     * @return 源对象
     * @throws UnsupportedOperationException 如果不支持反向转换
     */
    default S reverse(T target) {
        throw new UnsupportedOperationException("Reverse conversion is not supported");
    }
}
