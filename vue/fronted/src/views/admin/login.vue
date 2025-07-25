<template>
  <div class="admin-login">
    <div class="login-background">
      <div class="bg-pattern"></div>
      <div class="bg-overlay"></div>
    </div>

    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="admin-logo">
            <el-icon class="logo-icon"><Setting /></el-icon>
            <div class="logo-text">
              <h1>RE-BIN</h1>
              <p>管理后台</p>
            </div>
          </div>
        </div>

        <div class="login-form">
          <el-form
              :model="loginForm"
              :rules="rules"
              ref="loginFormRef"
              @keyup.enter="handleLogin"
          >
            <el-form-item prop="username">
              <el-input
                  v-model="loginForm.username"
                  placeholder="请输入管理员账号"
                  size="large"
                  clearable
              >
                <template #prefix>
                  <el-icon><UserFilled /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                  v-model="loginForm.password"
                  type="password"
                  placeholder="请输入密码"
                  size="large"
                  show-password
                  clearable
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item>
              <div class="form-options">
                <el-checkbox v-model="rememberMe">记住登录状态</el-checkbox>
              </div>
            </el-form-item>

            <el-form-item>
              <el-button
                  type="primary"
                  size="large"
                  @click="handleLogin"
                  :loading="loading"
                  class="login-btn"
              >
                <span v-if="!loading">登录管理后台</span>
                <span v-else>登录中...</span>
              </el-button>
            </el-form-item>
          </el-form>

          <div class="login-footer">
            <div class="security-notice">
              <el-icon><Warning /></el-icon>
              <span>请确保在安全的网络环境下登录</span>
            </div>

            <div class="back-home">
              <router-link to="/" class="home-link">
                <el-icon><House /></el-icon>
                返回网站首页
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 系统状态 -->
    <div class="system-status">
      <div class="status-item">
        <span class="status-label">系统状态</span>
        <el-tag type="success" size="small">正常运行</el-tag>
      </div>
      <div class="status-item">
        <span class="status-label">服务器时间</span>
        <span class="status-value">{{ currentTime }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAdminStore } from '@/stores/admin'
import { ElMessage } from 'element-plus'
import { Setting, UserFilled, Lock, Warning, House } from '@element-plus/icons-vue'

const router = useRouter()
const adminStore = useAdminStore()

// 响应式数据
const loading = ref(false)
const rememberMe = ref(false)
const currentTime = ref('')
const loginFormRef = ref()
let timeInterval = null

const loginForm = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入管理员账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 更新时间
const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    await loginFormRef.value.validate()
    loading.value = true

    await adminStore.login({
      username: loginForm.username,
      password: loginForm.password,
      remember: rememberMe.value
    })

    ElMessage.success('登录成功')
    router.push('/admin')
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败，请检查账号和密码')
  } finally {
    loading.value = false
  }
}

// 初始化
onMounted(() => {
  updateTime()
  timeInterval = setInterval(updateTime, 1000)
})

onUnmounted(() => {
  if (timeInterval) {
    clearInterval(timeInterval)
  }
})
</script>

<style scoped>
.admin-login {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
}

.bg-pattern {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image:
      radial-gradient(circle at 25% 25%, rgba(255,255,255,0.1) 0%, transparent 50%),
      radial-gradient(circle at 75% 75%, rgba(255,255,255,0.1) 0%, transparent 50%);
  background-size: 100px 100px;
  animation: patternMove 20s linear infinite;
}

@keyframes patternMove {
  0% { transform: translate(0, 0); }
  100% { transform: translate(100px, 100px); }
}

.bg-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.2);
}

.login-container {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 450px;
  padding: 20px;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.admin-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  margin-bottom: 20px;
}

.logo-icon {
  font-size: 3rem;
  color: #3498db;
  background: linear-gradient(135deg, #3498db, #2980b9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logo-text h1 {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin: 0;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.logo-text p {
  color: #7f8c8d;
  font-size: 1rem;
  margin: 5px 0 0 0;
  font-weight: 500;
}

.login-form {
  width: 100%;
}

.form-options {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 10px;
  background: linear-gradient(135deg, #2c3e50, #34495e);
  border: none;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(44, 62, 80, 0.3);
}

.login-footer {
  margin-top: 30px;
}

.security-notice {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: center;
  margin-bottom: 20px;
  padding: 10px;
  background: rgba(255, 193, 7, 0.1);
  border-radius: 8px;
  color: #856404;
  font-size: 0.9rem;
}

.back-home {
  text-align: center;
}

.home-link {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  color: #7f8c8d;
  text-decoration: none;
  font-size: 0.9rem;
  transition: color 0.3s ease;
}

.home-link:hover {
  color: #3498db;
}

.system-status {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 10px;
  padding: 15px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 10px;
  min-width: 200px;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.85rem;
}

.status-label {
  color: #7f8c8d;
  font-weight: 500;
}

.status-value {
  color: #2c3e50;
  font-weight: 600;
  font-family: 'Courier New', monospace;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-card {
    padding: 30px 20px;
    margin: 20px;
  }

  .admin-logo {
    flex-direction: column;
    gap: 10px;
  }

  .logo-text h1 {
    font-size: 2rem;
  }

  .system-status {
    position: static;
    margin-top: 20px;
    margin-left: 20px;
    margin-right: 20px;
  }
}

/* Element Plus 样式覆盖 */
:deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border: 1px solid #e1e8ed;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 15px rgba(44, 62, 80, 0.2);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 4px 15px rgba(44, 62, 80, 0.3);
}

:deep(.el-checkbox__label) {
  color: #7f8c8d;
}
</style>
