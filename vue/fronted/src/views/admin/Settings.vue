<template>
  <div class="settings">
    <h1 class="page-title">系统设置</h1>
    
    <el-card>
      <template #header>
        <div class="card-header">
          <span>基本设置</span>
        </div>
      </template>
      
      <el-form
        ref="formRef"
        :model="settings"
        :rules="formRules"
        label-width="120px"
        style="max-width: 600px"
      >
        <el-form-item label="网站名称" prop="siteName">
          <el-input v-model="settings.siteName" placeholder="请输入网站名称" />
        </el-form-item>
        
        <el-form-item label="网站描述" prop="siteDescription">
          <el-input
            v-model="settings.siteDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入网站描述"
          />
        </el-form-item>
        
        <el-form-item label="网站关键词" prop="siteKeywords">
          <el-input v-model="settings.siteKeywords" placeholder="请输入网站关键词，用逗号分隔" />
        </el-form-item>
        
        <el-form-item label="ICP备案号" prop="icpNumber">
          <el-input v-model="settings.icpNumber" placeholder="请输入ICP备案号" />
        </el-form-item>
        
        <el-form-item label="网站LOGO" prop="siteLogo">
          <div class="logo-upload">
            <el-upload
              class="logo-uploader"
              :action="uploadUrl"
              :show-file-list="false"
              :on-success="handleLogoSuccess"
              :before-upload="beforeLogoUpload"
              accept="image/*"
            >
              <img v-if="settings.siteLogo" :src="getImageUrl(settings.siteLogo)" class="logo" />
              <el-icon v-else class="logo-uploader-icon"><Plus /></el-icon>
            </el-upload>
            <div class="logo-tips">
              <p>建议尺寸：200x60px</p>
              <p>支持格式：JPG、PNG、GIF</p>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item label="邮箱设置">
          <el-switch
            v-model="settings.emailEnabled"
            active-text="启用邮箱通知"
            inactive-text="禁用邮箱通知"
          />
        </el-form-item>
        
        <el-form-item label="评论审核">
          <el-switch
            v-model="settings.commentReview"
            active-text="需要审核"
            inactive-text="自动通过"
          />
        </el-form-item>
        
        <el-form-item label="用户注册">
          <el-switch
            v-model="settings.userRegister"
            active-text="允许注册"
            inactive-text="禁止注册"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="saveSettings" :loading="submitLoading">
            保存设置
          </el-button>
          <el-button @click="resetSettings">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

// 响应式数据
const submitLoading = ref(false)
const uploadUrl = ref('http://localhost:9090/common/upload/image')
const settings = ref({
  siteName: 'RE-BIN 博客',
  siteDescription: '一个简洁优雅的个人博客系统',
  siteKeywords: '博客,技术,分享,生活',
  icpNumber: '',
  siteLogo: '',
  emailEnabled: true,
  commentReview: false,
  userRegister: true
})

// 表单验证规则
const formRules = {
  siteName: [
    { required: true, message: '请输入网站名称', trigger: 'blur' }
  ],
  siteDescription: [
    { required: true, message: '请输入网站描述', trigger: 'blur' }
  ]
}

const formRef = ref()

// 获取图片URL
const getImageUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return `http://localhost:9090${path}`
}

// LOGO上传成功处理
const handleLogoSuccess = (response) => {
  if (response.code === 200) {
    settings.value.siteLogo = response.data
    ElMessage.success('LOGO上传成功')
  } else {
    ElMessage.error('LOGO上传失败')
  }
}

// LOGO上传前验证
const beforeLogoUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 保存设置
const saveSettings = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    // 这里应该调用API保存设置
    // await adminApi.saveSettings(settings.value)
    
    // 模拟保存
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('设置保存成功')
    
    // 保存到本地存储
    localStorage.setItem('siteSettings', JSON.stringify(settings.value))
    
  } catch (error) {
    console.error('保存设置失败:', error)
    ElMessage.error('保存设置失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置设置
const resetSettings = () => {
  settings.value = {
    siteName: 'RE-BIN 博客',
    siteDescription: '一个简洁优雅的个人博客系统',
    siteKeywords: '博客,技术,分享,生活',
    icpNumber: '',
    siteLogo: '',
    emailEnabled: true,
    commentReview: false,
    userRegister: true
  }
  ElMessage.info('设置已重置')
}

// 加载设置
const loadSettings = () => {
  try {
    const savedSettings = localStorage.getItem('siteSettings')
    if (savedSettings) {
      settings.value = { ...settings.value, ...JSON.parse(savedSettings) }
    }
  } catch (error) {
    console.error('加载设置失败:', error)
  }
}

// 初始化
onMounted(() => {
  loadSettings()
})
</script>

<style scoped>
.settings {
  padding: 20px;
}

.page-title {
  margin: 0 0 30px 0;
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-upload {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.logo-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
}

.logo-uploader:hover {
  border-color: #409eff;
}

.logo-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 200px;
  height: 60px;
  text-align: center;
  line-height: 60px;
}

.logo {
  width: 200px;
  height: 60px;
  display: block;
  object-fit: contain;
}

.logo-tips {
  color: #909399;
  font-size: 12px;
}

.logo-tips p {
  margin: 0 0 5px 0;
}

@media (max-width: 768px) {
  .settings {
    padding: 15px;
  }
  
  .logo-upload {
    flex-direction: column;
    align-items: center;
  }
  
  .logo-tips {
    text-align: center;
    margin-top: 10px;
  }
}
</style>
