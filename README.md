# Common Tools

一个通用的 Java 工具库，提供基础规范定义、数据传输对象、异常处理等通用功能，旨在提高开发效率和代码规范性。

## 📚 快速导航

- [项目信息](#-项目信息)
- [技术栈](#-技术栈)
- [项目结构](#-项目结构)
- [已完成功能](#-已完成功能)
- [快速开始](#-快速开始)
- [使用示例](#-使用示例)
- [响应格式示例](#-响应格式示例)
- [核心特性](#-核心特性)
- [测试说明](#-测试说明)
- [后续规划](#-后续规划)
- [设计原则](#-设计原则)
- [版本历史](#-版本历史)
- [许可证](#-许可证)

## 📋 项目信息

| 项目 | 说明 |
|------|------|
| 项目名称 | common-tools |
| GroupId | com.linearizability |
| ArtifactId | common-tools |
| 版本 | 1.0-SNAPSHOT |
| Java 版本 | JDK 25 |
| 编码 | UTF-8 |
| 构建工具 | Maven 3.x |

## 🔧 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 25 | 核心编程语言 |
| Jackson | 2.20.1 | JSON 处理库 |
| JsonPath | 2.10.0 | JSON 路径查询 |
| JUnit | 5.10.0 | 单元测试框架 |
| Maven | 3.x | 项目构建工具 |
| Spotless | 2.44.4 | 代码格式化工具 |

## 📁 项目结构

```
common-tools/
├── src/
│   ├── main/java/com/linearizability/common/
│   │   ├── base/                    # 基础规范
│   │   │   ├── BaseEntity.java      # 实体类基类
│   │   │   ├── BaseEnum.java        # 枚举规范接口
│   │   │   ├── BaseRequest.java     # 基础请求类
│   │   │   ├── BaseResponse.java    # 基础响应类
│   │   │   ├── Converter.java       # 转换器接口
│   │   │   └── Validator.java       # 验证器接口
│   │   ├── dto/                     # 数据传输对象
│   │   │   ├── PageRequest.java     # 分页请求
│   │   │   ├── PageResult.java      # 分页响应
│   │   │   └── Result.java          # 统一响应结果
│   │   ├── enums/                   # 枚举实现
│   │   │   └── ResponseCodeEnum.java# 响应状态码枚举
│   │   ├── exception/               # 异常类体系
│   │   │   ├── BaseException.java   # 基础异常
│   │   │   ├── BusinessException.java
│   │   │   ├── SystemException.java
│   │   │   └── ValidationException.java
│   │   └── util/                    # 工具类
│   │       ├── CollectionHelper.java# 集合辅助工具
│   │       ├── DateUtil.java        # 日期时间工具
│   │       └── JsonUtil.java        # JSON 处理工具
│   └── test/java/com/linearizability/common/
│       └── util/
│           └── CollectionHelperTest.java  # 集合工具测试
├── pom.xml                          # Maven 配置文件
├── eclipse-formatter.xml            # 代码格式化配置
├── toolchains.xml                   # Maven 工具链配置
└── README.md                        # 本文件
```

## ✅ 已完成功能

### 1. 基础规范定义

#### BaseEnum - 枚举规范接口
- 定义枚举规范（`getCode()`, `getDesc()`）
- 提供通用查找方法（支持 Predicate 条件）
- 支持多种匹配方式（==、equals、字符串忽略大小写）
- 提供 `findByCode()`, `findByDesc()` 等方法
- 使用 `Optional` 返回值，避免空指针异常

#### BaseRequest / BaseResponse
- 请求/响应 ID（链路追踪）
- 时间戳和响应时间
- 用户信息、请求来源、客户端 IP
- 可扩展的参数字段

#### BaseEntity - 实体类基类
- 主键 ID、创建/更新时间
- 创建/更新人 ID、逻辑删除标记
- 乐观锁版本号、备注字段
- 生命周期方法（`beforeInsert()`, `beforeUpdate()`）

#### Converter / Validator
- 定义对象转换和验证规范
- 支持单个和批量操作
- 函数式接口，支持 Lambda 表达式

### 2. 数据传输对象

#### Result - 统一响应类
- HTTP 状态码 + 业务错误码（灵活扩展）
- 多种静态工厂方法（`success()`, `fail()` 等）
- 支持从异常自动转换

#### PageRequest / PageResult
- 分页信息封装
- 默认值和最大值限制
- 便捷方法（`getOffset()`, `getLimit()` 等）

### 3. 异常类体系
- BaseException、BusinessException、ValidationException、SystemException
- 灵活的错误码和错误消息
- 支持链式异常

### 4. 工具类

#### CollectionHelper - 集合辅助工具类
完整的集合操作工具，包括：

**单字段提取**:
- `extractField()` - 提取第一个非空字段值
- `extractNonNullFieldOrThrow()` - 提取第一个非空字段或抛异常
- `extractField(index)` - 按索引提取
- `extractFirstField()` / `extractLastField()` - 提取首尾元素
- `extractFieldOrThrow()` - 按索引提取或抛异常

**批量字段提取**:
- `extractFieldList()` - 提取为列表（保留 null）
- `extractNonNullFieldList()` - 提取非空值列表
- `extractDistinctFieldList()` - 提取去重列表
- `extractFieldSet()` - 提取为 Set（自动去重）

**条件提取**:
- `extractFieldByCondition()` - 按条件提取第一个值
- `extractFieldListByCondition()` - 按条件提取列表

**转换和映射**:
- `extractAndGroupBy()` - 按键分组
- `extractToMap()` - 转为键值对 Map（跳过 null）
- `extractFieldSet()` - 转为 Set

**工具方法**:
- `containsFieldValue()` - 检查字段值是否存在
- `countFieldValue()` - 统计字段值出现次数
- `sortByFieldDesc()` - 按字段降序排序

**多字段操作**:
- `extractMultipleFields()` - 提取多个字段并合并
- `extractMultipleFieldsDistinct()` - 提取多字段并去重
- `extractMultipleFieldsToSet()` - 提取多字段转为 Set

#### JsonUtil - JSON 处理工具
- 对象 ↔ JSON 字符串转换
- JSON 格式化输出
- JSON 有效性验证
- 支持泛型和复杂类型
- JSONPath 路径查询/修改（读取、设置、删除、添加）
- 支持 Java 8+ 时间 API

#### DateUtil - 日期时间工具
- 日期格式化、解析
- 日期计算（加/减天数、月数、年数）
- 日期比较和时间差计算
- 时间戳和时区转换
- 工作日计算（`isWorkday()`, `nextWorkday()` 等）
- 周期边界获取（周开始/结束、月开始/结束等）

### 5. 测试框架

#### CollectionHelper 单元测试
已为 `CollectionHelper` 编写完整测试套件（14 个测试用例），覆盖：
- **正常场景**: 各字段提取方法的标准用法
- **边界场景**: null 列表、空列表、越界索引等
- **异常场景**: 抛异常方法的异常验证
- **复杂操作**: 分组、映射、多字段合并、去重等
- **null 处理**: 验证各方法对 null 的处理策略
- **重复值**: 验证重复值过滤和保留逻辑

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

### 1. 克隆和编译

```bash
# 克隆项目
git clone <repository-url>
cd common-tools

# 编译
mvn clean compile

# 运行测试
mvn test

# 打包
mvn clean package

# 代码格式化
mvn spotless:apply
```

### 2. Maven 依赖

```xml
<dependency>
    <groupId>com.linearizability</groupId>
    <artifactId>common-tools</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## 📖 使用示例

### 集合工具类 (CollectionHelper)

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

## 📖 使用示例

### 集合工具类 (CollectionHelper)

```java
List<User> users = Arrays.asList(
    new User(1L, "admin", "admin@example.com"),
    new User(2L, "user", "user@example.com"),
    new User(3L, "test", "test@example.com")
);

// 提取第一个/最后一个用户名
String firstName = CollectionHelper.extractFirstField(users, User::getUsername);
String lastName = CollectionHelper.extractLastField(users, User::getUsername);

// 提取所有用户名列表
List<String> usernames = CollectionHelper.extractFieldList(users, User::getUsername);

// 提取去重的用户名
List<String> distinctNames = CollectionHelper.extractDistinctFieldList(users, User::getUsername);

// 检查是否存在指定用户
boolean hasAdmin = CollectionHelper.containsFieldValue(users, User::getUsername, "admin");

// 按 ID 分组
Map<Long, List<String>> groupedByUserId = CollectionHelper.extractAndGroupBy(
    users, User::getId, User::getUsername);

// 转为 ID → 用户名 Map
Map<Long, String> idToNameMap = CollectionHelper.extractToMap(
    users, User::getId, User::getUsername);

// 按 ID 降序排序
CollectionHelper.sortByFieldDesc(users, User::getId);

// 条件提取
List<String> emails = CollectionHelper.extractFieldListByCondition(users,
    u -> u.getId() > 1, User::getEmail);
```

### JSON 工具类 (JsonUtil)

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

### 日期时间工具类 (DateUtil)

```java
// 格式化
LocalDateTime now = LocalDateTime.now();
String formatted = DateUtil.format(now); // "2024-01-01 12:00:00"

// 解析
LocalDateTime dateTime = DateUtil.parseDateTime("2024-01-01 12:00:00");
LocalDate date = DateUtil.parseDate("2024-01-01");

// 计算
LocalDateTime tomorrow = DateUtil.plusDays(now, 1);
LocalDateTime nextMonth = DateUtil.plusMonths(now, 1);

// 比较
boolean isBefore = DateUtil.isBefore(date1, date2);
boolean isBetween = DateUtil.isBetween(dateTime, start, end);

// 时间差
long days = DateUtil.daysBetween(start, end);
long hours = DateUtil.hoursBetween(start, end);

// 工作日
boolean isWorkday = DateUtil.isWorkday(LocalDate.now());
LocalDate nextWorkday = DateUtil.nextWorkday(LocalDate.now());
long workdays = DateUtil.workdaysBetween(startDate, endDate);
```

### 其他使用示例

#### 枚举定义和使用

## 🎯 核心特性

### 1. 统一规范
- 枚举规范接口（BaseEnum）支持多种查找方式
- 统一的请求/响应格式（BaseRequest/BaseResponse/Result）
- 通用实体基类（BaseEntity）提供生命周期方法
- 完整的异常体系（BaseException 及其子类）

### 2. 函数式编程
- Lambda 友好的接口设计
- Stream API 深度集成
- 灵活的条件过滤和转换

### 3. 类型安全
- 全面使用泛型，避免强转
- Optional 返回值，异常安全
- 业务错误码灵活扩展（String 类型）

### 4. 高性能
- 线程安全的工具类实现
- 优化的集合操作（避免多次遍历）
- 预编译的格式化器和日期解析器

### 5. 完整的测试
- 单元测试全覆盖
- 边界和异常场景验证
- null 安全性检查

## 🧪 测试说明

本项目使用 **JUnit 5** 作为单元测试框架，所有工具类均有完整的测试覆盖。

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行指定测试类
mvn test -Dtest=CollectionHelperTest

# 查看测试覆盖率报告
mvn test -Dtest=CollectionHelperTest
```

### 现有测试

#### CollectionHelperTest （14 个测试用例）

| 测试方法 | 覆盖场景 |
|---------|---------|
| `extractField_nullOrEmpty` | null/空列表 |
| `extractField_firstNonNull` | 正常提取 |
| `extractNonNullFieldOrThrow_successAndFailure` | 异常处理 |
| `extractFieldByIndex` | 按索引提取和边界检查 |
| `extractFirstField` | 首元素提取 |
| `extractFieldOrThrow_index` | 异常处理 |
| `extractFirstFieldOrThrow` | 首元素异常处理 |
| `extractFieldListVariants` | 批量提取、去重等 |
| `extractFieldByCondition` | 条件提取 |
| `extractLastAndGroupAndMap` | 分组、映射、去重 |
| `containsAndCount` | 存在检查和计数 |
| `sortByFieldDesc` | 排序功能 |
| `multipleFieldExtraction` | 多字段提取 |

### 测试特点

✅ **全面的边界测试** - null 列表、空列表、越界索引  
✅ **异常场景验证** - 异常方法的异常抛出  
✅ **null 安全检查** - 各方法对 null 的处理验证  
✅ **详细的中文注释** - 每个测试方法都有详细说明

## 📝 最佳实践

### 集合操作
```java
// 推荐：使用 CollectionHelper 简化集合操作
List<String> names = CollectionHelper.extractFieldList(users, User::getName);
Map<Long, User> userMap = CollectionHelper.extractToMap(users, User::getId, u -> u);
```

### 日期处理
```java
// 推荐：统一使用 DateUtil 处理日期
LocalDate today = LocalDate.now();
LocalDate workday = DateUtil.nextWorkday(today);
long days = DateUtil.daysBetween(start, end);
```

### JSON 处理
```java
// 推荐：使用 JsonUtil 进行统一的 JSON 操作
String json = JsonUtil.toJson(user);
User user = JsonUtil.fromJson(json, User.class);
```

### 异常处理
```java
// 推荐：使用统一的异常体系
if (user == null) {
    throw new BusinessException("USER_NOT_FOUND", "用户不存在");
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

### 第一阶段（核心功能优化）
- [ ] 为 DateUtil 和 JsonUtil 补充单元测试
- [ ] 优化 CollectionHelper 性能（减少多次遍历）
- [ ] 支持更多的日期格式模式

### 第二阶段（常用工具扩展）
- [ ] 字符串工具类（StringUtil）- 脱敏、格式化、转换
- [ ] 对象工具类（ObjectUtil）- 属性复制、对象比较
- [ ] 验证工具类（ValidateUtil）- 邮箱、手机号、身份证验证
- [ ] 加密工具类（CryptoUtil）- MD5、AES、RSA 加密

### 第三阶段（高级功能）
- [ ] HTTP 工具类（HttpUtil）- HTTP 请求封装
- [ ] 反射工具类（ReflectUtil）- 动态调用、注解扫描
- [ ] 文件工具类（FileUtil）- 文件操作、压缩

### 第四阶段（扩展功能）
- [ ] 缓存工具类（CacheUtil）
- [ ] ID 生成工具类（IdUtil）
- [ ] 线程工具类（ThreadUtil）

## 🏗️ 设计原则

| 原则 | 说明 |
|------|------|
| 职责清晰 | 基础规范与具体实现分离 |
| 类型安全 | 使用泛型和 Optional，提高类型安全性 |
| 函数式 | 支持 Lambda 表达式和 Stream API |
| 线程安全 | 工具类方法均为静态方法，无共享状态 |
| 高性能 | 避免多次遍历，使用缓存和预编译 |
| 易于扩展 | 接口驱动，灵活组合 |
| 自动化 | 使用 Spotless 保证代码格式一致 |

## 📊 版本历史

### v1.0-SNAPSHOT (当前版本)

**✅ 已完成**:
- 基础规范定义（6 个基类和接口）
- 数据传输对象（3 个 DTO）
- 异常类体系（4 个异常类）
- 工具类实现：
  - CollectionHelper（17 个方法，14 个测试）
  - JsonUtil（15+ 个方法）
  - DateUtil（20+ 个方法）

**📊 统计**:
- 核心类数：13 个
- 工具类数：3 个
- 测试覆盖：14 个测试用例
- 代码行数：3000+ 行

## 📄 许可证

暂未指定（MIT/Apache 2.0 可选）

## 👤 作者

ZhangBoyuan

---

**最后更新**: 2026 年 2 月 27 日  
**项目状态**: 🟢 活跃开发中