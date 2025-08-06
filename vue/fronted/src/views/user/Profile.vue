<template>
  <div class="profile-page">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <div class="page-header">
      <h1 class="page-title">个人中心</h1>
      <p class="page-subtitle">管理您的个人信息和账户设置</p>
    </div>

    <div class="profile-container">
      <div class="profile-content">
        <!-- 个人信息卡片 -->
        <div class="profile-card">
          <div class="profile-header">
            <div class="avatar-section">
              <div class="avatar-wrapper">
                <el-avatar :src="getAvatarUrl(userInfo.avatar)" :size="120">
                  {{ userInfo.name?.charAt(0) || 'U' }}
                </el-avatar>
                <div class="avatar-overlay" @click="showAvatarUpload = true">
                  <el-icon><Camera /></el-icon>
                </div>
              </div>
              <el-button type="primary" size="small" @click="showAvatarUpload = true" class="change-avatar-btn">
                <el-icon><Edit /></el-icon>
                更换头像
              </el-button>
            </div>
            <div class="user-info">
              <h2 class="user-name">{{ userInfo.name || '未设置昵称' }}</h2>
              <p class="user-username">@{{ userInfo.username }}</p>
              <p class="user-email">
                <el-icon><Message /></el-icon>
                {{ userInfo.email }}
              </p>
            </div>
          </div>
        </div>

        <!-- 编辑信息表单 -->
        <div class="edit-form-card">
          <div class="card-header">
            <h3><el-icon><User /></el-icon>编辑个人信息</h3>
          </div>
          <el-form :model="editForm" :rules="rules" ref="editFormRef" label-width="120px" class="profile-form">
            <div class="form-row">
              <el-form-item label="用户名" prop="name" class="form-item-half">
                <el-input
                  v-model="editForm.name"
                  placeholder="请输入显示名称"
                  prefix-icon="User"
                  clearable
                />
              </el-form-item>

              <el-form-item label="账号" prop="username" class="form-item-half">
                <el-input
                  v-model="editForm.username"
                  prefix-icon="UserFilled"
                  disabled
                />
                <div class="form-tip">账号不可修改</div>
              </el-form-item>
            </div>

            <el-form-item label="邮箱地址" prop="email">
              <el-input
                v-model="editForm.email"
                placeholder="请输入邮箱"
                prefix-icon="Message"
                disabled
              />
              <div class="form-tip">邮箱地址不可修改</div>
            </el-form-item>

            <el-form-item label="个人简介" prop="bio">
              <el-input
                v-model="editForm.bio"
                type="textarea"
                :rows="4"
                placeholder="介绍一下自己吧..."
                maxlength="200"
                show-word-limit
                resize="none"
              />
            </el-form-item>

            <div class="form-row">
              <el-form-item label="个人网站" prop="website" class="form-item-half">
                <el-input
                  v-model="editForm.website"
                  placeholder="https://example.com"
                  prefix-icon="Link"
                  clearable
                />
              </el-form-item>

              <el-form-item label="所在地" prop="location" class="form-item-half">
                <el-cascader
                  v-model="editForm.locationArray"
                  :options="locationOptions"
                  :props="cascaderProps"
                  placeholder="请选择所在地"
                  clearable
                  filterable
                  @change="handleLocationChange"
                  style="width: 100%"
                />
              </el-form-item>
            </div>

            <el-form-item class="form-actions">
              <el-button type="primary" @click="handleSave" :loading="saving" size="large">
                <el-icon><Check /></el-icon>
                保存修改
              </el-button>
              <el-button @click="resetForm" size="large">
                <el-icon><RefreshLeft /></el-icon>
                重置
              </el-button>
            </el-form-item>
          </el-form>
          <div class="post-actions">
            <el-button @click="goBack">
              <el-icon><ArrowLeft /></el-icon>
              返回
            </el-button>
          </div>
        </div>

        <!-- 修改密码 -->
        <div class="password-card">
          <div class="card-header">
            <h3><el-icon><Lock /></el-icon>修改密码</h3>
          </div>
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="120px" class="password-form">
            <el-form-item label="当前密码" prop="currentPassword">
              <el-input
                v-model="passwordForm.currentPassword"
                type="password"
                placeholder="请输入当前密码"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>

            <div class="form-row">
              <el-form-item label="新密码" prop="newPassword" class="form-item-half">
                <el-input
                  v-model="passwordForm.newPassword"
                  type="password"
                  placeholder="请输入新密码"
                  prefix-icon="Key"
                  show-password
                />
              </el-form-item>

              <el-form-item label="确认密码" prop="confirmPassword" class="form-item-half">
                <el-input
                  v-model="passwordForm.confirmPassword"
                  type="password"
                  placeholder="请确认新密码"
                  prefix-icon="Key"
                  show-password
                />
              </el-form-item>
            </div>

            <el-form-item class="form-actions">
              <el-button type="primary" @click="handlePasswordChange" :loading="changingPassword" size="large">
                <el-icon><Check /></el-icon>
                修改密码
              </el-button>
              <el-button @click="resetPasswordForm" size="large">
                <el-icon><RefreshLeft /></el-icon>
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 头像上传对话框 -->
    <el-dialog v-model="showAvatarUpload" title="更换头像" width="500px" class="avatar-dialog">
      <div class="upload-container">
        <el-upload
          class="avatar-uploader"
          action="#"
          :show-file-list="false"
          :before-upload="beforeAvatarUpload"
          :http-request="handleAvatarUpload"
          drag
        >
          <div v-if="newAvatar" class="avatar-preview-container">
            <img :src="newAvatar" class="avatar-preview" />
            <div class="preview-overlay">
              <el-icon><Plus /></el-icon>
              <p>点击重新选择</p>
            </div>
          </div>
          <div v-else class="upload-placeholder">
            <el-icon class="upload-icon"><Plus /></el-icon>
            <div class="upload-text">
              <p>将图片拖到此处，或<em>点击上传</em></p>
              <p class="upload-tip">支持 JPG、PNG 格式，文件大小不超过 2MB</p>
            </div>
          </div>
        </el-upload>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelAvatarUpload" size="large">取消</el-button>
          <el-button type="primary" @click="confirmAvatarUpload" :disabled="!newAvatar" size="large">
            <el-icon><Check /></el-icon>
            确认上传
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Plus, Camera, Edit, Message, User, UserFilled, Link, Location,
  Check, RefreshLeft, Lock, Key
} from '@element-plus/icons-vue'
import { userApi } from '@/utils/api'
import { getAvatarUrl } from '@/utils/avatar'
import { locationOptions as importedLocationOptions } from '@/data/locations'
import {useRouter} from "vue-router";


