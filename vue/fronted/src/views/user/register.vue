<template>
  <div class="modern-register">
    <div class="register-background">
      <div class="bg-overlay"></div>
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>

    <div class="register-container">
      <div class="register-card">
        <div class="register-header">
          <div class="logo">
            <h1>RE-BIN</h1>
            <p>加入我们</p>
          </div>
        </div>

        <div class="register-form">
          <el-form 
            :model="registerForm" 
            :rules="rules" 
            ref="registerFormRef"
            @keyup.enter="handleRegister"
          >
            <el-form-item prop="username">
              <el-input 
                v-model="registerForm.username" 
                placeholder="请输入用户名"
                size="large"
                clearable
              >
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="email">
              <el-input 
                v-model="registerForm.email" 
                placeholder="请输入邮箱"
                size="large"
                clearable
              >
                <template #prefix>
                  <el-icon><Message /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input 
                v-model="registerForm.password" 
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

            <el-form-item prop="confirmPassword">
              <el-input 
                v-model="registerForm.confirmPassword" 
                type="password" 
                placeholder="请确认密码"
                size="large"
                show-password
                clearable
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="agreement">
              <el-checkbox v-model="registerForm.agreement">
                我已阅读并同意
                <el-link type="primary" @click="showTerms">《用户协议》</el-link>
                和
                <el-link type="primary" @click="showPrivacy">《隐私政策》</el-link>
              </el-checkbox>
            </el-form-item>

            <el-form-item>
              <el-button 
                type="primary" 
                size="large" 
                @click="handleRegister" 
                :loading="loading"
                class="register-btn"
              >
                <span v-if="!loading">注册</span>
                <span v-else>注册中...</span>
              </el-button>
            </el-form-item>
          </el-form>

          <div class="login-link">
            <span>已有账号？</span>
            <router-link to="/user/login" class="link">立即登录</router-link>
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

    <!-- 用户协议对话框 -->
    <el-dialog v-model="termsVisible" title="用户协议" width="600px">
      <div class="terms-content">
        <h3>1. 服务条款</h3>
        <p>欢迎使用RE-BIN博客平台。在使用本服务前，请仔细阅读本协议。</p>
        
        <h3>2. 用户责任</h3>
        <p>用户应对其发布的内容负责，不得发布违法、有害信息。</p>
        
        <h3>3. 隐私保护</h3>
        <p>我们承诺保护用户隐私，不会泄露用户个人信息。</p>
        
        <h3>4. 服务变更</h3>
        <p>我们保留随时修改或终止服务的权利。</p>
      </div>
      <template #footer>
        <el-button @click="termsVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 隐私政策对话框 -->
    <el-dialog v-model="privacyVisible" title="隐私政策" width="600px">
      <div class="privacy-content">
        <h3>1. 信息收集</h3>
        <p>我们只收集必要的用户信息，用于提供更好的服务。</p>
        
        <h3>2. 信息使用</h3>
        <p>收集的信息仅用于服务提供，不会用于其他商业目的。</p>
        
        <h3>3. 信息保护</h3>
        <p>我们采用先进的安全措施保护用户信息安全。</p>
        
        <h3>4. 第三方服务</h3>
        <p>我们可能使用第三方服务，但会确保用户隐私得到保护。</p>
      </div>
      <template #footer>
        <el-button @click="privacyVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Message, House } from '@element-plus/icons-vue'
import { userApi } from '@/utils/api'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const termsVisible = ref(false)
const privacyVisible = ref(false)
const registerFormRef = ref()

const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  agreement: false
})

// 自定义验证函数
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const validateAgreement = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请先同意用户协议和隐私政策'))
  } else {
    callback()
  }
}

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' },
    { pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]/, message: '密码必须包含大小写字母和数字', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  agreement: [
    { validator: validateAgreement, trigger: 'change' }
  ]
}

// 注册处理
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    await registerFormRef.value.validate()
    loading.value = true
    
    const response = await userApi.register({
      username: registerForm.username,
      email: registerForm.email,
      password: registerForm.password
    })
    
    if (response.code === 200) {
      ElMessage.success('注册成功，请登录')
      router.push('/user/login')
    } else {
      ElMessage.error(response.message || '注册失败')
    }
  } catch (error) {
    console.error('注册失败:', error)
    ElMessage.error('注册失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 显示用户协议
const showTerms = () => {
  termsVisible.value = true
}

// 显示隐私政策
const showPrivacy = () => {
  privacyVisible.value = true
}
</script>

<style scoped>
.modern-register {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.register-background {
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

.register-container {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 450px;
  padding: 20px;
}

.register-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.register-header {
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

.register-form {
  width: 100%;
}

.register-btn {
  width: 100%;
  height: 50px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 10px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  border: none;
  transition: all 0.3s ease;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(52, 152, 219, 0.3);
}

.login-link {
  text-align: center;
  margin-bottom: 20px;
  color: #7f8c8d;
}

.login-link .link {
  color: #3498db;
  text-decoration: none;
  font-weight: 500;
  margin-left: 5px;
}

.login-link .link:hover {
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

.terms-content,
.privacy-content {
  max-height: 400px;
  overflow-y: auto;
  line-height: 1.6;
}

.terms-content h3,
.privacy-content h3 {
  color: #2c3e50;
  margin-top: 20px;
  margin-bottom: 10px;
}

.terms-content p,
.privacy-content p {
  color: #5a6c7d;
  margin-bottom: 15px;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .register-card {
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
