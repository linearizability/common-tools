package com.linearizability.common.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合助手 - 提供集合元素字段提取、转换、分组、排序等功能
 *
 * @author ZhangBoyuan
 * @since  2025-11-06
 */
public class CollectionHelper {

    /**
     * 从列表中提取第一个指定字段值不为空的元素，返回该字段值
     *
     * @param  list           列表
     * @param  fieldExtractor 字段提取器
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                第一个不为空的字段值，如果都为空或列表为空返回null
     */
    public static <T, R> R extractField(List<T> list, Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().filter(Objects::nonNull).map(fieldExtractor).filter(Objects::nonNull).findFirst()
                .orElse(null);
    }

    /**
     * 从列表中提取第一个指定字段值不为空的元素，返回该字段值，找不到抛出异常
     *
     * @param  list                     列表
     * @param  fieldExtractor           字段提取器
     * @param  <T>                      列表元素类型
     * @param  <R>                      字段类型
     * @return                          第一个不为空的字段值
     * @throws IllegalArgumentException 如果列表为空或所有字段值都为空
     */
    public static <T, R> R extractNonNullFieldOrThrow(List<T> list, Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }
        return list.stream().filter(Objects::nonNull).map(fieldExtractor).filter(Objects::nonNull).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("All field values are null"));
    }

    /**
     * 从列表中提取指定索引元素的字段值，找不到返回null
     *
     * @param  list           列表
     * @param  index          索引
     * @param  fieldExtractor 字段提取器
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                字段值，如果列表为空或索引越界返回null
     */
    public static <T, R> R extractField(List<T> list, int index, Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty() || index < 0 || index >= list.size()) {
            return null;
        }
        T element = list.get(index);
        return element == null ? null : fieldExtractor.apply(element);
    }

    /**
     * 从列表中提取第一个元素的字段值，找不到返回null
     *
     * @param  list           列表
     * @param  fieldExtractor 字段提取器
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                字段值，如果列表为空返回null
     */
    public static <T, R> R extractFirstField(List<T> list, Function<T, R> fieldExtractor) {
        return extractField(list, 0, fieldExtractor);
    }

    /**
     * 从列表中提取指定索引元素的字段值，找不到抛出异常
     *
     * @param  list                     列表
     * @param  index                    索引
     * @param  fieldExtractor           字段提取器
     * @param  <T>                      列表元素类型
     * @param  <R>                      字段类型
     * @return                          字段值
     * @throws IllegalArgumentException 如果列表为空或索引越界
     */
    public static <T, R> R extractFieldOrThrow(List<T> list, int index, Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException("Index out of bounds: " + index + ", list size: " + list.size());
        }
        T element = list.get(index);
        if (element == null) {
            throw new IllegalArgumentException("Element at index " + index + " is null");
        }
        return fieldExtractor.apply(element);
    }

    /**
     * 从列表中提取第一个元素的字段值，找不到抛出异常
     *
     * @param  list                     列表
     * @param  fieldExtractor           字段提取器
     * @param  <T>                      列表元素类型
     * @param  <R>                      字段类型
     * @return                          字段值
     * @throws IllegalArgumentException 如果列表为空
     */
    public static <T, R> R extractFirstFieldOrThrow(List<T> list, Function<T, R> fieldExtractor) {
        return extractFieldOrThrow(list, 0, fieldExtractor);
    }

    /**
     * 从列表中提取所有元素的指定字段值，形成新的列表
     *
     * @param  list           列表
     * @param  fieldExtractor 字段提取器
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                字段值列表，如果输入列表为空返回空列表
     */
    public static <T, R> List<R> extractFieldList(List<T> list, Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        // 过滤掉 null 元素并允许 extractor 返回 null
        return list.stream().filter(Objects::nonNull).map(fieldExtractor).collect(Collectors.toList());
    }

    /**
     * 从列表中提取所有非空元素的指定字段值，形成新的列表
     *
     * @param  list           列表
     * @param  fieldExtractor 字段提取器
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                非空字段值列表
     */
    public static <T, R> List<R> extractNonNullFieldList(List<T> list, Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().filter(Objects::nonNull).map(fieldExtractor).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 从列表中提取去重后的字段值列表
     *
     * @param  list           列表
     * @param  fieldExtractor 字段提取器
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                去重后的字段值列表
     */
    public static <T, R> List<R> extractDistinctFieldList(List<T> list, Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().filter(Objects::nonNull).map(fieldExtractor).filter(Objects::nonNull).distinct()
                .collect(Collectors.toList());
    }

    /**
     * 从列表中提取字段值并转换为Set
     *
     * @param  list           列表
     * @param  fieldExtractor 字段提取器
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                字段值Set
     */
    public static <T, R> Set<R> extractFieldSet(List<T> list, Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            return Collections.emptySet();
        }
        return list.stream().filter(Objects::nonNull).map(fieldExtractor).filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * 根据条件从列表中提取字段值
     *
     * @param  list           列表
     * @param  condition      过滤条件
     * @param  fieldExtractor 字段提取器
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                满足条件的字段值列表
     */
    public static <T, R> List<R> extractFieldListByCondition(List<T> list, Predicate<T> condition,
            Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().filter(Objects::nonNull).filter(condition).map(fieldExtractor).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 根据条件从列表中提取第一个匹配的字段值
     *
     * @param  list           列表
     * @param  condition      过滤条件
     * @param  fieldExtractor 字段提取器
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                第一个匹配的字段值，找不到返回null
     */
    public static <T, R> R extractFieldByCondition(List<T> list, Predicate<T> condition,
            Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().filter(Objects::nonNull).filter(condition).map(fieldExtractor).filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    /**
     * 从列表中提取最后一个元素的字段值
     *
     * @param  list           列表
     * @param  fieldExtractor 字段提取器
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                最后一个元素的字段值，如果列表为空返回null
     */
    public static <T, R> R extractLastField(List<T> list, Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return extractField(list, list.size() - 1, fieldExtractor);
    }

    /**
     * 从列表中提取字段值并按指定字段分组
     *
     * @param  list           列表
     * @param  keyExtractor   分组键提取器
     * @param  valueExtractor 值提取器
     * @param  <T>            列表元素类型
     * @param  <K>            分组键类型
     * @param  <V>            值类型
     * @return                分组后的Map
     */
    public static <T, K, V> Map<K, List<V>> extractAndGroupBy(List<T> list, Function<T, K> keyExtractor,
            Function<T, V> valueExtractor) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyMap();
        }
        // 跳过 null 元素或 key 为 null 的条目
        return list.stream().filter(Objects::nonNull)
                .map(e -> new AbstractMap.SimpleEntry<>(keyExtractor.apply(e), valueExtractor.apply(e)))
                .filter(entry -> entry.getKey() != null).collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    /**
     * 从列表中提取字段值并转换为Map（一对一映射）
     *
     * @param  list           列表
     * @param  keyExtractor   键提取器
     * @param  valueExtractor 值提取器
     * @param  <T>            列表元素类型
     * @param  <K>            键类型
     * @param  <V>            值类型
     * @return                键值对Map
     */
    public static <T, K, V> Map<K, V> extractToMap(List<T> list, Function<T, K> keyExtractor,
            Function<T, V> valueExtractor) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyMap();
        }
        return list.stream().filter(Objects::nonNull)
                .map(e -> new AbstractMap.SimpleEntry<>(keyExtractor.apply(e), valueExtractor.apply(e)))
                // 跳过 key 或 value 为 null 的条目
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1));
    }

    /**
     * 检查列表中是否存在指定字段值
     *
     * @param  list           列表
     * @param  fieldExtractor 字段提取器
     * @param  value          要查找的值
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                是否存在
     */
    public static <T, R> boolean containsFieldValue(List<T> list, Function<T, R> fieldExtractor, R value) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        return list.stream().filter(Objects::nonNull).map(fieldExtractor)
                .anyMatch(fieldValue -> Objects.equals(fieldValue, value));
    }

    /**
     * 统计列表中指定字段值的出现次数
     *
     * @param  list           列表
     * @param  fieldExtractor 字段提取器
     * @param  value          要统计的值
     * @param  <T>            列表元素类型
     * @param  <R>            字段类型
     * @return                出现次数
     */
    public static <T, R> long countFieldValue(List<T> list, Function<T, R> fieldExtractor, R value) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return list.stream().filter(Objects::nonNull).map(fieldExtractor)
                .filter(fieldValue -> Objects.equals(fieldValue, value)).count();
    }

    /**
     * 按指定字段降序排序，null值排在最后
     *
     * @param list           列表
     * @param fieldExtractor 字段提取器
     * @param <T>            列表元素类型
     * @param <R>            字段类型（必须实现Comparable接口）
     */
    public static <T, R extends Comparable<R>> void sortByFieldDesc(List<T> list, Function<T, R> fieldExtractor) {
        if (list == null || list.isEmpty()) {
            return;
        }
        // 外层 nullsLast 保障列表中的 null 元素排在末尾
        // 内层比较器对字段进行降序排序，并对字段值为 null 的元素也放在末尾
        list.sort(Comparator
                .nullsLast(Comparator.comparing(fieldExtractor, Comparator.nullsLast(Comparator.reverseOrder()))));
    }

    /**
     * 从列表中提取多个字段值并合并到一个集合中 支持提取相同类型的多个字段值
     *
     * @param  list            列表
     * @param  fieldExtractors 可变参数的字段提取器数组
     * @param  <T>             列表元素类型
     * @param  <R>             字段类型
     * @return                 包含所有字段值的列表
     */
    @SafeVarargs
    public static <T, R> List<R> extractMultipleFields(List<T> list, Function<T, R>... fieldExtractors) {
        if (list == null || list.isEmpty() || fieldExtractors == null || fieldExtractors.length == 0) {
            return Collections.emptyList();
        }

        return list.stream().filter(Objects::nonNull)
                .flatMap(element -> Stream.of(fieldExtractors).filter(Objects::nonNull)
                        .map(extractor -> extractor.apply(element)).filter(Objects::nonNull))
                .collect(Collectors.toList());
    }

    /**
     * 从列表中提取多个字段值并合并到一个去重集合中 支持提取相同类型的多个字段值
     *
     * @param  list            列表
     * @param  fieldExtractors 可变参数的字段提取器数组
     * @param  <T>             列表元素类型
     * @param  <R>             字段类型
     * @return                 包含所有去重字段值的列表
     */
    @SafeVarargs
    public static <T, R> List<R> extractMultipleFieldsDistinct(List<T> list, Function<T, R>... fieldExtractors) {
        if (list == null || list.isEmpty() || fieldExtractors == null || fieldExtractors.length == 0) {
            return Collections.emptyList();
        }

        return list.stream().filter(Objects::nonNull)
                .flatMap(element -> Stream.of(fieldExtractors).filter(Objects::nonNull)
                        .map(extractor -> extractor.apply(element)).filter(Objects::nonNull))
                .distinct().collect(Collectors.toList());
    }

    /**
     * 从列表中提取多个字段值并转换为Set 自动去重，支持提取相同类型的多个字段值
     *
     * @param  list            列表
     * @param  fieldExtractors 可变参数的字段提取器数组
     * @param  <T>             列表元素类型
     * @param  <R>             字段类型
     * @return                 包含所有字段值的Set
     */
    @SafeVarargs
    public static <T, R> Set<R> extractMultipleFieldsToSet(List<T> list, Function<T, R>... fieldExtractors) {
        if (list == null || list.isEmpty() || fieldExtractors == null || fieldExtractors.length == 0) {
            return Collections.emptySet();
        }

        return list.stream().filter(Objects::nonNull)
                .flatMap(element -> Stream.of(fieldExtractors).filter(Objects::nonNull)
                        .map(extractor -> extractor.apply(element)).filter(Objects::nonNull))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
