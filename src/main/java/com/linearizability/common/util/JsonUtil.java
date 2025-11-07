package com.linearizability.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 * 提供JSON序列化和反序列化的常用方法
 *
 * @author ZhangBoyuan
 * @since 2025-11-07
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
    private static final Configuration JSONPATH_CONFIG = Configuration.builder()
        .jsonProvider(new JacksonJsonProvider())
        .mappingProvider(new JacksonMappingProvider())
        .options(Option.SUPPRESS_EXCEPTIONS)
        .build();

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
     * @param obj 待转换的对象
     * @return JSON字符串
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
     * @param obj 待转换的对象
     * @return 格式化后的JSON字符串
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
     * @param json  JSON字符串
     * @param clazz 目标类型
     * @param <T>   目标类型
     * @return 转换后的对象
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
     * @param json          JSON字符串
     * @param typeReference 类型引用
     * @param <T>           目标类型
     * @return 转换后的对象
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
     * @param json  JSON字符串
     * @param clazz List元素类型
     * @param <T>   元素类型
     * @return List对象
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
     * @param json JSON字符串
     * @return Map对象
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
     * @param inputStream 输入流
     * @param clazz       目标类型
     * @param <T>         目标类型
     * @return 转换后的对象
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
     * @param obj 待转换的对象
     * @return JSON字节数组
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
     * @param bytes JSON字节数组
     * @param clazz 目标类型
     * @param <T>   目标类型
     * @return 转换后的对象
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
     * @param obj 待转换的对象
     * @return JsonNode对象
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
     * @param json JSON字符串
     * @return JsonNode对象
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
     * @param json JSON字符串
     * @return true表示有效，false表示无效
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
     * @param obj 待转换的对象
     * @return Map对象
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
     * @param map   Map对象
     * @param clazz 目标类型
     * @param <T>   目标类型
     * @return 转换后的对象
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
     * @param obj   待拷贝的对象
     * @param clazz 目标类型
     * @param <T>   目标类型
     * @return 拷贝后的对象
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
     * @param obj 待拷贝的对象
     * @param <T> 目标类型
     * @return 拷贝后的对象
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
     * @param json     JSON字符串或对象
     * @param jsonPath JSONPath表达式（如：$.user.name）
     * @param <T>      返回值类型
     * @return 路径对应的值，如果路径不存在返回null
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
     * @param json     JSON字符串或对象
     * @param jsonPath JSONPath表达式
     * @param clazz    返回类型
     * @param <T>      返回值类型
     * @return 路径对应的值，如果路径不存在返回null
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
     * @param json     JSON字符串或对象
     * @param jsonPath JSONPath表达式
     * @param typeRef  类型引用
     * @param <T>      返回值类型
     * @return 路径对应的值，如果路径不存在返回null
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
     * @param json     JSON字符串或对象
     * @param jsonPath JSONPath表达式
     * @param clazz    列表元素类型
     * @param <T>      元素类型
     * @return 值列表，如果路径不存在返回空列表
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
     * @param json     JSON字符串或对象
     * @param jsonPath JSONPath表达式
     * @return true表示路径存在，false表示不存在
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
     * @param json     JSON字符串或对象
     * @param jsonPath JSONPath表达式
     * @param value    要设置的值
     * @return 修改后的JSON字符串
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
     * @param json     JSON字符串或对象
     * @param jsonPath JSONPath表达式
     * @return 修改后的JSON字符串
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
     * @param json     JSON字符串或对象
     * @param jsonPath JSONPath表达式
     * @param value    要添加的值
     * @return 修改后的JSON字符串
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
     * @param json     JSON字符串或对象
     * @param jsonPath JSONPath表达式
     * @param clazz    目标类型
     * @param <T>      目标类型
     * @return 转换后的对象
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
}