// 响应式数据
const saving = ref(false)
const changingPassword = ref(false)
const showAvatarUpload = ref(false)
const newAvatar = ref('')
const newAvatarFile = ref(null)
const editFormRef = ref()
const passwordFormRef = ref()
const router = useRouter()

const userInfo = ref({
  id: null,
  name: '',
  username: '',
  email: '',
  avatar: '',
  bio: '',
  website: '',
  location: ''
})

const userStats = ref({
  posts: 0,
  comments: 0,
  likes: 0
})

const editForm = reactive({
  name: '',
  username: '',
  email: '',
  bio: '',
  website: '',
  location: '',
  locationArray: []
})

const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 地址级联选择器配置
const cascaderProps = {
  value: 'value',
  label: 'label',
  children: 'children',
  emitPath: false
}

// 地址选项数据
const locationOptions = ref(importedLocationOptions)

// 验证规则
const rules = {
  name: [
    { required: true, message: '请输入显示名称', trigger: 'blur' },
    { min: 2, max: 20, message: '显示名称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  website: [
    { type: 'url', message: '请输入正确的网址格式', trigger: 'blur' }
  ]
}

// 返回上一级
const goBack = () => {
  router.go(-1)
}

const validateConfirmPassword = (_rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const response = await userApi.getUserInfo()
    if (response.code === 200) {
      const userData = response.data.user
      userInfo.value = {
        id: userData.id,
        name: userData.name || '',
        username: userData.username || '',
        email: userData.email || '',
        avatar: userData.avatar || '',
        bio: userData.bio || '',
        website: userData.website || '',
        location: userData.location || ''
      }

      // 获取用户统计信息（如果有相关API）
      // await fetchUserStats()

      initForm()
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
    // 如果获取用户信息失败，可能是未登录，跳转到登录页
    if (error.message === '未登录') {
      ElMessage.warning('请先登录')
      // 可以选择跳转到登录页
      // router.push('/user/login')
    }
  }
}

// 处理地址级联选择器变化
const handleLocationChange = (value) => {
  if (value && value.length > 0) {
    // 根据选择的值构建地址字符串
    const selectedLocation = findLocationPath(locationOptions.value, value)
    editForm.location = selectedLocation.join(' - ')
  } else {
    editForm.location = ''
  }
}

// 根据值查找地址路径
const findLocationPath = (options, targetValue) => {
  for (const option of options) {
    if (option.value === targetValue) {
      return [option.label]
    }
    if (option.children) {
      const childPath = findLocationPath(option.children, targetValue)
      if (childPath.length > 0) {
        return [option.label, ...childPath]
      }
    }
  }
  return []
}

// 根据地址字符串查找对应的级联值
const findLocationValue = (options, locationString) => {
  if (!locationString) return []

  const parts = locationString.split(' - ')
  let currentOptions = options
  let result = []

  for (const part of parts) {
    const found = currentOptions.find(option => option.label === part)
    if (found) {
      result.push(found.value)
      currentOptions = found.children || []
    } else {
      break
    }
  }

  return result.length === parts.length ? result[result.length - 1] : ''
}

// 初始化表单数据
const initForm = () => {
  editForm.name = userInfo.value.name
  editForm.username = userInfo.value.username
  editForm.email = userInfo.value.email
  editForm.bio = userInfo.value.bio
  editForm.website = userInfo.value.website
  editForm.location = userInfo.value.location

  // 初始化地址级联选择器的值
  editForm.locationArray = findLocationValue(locationOptions.value, userInfo.value.location)
}

// 保存个人信息
const handleSave = async () => {
  if (!editFormRef.value) return

  try {
    await editFormRef.value.validate()
    saving.value = true

    // 准备更新数据 - 修复字段映射问题
    const updateData = {
      id: userInfo.value.id,
      name: editForm.name,        // 显示名称对应数据库的name字段
      username: editForm.username, // 用户名对应数据库的username字段
      email: editForm.email,
      bio: editForm.bio,
      website: editForm.website,
      location: editForm.location
    }

    // 调用更新API
    const response = await userApi.updateProfile(updateData)

    if (response.code === 200) {
      // 更新本地用户信息
      userInfo.value.name = editForm.name
      userInfo.value.username = editForm.username
      userInfo.value.bio = editForm.bio
      userInfo.value.website = editForm.website
      userInfo.value.location = editForm.location
      ElMessage.success('保存成功')
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// 重置表单
const resetForm = () => {
  initForm()
}

// 重置密码表单
const resetPasswordForm = () => {
  passwordForm.currentPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

// 修改密码
const handlePasswordChange = async () => {
  if (!passwordFormRef.value) return

  try {
    await passwordFormRef.value.validate()
    changingPassword.value = true

    // 调用修改密码API
    const response = await userApi.changePassword({
      currentPassword: passwordForm.currentPassword,
      newPassword: passwordForm.newPassword
    })

    if (response.code === 200) {
      ElMessage.success('密码修改成功')

      // 清空表单
      passwordForm.currentPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    } else {
      ElMessage.error(response.message || '密码修改失败')
    }
  } catch (error) {
    console.error('密码修改失败:', error)
    ElMessage.error('密码修改失败')
  } finally {
    changingPassword.value = false
  }
}

// 头像上传前验证
const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('头像图片只能是 JPG/PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 处理头像上传
const handleAvatarUpload = (options) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    newAvatar.value = e.target.result
  }
  reader.readAsDataURL(options.file)

  // 保存文件对象用于上传
  newAvatarFile.value = options.file
}

// 确认头像上传
const confirmAvatarUpload = async () => {
  if (!newAvatarFile.value) {
    ElMessage.error('请选择头像文件')
    return
  }

  try {
    const response = await userApi.uploadAvatar(newAvatarFile.value)

    if (response.code === 200) {
      // 后端返回的是相对路径，直接保存
      userInfo.value.avatar = response.data
      showAvatarUpload.value = false
      newAvatar.value = ''
      newAvatarFile.value = null
      ElMessage.success('头像更新成功')
    } else {
      ElMessage.error(response.message || '头像上传失败')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error('头像上传失败')
  }
}

// 取消头像上传
const cancelAvatarUpload = () => {
  showAvatarUpload.value = false
  newAvatar.value = ''
  newAvatarFile.value = null
}

// 初始化
onMounted(async () => {
  await fetchUserInfo()
})
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 80px 0 40px;
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 200px;
  height: 200px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.circle-2 {
  width: 150px;
  height: 150px;
  top: 60%;
  right: 10%;
  animation-delay: 2s;
}

.circle-3 {
  width: 100px;
  height: 100px;
  top: 30%;
  right: 30%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
  position: relative;
  z-index: 1;
}

.page-title {
  font-size: 3.5rem;
  font-weight: 800;
  color: white;
  margin-bottom: 15px;
  text-shadow: 0 4px 20px rgba(0,0,0,0.3);
  letter-spacing: -1px;
}

.page-subtitle {
  font-size: 1.3rem;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  font-weight: 300;
}

.profile-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
  position: relative;
  z-index: 1;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.profile-card,
.edit-form-card,
.password-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 35px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.profile-card:hover,
.edit-form-card:hover,
.password-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 25px 50px rgba(0,0,0,0.15);
}

/* 个人信息卡片样式 */
.profile-header {
  display: flex;
  gap: 40px;
  align-items: center;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.avatar-wrapper {
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}

.avatar-wrapper:hover {
  transform: scale(1.05);
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.3s ease;
  color: white;
  font-size: 24px;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.change-avatar-btn {
  border-radius: 20px;
  padding: 8px 16px;
  font-size: 14px;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 2.2rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.user-username {
  font-size: 1.1rem;
  color: #7f8c8d;
  margin-bottom: 10px;
  font-weight: 500;
}

.user-email {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #7f8c8d;
  margin-bottom: 25px;
  font-size: 1rem;
}

.user-stats {
  display: flex;
  gap: 40px;
}

.stat-item {
  text-align: center;
  padding: 15px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 15px;
  color: white;
  min-width: 80px;
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
}

.stat-value {
  display: block;
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.9;
  font-weight: 500;
}

/* 卡片标题样式 */
.card-header {
  margin-bottom: 30px;
}

.card-header h3 {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1.6rem;
  color: #2c3e50;
  margin: 0;
  padding-bottom: 15px;
  border-bottom: 3px solid transparent;
  background: linear-gradient(90deg, #667eea, #764ba2) bottom;
  background-size: 100% 3px;
  background-repeat: no-repeat;
  font-weight: 600;
}

/* 表单样式 */
.profile-form,
.password-form {
  margin-top: 10px;
}

.profile-form :deep(.el-form-item__label),
.password-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: #2c3e50;
  line-height: 40px;
  text-align: right;
  padding-right: 12px;
}

.profile-form :deep(.el-form-item__content),
.password-form :deep(.el-form-item__content) {
  line-height: 40px;
}

.profile-form :deep(.el-input__wrapper),
.password-form :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.profile-form :deep(.el-input__wrapper:hover),
.password-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

.profile-form :deep(.el-input__wrapper.is-focus),
.password-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.profile-form :deep(.el-textarea__inner) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.profile-form :deep(.el-textarea__inner:hover) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

.profile-form :deep(.el-textarea__inner:focus) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.profile-form :deep(.el-cascader),
.password-form :deep(.el-cascader) {
  width: 100%;
}

.profile-form :deep(.el-cascader .el-input__wrapper),
.password-form :deep(.el-cascader .el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.form-row {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.form-item-half {
  flex: 1;
}

.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
  padding-left: 2px;
}

.form-actions {
  margin-top: 30px;
  text-align: center;
}

.form-actions .el-button {
  margin: 0 10px;
  padding: 12px 30px;
  border-radius: 25px;
  font-weight: 600;
  transition: all 0.3s ease;
  min-width: 120px;
}

.form-actions .el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.form-actions .el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

/* 头像上传对话框样式 */
.avatar-dialog :deep(.el-dialog) {
  border-radius: 20px;
  overflow: hidden;
}

.avatar-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px 30px;
}

.avatar-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

.upload-container {
  padding: 20px 0;
}

.avatar-uploader {
  display: flex;
  justify-content: center;
}

.avatar-uploader :deep(.el-upload) {
  border: 2px dashed #d9d9d9;
  border-radius: 15px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  width: 300px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.05);
}

.avatar-preview-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.avatar-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 10px;
}

.preview-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.3s ease;
  color: white;
  border-radius: 10px;
}

.avatar-preview-container:hover .preview-overlay {
  opacity: 1;
}

.upload-placeholder {
  text-align: center;
  color: #8c939d;
}

.upload-icon {
  font-size: 48px;
  margin-bottom: 15px;
  color: #667eea;
}

.upload-text p {
  margin: 5px 0;
  font-size: 16px;
}

.upload-text em {
  color: #667eea;
  font-style: normal;
  font-weight: 600;
}

.upload-tip {
  font-size: 12px;
  color: #999;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 15px;
  padding: 20px 0 10px;
}

.dialog-footer .el-button {
  padding: 10px 25px;
  border-radius: 20px;
  font-weight: 600;
}

/* 级联选择器样式优化 */
.profile-form :deep(.el-cascader-panel) {
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
}

.profile-form :deep(.el-cascader-menu) {
  border-radius: 8px;
}

.profile-form :deep(.el-cascader-node:hover) {
  background-color: rgba(102, 126, 234, 0.1);
}

.profile-form :deep(.el-cascader-node.is-active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

/* 表单项间距优化 */
.profile-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

.password-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

/* 输入框聚焦状态优化 */
.profile-form :deep(.el-input.is-focus .el-input__wrapper),
.password-form :deep(.el-input.is-focus .el-input__wrapper) {
  border-color: #667eea;
  box-shadow: 0 0 0 1px rgba(102, 126, 234, 0.2);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 2.8rem;
  }

  .profile-header {
    flex-direction: column;
    text-align: center;
    gap: 25px;
  }

  .user-stats {
    justify-content: center;
    gap: 20px;
  }

  .stat-item {
    min-width: 70px;
    padding: 12px 15px;
  }

  .profile-card,
  .edit-form-card,
  .password-card {
    padding: 25px 20px;
  }

  .form-row {
    flex-direction: column;
    gap: 0;
  }

  .user-name {
    font-size: 1.8rem;
  }

  .avatar-uploader :deep(.el-upload) {
    width: 250px;
    height: 180px;
  }

  /* 移动端表单标签宽度调整 */
  .profile-form :deep(.el-form-item__label),
  .password-form :deep(.el-form-item__label) {
    width: 100px !important;
    text-align: left;
  }
}

@media (max-width: 480px) {
  .page-title {
    font-size: 2.2rem;
  }

  .profile-container {
    padding: 0 15px;
  }

  .user-stats {
    flex-wrap: wrap;
    gap: 15px;
  }

  .stat-item {
    min-width: 60px;
    padding: 10px 12px;
  }

  .stat-value {
    font-size: 1.5rem;
  }

  .form-actions .el-button {
    padding: 10px 20px;
    margin: 5px;
  }

  /* 小屏幕表单标签宽度进一步调整 */
  .profile-form :deep(.el-form-item__label),
  .password-form :deep(.el-form-item__label) {
    width: 80px !important;
    font-size: 14px;
  }
}
</style>
