package com.linearizability.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 单元测试：{@link CollectionHelper}
 *
 * 每个公开方法均有对应的测试用例，覆盖正常、空数据、异常等场景。
 */
class CollectionHelperTest {

    private static class Dummy {
        private final Integer a;
        private final String b;

        Dummy(Integer a, String b) {
            this.a = a;
            this.b = b;
        }

        Integer getA() {
            return a;
        }

        String getB() {
            return b;
        }
    }

    private List<Dummy> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
        list.add(new Dummy(1, "x"));
        list.add(new Dummy(null, "y"));
        list.add(new Dummy(2, null));
        list.add(null);
        list.add(new Dummy(1, "z"));
    }

    /**
     * 测试 {@link CollectionHelper#extractField(List, Function)}
     *
     * 场景： 1. 传入 null 列表，应返回 null。 2. 传入空列表，应返回 null。
     */
    @Test
    void extractField_nullOrEmpty() {
        assertNull(CollectionHelper.extractField(null, Dummy::getA));
        assertNull(CollectionHelper.extractField(Collections.emptyList(), Dummy::getA));
    }

    /**
     * 验证 extractField 能正确从非空列表中提取第一个非空字段值。
     */
    @Test
    void extractField_firstNonNull() {
        Integer value = CollectionHelper.extractField(list, Dummy::getA);
        assertEquals(1, value);
    }

    /**
     * 测试 extractNonNullFieldOrThrow: - 正常列表能返回第一个非空字段。 - 所有字段为 null 时抛 IllegalArgumentException。 - 传 null 列表也应抛异常。
     */
    @Test
    void extractNonNullFieldOrThrow_successAndFailure() {
        Integer value = CollectionHelper.extractNonNullFieldOrThrow(list, Dummy::getA);
        assertEquals(1, value);

        List<Dummy> allNull = List.of(new Dummy(null, null));
        assertThrows(IllegalArgumentException.class,
                () -> CollectionHelper.extractNonNullFieldOrThrow(allNull, Dummy::getA));

        assertThrows(IllegalArgumentException.class,
                () -> CollectionHelper.extractNonNullFieldOrThrow(null, Dummy::getA));
    }

    /**
     * 验证带索引的 extractField: - null 列表或越界索引均返回 null。 - 合法索引返回对应字段，如果元素为 null 则返回 null。
     */
    @Test
    void extractFieldByIndex() {
        assertNull(CollectionHelper.extractField(null, 0, Dummy::getA));
        assertNull(CollectionHelper.extractField(list, -1, Dummy::getA));
        assertNull(CollectionHelper.extractField(list, 5, Dummy::getA));
        assertEquals(1, CollectionHelper.extractField(list, 0, Dummy::getA));
        assertNull(CollectionHelper.extractField(list, 1, Dummy::getA));
    }

    /**
     * 测试 extractFirstField 的简化行为： - null 列表返回 null。 - 非空列表返回第一个元素的字段。
     */
    @Test
    void extractFirstField() {
        assertNull(CollectionHelper.extractFirstField(null, Dummy::getA));
        assertEquals(1, CollectionHelper.extractFirstField(list, Dummy::getA));
    }

    /**
     * 测试 extractFieldOrThrow(index)： - 列表为 null/空 或 索引无效 会抛出 IllegalArgumentException。 - 指定索引处元素为 null 也会抛出。 -
     * 正常索引返回对应字段。
     */
    @Test
    void extractFieldOrThrow_index() {
        assertThrows(IllegalArgumentException.class, () -> CollectionHelper.extractFieldOrThrow(null, 0, Dummy::getA));
        assertThrows(IllegalArgumentException.class, () -> CollectionHelper.extractFieldOrThrow(list, -1, Dummy::getA));
        assertThrows(IllegalArgumentException.class, () -> CollectionHelper.extractFieldOrThrow(list, 3, Dummy::getA));
        assertThrows(IllegalArgumentException.class,
                () -> CollectionHelper.extractFieldOrThrow(Collections.singletonList(null), 0, Dummy::getA));
        assertEquals(1, CollectionHelper.extractFieldOrThrow(list, 0, Dummy::getA));
    }

    /**
     * 检查 extractFirstFieldOrThrow： - null 列表抛异常。 - 非空列表返回首字段。
     */
    @Test
    void extractFirstFieldOrThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> CollectionHelper.extractFirstFieldOrThrow(null, Dummy::getA));
        assertEquals(1, CollectionHelper.extractFirstFieldOrThrow(list, Dummy::getA));
    }

    /**
     * 组合测试： - extractFieldList 空或正常情况。 - extractNonNullFieldList 排除 null。 - extractDistinctFieldList 去重。 -
     * extractFieldSet 返回集合且去掉 null。
     */
    @Test
    void extractFieldListVariants() {
        assertTrue(CollectionHelper.extractFieldList(null, Dummy::getA).isEmpty());
        assertEquals(Arrays.asList(1, null, 2, 1), CollectionHelper.extractFieldList(list, Dummy::getA));

        assertEquals(Arrays.asList(1, 2, 1), CollectionHelper.extractNonNullFieldList(list, Dummy::getA));
        assertEquals(Arrays.asList(1, 2), CollectionHelper.extractDistinctFieldList(list, Dummy::getA));
        // extractFieldSet 会过滤掉 null 值
        assertEquals(new HashSet<>(Arrays.asList(1, 2)), CollectionHelper.extractFieldSet(list, Dummy::getA));
    }

    /**
     * 测试按条件提取字段： - extractFieldByCondition 找到第一个匹配值。 - 条件不满足返回 null。 - extractFieldListByCondition 返回所有匹配字段值。
     */
    @Test
    void extractFieldByCondition() {
        Predicate<Dummy> hasA1 = d -> d != null && Objects.equals(d.getA(), 1);
        assertEquals("x", CollectionHelper.extractFieldByCondition(list, hasA1, Dummy::getB));
        assertNull(CollectionHelper.extractFieldByCondition(list, d -> false, Dummy::getB));

        assertTrue(CollectionHelper.extractFieldListByCondition(list, hasA1, Dummy::getB).contains("x"));
    }

    /**
     * 复合检测： - extractLastField 对空列表和正常列表。 - extractAndGroupBy 根据键分组并确认某个键对应的值列表大小。 - extractToMap 生成一对一映射，且确认 null
     * 值被忽略。
     */
    @Test
    void extractLastAndGroupAndMap() {
        assertNull(CollectionHelper.extractLastField(null, Dummy::getA));
        assertEquals(1, CollectionHelper.extractLastField(list, Dummy::getA));

        Map<Integer, List<String>> grouped = CollectionHelper.extractAndGroupBy(list, Dummy::getA, Dummy::getB);
        assertEquals(2, grouped.get(1).size());

        Map<Integer, String> map = CollectionHelper.extractToMap(list, Dummy::getA, Dummy::getB);
        assertEquals("x", map.get(1));
        // key=2 对应的 value 为 null，因此整个条目应被忽略
        assertFalse(map.containsKey(2));
    }

    /**
     * 测试 containsFieldValue 和 countFieldValue: - 检查指定字段值是否存在。 - 统计出现次数。
     */
    @Test
    void containsAndCount() {
        assertTrue(CollectionHelper.containsFieldValue(list, Dummy::getA, 1));
        assertFalse(CollectionHelper.containsFieldValue(list, Dummy::getA, 100));
        assertEquals(2, CollectionHelper.countFieldValue(list, Dummy::getA, 1));
    }

    /**
     * 验证 sortByFieldDesc: - 列表复制后排序不会修改原始 list。 - 最大字段值应在前；字段 null 的元素排在倒数第二（最后一个则为 null 元素）。
     */
    @Test
    void sortByFieldDesc() {
        List<Dummy> copy = new ArrayList<>(list);
        CollectionHelper.sortByFieldDesc(copy, Dummy::getA);
        // 2 应该排在最前面；字段为 null 的元素排在后面
        assertEquals(2, copy.get(0).getA());
        assertNull(copy.get(copy.size() - 2).getA());
    }

    /**
     * 测试多字段提取方法： - extractMultipleFields 将所有提取结果合并为列表，允许重复，自动过滤 null。 - extractMultipleFieldsDistinct 进一步去重。 -
     * extractMultipleFieldsToSet 最终转成集合并自动去重。
     *
     * 注：list 包含 [Dummy(1,"x"), Dummy(null,"y"), Dummy(2,null), null, Dummy(1,"z")] 从 getB 提取：["x", "y", null→过滤, "z"] 从
     * getA 转字符串：["1", "null"(String.valueOf(null)), "2", "1"] 合并去重得 6 个不同值：{"x", "y", "z", "1", "null", "2"}
     */
    @Test
    void multipleFieldExtraction() {
        // extractMultipleFields：从两个提取器提取，结果可能有重复，null 被过滤
        List<String> all = CollectionHelper.extractMultipleFields(list, Dummy::getB,
                d -> d == null ? null : String.valueOf(d.getA()));
        // 检查两个提取器都被调用
        assertTrue(all.contains("x"), "应包含 Dummy::getB 的值 'x'");
        assertTrue(all.contains("y"), "应包含 Dummy::getB 的值 'y'");
        assertTrue(all.contains("z"), "应包含 Dummy::getB 的值 'z'");
        assertTrue(all.contains("1"), "应包含字符串化的 getA 值 '1'");
        assertTrue(all.contains("2"), "应包含字符串化的 getA 值 '2'");
        // 原始列表有两个 getA()=1，所以 all 中应有两个 "1"
        assertEquals(2, all.stream().filter("1"::equals).count(), "all 中应有两个重复的 '1'");
        // 多字段方法过滤 null，所以不应包含 null
        assertFalse(all.stream().anyMatch(Objects::isNull), "all 中不应包含 null");

        // extractMultipleFieldsDistinct：去重后列表大小应等于 Set 大小
        List<String> distinct = CollectionHelper.extractMultipleFieldsDistinct(list, Dummy::getB,
                d -> d == null ? null : String.valueOf(d.getA()));
        assertEquals(new HashSet<>(distinct).size(), distinct.size(), "distinct 列表中不应有重复元素");
        // 验证所有非 null 值都被保留
        assertTrue(distinct.contains("x"), "去重后应保留 'x'");
        assertTrue(distinct.contains("y"), "去重后应保留 'y'");
        assertTrue(distinct.contains("z"), "去重后应保留 'z'");
        assertEquals(1, distinct.stream().filter("1"::equals).count(), "去重后只应有一个 '1'");
        assertEquals(1, distinct.stream().filter("2"::equals).count(), "去重后只应有一个 '2'");
        assertFalse(distinct.stream().anyMatch(Objects::isNull), "distinct 中不应包含 null");

        // extractMultipleFieldsToSet：直接转为 Set，自动去重
        Set<String> set = CollectionHelper.extractMultipleFieldsToSet(list, Dummy::getB,
                d -> d == null ? null : String.valueOf(d.getA()));
        assertTrue(set.contains("x"), "Set 中应包含 'x'");
        assertTrue(set.contains("y"), "Set 中应包含 'y'");
        assertTrue(set.contains("z"), "Set 中应包含 'z'");
        assertTrue(set.contains("1"), "Set 中应包含 '1'");
        assertTrue(set.contains("2"), "Set 中应包含 '2'");
        assertFalse(set.stream().anyMatch(Objects::isNull), "Set 中不应包含 null");
        assertEquals(6, set.size(), "Set 应包含 6 个不同的值");
    }

    /**
     * 测试 hasIntersection：检查两个列表是否存在交集（null值不计入）
     */
    @Test
    void hasIntersection() {
        // 正常情况：存在交集
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        assertTrue(CollectionHelper.hasIntersection(list1, list2), "应存在交集");

        // 无交集
        List<Integer> list3 = Arrays.asList(1, 2);
        List<Integer> list4 = Arrays.asList(3, 4);
        assertFalse(CollectionHelper.hasIntersection(list3, list4), "应无交集");

        // null参数
        assertFalse(CollectionHelper.hasIntersection(null, list1), "null列表应返回false");
        assertFalse(CollectionHelper.hasIntersection(list1, null), "null列表应返回false");

        // 空列表
        assertFalse(CollectionHelper.hasIntersection(Collections.emptyList(), list1), "空列表应返回false");

        // 包含null元素：null不计入交集
        List<Integer> listWithNull = Arrays.asList(null, 1, 2);
        List<Integer> listWithOnlyNull = Arrays.asList(null, null);
        assertTrue(CollectionHelper.hasIntersection(listWithNull, list2), "非null元素应计入交集");
        assertFalse(CollectionHelper.hasIntersection(listWithOnlyNull, listWithOnlyNull), "全null列表应无交集");

        // 大小列表优化：小列表转Set，大列表遍历
        List<Integer> smallList = Arrays.asList(1);
        List<Integer> largeList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeList.add(i + 100);
        }
        assertFalse(CollectionHelper.hasIntersection(smallList, largeList), "大小列表无交集");
        largeList.add(1);
        assertTrue(CollectionHelper.hasIntersection(smallList, largeList), "大小列表有交集");
    }

    /**
     * 测试 extractFieldList 带转换器版本：提取字段并进行类型转换
     */
    @Test
    void extractFieldListWithConverter() {
        // 正常情况：Integer -> String 转换（null 字段值被过滤）
        List<String> result = CollectionHelper.extractFieldList(list, Dummy::getA, String::valueOf);
        assertEquals(Arrays.asList("1", "2", "1"), result);

        // null 列表返回空列表
        assertTrue(CollectionHelper.extractFieldList(null, Dummy::getA, String::valueOf).isEmpty());

        // 空列表返回空列表
        assertTrue(CollectionHelper.extractFieldList(Collections.emptyList(), Dummy::getA, String::valueOf).isEmpty());

        // 转换器返回 null 的情况（如空字符串转 Integer）
        List<Dummy> stringList = Arrays.asList(new Dummy(1, "123"), new Dummy(2, ""), new Dummy(3, "456"));
        Function<String, Integer> parseInt = s -> s == null || s.isEmpty() ? null : Integer.parseInt(s);
        List<Integer> parsed = CollectionHelper.extractFieldList(stringList, Dummy::getB, parseInt);
        // 空字符串被转为 null，然后被过滤
        assertEquals(Arrays.asList(123, 456), parsed);

        // 验证 null 元素被过滤（list 包含 null 元素）
        assertEquals(3, result.size(), "应过滤 null 元素和 null 字段值");
    }

    /**
     * 测试 intersection：计算两个列表的交集，保持list1顺序，去重，排除null
     */
    @Test
    void intersection() {
        // 正常情况：保持list1顺序
        List<Integer> list1 = Arrays.asList(3, 1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3);
        List<Integer> result = CollectionHelper.intersection(list1, list2);
        assertEquals(Arrays.asList(3, 2), result, "应保持list1顺序并去重");

        // 无交集
        List<Integer> list3 = Arrays.asList(1, 2);
        List<Integer> list4 = Arrays.asList(3, 4);
        assertTrue(CollectionHelper.intersection(list3, list4).isEmpty(), "无交集应返回空列表");

        // null参数
        assertTrue(CollectionHelper.intersection(null, list1).isEmpty(), "null参数应返回空列表");
        assertTrue(CollectionHelper.intersection(list1, null).isEmpty(), "null参数应返回空列表");

        // 空列表
        assertTrue(CollectionHelper.intersection(Collections.emptyList(), list1).isEmpty(), "空列表应返回空列表");

        // 包含null元素：null被过滤
        List<Integer> listWithNull = Arrays.asList(null, 1, 2, null);
        List<Integer> normalList = Arrays.asList(1, 2, 3);
        List<Integer> resultWithNull = CollectionHelper.intersection(listWithNull, normalList);
        assertEquals(Arrays.asList(1, 2), resultWithNull, "null元素应被过滤");
        assertFalse(resultWithNull.contains(null), "结果中不应包含null");

        // 去重验证
        List<Integer> dupList1 = Arrays.asList(1, 1, 2, 2, 3);
        List<Integer> dupList2 = Arrays.asList(1, 2);
        List<Integer> dedupResult = CollectionHelper.intersection(dupList1, dupList2);
        assertEquals(Arrays.asList(1, 2), dedupResult, "应去重并保持顺序");
        assertEquals(2, dedupResult.size(), "去重后大小应为2");

        // 字符串类型测试
        List<String> strList1 = Arrays.asList("b", "a", "c", "b");
        List<String> strList2 = Arrays.asList("a", "c", "d");
        List<String> strResult = CollectionHelper.intersection(strList1, strList2);
        assertEquals(Arrays.asList("a", "c"), strResult, "字符串类型应保持list1顺序，且'b'不在list2中");
    }
}