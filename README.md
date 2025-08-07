# RE-BIN 个人博客系统

一个基于 Spring Boot + Vue 3 的现代化个人博客系统，支持文章发布、分类管理、标签系统、评论功能、用户管理、文件上传等完整功能。

## 🚀 技术栈

### 后端
- **Spring Boot 3.3.1** - 主框架
- **Java 17** - 编程语言
- **MySQL 8.0** - 数据库
- **MyBatis Plus** - ORM框架
- **Sa-Token** - 认证授权
- **Spring Boot Mail** - 邮件服务
- **Hutool** - 工具库
- **Lombok** - 代码简化
- **Redis** - 缓存和会话存储

### 前端
- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **Element Plus** - UI组件库
- **Vue Router 4** - 路由管理
- **Pinia** - 状态管理
- **Axios** - HTTP客户端
- **Tailwind CSS** - 样式框架

## 📋 功能特性

### 🌐 博客前台
- ✅ **首页展示** - 文章列表、热门文章、最新文章
- ✅ **文章系统** - 文章详情、分页浏览、阅读统计
- ✅ **分类管理** - 分类列表、分类文章筛选、文章数量统计
- ✅ **标签系统** - 标签云、标签文章、权重显示
- ✅ **评论功能** - 文章评论、回复评论、评论点赞
- ✅ **搜索功能** - 全文搜索、高级筛选、搜索结果分页
- ✅ **文章归档** - 按年月归档、时间线展示
- ✅ **友情链接** - 友链管理和展示
- ✅ **响应式设计** - 适配PC、平板、手机

### 🛠️ 后台管理
- ✅ **仪表盘** - 数据统计、图表展示、快捷操作
- ✅ **文章管理** - 增删改查、状态管理、批量操作、富文本编辑
- ✅ **分类管理** - 分类CRUD、层级管理、文章关联
- ✅ **标签管理** - 标签CRUD、使用统计、批量管理
- ✅ **用户管理** - 用户列表、状态控制、权限管理、分页查询
- ✅ **管理员管理** - 管理员CRUD、角色分配、权限控制
- ✅ **评论管理** - 评论审核、批量删除、状态管理、分页查询
- ✅ **文件管理** - 文件上传、图片管理、存储配置
- ✅ **系统设置** - 站点配置、邮件设置、安全配置

### 👤 用户系统
- ✅ **用户注册** - 邮箱验证、验证码校验、重复检查
- ✅ **用户登录** - 多端登录、记住密码、状态验证
- ✅ **个人中心** - 个人信息、头像上传、密码修改
- ✅ **用户状态** - 账户禁用、状态管理、登录拦截
- ✅ **权限控制** - 角色权限、接口鉴权、页面守卫

### 🔧 系统功能
- ✅ **认证授权** - Sa-Token集成、JWT令牌、多端认证
- ✅ **文件上传** - 头像上传、文章封面、文件管理
- ✅ **邮件服务** - 注册验证、密码重置、系统通知
- ✅ **数据分页** - 统一分页、性能优化、前后端联动
- ✅ **异常处理** - 全局异常、错误日志、友好提示
- ✅ **安全防护** - CORS配置、XSS防护、SQL注入防护

## 🛠️ 安装部署

### 环境要求
- **Java 17+** - 后端运行环境
- **Node.js 18+** - 前端构建环境
- **MySQL 8.0+** - 数据库
- **Maven 3.8+** - 项目构建工具
- **Redis 6.0+** - 缓存服务（可选）

### 📦 快速开始

#### 1. 克隆项目
```bash
git clone <repository-url>
cd llf
```

#### 2. 数据库配置
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE llf CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库结构和初始数据
mysql -u root -p llf < src/main/llf.sql
```

#### 3. 后端配置
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    username: your_username
    password: your_password
    url: jdbc:mysql://localhost:3306/llf?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai

  # 邮件配置（用于注册验证）
  mail:
    host: smtp.qq.com
    port: 587
    username: your_email@qq.com
    password: your_email_auth_code

  # Redis配置（可选）
  redis:
    host: localhost
    port: 6379
    password: your_redis_password

# 文件上传配置
file:
  upload-path: ./uploads/
  max-size: 10MB
```

#### 4. 启动后端服务
```bash
# 编译项目
mvn clean compile

# 启动服务
mvn spring-boot:run
```
🚀 后端服务将在 http://localhost:8080 启动

#### 5. 前端部署

```bash
# 进入前端目录
cd vue/fronted

# 安装依赖
npm install
# 或使用 yarn
yarn install

# 启动开发服务器
npm run dev
```
🌐 前端服务将在 http://localhost:5173 启动

