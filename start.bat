@echo off
echo ================================
echo RE-BIN 博客系统启动脚本
echo ================================

echo.
echo 正在启动后端服务...
echo 请确保已经配置好数据库和邮箱设置
echo.

start "后端服务" cmd /k "mvn spring-boot:run"

echo 等待后端服务启动...
timeout /t 10 /nobreak > nul

echo.
echo 正在启动前端服务...
echo.

cd vue\fronted
start "前端服务" cmd /k "npm run dev"

echo.
echo ================================
echo 启动完成！
echo 后端服务: http://localhost:9090
echo 前端服务: http://localhost:5173
echo 管理后台: http://localhost:5173/login
echo ================================
echo.

pause
