# 部署检查清单

## 🔧 环境准备

### 必需软件
- [ ] Java 17 或更高版本
- [ ] Maven 3.6 或更高版本
- [ ] Node.js 16 或更高版本
- [ ] MySQL 8.0 或更高版本

### 验证命令
```bash
java -version
mvn -version
node -version
npm -version
mysql --version
```

## 📊 数据库配置

### 1. 创建数据库
```sql
CREATE DATABASE llf CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 导入数据结构
```bash
mysql -u root -p llf < src/main/llf.sql
```

### 3. 验证数据导入
```sql
USE llf;
SHOW TABLES;
SELECT COUNT(*) FROM user;
SELECT COUNT(*) FROM admin;
SELECT COUNT(*) FROM post;
```

## ⚙️ 配置文件

### 1. 复制配置文件
```bash
cp src/main/resources/application-example.yml src/main/resources/application.yml
```

### 2. 修改数据库配置
- [ ] 数据库用户名
- [ ] 数据库密码
- [ ] 数据库连接URL

### 3. 修改邮箱配置
- [ ] 邮箱地址
- [ ] 邮箱授权码（不是登录密码）

### 4. 获取QQ邮箱授权码步骤
1. 登录QQ邮箱
2. 设置 → 账户
3. 开启SMTP服务
4. 生成授权码
5. 将授权码填入配置文件

## 🚀 启动服务

### 方法一：使用启动脚本（Windows）
```bash
start.bat
```

### 方法二：手动启动

#### 启动后端
```bash
mvn spring-boot:run
```

#### 启动前端
```bash
cd vue/fronted
npm install
npm run dev
```

## ✅ 功能测试

### 1. 后端服务测试
- [ ] 访问 http://localhost:9090
- [ ] 检查控制台无错误信息

### 2. 前端服务测试
- [ ] 访问 http://localhost:5173
- [ ] 页面正常显示

### 3. 数据库连接测试
- [ ] 后端启动时无数据库连接错误
- [ ] 可以正常查看文章列表

### 4. 邮件服务测试
- [ ] 用户注册时能收到验证码邮件
- [ ] 邮件内容格式正确

### 5. 登录功能测试

#### 管理员登录
- [ ] 访问 http://localhost:5173/login
- [ ] 使用默认账号：123 / 123123
- [ ] 成功进入管理后台

#### 用户注册登录
- [ ] 访问 http://localhost:5173/user/register
- [ ] 输入邮箱获取验证码
- [ ] 完成注册流程
- [ ] 使用新账号登录

### 6. 核心功能测试

#### 文章管理
- [ ] 新增文章
- [ ] 编辑文章
- [ ] 删除文章
- [ ] 文章列表显示

#### 分类管理
- [ ] 新增分类
- [ ] 编辑分类
- [ ] 删除分类

#### 评论功能
- [ ] 发表评论
- [ ] 查看评论
- [ ] 删除评论

## 🐛 常见问题排查

### 后端启动失败
1. 检查Java版本是否正确
2. 检查数据库是否启动
3. 检查配置文件是否正确
4. 查看控制台错误信息

### 前端页面空白
1. 检查后端服务是否启动
2. 检查浏览器控制台错误
3. 检查网络请求是否正常

### 邮件发送失败
1. 检查邮箱配置是否正确
2. 确认使用的是授权码而不是登录密码
3. 检查网络连接

### 数据库连接失败
1. 检查MySQL服务是否启动
2. 检查用户名密码是否正确
3. 检查数据库是否存在

## 📝 生产环境部署

### 1. 构建前端
```bash
cd vue/fronted
npm run build
```

### 2. 打包后端
```bash
mvn clean package -DskipTests
```

### 3. 部署建议
- 使用Nginx代理前端静态文件
- 使用systemd管理后端服务
- 配置SSL证书
- 设置防火墙规则
- 定期备份数据库

## 🔒 安全建议

- [ ] 修改默认管理员密码
- [ ] 使用强密码策略
- [ ] 定期更新依赖版本
- [ ] 配置HTTPS
- [ ] 限制数据库访问权限
- [ ] 定期备份数据

## 📞 技术支持

如遇到问题，请检查：
1. 日志文件
2. 配置文件
3. 网络连接
4. 服务状态

或联系技术支持获取帮助。
