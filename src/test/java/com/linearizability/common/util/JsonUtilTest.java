package com.linearizability.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.linearizability.common.util.JsonUtil.KeyContext;

/**
 * 单元测试：{@link JsonUtil}
 *
 * 覆盖复杂嵌套JSON Key查找功能的所有方法。
 */
class JsonUtilTest {

    // 复杂嵌套JSON：多层数组+对象嵌套
    private static final String COMPLEX_NESTED_JSON = """
            {
              "departments": [
                {
                  "deptName": "技术部",
                  "employees": [
                    {
                      "empId": "E001",
                      "empName": "张三",
                      "projects": [
                        {"projId": "P100", "projName": "核心系统", "tasks": [
                          {"taskId": "T1", "taskName": "架构设计", "status": "完成"},
                          {"taskId": "T2", "taskName": "编码开发", "status": "进行中"}
                        ]},
                        {"projId": "P101", "projName": "数据中台", "tasks": [
                          {"taskId": "T3", "taskName": "数据建模", "status": "进行中"}
                        ]}
                      ]
                    },
                    {
                      "empId": "E002",
                      "empName": "李四",
                      "projects": [
                        {"projId": "P102", "projName": "移动端APP", "tasks": []}
                      ]
                    }
                  ]
                },
                {
                  "deptName": "产品部",
                  "employees": [
                    {
                      "empId": "E003",
                      "empName": "王五",
                      "projects": []
                    }
                  ]
                }
              ]
            }
            """;

    // 简单JSON
    private static final String SIMPLE_JSON = """
            {
              "name": "张三",
              "age": 25,
              "address": {
                "city": "北京",
                "zip": "100000"
              }
            }
            """;

    // 数组嵌套JSON
    private static final String ARRAY_NESTED_JSON = """
            {
              "items": [
                {"id": 1, "name": "item1", "tags": ["a", "b"]},
                {"id": 2, "name": "item2", "tags": ["c"]},
                {"id": 3, "name": "item3", "tags": []}
              ]
            }
            """;

    private String complexJson;
    private String simpleJson;
    private String arrayJson;

    @BeforeEach
    void setUp() {
        complexJson = COMPLEX_NESTED_JSON;
        simpleJson = SIMPLE_JSON;
        arrayJson = ARRAY_NESTED_JSON;
    }

    // ==================== findAll 测试 ====================

    @Test
    void findAll_nullOrEmpty() {
        assertTrue(JsonUtil.findAll(null, "key").isEmpty());
        assertTrue(JsonUtil.findAll("", "key").isEmpty());
        assertTrue(JsonUtil.findAll("{}", null).isEmpty());
        assertTrue(JsonUtil.findAll("{}", "").isEmpty());
    }

    @Test
    void findAll_simpleJson() {
        List<Object> names = JsonUtil.findAll(simpleJson, "name");
        assertEquals(1, names.size());
        assertEquals("张三", names.get(0));
    }

    @Test
    void findAll_complexNested() {
        // 查找所有empName（跨越多层数组）
        List<Object> empNames = JsonUtil.findAll(complexJson, "empName");
        assertEquals(3, empNames.size());
        assertEquals("张三", empNames.get(0));
        assertEquals("李四", empNames.get(1));
        assertEquals("王五", empNames.get(2));

        // 查找所有taskName
        List<Object> taskNames = JsonUtil.findAll(complexJson, "taskName");
        assertEquals(3, taskNames.size());
        assertEquals("架构设计", taskNames.get(0));
        assertEquals("编码开发", taskNames.get(1));
        assertEquals("数据建模", taskNames.get(2));
    }

    @Test
    void findAll_duplicateKeys() {
        // 查找所有status（多个任务有status字段）
        List<Object> statuses = JsonUtil.findAll(complexJson, "status");
        assertEquals(3, statuses.size());
    }

    // ==================== findAllWithPaths 测试 ====================

    @Test
    void findAllWithPaths_nullOrEmpty() {
        assertTrue(JsonUtil.findAllWithPaths(null, "key").isEmpty());
        assertTrue(JsonUtil.findAllWithPaths("", "key").isEmpty());
    }