#### 6. 生产环境部署
```bash
# 构建生产版本
npm run build

# 预览构建结果
npm run preview
```

### 🔑 默认账户

#### 管理员账户
- **用户名**: `admin`
- **密码**: `123123`
- **访问地址**: http://localhost:5173/admin/login

#### 测试用户账户
- **用户名**: `testuser`
- **密码**: `123123`
- **访问地址**: http://localhost:5173/login

### 🌍 访问地址

- **博客首页**: http://localhost:5173/
- **用户登录**: http://localhost:5173/login
- **用户注册**: http://localhost:5173/register
- **管理后台**: http://localhost:5173/admin
- **API文档**: http://localhost:8080/doc.html (如果集成了Swagger)

## 📁 项目结构

```
llf/
├── src/main/java/llf/llf/          # Java源码
│   ├── controller/                 # 控制器层
│   │   ├── AdminController.java    # 管理员控制器
│   │   ├── UserController.java     # 用户控制器
│   │   ├── PostController.java     # 文章控制器
│   │   ├── BlogController.java     # 博客前台控制器
│   │   ├── CommentController.java  # 评论控制器
│   │   └── AuthController.java     # 认证控制器
│   ├── service/                    # 服务层
│   │   ├── impl/                   # 服务实现类
│   │   ├── UserService.java        # 用户服务
│   │   ├── PostService.java        # 文章服务
│   │   ├── BlogService.java        # 博客服务
│   │   └── CommentService.java     # 评论服务
│   ├── mapper/                     # 数据访问层
│   │   ├── UserMapper.java         # 用户数据访问
│   │   ├── PostMapper.java         # 文章数据访问
│   │   └── CommentMapper.java      # 评论数据访问
│   ├── pojo/                       # 实体类
│   │   ├── User.java               # 用户实体
│   │   ├── Post.java               # 文章实体
│   │   ├── Comment.java            # 评论实体
│   │   ├── Category.java           # 分类实体
│   │   └── Tag.java                # 标签实体
│   ├── common/                     # 公共工具类
│   │   ├── Result.java             # 统一响应结果
│   │   └── SaTokenUtil.java        # 认证工具类
│   ├── config/                     # 配置类
│   │   ├── SaTokenConfig.java      # 认证配置
│   │   ├── CorsConfig.java         # 跨域配置
│   │   ├── FileConfig.java         # 文件配置
│   │   └── RedisConfig.java        # Redis配置
│   └── utils/                      # 工具类
├── src/main/resources/
│   ├── mapper/                     # MyBatis映射文件
│   │   ├── UserMapper.xml          # 用户SQL映射
│   │   ├── PostMapper.xml          # 文章SQL映射
│   │   └── CommentMapper.xml       # 评论SQL映射
│   ├── application.yml             # 应用配置
│   └── llf.sql                     # 数据库初始化脚本
├── vue/fronted/                    # Vue前端项目
│   ├── src/
│   │   ├── views/                  # 页面组件
│   │   │   ├── user/               # 用户相关页面
│   │   │   ├── admin/              # 管理后台页面
│   │   │   └── blog/               # 博客前台页面
│   │   ├── components/             # 公共组件
│   │   ├── layout/                 # 布局组件
│   │   │   ├── BlogLayout.vue      # 博客布局
│   │   │   ├── AdminLayout.vue     # 管理后台布局
│   │   │   └── UserLayout.vue      # 用户中心布局
│   │   ├── stores/                 # 状态管理
│   │   │   ├── user.js             # 用户状态
│   │   │   └── admin.js            # 管理员状态
│   │   ├── router/                 # 路由配置
│   │   ├── api/                    # API接口
│   │   │   ├── modules/            # 模块化API
│   │   │   └── request.js          # 请求拦截器
│   │   ├── utils/                  # 工具函数
│   │   └── assets/                 # 静态资源
│   ├── public/                     # 公共资源
│   ├── package.json                # 依赖配置
│   └── vite.config.js              # Vite配置
├── uploads/                        # 文件上传目录
│   ├── covers/                     # 文章封面
│   └── avatars/                    # 用户头像
├── pom.xml                         # Maven配置
└── README.md                       # 项目说明
```

## 🔧 API接口文档

### 🔐 认证相关
```
POST /user/auth/login          # 用户登录
POST /user/auth/logout         # 用户退出
POST /user/auth/info           # 获取用户信息
POST /admins/auth/login        # 管理员登录
POST /admins/auth/logout       # 管理员退出
POST /user/sendEmailCode       # 发送邮箱验证码
POST /user/add                 # 用户注册
GET  /auth/info                # 获取当前用户信息
GET  /auth/check               # 检查登录状态
```

