# 后台管理系统前端

这是一个基于 Vue 3 + Element Plus 的现代化后台管理系统前端项目。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vue Router 4** - 官方路由管理器
- **Vuex 4** - 状态管理模式
- **Element Plus** - 基于 Vue 3 的组件库
- **Axios** - HTTP 客户端
- **Vite** - 下一代前端构建工具

## 功能特性

- 🎨 现代化 UI 设计
- 🔐 用户认证和授权
- 👥 用户管理
- 👨‍💼 管理员管理
- 📱 响应式布局
- 🔄 实时数据更新
- ⚡ 快速开发体验

## 项目结构

```
src/
├── assets/          # 静态资源
├── component/       # 公共组件
├── router/          # 路由配置
├── utils/           # 工具函数
├── views/           # 页面组件
│   ├── admin/       # 管理员相关页面
│   ├── user/        # 用户相关页面
│   └── login.vue    # 登录页面
├── App.vue          # 根组件
├── main.js          # 入口文件
└── store.js         # 状态管理
```

## 快速开始

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 环境要求

- Node.js >= 16.0.0
- npm >= 8.0.0

## 开发说明

### 后端接口

项目需要配合后端 Spring Boot 项目使用，后端服务默认运行在 `http://localhost:9090`。

### 主要功能

1. **用户认证**
   - 登录/登出
   - JWT Token 管理
   - 路由守卫

2. **用户管理**
   - 用户列表展示
   - 新增用户
   - 编辑用户
   - 删除用户

3. **管理员管理**
   - 管理员列表展示
   - 新增管理员
   - 编辑管理员
   - 删除管理员

### 代码规范

- 使用 Vue 3 Composition API
- 使用 Element Plus 组件库
- 遵循 ESLint 规范
- 使用 async/await 处理异步操作

## 部署

### 构建

```bash
npm run build
```

构建完成后，`dist` 目录包含可部署的静态文件。

### 部署到服务器

将 `dist` 目录的内容部署到 Web 服务器即可。

## 许可证

MIT License
