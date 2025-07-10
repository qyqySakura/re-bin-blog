<!-- views/login.vue -->
<template>
  <div class="login-bg">
    <div class="login-card">
      <div class="login-header">
        <el-icon class="login-icon"><UserFilled /></el-icon>
        <h2>后台管理系统</h2>
        <p>请登录您的账户</p>
      </div>
      
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0" class="login-form">
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="请输入用户名"
            :prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码" 
            show-password
            :prefix-icon="Lock"
            size="large"
            @keyup.enter="login"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="login" 
            :loading="loading"
            size="large"
            class="login-btn"
            style="width: 100%"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { User, Lock, UserFilled } from '@element-plus/icons-vue'
import axios from '@/utils/request'

const router = useRouter()
const store = useStore()
const formRef = ref()
const loading = ref(false)
const form = ref({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

async function login() {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    const response = await axios.post('/admins/auth/login', form.value)
    
    // 假设后端返回 { token: 'xxx', user: {...} }
    const { token, user } = response.data || {}
    
    if (token) {
      store.dispatch('login', { token, user })
      ElMessage.success('登录成功')
      router.push('/users')
    } else {
      ElMessage.error('登录失败，请检查用户名或密码')
    }
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败，请检查用户名或密码')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 32px 0 rgba(76, 110, 245, 0.12);
  padding: 48px 36px 36px 36px;
  width: 400px;
  max-width: 90vw;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.login-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.login-icon {
  font-size: 38px;
  color: #409EFF;
  margin-bottom: 8px;
}

.login-form {
  margin-top: 20px;
  width: 100%;
}

.login-form .el-form-item {
  margin-bottom: 20px;
}

.login-form .el-input {
  --el-input-border-radius: 8px;
}

.login-btn {
  border-radius: 8px;
  font-weight: 500;
  height: 44px;
  letter-spacing: 2px;
}
</style>
