<template>
  <div class="profile-page">
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
              <el-avatar :src="userInfo.avatar" :size="100">
                {{ userInfo.name?.charAt(0) }}
              </el-avatar>
              <el-button type="primary" @click="showAvatarUpload = true">
                更换头像
              </el-button>
            </div>
            <div class="user-info">
              <h2>{{ userInfo.name }}</h2>
              <p class="user-email">{{ userInfo.email }}</p>
              <div class="user-stats">
                <div class="stat-item">
                  <span class="stat-value">{{ userStats.posts }}</span>
                  <span class="stat-label">文章</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ userStats.comments }}</span>
                  <span class="stat-label">评论</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ userStats.likes }}</span>
                  <span class="stat-label">点赞</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 编辑信息表单 -->
        <div class="edit-form-card">
          <h3>编辑个人信息</h3>
          <el-form :model="editForm" :rules="rules" ref="editFormRef" label-width="100px">
            <el-form-item label="用户名" prop="name">
              <el-input v-model="editForm.name" placeholder="请输入用户名" />
            </el-form-item>
            
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="editForm.email" placeholder="请输入邮箱" disabled />
            </el-form-item>
            
            <el-form-item label="个人简介" prop="bio">
              <el-input 
                v-model="editForm.bio" 
                type="textarea" 
                :rows="4"
                placeholder="介绍一下自己吧..."
              />
            </el-form-item>
            
            <el-form-item label="个人网站" prop="website">
              <el-input v-model="editForm.website" placeholder="https://example.com" />
            </el-form-item>
            
            <el-form-item label="所在地" prop="location">
              <el-input v-model="editForm.location" placeholder="请输入所在地" />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="handleSave" :loading="saving">
                保存修改
              </el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 修改密码 -->
        <div class="password-card">
          <h3>修改密码</h3>
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
            <el-form-item label="当前密码" prop="currentPassword">
              <el-input 
                v-model="passwordForm.currentPassword" 
                type="password" 
                placeholder="请输入当前密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item label="新密码" prop="newPassword">
              <el-input 
                v-model="passwordForm.newPassword" 
                type="password" 
                placeholder="请输入新密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input 
                v-model="passwordForm.confirmPassword" 
                type="password" 
                placeholder="请确认新密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="handlePasswordChange" :loading="changingPassword">
                修改密码
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 头像上传对话框 -->
    <el-dialog v-model="showAvatarUpload" title="更换头像" width="400px">
      <el-upload
        class="avatar-uploader"
        action="#"
        :show-file-list="false"
        :before-upload="beforeAvatarUpload"
        :http-request="handleAvatarUpload"
      >
        <img v-if="newAvatar" :src="newAvatar" class="avatar-preview" />
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
      <template #footer>
        <el-button @click="showAvatarUpload = false">取消</el-button>
        <el-button type="primary" @click="confirmAvatarUpload" :disabled="!newAvatar">
          确认上传
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { userApi, commonApi } from '@/utils/api'

// 响应式数据
const saving = ref(false)
const changingPassword = ref(false)
const showAvatarUpload = ref(false)
const newAvatar = ref('')
const editFormRef = ref()
const passwordFormRef = ref()

const userInfo = ref({
  id: null,
  name: '',
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
  email: '',
  bio: '',
  website: '',
  location: ''
})

const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 验证规则
const rules = {
  name: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const validateConfirmPassword = (rule, value, callback) => {
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
    const response = await commonApi.getCurrentUser()
    if (response.code === 200) {
      const userData = response.data.user
      userInfo.value = {
        id: userData.id,
        name: userData.username || userData.name || '',
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
  }
}

// 初始化表单数据
const initForm = () => {
  editForm.name = userInfo.value.name
  editForm.email = userInfo.value.email
  editForm.bio = userInfo.value.bio
  editForm.website = userInfo.value.website
  editForm.location = userInfo.value.location
}

// 保存个人信息
const handleSave = async () => {
  if (!editFormRef.value) return

  try {
    await editFormRef.value.validate()
    saving.value = true

    // 准备更新数据
    const updateData = {
      id: userInfo.value.id,
      username: editForm.name,
      email: editForm.email,
      bio: editForm.bio,
      website: editForm.website,
      location: editForm.location
    }

    // 调用更新API
    const response = await userApi.updateProfile(updateData)

    if (response.code === 200) {
      // 更新本地用户信息
      Object.assign(userInfo.value, editForm)
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

// 新增：保存文件对象
const newAvatarFile = ref(null)

// 确认头像上传
const confirmAvatarUpload = async () => {
  if (!newAvatarFile.value) {
    ElMessage.error('请选择头像文件')
    return
  }

  try {
    const formData = new FormData()
    formData.append('file', newAvatarFile.value)

    const response = await userApi.uploadAvatar(formData)

    if (response.code === 200) {
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

// 初始化
onMounted(async () => {
  await fetchUserInfo()
})
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 80px 0 40px;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 3rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 15px;
  text-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.page-subtitle {
  font-size: 1.2rem;
  color: #7f8c8d;
  margin: 0;
}

.profile-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.profile-card,
.edit-form-card,
.password-card {
  background: white;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
}

.profile-header {
  display: flex;
  gap: 30px;
  align-items: center;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.user-info h2 {
  font-size: 1.8rem;
  color: #2c3e50;
  margin-bottom: 5px;
}

.user-email {
  color: #7f8c8d;
  margin-bottom: 20px;
}

.user-stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 1.5rem;
  font-weight: 600;
  color: #3498db;
}

.stat-label {
  font-size: 0.9rem;
  color: #7f8c8d;
}

.edit-form-card h3,
.password-card h3 {
  font-size: 1.5rem;
  color: #2c3e50;
  margin-bottom: 25px;
  padding-bottom: 10px;
  border-bottom: 2px solid #3498db;
}

.avatar-uploader {
  display: flex;
  justify-content: center;
}

.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar-preview {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 2.5rem;
  }
  
  .profile-header {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }
  
  .user-stats {
    justify-content: center;
  }
  
  .profile-card,
  .edit-form-card,
  .password-card {
    padding: 20px;
  }
}
</style>
