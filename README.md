# Common Tools

一个通用的 Java 工具库，提供基础规范定义、数据传输对象、异常处理等通用功能，旨在提高开发效率和代码规范性。

## 📚 目录

- [项目信息](#-项目信息)
- [技术栈](#-技术栈)
- [主要依赖](#-主要依赖)
- [项目结构](#-项目结构)
- [已完成功能](#-已完成功能)
- [快速开始](#-快速开始)
- [使用示例](#使用示例)
- [响应格式示例](#-响应格式示例)
- [核心特性](#-核心特性)
- [后续规划](#-后续规划)
- [设计原则](#️-设计原则)
- [快速集成](#-快速集成)
- [项目统计](#-项目统计)
- [版本历史](#-版本历史)
- [许可证](#-许可证)
- [作者](#-作者)
- [贡献指南](#-贡献指南)
- [联系方式](#-联系方式)

## 📋 项目信息

- **项目名称**: common-tools
- **GroupId**: com.linearizability
- **ArtifactId**: common-tools
- **版本**: 1.0-SNAPSHOT
- **Java 版本**: JDK 25
- **编码**: UTF-8
- **构建工具**: Maven

## 🔧 技术栈

- **核心框架**: Java 25
- **JSON处理**: Jackson 2.20.1
- **JSONPath**: JsonPath 2.10.0
- **代码格式化**: Spotless Maven Plugin
- **构建工具**: Maven 3.x

## 📦 主要依赖

```xml
<!-- Jackson JSON处理库 -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.20.1</version>
</dependency>

<!-- Jackson支持Java 8+时间API -->
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
    <version>2.20.1</version>
</dependency>

<!-- JSONPath支持 -->
<dependency>
    <groupId>com.jayway.jsonpath</groupId>
    <artifactId>json-path</artifactId>
    <version>2.10.0</version>
</dependency>
```

## 📁 项目结构

```
common-tools/
├── src/main/java/com/linearizability/common/
│   ├── base/                    # 基础规范定义
│   │   ├── BaseEnum.java        # 枚举规范接口
│   │   ├── BaseRequest.java     # 基础请求类
│   │   ├── BaseResponse.java    # 基础响应类
│   │   ├── BaseEntity.java      # 实体类基类
│   │   ├── Converter.java       # 转换器接口
│   │   └── Validator.java       # 验证器接口
│   ├── dto/                     # 数据传输对象
│   │   ├── Result.java          # 统一响应结果
│   │   ├── PageRequest.java     # 分页请求
│   │   └── PageResult.java      # 分页响应
│   ├── enums/                   # 枚举实现类
│   │   └── ResponseCodeEnum.java # 响应状态码枚举示例
│   ├── exception/              # 异常类体系
│   │   ├── BaseException.java
│   │   ├── BusinessException.java
│   │   ├── ValidationException.java
│   │   └── SystemException.java
│   └── util/                    # 工具类
│       ├── CollectionHelper.java # 集合助手工具类
│       ├── DateUtil.java        # 日期时间工具类
│       └── JsonUtil.java        # JSON工具类
└── pom.xml
```

## ✅ 已完成功能

### 1. 基础规范定义 (`common.base`)

#### BaseEnum - 枚举规范接口
- ✅ 定义枚举规范（`getCode()`, `getDesc()`）
- ✅ 提供通用查找方法（支持 `Predicate` 条件）
- ✅ 支持多种匹配方式（==、equals、字符串忽略大小写）
- ✅ 提供 `findByCode()`, `findByDesc()` 等方法
- ✅ 提供 `findByCodeOrThrow()`, `exists()` 等便捷方法
- ✅ 使用 `Optional` 返回值，避免空指针异常

#### BaseRequest - 基础请求类
- ✅ 请求ID（链路追踪）
- ✅ 请求时间戳
- ✅ 请求来源、客户端IP
- ✅ 用户信息（userId、username）
- ✅ 扩展参数字段

#### BaseResponse - 基础响应类
- ✅ 响应ID
- ✅ 响应时间戳、响应时间
- ✅ 处理耗时
- ✅ 扩展参数字段

#### BaseEntity - 实体类基类
- ✅ 主键ID
- ✅ 创建/更新时间
- ✅ 创建/更新人ID
- ✅ 逻辑删除标记
- ✅ 乐观锁版本号
- ✅ 备注字段
- ✅ 提供 `beforeInsert()`, `beforeUpdate()` 等生命周期方法

#### Converter - 转换器接口
- ✅ 定义对象转换规范
- ✅ 支持单个和批量转换
- ✅ 支持反向转换（可选）
- ✅ 函数式接口，支持 Lambda 表达式

#### Validator - 验证器接口
- ✅ 定义数据验证规范
- ✅ 支持验证失败时抛出异常
- ✅ 支持验证器组合（AND、OR、NOT）
- ✅ 函数式接口，支持 Lambda 表达式

### 2. 数据传输对象 (`common.dto`)

#### Result - 统一响应结果类
- ✅ HTTP 状态码（Integer，符合 HTTP 标准）
- ✅ 业务错误码（String，灵活扩展）
- ✅ 响应消息、响应数据
- ✅ 成功/失败标识
- ✅ 提供多种静态工厂方法
- ✅ 支持从异常自动转换

#### PageRequest - 分页请求类
- ✅ 页码、每页大小（带默认值和最大值限制）
- ✅ 排序字段、排序方向
- ✅ 是否需要总数
- ✅ 提供 `getOffset()`, `getLimit()` 等便捷方法

#### PageResult - 分页响应结果类
- ✅ 数据列表
- ✅ 分页信息（PageInfo）
- ✅ 总记录数、总页数
- ✅ 是否有上一页/下一页

### 3. 异常类体系 (`common.exception`)

#### BaseException - 基础异常类
- ✅ 错误码（String 类型，灵活扩展）
- ✅ 错误消息
- ✅ 错误详情（可选）
- ✅ 支持链式异常

#### BusinessException - 业务异常类
- ✅ 默认错误码：`BUSINESS_ERROR`
- ✅ 用于业务逻辑异常

#### ValidationException - 验证异常类
- ✅ 默认错误码：`VALIDATION_ERROR`
- ✅ 用于参数验证异常

#### SystemException - 系统异常类
- ✅ 默认错误码：`SYSTEM_ERROR`
- ✅ 用于系统级别异常

### 4. 枚举示例 (`common.enums`)

#### ResponseCodeEnum - 响应状态码枚举
- ✅ 实现 `BaseEnum` 接口
- ✅ 提供常用 HTTP 状态码
- ✅ 展示枚举使用规范

### 5. 工具类 (`common.util`)

#### CollectionHelper - 集合助手工具类
- ✅ 字段提取（extractField、extractFirstField、extractLastField）
- ✅ 字段提取异常处理（extractNonNullFieldOrThrow、extractFieldOrThrow）
- ✅ 批量字段提取（extractFieldList、extractNonNullFieldList、extractDistinctFieldList）
- ✅ 集合转换（extractFieldSet、extractToMap、extractAndGroupBy）
- ✅ 条件过滤提取（extractFieldByCondition、extractFieldListByCondition）
- ✅ 字段值检查（containsFieldValue、countFieldValue）
- ✅ 集合排序（sortByFieldDesc）

#### JsonUtil - JSON工具类

#### JsonUtil - JSON工具类
- ✅ 对象转JSON字符串（`toJson()`）
- ✅ JSON字符串转对象（`fromJson()`）
- ✅ JSON格式化输出（`toPrettyJson()`）
- ✅ JSON验证（`isValid()`）
- ✅ JSON转List、Map（`fromJsonToList()`, `fromJsonToMap()`）
- ✅ 对象与Map互转（`toMap()`, `fromMap()`）
- ✅ 深拷贝对象（`deepClone()`）
- ✅ 支持泛型（`TypeReference`）
- ✅ 支持输入流和字节数组
- ✅ 支持JsonNode操作
- ✅ 线程安全的ObjectMapper实例
- ✅ 支持Java 8时间API
- ✅ JSONPath支持（读取、设置、删除、添加、路径检查等）

#### DateUtil - 日期时间工具类
- ✅ 日期格式化、解析（支持LocalDateTime、LocalDate、LocalTime）
- ✅ 日期计算（加/减天数、小时、月数、年数）
- ✅ 日期比较（isBefore、isAfter、isBetween、isEqual）
- ✅ 时间差计算（daysBetween、hoursBetween、minutesBetween、secondsBetween）
- ✅ 时间戳转换（毫秒、秒，支持与Date互转）
- ✅ 时区转换（convertZone）
- ✅ 工作日计算（isWorkday、isWeekend、nextWorkday、previousWorkday、workdaysBetween）
- ✅ 获取周期边界（周开始/结束、月开始/结束、年开始/结束）
- ✅ 获取当前时间（now、today、currentTimestamp）

## 🚀 快速开始

### Maven 依赖

```xml

<dependency>
    <groupId>com.linearizability</groupId>
    <artifactId>common-tools</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 使用示例

#### 1. 枚举使用

```java
// 定义枚举
public enum UserStatusEnum implements BaseEnum<String> {
    ACTIVE("ACTIVE", "激活"),
    INACTIVE("INACTIVE", "未激活");
    
    private final String code;
    private final String desc;
    
    // ... 构造方法和getter方法
    
    @Override
    public String getCode() { return code; }
    
    @Override
    public String getDesc() { return desc; }
}

// 使用枚举
Optional<UserStatusEnum> status = BaseEnum.findByCode(UserStatusEnum.class, "ACTIVE");
status.ifPresent(s -> System.out.println(s.getDesc()));
```

#### 2. 请求响应使用

```java
// 创建分页请求
PageRequest request = new PageRequest(1, 10);
request.setOrderBy("createTime");
request.setOrderDirection("DESC");

// 创建成功响应
Result<User> result = Result.success("查询成功", user);

// 创建分页响应
List<User> users = ...;
PageResult<User> pageResult = PageResult.success(users, 100L, request);
```

#### 3. 异常处理

```java
// 抛出业务异常
throw new BusinessException("USER_NOT_FOUND", "用户不存在");

// 在全局异常处理器中
@ExceptionHandler(BaseException.class)
public Result<?> handleException(BaseException e) {
    Integer httpCode = e instanceof ValidationException ? 400 : 
                       e instanceof BusinessException ? 400 : 500;
    return Result.fail(httpCode, e);
}
```

#### 4. 实体类使用

```java
public class User extends BaseEntity {
    private String username;
    private String email;
    // ... 其他字段
    
    // 自动继承通用字段和方法
}

// 使用
User user = new User();
user.beforeInsert(); // 自动设置创建时间和更新时间
```

#### 5. 转换器使用

```java
Converter<UserEntity, UserDTO> converter = entity -> {
    UserDTO dto = new UserDTO();
    dto.setId(entity.getId());
    dto.setUsername(entity.getUsername());
    return dto;
};

UserDTO dto = converter.convert(entity);
List<UserDTO> dtoList = converter.convertList(entityList);
```

#### 6. 验证器使用

```java
Validator<String> usernameValidator = username -> 
    username != null && username.length() >= 3;

Validator<String> emailValidator = email -> 
    email != null && email.contains("@");

// 组合验证器
Validator<String> combined = usernameValidator.and(emailValidator);
combined.validateOrThrow(username, "用户名或邮箱格式不正确");
```

#### 7. 集合助手工具类使用

```java
// 从用户列表中提取第一个用户的用户名
List<User> users = Arrays.asList(
    new User(1L, "admin", "admin@example.com"),
    new User(2L, "user", "user@example.com")
);

// 提取第一个用户名
String firstName = CollectionHelper.extractFirstField(users, User::getUsername);
// 输出: "admin"

// 提取所有用户名
List<String> usernames = CollectionHelper.extractFieldList(users, User::getUsername);
// 输出: ["admin", "user"]

// 提取去重的邮箱域名
List<String> domains = CollectionHelper.extractDistinctFieldList(users, 
    user -> user.getEmail().split("@")[1]);
// 输出: ["example.com"]

// 按用户ID分组
Map<Long, List<String>> groupedByUserId = CollectionHelper.extractAndGroupBy(users, 
    User::getId, User::getUsername);
// 输出: {1=["admin"], 2=["user"]}

// 转换为ID到用户名的映射
Map<Long, String> idToNameMap = CollectionHelper.extractToMap(users, 
    User::getId, User::getUsername);
// 输出: {1="admin", 2="user"}

// 检查是否包含指定用户名
boolean hasAdmin = CollectionHelper.containsFieldValue(users, User::getUsername, "admin");
// 输出: true

// 统计用户名为"admin"的数量
long adminCount = CollectionHelper.countFieldValue(users, User::getUsername, "admin");
// 输出: 1

// 按ID降序排序（假设User实现了Comparable或ID是Comparable类型）
CollectionHelper.sortByFieldDesc(users, User::getId);
// users列表现在按ID降序排列

// 根据条件提取字段（假设User有getStatus方法）
List<String> activeUsernames = CollectionHelper.extractFieldListByCondition(users,
    user -> "ACTIVE".equals(user.getStatus()), User::getUsername);
```

#### 8. JSON工具类使用

```java
// 对象转JSON
User user = new User();
user.setId(1L);
user.setUsername("admin");
String json = JsonUtil.toJson(user);
// 输出: {"id":1,"username":"admin"}

// 格式化输出
String prettyJson = JsonUtil.toPrettyJson(user);

// JSON转对象
User user2 = JsonUtil.fromJson(json, User.class);

// JSON转List
String jsonArray = "[{\"id\":1},{\"id\":2}]";
List<User> users = JsonUtil.fromJsonToList(jsonArray, User.class);

// JSON转Map
Map<String, Object> map = JsonUtil.fromJsonToMap(json);

// 对象转Map
Map<String, Object> userMap = JsonUtil.toMap(user);

// Map转对象
User user3 = JsonUtil.fromMap(userMap, User.class);

// 深拷贝
User clonedUser = JsonUtil.deepClone(user);

// 验证JSON
boolean valid = JsonUtil.isValid(json);

// 支持泛型
TypeReference<Map<String, List<User>>> typeRef = new TypeReference<Map<String, List<User>>>() {};
Map<String, List<User>> result = JsonUtil.fromJson(json, typeRef);

// JSONPath操作
String json = "{\"user\":{\"name\":\"admin\",\"age\":30},\"items\":[1,2,3]}";

// 读取路径值
String name = JsonUtil.readPath(json, "$.user.name");
Integer age = JsonUtil.readPath(json, "$.user.age", Integer.class);

// 读取列表
List<Integer> items = JsonUtil.readPathList(json, "$.items", Integer.class);

// 检查路径是否存在
boolean exists = JsonUtil.isPathExists(json, "$.user.name");

// 设置路径值
String modifiedJson = JsonUtil.setPath(json, "$.user.name", "newName");

// 删除路径
String deletedJson = JsonUtil.deletePath(json, "$.user.age");

// 添加值到数组
String addedJson = JsonUtil.addPath(json, "$.items", 4);

// 读取路径值并转换为对象
User user = JsonUtil.readPathAsObject(json, "$.user", User.class);

#### 9. 日期时间工具类使用

```java
// 格式化日期时间
LocalDateTime now = LocalDateTime.now();
String formatted = DateUtil.format(now); // "2024-01-01 12:00:00"
String customFormat = DateUtil.format(now, "yyyy/MM/dd HH:mm"); // "2024/01/01 12:00"

// 解析日期时间
LocalDateTime dateTime = DateUtil.parseDateTime("2024-01-01 12:00:00");
LocalDate date = DateUtil.parseDate("2024-01-01");

// 日期计算
LocalDateTime tomorrow = DateUtil.plusDays(now, 1);
LocalDateTime nextMonth = DateUtil.plusMonths(now, 1);
LocalDateTime nextYear = DateUtil.plusYears(now, 1);

// 日期比较
boolean isBefore = DateUtil.isBefore(dateTime1, dateTime2);
boolean isAfter = DateUtil.isAfter(dateTime1, dateTime2);
boolean isBetween = DateUtil.isBetween(dateTime, start, end);

// 计算时间差
long days = DateUtil.daysBetween(start, end);
long hours = DateUtil.hoursBetween(start, end);

// 时间戳转换
long timestamp = DateUtil.toTimestamp(now);
LocalDateTime fromTimestamp = DateUtil.fromTimestamp(timestamp);

// 时区转换
LocalDateTime utcTime = DateUtil.convertZone(now, ZoneId.of("UTC"));
LocalDateTime beijingTime = DateUtil.convertZone(now, ZoneId.of("Asia/Shanghai"));

// 工作日计算
boolean isWorkday = DateUtil.isWorkday(LocalDate.now());
LocalDate nextWorkday = DateUtil.nextWorkday(LocalDate.now());
long workdays = DateUtil.workdaysBetween(startDate, endDate);

// 获取周期边界
LocalDate weekStart = DateUtil.getWeekStart(LocalDate.now());
LocalDate monthEnd = DateUtil.getMonthEnd(LocalDate.now());
```

## 🎯 核心特性

### 1. 统一规范
- 统一的枚举规范（BaseEnum）
- 统一的请求响应格式（BaseRequest/BaseResponse）
- 统一的实体类基类（BaseEntity）
- 统一的异常处理体系

### 2. 函数式编程支持
- 转换器接口支持Lambda表达式
- 验证器接口支持函数式组合
- 集合工具类支持Stream API

### 3. 类型安全
- 大量使用泛型，避免类型转换错误
- Optional返回值，避免空指针异常
- 强类型的枚举和常量定义

### 4. 高性能
- 线程安全的单例模式
- 预编译的正则表达式和格式化器
- 高效的集合操作和字段提取

### 5. 易于扩展
- 接口和抽象类设计
- 插件化的验证器和转换器
- 灵活的配置和自定义选项

## 🎆 使用场景

### 1. Web API 开发
- 使用 `Result` 类统一 API 响应格式
- 使用 `BaseRequest`/`BaseResponse` 实现请求响应链路追踪
- 使用 `PageRequest`/`PageResult` 实现分页查询

### 2. 微服务架构
- 使用 `BaseException` 体系统一异常处理
- 使用 `BaseEnum` 规范化枚举定义
- 使用 `JsonUtil` 实现服务间数据交换

### 3. 数据处理
- 使用 `CollectionHelper` 高效处理集合数据
- 使用 `DateUtil` 处理日期时间计算
- 使用 `JsonUtil` 处理JSON数据转换

### 4. 企业应用开发
- 使用 `BaseEntity` 统一实体类设计
- 使用 `Converter` 实现DTO转换
- 使用 `Validator` 实现数据校验

## 📝 最佳实践

### 1. 异常处理
```java
// 推荐：使用全局异常处理器
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.fail(400, e);
    }
    
    @ExceptionHandler(ValidationException.class)
    public Result<?> handleValidationException(ValidationException e) {
        return Result.fail(400, e);
    }
    
    @ExceptionHandler(SystemException.class)
    public Result<?> handleSystemException(SystemException e) {
        return Result.fail(500, e);
    }
}
```

### 2. 枚举使用
```java
// 推荐：使用枚举管理常量
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserStatusEnum implements BaseEnum<String> {
    ACTIVE("ACTIVE", "激活"),
    INACTIVE("INACTIVE", "未激活"),
    LOCKED("LOCKED", "锁定");
    
    private final String code;
    private final String desc;
    
    // 构造方法和getter方法...
}
```

### 3. 分页查询
```java
// 推荐：统一分页查询接口
@GetMapping("/users")
public PageResult<UserDTO> getUsers(PageRequest pageRequest) {
    // 参数校验
    pageRequest.validate();
    
    // 查询数据
    List<User> users = userService.findUsers(pageRequest);
    Long total = pageRequest.isNeedTotal() ? userService.countUsers() : null;
    
    // 转换和返回
    List<UserDTO> userDTOs = userConverter.convertList(users);
    return PageResult.success(userDTOs, total, pageRequest);
}
```

### 4. JSON处理
```java
// 推荐：使用统一的JSON工具类
public class ApiResponse {
    public static <T> String toJson(Result<T> result) {
        return JsonUtil.toJson(result);
    }
    
    public static <T> Result<T> fromJson(String json, Class<T> dataClass) {
        TypeReference<Result<T>> typeRef = new TypeReference<Result<T>>() {};
        return JsonUtil.fromJson(json, typeRef);
    }
}
```

### 5. 日期处理
```java
// 推荐：统一日期格式处理
public class DateTimeConfig {
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    @JsonFormat(pattern = DEFAULT_PATTERN)
    @DateTimeFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime createTime;
}
```
```

## 📊 响应格式示例

### 成功响应

```json
{
  "code": 200,
  "errorCode": null,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin"
  },
  "success": true,
  "timestamp": 1234567890,
  "responseTime": "2024-01-01T12:00:00"
}
```

### 失败响应（带业务错误码）

```json
{
  "code": 400,
  "errorCode": "USER_NOT_FOUND",
  "message": "用户不存在",
  "data": null,
  "success": false,
  "timestamp": 1234567890,
  "responseTime": "2024-01-01T12:00:00"
}
```

### 分页响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {"id": 1, "username": "user1"},
    {"id": 2, "username": "user2"}
  ],
  "success": true,
  "pageInfo": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 100,
    "totalPages": 10,
    "hasPrevious": false,
    "hasNext": true
  }
}
```

## 🔮 后续规划

### 高优先级（核心功能）

#### 1. 集合工具类 (`common.util.CollectionUtil`)
- [x] 集合字段提取和转换（CollectionHelper已实现）
- [ ] 集合判空（isEmpty、isNotEmpty）
- [ ] 集合转换（List、Set、Map 互转）
- [ ] 集合过滤、分组、排序（部分已实现）
- [ ] 集合去重、合并
- [ ] 集合分页

#### 2. 字符串工具类 (`common.util.StringUtil`)
- [ ] 字符串判空（isEmpty、isNotEmpty、isBlank、isNotBlank）
- [ ] 字符串截取、格式化
- [ ] 字符串转换（驼峰、下划线、短横线）
- [ ] 字符串匹配、分割、合并
- [ ] 字符串编码转换
- [ ] 字符串脱敏（手机号、邮箱、身份证等）

#### 3. 对象工具类 (`common.util.ObjectUtil`)
- [ ] 对象判空
- [ ] 对象比较、克隆
- [ ] 对象属性复制
- [ ] 对象转 Map、Map 转对象

#### 4. 文件工具类 (`common.util.FileUtil`)
- [ ] 文件/目录操作
- [ ] 文件读取、写入
- [ ] 文件复制、移动
- [ ] 文件压缩、解压

#### 5. 验证工具类 (`common.util.ValidateUtil`)
- [ ] 邮箱验证
- [ ] 手机号验证
- [ ] 身份证验证
- [ ] 银行卡号验证
- [ ] IP 地址验证
- [ ] URL 验证

### 中优先级（常用功能）

#### 6. 加密工具类 (`common.util.CryptoUtil`)
- [ ] MD5、SHA 系列加密
- [ ] AES、RSA 加密
- [ ] Base64 编码/解码
- [ ] 密码加密与验证

#### 7. HTTP 工具类 (`common.util.HttpUtil`)
- [ ] GET、POST、PUT、DELETE 请求
- [ ] 文件上传、下载
- [ ] Cookie 管理
- [ ] 超时设置、重试机制

#### 8. 反射工具类 (`common.util.ReflectUtil`)
- [ ] 获取类信息
- [ ] 动态创建对象
- [ ] 动态调用方法
- [ ] 注解扫描

#### 9. 配置工具类 (`common.util.ConfigUtil`)
- [ ] Properties 文件读取
- [ ] YAML 文件读取
- [ ] 环境变量读取
- [ ] 配置缓存

### 低优先级（扩展功能）

#### 10. 其他工具类
- [ ] 数学工具类（MathUtil）
- [ ] 正则工具类（RegexUtil）
- [ ] 缓存工具类（CacheUtil）
- [ ] ID 生成工具类（IdUtil）
- [ ] 线程工具类（ThreadUtil）
- [ ] 系统工具类（SystemUtil）

### 测试与质量保证

- [ ] 为核心工具类编写单元测试
- [ ] 测试覆盖率 ≥ 80%
- [ ] 性能基准测试
- [ ] 代码质量检查（Checkstyle、SpotBugs）

### 文档与发布

- [ ] 完善 JavaDoc 文档
- [ ] 编写使用指南
- [ ] 配置 Maven 打包插件
- [ ] 发布到 Maven 仓库（可选）

## 🏗️ 设计原则

1. **职责清晰**: 基础规范与具体实现分离
2. **易于扩展**: 提供接口和抽象类，便于扩展
3. **符合标准**: HTTP 状态码符合 HTTP 标准
4. **灵活性强**: 业务错误码使用 String，支持灵活扩展
5. **类型安全**: 使用泛型和 Optional，提高类型安全性
6. **函数式**: 转换器和验证器支持函数式编程
7. **线程安全**: 工具类方法均为静态方法，线程安全
8. **性能优化**: 使用缓存和预编译模式，提高性能
9. **异常友好**: 提供详细的异常信息和错误处理
10. **代码规范**: 使用Spotless自动格式化，保持代码风格一致

## 📝 版本历史

### v1.0-SNAPSHOT (当前版本)

- ✅ 完成基础规范定义（BaseEnum、BaseRequest、BaseResponse、BaseEntity）
- ✅ 完成数据传输对象（Result、PageRequest、PageResult）
- ✅ 完成异常类体系（BaseException、BusinessException、ValidationException、SystemException）
- ✅ 完成转换器和验证器接口
- ✅ 完成响应状态码枚举示例
- ✅ 完成集合助手工具类（CollectionHelper）- 支持字段提取、转换、分组、排序、条件过滤等功能
- ✅ 完成JSON工具类（JsonUtil）- 支持对象转JSON、JSON转对象、格式化、验证、JSONPath等功能
- ✅ 完成日期时间工具类（DateUtil）- 支持格式化、解析、计算、比较、时间戳转换、时区转换、工作日计算等功能

## 🚀 快速集成

### 1. 克隆项目

```bash
git clone <repository-url>
cd common-tools
```

### 2. 编译安装

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 打包
mvn clean package

# 安装到本地仓库
mvn clean install
```

### 3. 代码格式化

```bash
# 检查代码格式
mvn spotless:check

# 自动格式化代码
mvn spotless:apply
```

## 📊 项目统计

- **代码行数**: 约 3000+ 行
- **工具类数量**: 4 个
- **基础类数量**: 6 个
- **异常类数量**: 4 个
- **DTO类数量**: 3 个
- **枚举类数量**: 1 个

## 📄 许可证

待定

## 👤 作者

ZhangBoyuan

---

**注意**: 本项目目前处于开发阶段，API 可能会发生变化。建议在生产环境使用前进行充分测试。