    @Test
    void findAllWithPaths_simpleJson() {
        Map<String, Object> results = JsonUtil.findAllWithPaths(simpleJson, "city");
        assertEquals(1, results.size());
        assertTrue(results.containsKey("address.city"));
        assertEquals("北京", results.get("address.city"));
    }

    @Test
    void findAllWithPaths_complexNested() {
        Map<String, Object> results = JsonUtil.findAllWithPaths(complexJson, "taskName");
        assertEquals(3, results.size());

        // 验证路径格式
        Set<String> paths = results.keySet();
        assertTrue(paths.stream().anyMatch(p -> p.contains("departments[0]") && p.contains("employees[0]")
                && p.contains("projects[0]") && p.contains("tasks[0]")));
    }

    // ==================== findByPattern 测试 ====================

    @Test
    void findByPattern_nullOrEmpty() {
        assertTrue(JsonUtil.findByPattern(null, "*").isEmpty());
        assertTrue(JsonUtil.findByPattern("", "*").isEmpty());
        assertTrue(JsonUtil.findByPattern("{}", null).isEmpty());
    }

    @Test
    void findByPattern_wildcard() {
        // 使用通配符查找所有员工姓名
        List<Object> empNames = JsonUtil.findByPattern(complexJson, "departments.*.employees.*.empName");
        assertEquals(3, empNames.size());
    }

    @Test
    void findByPattern_exactPath() {
        // 精确路径查找部门名称
        List<Object> deptNames = JsonUtil.findByPattern(complexJson, "departments.*.deptName");
        assertEquals(2, deptNames.size());
        assertEquals("技术部", deptNames.get(0));
        assertEquals("产品部", deptNames.get(1));
    }

    // ==================== findByPatternRecursive 测试 ====================

    @Test
    void findByPatternRecursive_nullOrEmpty() {
        assertTrue(JsonUtil.findByPatternRecursive(null, "**.key").isEmpty());
        assertTrue(JsonUtil.findByPatternRecursive("", "**.key").isEmpty());
    }

    @Test
    void findByPatternRecursive_invalidPattern() {
        assertThrows(IllegalArgumentException.class,
                () -> JsonUtil.findByPatternRecursive(complexJson, "invalid.pattern"));
    }

    @Test
    void findByPatternRecursive_recursiveSearch() {
        // 递归查找所有taskName（无视层级）
        List<String> taskNames = JsonUtil.findByPatternRecursive(complexJson, "**.taskName");
        assertEquals(3, taskNames.size());
        assertTrue(taskNames.contains("架构设计"));
        assertTrue(taskNames.contains("编码开发"));
        assertTrue(taskNames.contains("数据建模"));
    }

    @Test
    void findByPatternRecursive_allStatus() {
        List<String> statuses = JsonUtil.findByPatternRecursive(complexJson, "**.status");
        assertEquals(3, statuses.size());
    }

    // ==================== findWhere 测试 ====================

    @Test
    void findWhere_nullOrEmpty() {
        assertTrue(JsonUtil.findWhere(null, "key", m -> true).isEmpty());
        assertTrue(JsonUtil.findWhere("", "key", m -> true).isEmpty());
        assertTrue(JsonUtil.findWhere("{}", null, m -> true).isEmpty());
        assertTrue(JsonUtil.findWhere("{}", "key", (java.util.function.Predicate<Map<String, Object>>) null).isEmpty());
    }

    @Test
    void findWhere_withPredicate() {
        // 查找所有进行中的任务
        List<Map<String, Object>> activeTasks = JsonUtil.findWhere(complexJson, "tasks", task -> {
            Object status = task.get("status");
            return "进行中".equals(status);
        });
        assertEquals(2, activeTasks.size());
    }

    @Test
    void findWhere_withConditions() {
        // 使用多条件查找
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("status", "完成");

        List<Map<String, Object>> completedTasks = JsonUtil.findWhere(complexJson, "tasks", conditions);
        assertEquals(1, completedTasks.size());
        assertEquals("架构设计", completedTasks.get(0).get("taskName"));
    }

    @Test
    void findWhere_noMatch() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("status", "不存在的状态");