### 📝 文章相关
```
# 博客前台
GET  /api/blog/posts           # 获取首页文章列表（分页）
GET  /api/blog/posts/{id}      # 获取文章详情
GET  /api/blog/posts/popular   # 获取热门文章
GET  /api/blog/posts/latest    # 获取最新文章
POST /api/blog/posts/{id}/like # 文章点赞

# 管理后台
GET  /posts                    # 获取所有文章（管理）
GET  /posts/{id}               # 获取文章详情（管理）
POST /posts/add                # 新增文章
PUT  /posts/update             # 更新文章
DELETE /posts/del/{id}         # 删除文章
PUT  /posts/status/{id}        # 更新文章状态
```

### 📂 分类相关
```
GET  /api/blog/categories      # 获取分类列表（含文章数）
GET  /categories               # 获取所有分类
GET  /categories/{id}          # 获取分类详情
POST /categories/add           # 新增分类
PUT  /categories/update        # 更新分类
DELETE /categories/del/{id}    # 删除分类
```

### 🏷️ 标签相关
```
GET  /api/blog/tags            # 获取标签列表（含文章数）
GET  /tags                     # 获取所有标签
GET  /tags/{id}                # 获取标签详情
GET  /tags/post/{postId}       # 获取文章标签
POST /tags/add                 # 新增标签
PUT  /tags/update              # 更新标签
DELETE /tags/del/{id}          # 删除标签
```

### 💬 评论相关
```
GET  /comments                 # 获取评论列表（分页、筛选）
GET  /comments/{id}            # 获取评论详情
GET  /comments/post/{postId}   # 获取文章评论
POST /comments/add             # 新增评论
PUT  /comments/update          # 更新评论
DELETE /comments/del/{id}      # 删除评论
POST /comments/{id}/like       # 评论点赞
```

### 👥 用户管理
```
GET  /user                     # 获取用户列表
GET  /user/{id}                # 获取用户详情
PUT  /user/update              # 更新用户信息
DELETE /user/del/{id}          # 删除用户
POST /user/changePassword      # 修改密码
POST /user/upload/avatar       # 上传头像
PUT  /user/status/{id}         # 更新用户状态
```

### 🛠️ 管理员相关
```
GET  /admins                   # 获取管理员列表
GET  /admins/{id}              # 获取管理员详情
POST /admins/add               # 新增管理员
PUT  /admins/update            # 更新管理员
DELETE /admins/del/{id}        # 删除管理员
```

### 📊 博客数据
```
GET  /api/blog/statistics      # 获取博客统计数据
GET  /api/blog/archives        # 获取文章归档
GET  /api/blog/home            # 获取首页数据
GET  /api/blog/site-info       # 获取站点信息
```

## 🎯 使用说明

### 🚀 快速体验

#### 1. 博客前台体验
- **首页访问**: http://localhost:5173/
- **功能**: 浏览文章、查看分类、搜索内容、阅读评论

#### 2. 用户功能体验
- **注册地址**: http://localhost:5173/register
- **登录地址**: http://localhost:5173/login
- **功能**: 注册账户、发表评论、管理个人信息

#### 3. 管理后台体验
- **访问地址**: http://localhost:5173/admin
- **默认账号**: `admin` / `123123`
- **功能**: 文章管理、用户管理、系统设置

### 📋 功能操作指南

#### 文章管理
1. 登录管理后台
2. 进入"文章管理"页面
3. 点击"新增文章"创建文章
4. 支持富文本编辑、分类选择、标签添加
5. 可设置文章状态（发布/草稿）

#### 用户管理
1. 在"用户管理"页面查看所有用户
2. 可以禁用/启用用户账户
3. 支持用户信息编辑和删除
4. 实时查看用户状态和注册时间

#### 评论管理
1. 在"评论管理"页面查看所有评论
2. 支持评论审核和删除
3. 可按状态和关键词筛选
4. 支持批量操作

## 🔒 安全特性

### 🛡️ 认证授权
- **Sa-Token集成** - 现代化的权限认证框架
- **JWT令牌** - 无状态的用户认证
- **多端登录** - 支持用户和管理员分离登录
- **权限控制** - 基于角色的访问控制
- **登录拦截** - 自动拦截未授权访问

### 🔐 数据安全
- **密码加密** - BCrypt加密存储用户密码
- **邮箱验证** - 注册时必须验证邮箱
- **用户状态** - 支持账户禁用功能
- **SQL注入防护** - MyBatis预编译语句
- **XSS防护** - 前端输入过滤和转义

### 🌐 网络安全
- **CORS配置** - 跨域资源共享控制
- **请求拦截** - 统一的请求响应处理
- **异常处理** - 全局异常捕获和处理
- **接口鉴权** - 细粒度的接口权限控制
- **文件上传** - 安全的文件类型和大小限制

