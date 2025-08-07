<template>
  <div class="modern-login">
    <div class="login-background">
      <div class="bg-overlay"></div>
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>

    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo">
            <h1>RE-BIN</h1>
            <p>欢迎回来</p>
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
                placeholder="请输入用户名或邮箱"
                size="large"
                clearable
              >
                <template #prefix>
                  <el-icon><User /></el-icon>
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
                <el-checkbox v-model="rememberMe">记住我</el-checkbox>
                <el-link type="primary" @click="showForgotPassword">忘记密码？</el-link>
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
                <span v-if="!loading">登录</span>
                <span v-else>登录中...</span>
              </el-button>
            </el-form-item>
          </el-form>

          <div class="divider">
            <span>或</span>
          </div>

          <div class="social-login">
            <el-button class="social-btn github-btn" @click="socialLogin('github')">
              <el-icon><svg viewBox="0 0 1024 1024"><path d="M512 12.64c-282.752 0-512 229.216-512 512 0 226.208 146.688 418.144 350.08 485.824 25.6 4.736 35.008-11.104 35.008-24.64 0-12.192-0.48-52.544-0.704-95.328-142.464 30.976-172.512-60.416-172.512-60.416-23.296-59.168-56.832-74.912-56.832-74.912-46.464-31.776 3.52-31.136 3.52-31.136 51.392 3.616 78.464 52.768 78.464 52.768 45.664 78.272 119.776 55.648 148.992 42.56 4.576-33.088 17.856-55.68 32.512-68.48-113.728-12.928-233.216-56.864-233.216-253.024 0-55.904 19.936-101.568 52.672-137.44-5.312-12.896-22.848-64.96 4.96-135.488 0 0 42.88-13.76 140.8 52.48 40.832-11.36 84.64-17.024 128.16-17.248 43.488 0.192 87.328 5.888 128.256 17.248 97.728-66.24 140.64-52.48 140.64-52.48 27.872 70.528 10.336 122.592 5.024 135.488 32.832 35.84 52.608 81.536 52.608 137.44 0 196.64-119.776 239.936-233.856 252.64 18.368 15.904 34.72 47.04 34.72 94.816 0 68.512-0.608 123.648-0.608 140.512 0 13.632 9.216 29.6 35.168 24.576C877.472 942.08 1024 750.208 1024 524.64c0-282.784-229.248-512-512-512z"/></svg></el-icon>
              GitHub 登录
            </el-button>
          </div>

          <div class="register-link">
            <span>还没有账号？</span>
            <router-link to="/user/register" class="link">立即注册</router-link>
          </div>

          <div class="back-home">
            <router-link to="/" class="home-link">
              <el-icon><House /></el-icon>
              返回首页
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 忘记密码对话框 -->
    <el-dialog v-model="forgotPasswordVisible" title="找回密码" width="400px">
      <el-form :model="forgotForm" ref="forgotFormRef">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="forgotForm.email" placeholder="请输入注册邮箱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="forgotPasswordVisible = false">取消</el-button>
        <el-button type="primary" @click="handleForgotPassword">发送重置邮件</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, House } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const loading = ref(false)
const rememberMe = ref(false)
const forgotPasswordVisible = ref(false)
const loginFormRef = ref()
const forgotFormRef = ref()

const loginForm = reactive({
  username: '',
  password: ''
})

const forgotForm = reactive({
  email: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名或邮箱', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度在 3 到 50 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    await loginFormRef.value.validate()
    loading.value = true

    await userStore.login({
      username: loginForm.username,
      password: loginForm.password,
      remember: rememberMe.value
    })

    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    console.error('登录失败:', error)
    // 错误信息已经在request拦截器中显示了，这里不需要重复显示
  } finally {
    loading.value = false
  }
}

// 社交登录
const socialLogin = (provider) => {
  ElMessage.info(`${provider} 登录功能开发中...`)
}

// 显示忘记密码对话框
const showForgotPassword = () => {
  forgotPasswordVisible.value = true
}

// 处理忘记密码
const handleForgotPassword = () => {
  ElMessage.info('找回密码功能开发中...')
  forgotPasswordVisible.value = false
}
</script>

<style scoped>
.modern-login {
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.bg-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.1);
}

.floating-shapes {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 80px;
  height: 80px;
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 120px;
  height: 120px;
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.shape-3 {
  width: 60px;
  height: 60px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

.login-container {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 400px;
  padding: 20px;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo h1 {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 5px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.logo p {
  color: #7f8c8d;
  font-size: 1rem;
  margin: 0;
}

.login-form {
  width: 100%;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 10px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  border: none;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(52, 152, 219, 0.3);
}

.divider {
  text-align: center;
  margin: 30px 0;
  position: relative;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #e1e8ed;
}

.divider span {
  background: rgba(255, 255, 255, 0.95);
  padding: 0 20px;
  color: #7f8c8d;
  font-size: 0.9rem;
}

.social-login {
  margin-bottom: 30px;
}

.social-btn {
  width: 100%;
  height: 45px;
  border-radius: 10px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.github-btn {
  background: #333;
  color: white;
  border: none;
}

.github-btn:hover {
  background: #24292e;
  transform: translateY(-2px);
}

.register-link {
  text-align: center;
  margin-bottom: 20px;
  color: #7f8c8d;
}

.register-link .link {
  color: #3498db;
  text-decoration: none;
  font-weight: 500;
  margin-left: 5px;
}

.register-link .link:hover {
  text-decoration: underline;
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

/* 响应式设计 */
@media (max-width: 480px) {
  .login-card {
    padding: 30px 20px;
    margin: 20px;
  }
  
  .logo h1 {
    font-size: 2rem;
  }
}

/* Element Plus 样式覆盖 */
:deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border: 1px solid #e1e8ed;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 15px rgba(52, 152, 219, 0.2);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);
}

:deep(.el-checkbox__label) {
  color: #7f8c8d;
}
</style>
