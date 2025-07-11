<template>
  <div class="register-container">
    <div class="register-left">
      <div class="welcome-title">欢迎来到我的个人博客</div>
      <div class="welcome-desc">
        在这里你可以学习如何使用Java，如何搭建网站，如何拥有自己的个人网站。
      </div>
      <div class="welcome-avatars">
        <img src="@/assets/ka.png" alt="avatars" />
      </div>
    </div>
    <div class="register-right">
      <div class="register-card">
        <div class="register-title">注册新用户</div>
        <div class="register-subtitle">欢迎注册我们的学习平台，请在下方填写相关信息</div>
        <el-form :model="form" :rules="rules" ref="formRef" class="register-form">
          <el-form-item prop="username">
            <el-input v-model="form.username" :prefix-icon="User" placeholder="用户名" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" :prefix-icon="Lock" type="password" placeholder="密码" show-password />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="form.confirmPassword" :prefix-icon="Lock" type="password" placeholder="重复密码" show-password />
          </el-form-item>
          <el-form-item prop="email">
            <el-input v-model="form.email" :prefix-icon="Message" placeholder="电子邮件地址" />
          </el-form-item>
          <el-form-item prop="code">
            <div class="code-row">
              <el-input v-model="form.code" :prefix-icon="Edit" placeholder="请输入验证码" style="flex:1;" />
              <el-button type="success" class="code-btn" @click="getCode" :disabled="codeLoading || codeCountdown > 0">
                {{ codeCountdown > 0 ? codeCountdown + 's' : '获取验证码' }}
              </el-button>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="warning" class="register-btn" @click="register" :loading="loading" round>
              立即注册
            </el-button>
          </el-form-item>
        </el-form>
        <div class="register-login-tip">
          已有账号？<router-link to="/user/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Message, Edit } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const codeLoading = ref(false)
const codeCountdown = ref(0)
let codeTimer = null

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  code: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: (rule, value) => value === form.value.password, message: '两次密码不一致', trigger: 'blur' }
  ],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

async function getCode() {
  if (!form.value.email) {
    ElMessage.warning('请先输入邮箱')
    return
  }
  codeLoading.value = true
  try {
    await request.post('/users/sendCode', null, { params: { email: form.value.email } })
    ElMessage.success('验证码已发送')
    codeCountdown.value = 60
    codeTimer = setInterval(() => {
      codeCountdown.value--
      if (codeCountdown.value <= 0) clearInterval(codeTimer)
    }, 1000)
  } catch (e) {
    ElMessage.error('验证码发送失败')
  } finally {
    codeLoading.value = false
  }
}

async function register() {
  await formRef.value.validate()
  loading.value = true
  try {
    const res = await request.post('/users/add', {
      username: form.value.username,
      password: form.value.password,
      email: form.value.email
    }, { params: { code: form.value.code } })
    if (res.code === 200) {
      ElMessage.success('注册成功，请登录')
      router.push('/user/login')
    } else {
      ElMessage.error(res.msg || '注册失败')
    }
  } catch (e) {
    ElMessage.error('注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}
.register-left {
  flex: 1.8;
  background: url('@/assets/231.png') center center/cover no-repeat;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: flex-start;
  padding: 0 0 60px 60px;
}
.welcome-title {
  font-size: 2.4rem;
  font-weight: bold;
  color: #fff;
  margin-bottom: 16px;
  text-shadow: 0 2px 12px rgba(0,0,0,0.28);
}
.welcome-desc {
  font-size: 1.15rem;
  color: #fff;
  margin-bottom: 40px;
  text-shadow: 0 1px 6px rgba(0,0,0,0.18);
  max-width: 420px;
}
.welcome-avatars {
  position: absolute;
  left: 60px;
  bottom: -16px;
  z-index: 2;
}
.welcome-avatars img {
  height: 90px;
  filter: drop-shadow(0 4px 16px rgba(0,0,0,0.18));
  /* 让图片部分溢出到右侧 */
  transform: translateX(40px);
}
.register-right {
  flex: 1;
  background: linear-gradient(120deg, #181818 80%, #232b3b 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  z-index: 1;
}
.register-card {
  width: 400px;
  background: rgba(30, 34, 44, 0.82);
  color: #fff;
  padding: 44px 36px 28px 36px;
  border-radius: 28px;
  box-shadow: 0 8px 40px 0 rgba(0,0,0,0.22);
  display: flex;
  flex-direction: column;
  align-items: center;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1.5px solid rgba(255,255,255,0.08);
}
.register-title {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 10px;
  text-align: center;
  letter-spacing: 1px;
}
.register-subtitle {
  font-size: 1.05rem;
  color: #ccc;
  margin-bottom: 28px;
  text-align: center;
}
.register-form {
  width: 100%;
}
.code-row {
  display: flex;
  align-items: center;
  gap: 8px;
}
.code-btn {
  min-width: 100px;
}
.register-btn {
  width: 100%;
  font-weight: 600;
  font-size: 1.1rem;
  margin-top: 8px;
  border-radius: 12px;
}
.register-login-tip {
  margin-top: 22px;
  color: #ccc;
  font-size: 1rem;
  text-align: center;
}
.register-login-tip a {
  color: #ffd04b;
  text-decoration: underline;
  margin-left: 4px;
}
.register-login-tip a:hover {
  color: #409eff;
}
@media (max-width: 900px) {
  .register-container { flex-direction: column; }
  .register-left, .register-right { flex: none; width: 100vw; }
  .register-card { margin: 32px auto; }
  .welcome-avatars { left: 24px; }
}
</style>