        List<Map<String, Object>> results = JsonUtil.findWhere(complexJson, "tasks", conditions);
        assertTrue(results.isEmpty());
    }

    // ==================== findAtDepth 测试 ====================

    @Test
    void findAtDepth_nullOrEmpty() {
        assertTrue(JsonUtil.findAtDepth(null, "key", 0).isEmpty());
        assertTrue(JsonUtil.findAtDepth("", "key", 0).isEmpty());
        assertTrue(JsonUtil.findAtDepth("{}", "key", -1).isEmpty());
    }

    @Test
    void findAtDepth_differentDepths() {
        // depth 0: 根节点的直接子节点
        List<Object> depth0 = JsonUtil.findAtDepth(complexJson, "departments", 0);
        assertEquals(1, depth0.size()); // departments数组本身

        // depth 1: departments下的子节点
        List<Object> depth1 = JsonUtil.findAtDepth(complexJson, "deptName", 1);
        assertEquals(2, depth1.size());
    }

    // ==================== getPathsToKey 测试 ====================

    @Test
    void getPathsToKey_nullOrEmpty() {
        assertTrue(JsonUtil.getPathsToKey(null, "key").isEmpty());
        assertTrue(JsonUtil.getPathsToKey("", "key").isEmpty());
    }

    @Test
    void getPathsToKey_simple() {
        Set<String> paths = JsonUtil.getPathsToKey(simpleJson, "city");
        assertEquals(1, paths.size());
        assertTrue(paths.contains("address.city"));
    }

    @Test
    void getPathsToKey_complex() {
        Set<String> paths = JsonUtil.getPathsToKey(complexJson, "taskName");
        assertEquals(3, paths.size());
        // 所有路径都应该包含tasks数组索引
        assertTrue(paths.stream().allMatch(p -> p.contains("tasks[")));
    }

    // ==================== flatten 测试 ====================

    @Test
    void flatten_nullOrEmpty() {
        assertTrue(JsonUtil.flatten(null, "key").isEmpty());
        assertTrue(JsonUtil.flatten("", "key").isEmpty());
        assertTrue(JsonUtil.flatten("{}", (String[]) null).isEmpty());
        assertTrue(JsonUtil.flatten("{}", new String[0]).isEmpty());
    }

    @Test
    void flatten_simplePath() {
        // 扁平化items数组
        List<Map<String, Object>> items = JsonUtil.flatten(arrayJson, "items");
        assertEquals(3, items.size());
        assertEquals(1, items.get(0).get("id"));
        assertEquals("item1", items.get(0).get("name"));
    }

    @Test
    void flatten_complexPath() {
        // 扁平化所有员工
        List<Map<String, Object>> employees = JsonUtil.flatten(complexJson, "departments", "employees");
        assertEquals(3, employees.size());
        assertEquals("E001", employees.get(0).get("empId"));
        assertEquals("张三", employees.get(0).get("empName"));
    }

    @Test
    void flatten_nestedPath() {
        // 扁平化所有项目
        List<Map<String, Object>> projects = JsonUtil.flatten(complexJson, "departments", "employees", "projects");
        assertEquals(3, projects.size()); // P100, P101, P102
    }

    // ==================== toTable 测试 ====================

    @Test
    void toTable_nullOrEmpty() {
        assertTrue(JsonUtil.toTable(null, new String[]{"key"}, new String[]{"col"}).isEmpty());
        assertTrue(JsonUtil.toTable("", new String[]{"key"}, new String[]{"col"}).isEmpty());
    }

    @Test
    void toTable_simple() {
        // 将items转为表格
        List<Map<String, Object>> table = JsonUtil.toTable(arrayJson, new String[]{"items"},
                new String[]{"id", "name"});

        assertEquals(3, table.size());
        assertEquals(1, table.get(0).get("id"));
        assertEquals("item1", table.get(0).get("name"));
        assertNull(table.get(0).get("tags")); // tags不在列中
    }

    @Test
    void toTable_complex() {
        // 将所有任务转为表格
        List<Map<String, Object>> table = JsonUtil.toTable(complexJson,
                new String[]{"departments", "employees", "projects", "tasks"},
                new String[]{"taskId", "taskName", "status"});

        assertEquals(3, table.size());
        assertEquals("T1", table.get(0).get("taskId"));
        assertEquals("架构设计", table.get(0).get("taskName"));
        assertEquals("完成", table.get(0).get("status"));
    }

    // ==================== findWithContext 测试 ====================

    @Test
    void findWithContext_nullOrEmpty() {
        assertTrue(JsonUtil.findWithContext(null, "key").isEmpty());
        assertTrue(JsonUtil.findWithContext("", "key").isEmpty());
        assertTrue(JsonUtil.findWithContext("{}", null).isEmpty());
    }

    @Test
    void findWithContext_simple() {
        List<KeyContext<String>> contexts = JsonUtil.findWithContext(complexJson, "taskName", "deptName", "empName");

        assertEquals(3, contexts.size());

        // 验证第一个任务的上下文
        KeyContext<String> first = contexts.get(0);
        assertEquals("架构设计", first.getValue());
        assertEquals("技术部", first.getAncestors().get("deptName"));
        assertEquals("张三", first.getAncestors().get("empName"));
        assertNotNull(first.getFullPath());
    }

    @Test
    void findWithContext_noAncestors() {
        // 不指定祖先key
        List<KeyContext<String>> contexts = JsonUtil.findWithContext(complexJson, "taskName");

        assertEquals(3, contexts.size());
        // 祖先信息应该为空
        assertTrue(contexts.get(0).getAncestors().isEmpty());
    }

    @Test
    void findWithContext_partialAncestors() {
        // 只指定部分祖先key
        List<KeyContext<String>> contexts = JsonUtil.findWithContext(complexJson, "taskName", "deptName");

        assertEquals(3, contexts.size());
        assertEquals("技术部", contexts.get(0).getAncestors().get("deptName"));
        assertNull(contexts.get(0).getAncestors().get("empName")); // 未指定
    }

    // ==================== KeyContext 测试 ====================

    @Test
    void keyContext_gettersAndSetters() {
        KeyContext<String> context = new KeyContext<>();
        context.setValue("testValue");
        context.setFullPath("path.to.value");
        Map<String, Object> ancestors = new HashMap<>();
        ancestors.put("parent", "parentValue");
        context.setAncestors(ancestors);

        assertEquals("testValue", context.getValue());
        assertEquals("path.to.value", context.getFullPath());
        assertEquals("parentValue", context.getAncestors().get("parent"));
    }

    @Test
    void keyContext_toString() {
        KeyContext<String> context = new KeyContext<>();
        context.setValue("test");
        context.setFullPath("path");
        context.setAncestors(new HashMap<>());

        String str = context.toString();
        assertTrue(str.contains("test"));
        assertTrue(str.contains("path"));
    }

    // ==================== 边界场景测试 ====================

    @Test
    void edgeCase_emptyArrays() {
        String jsonWithEmptyArray = """
                {"items": [], "name": "test"}
                """;
        List<Object> results = JsonUtil.findAll(jsonWithEmptyArray, "id");
        assertTrue(results.isEmpty());
    }

    @Test
    void edgeCase_deeplyNested() {
        String deepJson = """
                {"a": {"b": {"c": {"d": {"e": "deepValue"}}}}}
                """;
        List<Object> results = JsonUtil.findAll(deepJson, "e");
        assertEquals(1, results.size());
        assertEquals("deepValue", results.get(0));
    }

    @Test
    void edgeCase_mixedTypes() {
        String mixedJson = """
                {
                  "string": "text",
                  "number": 42,
                  "boolean": true,
                  "null": null,
                  "array": [1, 2, 3],
                  "object": {"key": "value"}
                }
                """;

        Map<String, Object> results = JsonUtil.findAllWithPaths(mixedJson, "string");
        assertEquals("text", results.get("string"));

        results = JsonUtil.findAllWithPaths(mixedJson, "number");
        assertEquals(42, results.get("number"));

        results = JsonUtil.findAllWithPaths(mixedJson, "boolean");
        assertEquals(true, results.get("boolean"));
    }

    @Test
    void edgeCase_specialCharactersInKey() {
        String specialJson = """
                {"key-with-dash": "value1", "key_with_underscore": "value2"}
                """;
        List<Object> results1 = JsonUtil.findAll(specialJson, "key-with-dash");
        assertEquals(1, results1.size());
        assertEquals("value1", results1.get(0));

        List<Object> results2 = JsonUtil.findAll(specialJson, "key_with_underscore");
        assertEquals(1, results2.size());
        assertEquals("value2", results2.get(0));
    }
}
