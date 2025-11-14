package com.linearizability.common.base;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * 基础枚举接口，定义枚举规范 所有业务枚举应实现此接口，提供统一的编码和描述获取方法
 *
 * @param  <V> 编码类型，通常为 Integer 或 String
 * @author     ZhangBoyuan
 * @since      2025-11-07
 */
public interface BaseEnum<V> {

    /**
     * 通用查找方法，根据条件查找枚举
     *
     * @param  type      枚举类型
     * @param  predicate 匹配条件
     * @param  <T>       枚举类型
     * @return           匹配的枚举，未找到返回 Optional.empty()
     */
    static <T extends Enum<T> & BaseEnum<?>> Optional<T> find(Class<T> type, Predicate<T> predicate) {
        if (type == null || predicate == null || !type.isEnum()) {
            return Optional.empty();
        }
        for (T baseEnum : type.getEnumConstants()) {
            if (predicate.test(baseEnum)) {
                return Optional.of(baseEnum);
            }
        }
        return Optional.empty();
    }

    /**
     * 根据编码查找枚举 支持多种匹配方式：== 比较、equals 比较、字符串忽略大小写比较
     *
     * @param  type 枚举类型
     * @param  code 编码
     * @param  <T>  枚举类型
     * @return      匹配的枚举，未找到返回 Optional.empty()
     */
    static <T extends Enum<T> & BaseEnum<?>> Optional<T> findByCode(Class<T> type, Object code) {
        if (code == null || type == null || !type.isEnum()) {
            return Optional.empty();
        }
        return find(type, e -> e.getCode() == code || (e.getCode() != null && e.getCode().equals(code))
                || String.valueOf(e.getCode()).equalsIgnoreCase(String.valueOf(code)));
    }

    /**
     * 根据描述查找枚举（忽略大小写）
     *
     * @param  type 枚举类型
     * @param  desc 描述
     * @param  <T>  枚举类型
     * @return      匹配的枚举，未找到返回 Optional.empty()
     */
    static <T extends Enum<T> & BaseEnum<?>> Optional<T> findByDesc(Class<T> type, String desc) {
        if (desc == null || type == null || !type.isEnum()) {
            return Optional.empty();
        }
        return find(type, e -> e.getDesc() != null && e.getDesc().equalsIgnoreCase(desc));
    }

    /**
     * 根据编码查找枚举，未找到时抛出异常
     *
     * @param  type                     枚举类型
     * @param  code                     编码
     * @param  <T>                      枚举类型
     * @return                          匹配的枚举
     * @throws IllegalArgumentException 未找到匹配的枚举时抛出
     */
    static <T extends Enum<T> & BaseEnum<?>> T findByCodeOrThrow(Class<T> type, Object code) {
        return findByCode(type, code).orElseThrow(() -> new IllegalArgumentException(
                "No enum constant found with code: " + code + " in " + type.getName()));
    }

    /**
     * 根据描述查找枚举，未找到时抛出异常
     *
     * @param  type                     枚举类型
     * @param  desc                     描述
     * @param  <T>                      枚举类型
     * @return                          匹配的枚举
     * @throws IllegalArgumentException 未找到匹配的枚举时抛出
     */
    static <T extends Enum<T> & BaseEnum<?>> T findByDescOrThrow(Class<T> type, String desc) {
        return findByDesc(type, desc).orElseThrow(() -> new IllegalArgumentException(
                "No enum constant found with desc: " + desc + " in " + type.getName()));
    }

    /**
     * 判断编码是否存在
     *
     * @param  type 枚举类型
     * @param  code 编码
     * @param  <T>  枚举类型
     * @return      存在返回 true，否则返回 false
     */
    static <T extends Enum<T> & BaseEnum<?>> boolean exists(Class<T> type, Object code) {
        return findByCode(type, code).isPresent();
    }

    /**
     * 判断描述是否存在
     *
     * @param  type 枚举类型
     * @param  desc 描述
     * @param  <T>  枚举类型
     * @return      存在返回 true，否则返回 false
     */
    static <T extends Enum<T> & BaseEnum<?>> boolean existsByDesc(Class<T> type, String desc) {
        return findByDesc(type, desc).isPresent();
    }

    /**
     * 获取枚举编码
     *
     * @return 枚举编码
     */
    V getCode();

    /**
     * 获取枚举描述
     *
     * @return 枚举描述
     */
    String getDesc();
}
