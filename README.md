# RE-BIN 个人博客系统

一个基于 Spring Boot + Vue 3 的现代化个人博客系统，支持文章发布、分类管理、标签系统、评论功能等。

## 🚀 技术栈

### 后端
- **Spring Boot 3.3.1** - 主框架
- **Java 17** - 编程语言
- **MySQL 8.0** - 数据库
- **MyBatis** - ORM框架
- **Sa-Token** - 认证授权
- **Spring Boot Mail** - 邮件服务
- **Hutool** - 工具库
- **Lombok** - 代码简化

### 前端
- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **Element Plus** - UI组件库
- **Vue Router 4** - 路由管理
- **Vuex 4** - 状态管理
- **Axios** - HTTP客户端

## 📋 功能特性

### 博客前台
- ✅ 文章列表展示（分页）
- ✅ 文章详情查看
- ✅ 分类筛选
- ✅ 标签云
- ✅ 评论系统
- ✅ 响应式设计

### 后台管理
- ✅ 文章管理（增删改查）
- ✅ 分类管理
- ✅ 标签管理
- ✅ 用户管理
- ✅ 管理员管理
- ✅ 评论管理

### 用户系统
- ✅ 用户注册（邮箱验证）
- ✅ 用户登录
- ✅ 头像上传
- ✅ 个人信息管理

## 🛠️ 安装部署

### 环境要求
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 后端部署

1. **克隆项目**
```bash
git clone <repository-url>
cd llf
```

2. **配置数据库**
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE llf CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库结构和初始数据
mysql -u root -p llf < src/main/llf.sql
```

3. **修改配置**
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    username: your_username
    password: your_password
    url: jdbc:mysql://localhost:3306/llf?useUnicode=true&characterEncoding=utf-8
  mail:
    username: your_email@qq.com
    password: your_email_password
```

4. **启动后端**
```bash
mvn spring-boot:run
```
后端服务将在 http://localhost:9090 启动

### 前端部署

1. **进入前端目录**
```bash
cd vue/fronted
```

2. **安装依赖**
```bash
npm install
```

3. **启动开发服务器**
```bash
npm run dev
```
前端服务将在 http://localhost:5173 启动

4. **构建生产版本**
```bash
npm run build
```

## 📁 项目结构

```
llf/
├── src/main/java/llf/llf/          # Java源码
│   ├── controller/                 # 控制器层
│   ├── service/                    # 服务层
│   ├── mapper/                     # 数据访问层
│   ├── pojo/                       # 实体类
│   ├── common/                     # 公共工具类
│   └── config/                     # 配置类
├── src/main/resources/
│   ├── mapper/                     # MyBatis映射文件
│   └── application.yml             # 应用配置
├── vue/fronted/                    # Vue前端项目
│   ├── src/
│   │   ├── views/                  # 页面组件
│   │   ├── components/             # 公共组件
│   │   ├── utils/                  # 工具函数
│   │   ├── router/                 # 路由配置
│   │   └── layout/                 # 布局组件
│   └── package.json
└── pom.xml                         # Maven配置
```

## 🔧 API接口

### 认证相关
- `POST /users/auth/login` - 用户登录
- `POST /admins/auth/login` - 管理员登录
- `POST /users/sendCode` - 发送邮箱验证码
- `POST /users/add` - 用户注册

### 文章相关
- `GET /posts/published` - 获取已发布文章（分页）
- `GET /posts/{id}` - 获取文章详情
- `POST /posts/add` - 新增文章
- `PUT /posts/update` - 更新文章
- `DELETE /posts/del/{id}` - 删除文章

### 分类相关
- `GET /categories` - 获取所有分类
- `POST /categories/add` - 新增分类
- `PUT /categories/update` - 更新分类
- `DELETE /categories/del/{id}` - 删除分类

### 标签相关
- `GET /tags` - 获取所有标签
- `GET /tags/post/{postId}` - 获取文章标签
- `POST /tags/add` - 新增标签

### 评论相关
- `GET /comments/post/{postId}` - 获取文章评论
- `POST /comments/add` - 新增评论
- `DELETE /comments/del/{id}` - 删除评论

## 🎯 使用说明

### 管理员登录
- 访问：http://localhost:5173/login
- 默认账号：123 / 123123

### 用户注册
- 访问：http://localhost:5173/user/register
- 需要邮箱验证码

### 博客首页
- 访问：http://localhost:5173/
- 查看已发布的文章列表

## 🔒 安全特性

- Sa-Token 身份认证
- 邮箱验证注册
- 密码加密存储
- CORS 跨域配置
- 全局异常处理

## 📝 开发说明

### 代码规范
- 使用 Lombok 简化代码
- 统一的 Result 响应格式
- RESTful API 设计
- 分层架构设计

### 数据库设计
- 用户表（user）
- 管理员表（admin）
- 文章表（post）
- 分类表（category）
- 标签表（tag）
- 评论表（comment）
- 文章标签关联表（post_tag）

## 🐛 常见问题

1. **邮件发送失败**
   - 检查邮箱配置是否正确
   - 确认邮箱授权码是否有效

2. **数据库连接失败**
   - 检查数据库服务是否启动
   - 确认用户名密码是否正确

3. **前端页面空白**
   - 检查后端服务是否启动
   - 查看浏览器控制台错误信息

作者：sakura
邮箱：2692433610@qq.com