# Sa-Token 集成说明

## 概述

本项目已成功集成 Sa-Token 框架，替换了原有的 JWT 认证方案。Sa-Token 是一个轻量级 Java 权限认证框架，提供了更丰富的功能和更好的用户体验。

## 主要特性

1. **多端登录支持**：支持管理员和普通用户两种登录类型
2. **自动登录验证**：通过拦截器自动验证登录状态
3. **Token 管理**：自动生成和管理 Token
4. **密码加密**：使用 BCrypt 进行密码加密
5. **会话管理**：支持会话超时和自动续期

## 配置说明

### 1. Maven 依赖

已在 `pom.xml` 中添加以下依赖：

```xml
<!-- Sa-Token -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-spring-boot3-starter</artifactId>
    <version>1.37.0</version>
</dependency>
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-redis-jackson</artifactId>
    <version>1.37.0</version>
</dependency>
```

### 2. 配置文件

在 `application.yml` 中添加了 Sa-Token 配置：

```yaml
# Sa-Token配置
sa-token:
  # token名称 (同时也是cookie名称)
  token-name: Authorization
  # token有效期，单位s 默认30天, -1代表永不过期
  timeout: 2592000
  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒
  activity-timeout: -1
  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
  is-share: false
  # token风格
  token-style: uuid
  # 是否输出操作日志
  is-log: false
```

## API 接口

### 管理员相关接口

1. **管理员登录**
   - 路径：`POST /admins/auth/login`
   - 参数：`{"username": "admin", "password": "123456"}`
   - 返回：`{"token": "xxx", "user": {...}}`

2. **管理员退出登录**
   - 路径：`POST /admins/auth/logout`
   - 返回：`{"message": "退出登录成功"}`

### 用户相关接口

1. **用户登录**
   - 路径：`POST /users/auth/login`
   - 参数：`{"username": "user", "password": "123456"}`
   - 返回：`{"token": "xxx", "user": {...}}`

2. **用户退出登录**
   - 路径：`POST /users/auth/logout`
   - 返回：`{"message": "退出登录成功"}`

### 通用认证接口

1. **获取当前用户信息**
   - 路径：`GET /auth/info`
   - 返回：`{"user": {...}, "userType": "admin|user", "token": "xxx"}`

2. **检查登录状态**
   - 路径：`GET /auth/check`
   - 返回：`{"adminLogin": true, "userLogin": false, "isLogin": true}`

## 使用方式

### 1. 前端请求

在发送需要认证的请求时，需要在请求头中添加 Token：

```javascript
// 设置请求头
headers: {
  'Authorization': 'Bearer ' + token
}
```

### 2. 后端验证

Sa-Token 会自动拦截需要认证的请求，验证 Token 的有效性。如果 Token 无效或过期，会返回 401 错误。

### 3. 权限控制

可以在控制器方法上添加 Sa-Token 注解进行权限控制：

```java
// 需要登录
@SaCheckLogin
@GetMapping("/protected")
public Result<String> protectedApi() {
    return Result.success("需要登录才能访问");
}

// 需要管理员权限
@SaCheckRole("admin")
@GetMapping("/admin-only")
public Result<String> adminOnly() {
    return Result.success("只有管理员才能访问");
}
```

## 工具类

### SaTokenUtil

提供了常用的 Sa-Token 操作方法：

- `login(Object loginId, String loginType)` - 用户登录
- `logout(String loginType)` - 用户退出登录
- `getLoginId()` - 获取当前登录用户ID
- `isLogin()` - 检查是否已登录
- `getTokenValue()` - 获取当前Token值
- `encryptPassword(String password)` - 密码加密
- `checkPassword(String password, String hashedPassword)` - 密码验证

## 注意事项

1. **Token 存储**：Token 默认存储在内存中，生产环境建议配置 Redis 存储
2. **密码安全**：建议使用 `SaTokenUtil.encryptPassword()` 对密码进行加密存储
3. **会话管理**：可以通过配置文件调整 Token 有效期和会话超时时间
4. **并发登录**：当前配置允许同一账号并发登录，可根据需要调整

## 迁移说明

从 JWT 迁移到 Sa-Token 的主要变化：

1. 移除了 `JwtUtil` 类，使用 `SaTokenUtil` 替代
2. 登录接口返回的 Token 格式发生变化
3. 前端需要在请求头中添加 `Authorization: Bearer <token>` 格式的认证信息
4. 新增了用户登录接口和认证状态检查接口

## 下一步优化建议

1. **Redis 集成**：配置 Redis 存储 Token，提高性能和可扩展性
2. **权限系统**：集成 Sa-Token 的权限和角色功能
3. **密码加密**：在用户注册和更新时使用 BCrypt 加密密码
4. **日志记录**：启用 Sa-Token 的操作日志功能
5. **异常处理**：完善认证失败时的异常处理机制 