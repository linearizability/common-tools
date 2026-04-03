package com.linearizability.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

/**
 * JSON工具类 提供JSON序列化和反序列化的常用方法
 *
 * @author ZhangBoyuan
 * @since  2025-11-07
 */
public class JsonUtil {

    /**
     * 默认ObjectMapper实例（线程安全）
     */
    private static final ObjectMapper DEFAULT_MAPPER = createDefaultMapper();

    /**
     * 格式化输出的ObjectMapper实例（线程安全）
     */
    private static final ObjectMapper PRETTY_MAPPER = createPrettyMapper();

    /**
     * JSONPath配置（使用Jackson作为JSON提供者）
     */
    private static final Configuration JSONPATH_CONFIG = Configuration.builder().jsonProvider(new JacksonJsonProvider())
            .mappingProvider(new JacksonMappingProvider()).options(Option.SUPPRESS_EXCEPTIONS).build();

    /**
     * JSONPath解析上下文（线程安全）
     */
    private static final ParseContext JSONPATH_PARSE_CONTEXT = JsonPath.using(JSONPATH_CONFIG);

    /**
     * 创建默认ObjectMapper
     *
     * @return ObjectMapper实例
     */
    private static ObjectMapper createDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 注册Java 8时间模块
        mapper.registerModule(new JavaTimeModule());
        // 忽略未知属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略空值
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 日期时间格式化为字符串
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    /**
     * 创建格式化输出的ObjectMapper
     *
     * @return ObjectMapper实例
     */
    private static ObjectMapper createPrettyMapper() {
        ObjectMapper mapper = createDefaultMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    /**
     * 对象转JSON字符串
     *
     * @param  obj              待转换的对象
     * @return                  JSON字符串
     * @throws RuntimeException 转换失败时抛出
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return DEFAULT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON失败: " + e.getMessage(), e);
        }
    }

    /**
     * 对象转JSON字符串（格式化输出）
     *
     * @param  obj              待转换的对象
     * @return                  格式化后的JSON字符串
     * @throws RuntimeException 转换失败时抛出
     */
    public static String toPrettyJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return PRETTY_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON失败: " + e.getMessage(), e);
        }
    }

    /**
     * JSON字符串转对象
     *
     * @param  json             JSON字符串
     * @param  clazz            目标类型
     * @param  <T>              目标类型
     * @return                  转换后的对象
     * @throws RuntimeException 转换失败时抛出
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return DEFAULT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转对象失败: " + e.getMessage(), e);
        }
    }

    /**
     * JSON字符串转对象（支持泛型）
     *
     * @param  json             JSON字符串
     * @param  typeReference    类型引用
     * @param  <T>              目标类型
     * @return                  转换后的对象
     * @throws RuntimeException 转换失败时抛出
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return DEFAULT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转对象失败: " + e.getMessage(), e);
        }
    }

    /**
     * JSON字符串转List
     *
     * @param  json             JSON字符串
     * @param  clazz            List元素类型
     * @param  <T>              元素类型
     * @return                  List对象
     * @throws RuntimeException 转换失败时抛出
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return DEFAULT_MAPPER.readValue(json,
                    DEFAULT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转List失败: " + e.getMessage(), e);
        }
    }

    /**
     * JSON字符串转Map
     *
     * @param  json             JSON字符串
     * @return                  Map对象
     * @throws RuntimeException 转换失败时抛出
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        return fromJson(json, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * 从输入流读取JSON并转换为对象
     *
     * @param  inputStream      输入流
     * @param  clazz            目标类型
     * @param  <T>              目标类型
     * @return                  转换后的对象
     * @throws RuntimeException 转换失败时抛出
     */
    public static <T> T fromJson(InputStream inputStream, Class<T> clazz) {
        if (inputStream == null) {
            return null;
        }
        try {
            return DEFAULT_MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new RuntimeException("从输入流读取JSON失败: " + e.getMessage(), e);
        }
    }

    /**
     * 对象转JSON字节数组
     *
     * @param  obj              待转换的对象
     * @return                  JSON字节数组
     * @throws RuntimeException 转换失败时抛出
     */
    public static byte[] toJsonBytes(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return DEFAULT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON字节数组失败: " + e.getMessage(), e);
        }
    }

    /**
     * JSON字节数组转对象
     *
     * @param  bytes            JSON字节数组
     * @param  clazz            目标类型
     * @param  <T>              目标类型
     * @return                  转换后的对象
     * @throws RuntimeException 转换失败时抛出
     */
    public static <T> T fromJson(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return DEFAULT_MAPPER.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON字节数组转对象失败: " + e.getMessage(), e);
        }
    }

    /**
     * 对象转JsonNode
     *
     * @param  obj              待转换的对象
     * @return                  JsonNode对象
     * @throws RuntimeException 转换失败时抛出
     */
    public static JsonNode toJsonNode(Object obj) {
        if (obj == null) {
            return null;
        }
        return DEFAULT_MAPPER.valueToTree(obj);
    }

    /**
     * JSON字符串转JsonNode
     *
     * @param  json             JSON字符串
     * @return                  JsonNode对象
     * @throws RuntimeException 转换失败时抛出
     */
    public static JsonNode parseJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return DEFAULT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析JSON失败: " + e.getMessage(), e);
        }
    }

    /**
     * 验证JSON字符串是否有效
     *
     * @param  json JSON字符串
     * @return      true表示有效，false表示无效
     */
    public static boolean isValid(String json) {
        if (json == null || json.trim().isEmpty()) {
            return false;
        }
        try {
            DEFAULT_MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    /**
     * 对象转Map
     *
     * @param  obj              待转换的对象
     * @return                  Map对象
     * @throws RuntimeException 转换失败时抛出
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return DEFAULT_MAPPER.convertValue(obj, Map.class);
    }

    /**
     * Map转对象
     *
     * @param  map              Map对象
     * @param  clazz            目标类型
     * @param  <T>              目标类型
     * @return                  转换后的对象
     * @throws RuntimeException 转换失败时抛出
     */
    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        return DEFAULT_MAPPER.convertValue(map, clazz);
    }

    /**
     * 深拷贝对象（通过JSON序列化和反序列化）
     *
     * @param  obj              待拷贝的对象
     * @param  clazz            目标类型
     * @param  <T>              目标类型
     * @return                  拷贝后的对象
     * @throws RuntimeException 拷贝失败时抛出
     */
    public static <T> T deepClone(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        String json = toJson(obj);
        return fromJson(json, clazz);
    }

    /**
     * 深拷贝对象（保持原类型）
     *
     * @param  obj              待拷贝的对象
     * @param  <T>              目标类型
     * @return                  拷贝后的对象
     * @throws RuntimeException 拷贝失败时抛出
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepClone(T obj) {
        if (obj == null) {
            return null;
        }
        String json = toJson(obj);
        return (T) fromJson(json, obj.getClass());
    }

    /**
     * 获取ObjectMapper实例（用于高级用法）
     *
     * @return ObjectMapper实例
     */
    public static ObjectMapper getMapper() {
        return DEFAULT_MAPPER;
    }

    /**
     * 获取格式化输出的ObjectMapper实例
     *
     * @return ObjectMapper实例
     */
    public static ObjectMapper getPrettyMapper() {
        return PRETTY_MAPPER;
    }

    // ==================== JSONPath 相关方法 ====================

    /**
     * 读取JSONPath路径的值
     *
     * @param  json             JSON字符串或对象
     * @param  jsonPath         JSONPath表达式（如：$.user.name）
     * @param  <T>              返回值类型
     * @return                  路径对应的值，如果路径不存在返回null
     * @throws RuntimeException 解析失败时抛出
     */
    public static <T> T readPath(Object json, String jsonPath) {
        if (json == null || jsonPath == null || jsonPath.trim().isEmpty()) {
            return null;
        }
        try {
            DocumentContext context = JSONPATH_PARSE_CONTEXT.parse(json);
            return context.read(jsonPath);
        } catch (Exception e) {
            throw new RuntimeException("读取JSONPath失败: " + e.getMessage(), e);
        }
    }

    /**
     * 读取JSONPath路径的值（指定返回类型）
     *
     * @param  json             JSON字符串或对象
     * @param  jsonPath         JSONPath表达式
     * @param  clazz            返回类型
     * @param  <T>              返回值类型
     * @return                  路径对应的值，如果路径不存在返回null
     * @throws RuntimeException 解析失败时抛出
     */
    public static <T> T readPath(Object json, String jsonPath, Class<T> clazz) {
        if (json == null || jsonPath == null || jsonPath.trim().isEmpty()) {
            return null;
        }
        try {
            DocumentContext context = JSONPATH_PARSE_CONTEXT.parse(json);
            return context.read(jsonPath, clazz);
        } catch (Exception e) {
            throw new RuntimeException("读取JSONPath失败: " + e.getMessage(), e);
        }
    }

    /**
     * 读取JSONPath路径的值（支持泛型）
     *
     * @param  json             JSON字符串或对象
     * @param  jsonPath         JSONPath表达式
     * @param  typeRef          类型引用
     * @param  <T>              返回值类型
     * @return                  路径对应的值，如果路径不存在返回null
     * @throws RuntimeException 解析失败时抛出
     */
    public static <T> T readPath(Object json, String jsonPath, TypeRef<T> typeRef) {
        if (json == null || jsonPath == null || jsonPath.trim().isEmpty()) {
            return null;
        }
        try {
            DocumentContext context = JSONPATH_PARSE_CONTEXT.parse(json);
            return context.read(jsonPath, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("读取JSONPath失败: " + e.getMessage(), e);
        }
    }

    /**
     * 读取JSONPath路径的值列表
     *
     * @param  json             JSON字符串或对象
     * @param  jsonPath         JSONPath表达式
     * @param  clazz            列表元素类型
     * @param  <T>              元素类型
     * @return                  值列表，如果路径不存在返回空列表
     * @throws RuntimeException 解析失败时抛出
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readPathList(Object json, String jsonPath, Class<T> clazz) {
        if (json == null || jsonPath == null || jsonPath.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        try {
            DocumentContext context = JSONPATH_PARSE_CONTEXT.parse(json);
            List<Object> list = context.read(jsonPath);
            if (list == null) {
                return new java.util.ArrayList<>();
            }
            List<T> result = new java.util.ArrayList<>();
            for (Object item : list) {
                if (item instanceof Map) {
                    result.add(fromMap((Map<String, Object>) item, clazz));
                } else {
                    result.add((T) item);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("读取JSONPath列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 检查JSONPath路径是否存在
     *
     * @param  json     JSON字符串或对象
     * @param  jsonPath JSONPath表达式
     * @return          true表示路径存在，false表示不存在
     */
    public static boolean isPathExists(Object json, String jsonPath) {
        if (json == null || jsonPath == null || jsonPath.trim().isEmpty()) {
            return false;
        }
        try {
            DocumentContext context = JSONPATH_PARSE_CONTEXT.parse(json);
            Object result = context.read(jsonPath);
            return result != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置JSONPath路径的值
     *
     * @param  json             JSON字符串或对象
     * @param  jsonPath         JSONPath表达式
     * @param  value            要设置的值
     * @return                  修改后的JSON字符串
     * @throws RuntimeException 设置失败时抛出
     */
    public static String setPath(Object json, String jsonPath, Object value) {
        if (json == null || jsonPath == null || jsonPath.trim().isEmpty()) {
            return null;
        }
        try {
            DocumentContext context = JSONPATH_PARSE_CONTEXT.parse(json);
            context.set(jsonPath, value);
            return context.jsonString();
        } catch (Exception e) {
            throw new RuntimeException("设置JSONPath失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除JSONPath路径的值
     *
     * @param  json             JSON字符串或对象
     * @param  jsonPath         JSONPath表达式
     * @return                  修改后的JSON字符串
     * @throws RuntimeException 删除失败时抛出
     */
    public static String deletePath(Object json, String jsonPath) {
        if (json == null || jsonPath == null || jsonPath.trim().isEmpty()) {
            return null;
        }
        try {
            DocumentContext context = JSONPATH_PARSE_CONTEXT.parse(json);
            context.delete(jsonPath);
            return context.jsonString();
        } catch (Exception e) {
            throw new RuntimeException("删除JSONPath失败: " + e.getMessage(), e);
        }
    }

    /**
     * 添加值到JSONPath路径（用于数组）
     *
     * @param  json             JSON字符串或对象
     * @param  jsonPath         JSONPath表达式
     * @param  value            要添加的值
     * @return                  修改后的JSON字符串
     * @throws RuntimeException 添加失败时抛出
     */
    public static String addPath(Object json, String jsonPath, Object value) {
        if (json == null || jsonPath == null || jsonPath.trim().isEmpty()) {
            return null;
        }
        try {
            DocumentContext context = JSONPATH_PARSE_CONTEXT.parse(json);
            context.add(jsonPath, value);
            return context.jsonString();
        } catch (Exception e) {
            throw new RuntimeException("添加JSONPath失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将JSONPath路径的值转换为对象
     *
     * @param  json             JSON字符串或对象
     * @param  jsonPath         JSONPath表达式
     * @param  clazz            目标类型
     * @param  <T>              目标类型
     * @return                  转换后的对象
     * @throws RuntimeException 转换失败时抛出
     */
    @SuppressWarnings("unchecked")
    public static <T> T readPathAsObject(Object json, String jsonPath, Class<T> clazz) {
        Object value = readPath(json, jsonPath);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return fromJson((String) value, clazz);
        }
        return fromMap((Map<String, Object>) value, clazz);
    }

    // ==================== 复杂嵌套JSON Key查找方法 ====================

    /**
     * 递归查找所有指定key的值（无视层级和数组嵌套）
     *
     * @param  json JSON字符串
     * @param  key  要查找的key
     * @param  <T>  返回值类型
     * @return      所有匹配值的列表，未找到返回空列表
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> findAll(String json, String key) {
        if (json == null || json.trim().isEmpty() || key == null || key.trim().isEmpty()) {
            return new ArrayList<>();
        }
        JsonNode rootNode = parseJson(json);
        if (rootNode == null) {
            return new ArrayList<>();
        }
        List<T> results = new ArrayList<>();
        findAllRecursive(rootNode, key, results);
        return results;
    }

    /**
     * 递归查找所有指定key的值，并返回带完整路径的映射
     *
     * @param  json JSON字符串
     * @param  key  要查找的key
     * @return      路径到值的映射，路径格式如：departments[0].employees[1].name
     */
    public static Map<String, Object> findAllWithPaths(String json, String key) {
        if (json == null || json.trim().isEmpty() || key == null || key.trim().isEmpty()) {
            return new LinkedHashMap<>();
        }
        JsonNode rootNode = parseJson(json);
        if (rootNode == null) {
            return new LinkedHashMap<>();
        }
        Map<String, Object> results = new LinkedHashMap<>();
        findAllWithPathsRecursive(rootNode, key, "", results);
        return results;
    }

    /**
     * 通配符路径查找（*匹配任意属性名或数组索引） 示例："departments[*].employees[*].name"
     *
     * @param  json    JSON字符串
     * @param  pattern 通配符路径模式
     * @param  <T>     返回值类型
     * @return         匹配值的列表
     */
    public static <T> List<T> findByPattern(String json, String pattern) {
        if (json == null || json.trim().isEmpty() || pattern == null || pattern.trim().isEmpty()) {
            return new ArrayList<>();
        }
        JsonNode rootNode = parseJson(json);
        if (rootNode == null) {
            return new ArrayList<>();
        }
        String[] parts = pattern.split("\\.");
        List<JsonNode> currentNodes = Collections.singletonList(rootNode);

        for (String part : parts) {
            if (currentNodes.isEmpty()) {
                break;
            }
            List<JsonNode> nextNodes = new ArrayList<>();
            for (JsonNode node : currentNodes) {
                nextNodes.addAll(matchPatternPart(node, part));
            }
            currentNodes = nextNodes;
        }

        return currentNodes.stream().map(JsonUtil::convertNodeToValue).map(obj -> (T) obj).collect(Collectors.toList());
    }

    /**
     * 递归通配符查找（**匹配任意层级） 示例："**.taskName" 匹配所有层级的taskName
     *
     * @param  json    JSON字符串
     * @param  pattern 递归通配符模式，以**开头
     * @param  <T>     返回值类型
     * @return         匹配值的列表
     */
    public static <T> List<T> findByPatternRecursive(String json, String pattern) {
        if (json == null || json.trim().isEmpty() || pattern == null || pattern.trim().isEmpty()) {
            return new ArrayList<>();
        }
        if (!pattern.startsWith("**.")) {
            throw new IllegalArgumentException("递归模式必须以**.开头");
        }
        String targetKey = pattern.substring(3);
        return findAll(json, targetKey);
    }

    /**
     * 条件查找：查找指定key的节点中满足条件的值
     *
     * @param  json      JSON字符串
     * @param  key       要查找的key（通常是对象节点）
     * @param  condition 条件判断函数
     * @param  <T>       返回值类型
     * @return           满足条件的值列表
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> findWhere(String json, String key, Predicate<Map<String, Object>> condition) {
        if (json == null || json.trim().isEmpty() || key == null || condition == null) {
            return new ArrayList<>();
        }
        List<Object> allValues = findAll(json, key);
        return allValues.stream().filter(v -> v instanceof Map).map(v -> (Map<String, Object>) v).filter(condition)
                .map(m -> (T) m).collect(Collectors.toList());
    }

    /**
     * 多条件组合查找
     *
     * @param  json       JSON字符串
     * @param  key        要查找的key
     * @param  conditions 条件映射，key为属性名，value为期望的值
     * @param  <T>        返回值类型
     * @return            满足所有条件的值列表
     */
    public static <T> List<T> findWhere(String json, String key, Map<String, Object> conditions) {
        return findWhere(json, key, map -> {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                Object actualValue = map.get(entry.getKey());
                if (!Objects.equals(actualValue, entry.getValue())) {
                    return false;
                }
            }
            return true;
        });
    }

    /**
     * 在指定深度查找key的所有值
     *
     * @param  json  JSON字符串
     * @param  key   要查找的key
     * @param  depth 深度（从0开始）
     * @param  <T>   返回值类型
     * @return       该深度上所有匹配值的列表
     */
    public static <T> List<T> findAtDepth(String json, String key, int depth) {
        if (json == null || json.trim().isEmpty() || key == null || depth < 0) {
            return new ArrayList<>();
        }
        JsonNode rootNode = parseJson(json);
        if (rootNode == null) {
            return new ArrayList<>();
        }
        List<T> results = new ArrayList<>();
        findAtDepthRecursive(rootNode, key, depth, 0, results);
        return results;
    }

    /**
     * 获取从根到目标key的所有路径
     *
     * @param  json JSON字符串
     * @param  key  目标key
     * @return      所有路径的集合
     */
    public static Set<String> getPathsToKey(String json, String key) {
        Map<String, Object> results = findAllWithPaths(json, key);
        return results.keySet();
    }

    /**
     * 扁平化提取：把嵌套数组结构按指定路径打平 示例：flatten(json, "departments", "employees", "projects")
     *
     * @param  json     JSON字符串
     * @param  pathKeys 路径key链
     * @return          扁平化后的对象列表
     */
    public static List<Map<String, Object>> flatten(String json, String... pathKeys) {
        if (json == null || json.trim().isEmpty() || pathKeys == null || pathKeys.length == 0) {
            return new ArrayList<>();
        }
        JsonNode rootNode = parseJson(json);
        if (rootNode == null) {
            return new ArrayList<>();
        }

        List<JsonNode> currentNodes = Collections.singletonList(rootNode);
        for (String key : pathKeys) {
            if (currentNodes.isEmpty()) {
                break;
            }
            List<JsonNode> nextNodes = new ArrayList<>();
            for (JsonNode node : currentNodes) {
                JsonNode child = node.get(key);
                if (child != null) {
                    if (child.isArray()) {
                        child.forEach(nextNodes::add);
                    } else {
                        nextNodes.add(child);
                    }
                }
            }
            currentNodes = nextNodes;
        }

        return currentNodes.stream().map(node -> (Map<String, Object>) convertNodeToValue(node))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 树形转表格：按指定行路径提取列为二维结构
     *
     * @param  json    JSON字符串
     * @param  rowPath 行路径key链（如 ["departments", "employees"]）
     * @param  columns 要提取的列名数组
     * @return         表格形式的数据，每行是一个Map
     */
    public static List<Map<String, Object>> toTable(String json, String[] rowPath, String[] columns) {
        List<Map<String, Object>> rows = flatten(json, rowPath);
        return rows.stream().map(row -> {
            Map<String, Object> newRow = new LinkedHashMap<>();
            for (String col : columns) {
                newRow.put(col, row.get(col));
            }
            return newRow;
        }).collect(Collectors.toList());
    }

    /**
     * 获取key时携带祖先节点信息
     *
     * @param  json         JSON字符串
     * @param  targetKey    目标key
     * @param  ancestorKeys 要收集的祖先key数组
     * @param  <T>          目标值类型
     * @return              KeyContext列表
     */
    public static <T> List<KeyContext<T>> findWithContext(String json, String targetKey, String... ancestorKeys) {
        if (json == null || json.trim().isEmpty() || targetKey == null) {
            return new ArrayList<>();
        }
        JsonNode rootNode = parseJson(json);
        if (rootNode == null) {
            return new ArrayList<>();
        }
        List<KeyContext<T>> results = new ArrayList<>();
        Set<String> ancestorKeySet = ancestorKeys != null
                ? new HashSet<>(Arrays.asList(ancestorKeys))
                : Collections.emptySet();
        findWithContextRecursive(rootNode, targetKey, ancestorKeySet, "", new HashMap<>(), results);
        return results;
    }

    // ==================== 内部辅助方法 ====================

    @SuppressWarnings("unchecked")
    private static <T> void findAllRecursive(JsonNode node, String key, List<T> results) {
        if (node == null || node.isNull()) {
            return;
        }
        if (node.isObject()) {
            ObjectNode objNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if (entry.getKey().equals(key)) {
                    results.add((T) convertNodeToValue(entry.getValue()));
                }
                findAllRecursive(entry.getValue(), key, results);
            }
        } else if (node.isArray()) {
            for (JsonNode element : node) {
                findAllRecursive(element, key, results);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void findAllWithPathsRecursive(JsonNode node, String key, String currentPath,
            Map<String, Object> results) {
        if (node == null || node.isNull()) {
            return;
        }
        if (node.isObject()) {
            ObjectNode objNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String newPath = currentPath.isEmpty() ? entry.getKey() : currentPath + "." + entry.getKey();
                if (entry.getKey().equals(key)) {
                    results.put(newPath, convertNodeToValue(entry.getValue()));
                }
                findAllWithPathsRecursive(entry.getValue(), key, newPath, results);
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                String newPath = currentPath + "[" + i + "]";
                findAllWithPathsRecursive(node.get(i), key, newPath, results);
            }
        }
    }

    private static List<JsonNode> matchPatternPart(JsonNode node, String pattern) {
        List<JsonNode> results = new ArrayList<>();
        if (node == null || node.isNull()) {
            return results;
        }
        if ("*".equals(pattern)) {
            if (node.isObject()) {
                node.forEach(results::add);
            } else if (node.isArray()) {
                node.forEach(results::add);
            }
        } else if (pattern.matches("\\*\\[\\d+\\]")) {
            // 处理 *[n] 格式
            int index = Integer.parseInt(pattern.replaceAll("[^0-9]", ""));
            if (node.isArray() && index >= 0 && index < node.size()) {
                results.add(node.get(index));
            }
        } else if (pattern.matches("\\[\\d+\\]")) {
            // 处理 [n] 格式
            int index = Integer.parseInt(pattern.replaceAll("[^0-9]", ""));
            if (node.isArray() && index >= 0 && index < node.size()) {
                results.add(node.get(index));
            }
        } else {
            // 精确匹配属性名
            if (node.isObject() && node.has(pattern)) {
                results.add(node.get(pattern));
            }
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    private static <T> void findAtDepthRecursive(JsonNode node, String key, int targetDepth, int currentDepth,
            List<T> results) {
        if (node == null || node.isNull()) {
            return;
        }
        if (node.isObject()) {
            ObjectNode objNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if (entry.getKey().equals(key) && currentDepth == targetDepth) {
                    results.add((T) convertNodeToValue(entry.getValue()));
                }
                findAtDepthRecursive(entry.getValue(), key, targetDepth, currentDepth + 1, results);
            }
        } else if (node.isArray()) {
            for (JsonNode element : node) {
                findAtDepthRecursive(element, key, targetDepth, currentDepth, results);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void findWithContextRecursive(JsonNode node, String targetKey, Set<String> ancestorKeys,
            String currentPath, Map<String, Object> currentAncestors, List<KeyContext<T>> results) {
        if (node == null || node.isNull()) {
            return;
        }
        if (node.isObject()) {
            ObjectNode objNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String fieldName = entry.getKey();
                JsonNode childNode = entry.getValue();
                String newPath = currentPath.isEmpty() ? fieldName : currentPath + "." + fieldName;

                // 收集祖先信息
                Map<String, Object> newAncestors = new HashMap<>(currentAncestors);
                if (ancestorKeys.contains(fieldName)) {
                    newAncestors.put(fieldName, convertNodeToValue(childNode));
                }

                if (fieldName.equals(targetKey)) {
                    KeyContext<T> context = new KeyContext<>();
                    context.value = (T) convertNodeToValue(childNode);
                    context.ancestors = new HashMap<>(newAncestors);
                    context.fullPath = newPath;
                    results.add(context);
                }

                findWithContextRecursive(childNode, targetKey, ancestorKeys, newPath, newAncestors, results);
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                String newPath = currentPath + "[" + i + "]";
                findWithContextRecursive(node.get(i), targetKey, ancestorKeys, newPath, currentAncestors, results);
            }
        }
    }

    private static Object convertNodeToValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isTextual()) {
            return node.asText();
        }
        if (node.isNumber()) {
            if (node.isInt()) {
                return node.asInt();
            } else if (node.isLong()) {
                return node.asLong();
            } else if (node.isDouble() || node.isFloatingPointNumber()) {
                return node.asDouble();
            }
            return node.numberValue();
        }
        if (node.isBoolean()) {
            return node.asBoolean();
        }
        if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            for (JsonNode element : node) {
                list.add(convertNodeToValue(element));
            }
            return list;
        }
        if (node.isObject()) {
            Map<String, Object> map = new LinkedHashMap<>();
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                map.put(entry.getKey(), convertNodeToValue(entry.getValue()));
            }
            return map;
        }
        return node.toString();
    }

    /**
     * Key上下文信息类，用于携带祖先节点信息
     *
     * @param <T> 目标值的类型
     */
    public static class KeyContext<T> {
        private T value;
        private Map<String, Object> ancestors;
        private String fullPath;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Map<String, Object> getAncestors() {
            return ancestors;
        }

        public void setAncestors(Map<String, Object> ancestors) {
            this.ancestors = ancestors;
        }

        public String getFullPath() {
            return fullPath;
        }

        public void setFullPath(String fullPath) {
            this.fullPath = fullPath;
        }

        @Override
        public String toString() {
            return "KeyContext{" + "value=" + value + ", ancestors=" + ancestors + ", fullPath='" + fullPath + '\''
                    + '}';
        }
    }
}
