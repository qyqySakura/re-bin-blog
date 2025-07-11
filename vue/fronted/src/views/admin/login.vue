<template>
    <div class="loginVideo">
      <video src="../../assets/video/login.mp4" autoplay muted loop preload="auto" />
      <div class="loginBox">
        <div
          class="glass-card"
          :class="{ expanded: isExpanded }"
          @click="expandLogin"
        >
          <div class="login-title">RE-BIN</div>
          <div class="login-subtitle" v-if="!isExpanded">点击登录</div>
          <el-form
            v-if="isExpanded"
            ref="formRef"
            :model="form"
            :rules="rules"
            class="form-area"
            @click.stop
          >
            <el-form-item prop="username">
              <el-input
                v-model="form.username"
                placeholder="请输入账号"
                :prefix-icon="User"
                size="large"
                class="input-glass"
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
                class="input-glass"
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
              >
                {{ loading ? '登录中...' : '登 录' }}
              </el-button>
            </el-form-item>
          </el-form>
          <div class="register-tip" v-if="isExpanded">
            还没有账号？请联系BIN
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { useStore } from 'vuex'
  import { ElMessage } from 'element-plus'
  import { User, Lock } from '@element-plus/icons-vue'
  import axios from '@/utils/request'
  
  const router = useRouter()
  const store = useStore()
  const formRef = ref()
  const loading = ref(false)
  const isExpanded = ref(false)
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
  
  function expandLogin() {
    if (!isExpanded.value) isExpanded.value = true
  }
  
  async function login() {
    if (!formRef.value) return
    try {
      await formRef.value.validate()
      loading.value = true
      const response = await axios.post('/admins/auth/login', form.value)
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
  .loginVideo {
    width: 100vw;
    height: 100vh;
    overflow: hidden;
    position: relative;
  }
  video {
    width: 100vw;
    height: 100vh;
    object-fit: cover;
    object-position: center;
    position: absolute;
    top: 0; left: 0;
    z-index: 0;
  }
  .loginBox {
    position: absolute;
    width: 100vw;
    left: 0;
    top: 0;
    z-index: 1;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    pointer-events: none;
    height: 100vh;
  }
  .glass-card {
    min-width: 260px;
    max-width: 400px;
    padding: 24px 32px;
    border-radius: 32px;
    background: rgba(255,255,255,0.18);
    box-shadow: 0 8px 32px 0 rgba(31,38,135,0.37);
    backdrop-filter: blur(16px);
    -webkit-backdrop-filter: blur(16px);
    border: 1.5px solid rgba(255,255,255,0.28);
    display: flex;
    flex-direction: column;
    align-items: center;
    position: absolute;
    left: 50%;
    top: 40px;
    transform: translateX(-50%);
    margin-top: 0;
    transition:
      top 0.6s cubic-bezier(.4,2,.6,1),
      transform 0.6s cubic-bezier(.4,2,.6,1),
      box-shadow 0.4s,
      background 0.4s,
      min-width 0.4s,
      padding 0.4s;
    cursor: pointer;
    pointer-events: auto;
  }
  .glass-card.expanded {
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    min-width: 350px;
    padding: 40px 32px 24px 32px;
    background: rgba(255,255,255,0.22);
    box-shadow: 0 12px 48px 0 rgba(31,38,135,0.45);
    cursor: default;
  }
  .glass-card .login-title {
    font-size: 2rem;
    font-weight: bold;
    color: #fff;
    text-shadow: 0 2px 8px rgba(0,0,0,0.25);
    margin-bottom: -8px;
    letter-spacing: 2px;
  }
  .glass-card .login-subtitle {
    font-size: 1.1rem;
    color: #e0e0e0;
    margin-bottom: 8px;
    letter-spacing: 1px;
  }
  .form-area {
    width: 100%;
  }
  .input-glass .el-input__wrapper {
    background: rgba(255,255,255,0.35) !important;
    border-radius: 16px !important;
    box-shadow: 0 2px 8px rgba(31,38,135,0.10);
    border: none;
  }
  .login-btn {
    width: 100%;
    border-radius: 16px;
    font-weight: 600;
    background: linear-gradient(90deg, #409eff 0%, #6dd5fa 100%);
    box-shadow: 0 4px 16px 0 rgba(64,158,255,0.15);
    transition: all 0.2s;
  }
  .login-btn:hover {
    filter: brightness(1.08);
    transform: translateY(-2px) scale(1.03);
  }
  .register-tip {
    margin-top: 18px;
    color: #e0e0e0;
    font-size: 0.95rem;
    text-align: center;
  }
  .register-tip a {
    color: #fff;
    text-decoration: underline;
    transition: color 0.2s;
  }
  .register-tip a:hover {
    color: #409eff;
  }
  </style>