## 📝 开发说明

### 💻 代码规范
- **Lombok使用** - 简化实体类代码，减少样板代码
- **统一响应** - Result类封装所有API响应
- **RESTful设计** - 遵循REST API设计规范
- **分层架构** - Controller-Service-Mapper三层架构
- **异常处理** - 全局异常处理器统一处理错误
- **日志记录** - 完善的日志记录和错误追踪

### 🗄️ 数据库设计

#### 核心表结构
```sql
# 用户表
user (id, username, password, email, avatar, status, create_time, update_time)

# 管理员表
admin (id, username, password, email, avatar, create_time, update_time)

# 文章表
post (id, user_id, category_id, title, content, summary, cover, status, view_count, like_count, create_time, update_time)

# 分类表
category (id, name, description, create_time, update_time)

# 标签表
tag (id, name, description, create_time, update_time)

# 评论表
comment (id, post_id, user_id, content, parent_id, reply_to_user_id, status, like_count, create_time)

# 文章标签关联表
post_tag (id, post_id, tag_id, create_time)

# 友情链接表
friend_link (id, name, url, description, avatar, status, create_time, update_time)
```

#### 表关系说明
- **用户-文章**: 一对多关系，一个用户可以发布多篇文章
- **分类-文章**: 一对多关系，一个分类包含多篇文章
- **文章-标签**: 多对多关系，通过post_tag表关联
- **文章-评论**: 一对多关系，一篇文章可以有多个评论
- **评论-回复**: 自关联关系，支持评论回复功能

### 🔧 技术架构

#### 后端架构
```
┌─────────────────┐
│   Controller    │  # 控制器层 - 处理HTTP请求
├─────────────────┤
│    Service      │  # 服务层 - 业务逻辑处理
├─────────────────┤
│     Mapper      │  # 数据访问层 - 数据库操作
├─────────────────┤
│    Database     │  # 数据库层 - MySQL存储
└─────────────────┘
```

#### 前端架构
```
┌─────────────────┐
│     Views       │  # 页面组件 - 用户界面
├─────────────────┤
│   Components    │  # 公共组件 - 可复用组件
├─────────────────┤
│     Stores      │  # 状态管理 - Pinia状态
├─────────────────┤
│      API        │  # 接口层 - HTTP请求
└─────────────────┘
```

## 🐛 常见问题

### ❓ 部署问题

#### 1. 邮件发送失败
```
问题：用户注册时收不到验证码邮件
解决：
- 检查application.yml中邮箱配置是否正确
- 确认邮箱授权码（不是登录密码）
- 检查邮箱服务商SMTP设置
- 查看后端日志中的邮件发送错误信息
```

#### 2. 数据库连接失败
```
问题：启动时报数据库连接错误
解决：
- 确认MySQL服务是否启动
- 检查数据库用户名密码是否正确
- 确认数据库llf是否已创建
- 检查数据库URL中的端口和参数
- 确认防火墙是否阻止了数据库连接
```

#### 3. 前端页面空白或404
```
问题：访问页面显示空白或404错误
解决：
- 检查后端服务是否在8080端口启动
- 确认前端服务是否在5173端口启动
- 查看浏览器控制台的错误信息
- 检查API请求是否正常返回
- 确认路由配置是否正确
```

#### 4. 文件上传失败
```
问题：头像或文章封面上传失败
解决：
- 检查uploads目录是否存在且有写权限
- 确认文件大小是否超过限制（默认10MB）
- 检查文件类型是否被允许
- 查看后端日志中的文件上传错误
```

### 🔧 开发问题

#### 5. 跨域问题
```
问题：前端请求后端API时出现跨域错误
解决：
- 检查CorsConfig配置是否正确
- 确认前端请求的域名和端口
- 检查浏览器是否启用了CORS
```

#### 6. 认证失败
```
问题：登录后访问接口提示未认证
解决：
- 检查Sa-Token配置是否正确
- 确认token是否正确设置到请求头
- 查看SaTokenConfig中的路径配置
- 检查token是否过期
```

## 📞 技术支持

### 🤝 贡献指南
欢迎提交Issue和Pull Request来改进项目！

### 📧 联系方式
- **项目作者**: RE-BIN Team
- **邮箱**: rebin.dev@example.com
- **GitHub**: https://github.com/rebin-team/rebin-blog

### 📄 开源协议
本项目采用 MIT 协议开源，详情请查看 [LICENSE](LICENSE) 文件。

### 🌟 致谢
感谢所有为这个项目做出贡献的开发者！

---

**如果这个项目对你有帮助，请给个 ⭐ Star 支持一下！**