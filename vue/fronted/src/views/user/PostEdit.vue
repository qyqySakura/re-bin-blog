<template>
  <div class="post-edit-page">
    <div class="page-header">
      <h1 class="page-title">{{ isEdit ? '编辑文章' : '写新文章' }}</h1>
      <p class="page-subtitle">{{ isEdit ? '修改您的文章内容' : '创作您的精彩内容' }}</p>

      <!-- 自动保存状态 -->
      <div class="auto-save-status" v-if="lastSaveTime || autoSaving">
        <span v-if="autoSaving" class="saving">
          <i class="fas fa-spinner fa-spin"></i>
          正在自动保存...
        </span>
        <span v-else-if="lastSaveTime" class="saved">
          <i class="fas fa-check"></i>
          已于 {{ formatTime(lastSaveTime) }} 自动保存
        </span>
      </div>
    </div>

    <div class="edit-container">
      <el-form 
        :model="postForm" 
        :rules="rules" 
        ref="postFormRef" 
        label-width="100px"
        class="post-form"
      >
        <div class="form-row">
          <el-form-item label="文章标题" prop="title" class="title-item">
            <el-input
              v-model="postForm.title"
              placeholder="请输入文章标题"
              maxlength="100"
              show-word-limit
              size="large"
            />
          </el-form-item>
        </div>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="文章分类" prop="categoryId">
              <el-select
                v-model="postForm.categoryId"
                placeholder="请选择分类"
                size="large"
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="category in categories"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文章标签">
              <el-select
                v-model="postForm.tagIds"
                multiple
                placeholder="请选择标签"
                size="large"
                style="width: 100%"
                filterable
                collapse-tags
                collapse-tags-tooltip
                :max-collapse-tags="3"
              >
                <el-option
                  v-for="tag in tags"
                  :key="tag.id"
                  :label="tag.name"
                  :value="tag.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="文章摘要" prop="summary">
          <el-input
            v-model="postForm.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入文章摘要，将显示在文章列表中（可选）"
            maxlength="200"
            show-word-limit
            resize="none"
          />
        </el-form-item>

        <el-form-item label="封面图片">
          <el-tabs v-model="coverTabActive" class="cover-tabs">
            <!-- URL方式 -->
            <el-tab-pane label="网络图片" name="url">
              <el-row :gutter="12">
                <el-col :span="18">
                  <el-input
                    v-model="postForm.cover"
                    placeholder="请输入封面图片URL（可选）"
                    clearable
                    @input="onCoverUrlChange"
                  >
                    <template #prepend>
                      <el-icon><i class="fas fa-link"></i></el-icon>
                    </template>
                  </el-input>
                </el-col>
                <el-col :span="6">
                  <el-button
                    type="primary"
                    plain
                    style="width: 100%"
                    @click="showCoverPreview = !showCoverPreview"
                    :disabled="!postForm.cover"
                  >
                    <el-icon><i class="fas fa-eye"></i></el-icon>
                    预览
                  </el-button>
                </el-col>
              </el-row>
            </el-tab-pane>

            <!-- 上传方式 -->
            <el-tab-pane label="上传图片" name="upload">
              <div class="cover-upload-area" @click="triggerCoverUpload" @dragover.prevent @drop.prevent="handleCoverDrop">
                <el-icon class="upload-icon" v-if="!coverUploading && !postForm.cover">
                  <i class="fas fa-cloud-upload-alt"></i>
                </el-icon>
                <div class="upload-text" v-if="!coverUploading && !postForm.cover">
                  <p>将封面图片拖拽到此处，或<em>点击上传</em></p>
                  <p class="upload-tip">建议尺寸：800x450，支持 JPG、PNG 格式，文件大小不超过 2MB</p>
                </div>
                <el-progress
                  v-if="coverUploading"
                  :percentage="coverUploadProgress"
                  :stroke-width="6"
                  status="success"
                />
                <div v-if="postForm.cover && !coverUploading && coverTabActive === 'upload'" class="uploaded-cover">
                  <img :src="postForm.cover" alt="封面图片" />
                  <div class="cover-actions">
                    <el-button size="small" @click.stop="clearCoverUpload">重新上传</el-button>
                    <el-button size="small" type="danger" @click.stop="removeCover">移除封面</el-button>
                  </div>
                </div>
              </div>
              <input
                ref="coverFileInput"
                type="file"
                accept="image/*"
                style="display: none"
                @change="handleCoverFileSelect"
              />
            </el-tab-pane>
          </el-tabs>

          <!-- 预览区域 -->
          <div v-if="showCoverPreview && postForm.cover && coverTabActive === 'url'" class="cover-preview">
            <img :src="postForm.cover" alt="封面预览" @error="coverError = true" @load="coverError = false" />
            <p v-if="coverError" class="error-text">图片加载失败，请检查URL是否正确</p>
          </div>
        </el-form-item>

        <div class="form-row">
          <el-form-item label="文章内容" prop="content" class="content-item">
            <div class="editor-container">
              <SimpleRichTextEditor
                v-model="postForm.content"
                style="min-height: 400px; width:900px ;"
              />
            </div>
          </el-form-item>
        </div>

        <div class="form-actions">
          <el-row :gutter="12" justify="center">
            <el-col :span="6">
              <el-button
                @click="goBack"
                size="large"
                style="width: 100%"
              >
                <el-icon><i class="fas fa-arrow-left"></i></el-icon>
                取消
              </el-button>
            </el-col>
            <el-col :span="6">
              <el-button
                type="info"
                @click="saveDraft"
                :loading="saving"
                size="large"
                style="width: 100%"
              >
                <el-icon><i class="fas fa-save"></i></el-icon>
                保存草稿
              </el-button>
            </el-col>
            <el-col :span="6">
              <el-button
                type="primary"
                @click="publishPost"
                :loading="publishing"
                size="large"
                style="width: 100%"
              >
                <el-icon><i class="fas fa-paper-plane"></i></el-icon>
                {{ isEdit ? '更新文章' : '发布文章' }}
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { fileApi, postApi } from '@/utils/api'
import SimpleRichTextEditor from '@/components/SimpleRichTextEditor.vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 响应式数据
const postFormRef = ref()
const loading = ref(false)
const saving = ref(false)
const publishing = ref(false)
const autoSaving = ref(false)
const categories = ref([])
const tags = ref([])
const lastSaveTime = ref(null)
const showCoverPreview = ref(false)
const coverError = ref(false)
const coverTabActive = ref('url')
const coverUploading = ref(false)
const coverUploadProgress = ref(0)
const coverFileInput = ref()

// 表单数据
const postForm = ref({
  id: null,
  title: '',
  content: '',
  summary: '',
  categoryId: null,
  tagIds: [],
  cover: '',
  status: 0 // 0=草稿, 1=已发布
})

// 是否为编辑模式
const isEdit = computed(() => !!route.query.id)

// 格式化时间
const formatTime = (date) => {
  if (!date) return ''
  return date.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' },
    { min: 1, max: 100, message: '标题长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入文章内容', trigger: 'blur' },
    { min: 10, message: '文章内容至少10个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择文章分类', trigger: 'change' }
  ]
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    const response = await request.get('/categories')
    if (response.code === 200) {
      categories.value = response.data || []
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

// 获取标签列表
const fetchTags = async () => {
  try {
    const response = await request.get('/tags')
    if (response.code === 200) {
      tags.value = response.data || []
    }
  } catch (error) {
    console.error('获取标签列表失败:', error)
  }
}

// 获取文章详情（编辑模式）
const fetchPostDetail = async (postId) => {
  try {
    loading.value = true
    const response = await request.get(`/posts/${postId}`)
    if (response.code === 200) {
      const post = response.data
      postForm.value = {
        id: post.id,
        title: post.title,
        content: post.content,
        summary: post.summary || '',
        categoryId: post.categoryId,
        tagIds: post.tags ? post.tags.map(tag => tag.id) : [],
        cover: post.cover || '',
        status: post.status
      }
    } else {
      ElMessage.error('获取文章详情失败')
      goBack()
    }
  } catch (error) {
    console.error('获取文章详情失败:', error)
    ElMessage.error('获取文章详情失败')
    goBack()
  } finally {
    loading.value = false
  }
}

// 保存草稿
const saveDraft = async () => {
  try {
    await postFormRef.value.validate()
    saving.value = true

    const formData = {
      ...postForm.value,
      status: 0, // 草稿状态
      userId: userStore.userInfo?.id // 添加用户ID
    }

    const response = isEdit.value
      ? await request.put('/posts/update', formData)
      : await request.post('/posts/add', formData)

    if (response.code === 200) {
      ElMessage.success('草稿保存成功')
      if (!isEdit.value) {
        // 新建文章成功后，切换到编辑模式
        postForm.value.id = response.data.id
        router.replace(`/user/edit?id=${response.data.id}`)
      }
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      console.error('保存草稿失败:', error)
      ElMessage.error('保存失败，请检查网络连接')
    }
  } finally {
    saving.value = false
  }
}

// 发布文章
const publishPost = async () => {
  try {
    await postFormRef.value.validate()
    publishing.value = true

    const formData = {
      ...postForm.value,
      status: 1, // 已发布状态
      userId: userStore.userInfo?.id // 添加用户ID
    }

    const response = isEdit.value
      ? await request.put('/posts/update', formData)
      : await request.post('/posts/add', formData)

    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '文章更新成功' : '文章发布成功')
      router.push('/user/posts')
    } else {
      ElMessage.error(response.message || (isEdit.value ? '更新失败' : '发布失败'))
    }
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      console.error('发布文章失败:', error)
      ElMessage.error('操作失败，请检查网络连接')
    }
  } finally {
    publishing.value = false
  }
}

// 自动保存草稿
const autoSaveDraft = async () => {
  if (!postForm.value.title && !postForm.value.content) {
    return // 没有内容时不自动保存
  }

  try {
    autoSaving.value = true
    const formData = {
      ...postForm.value,
      status: 0, // 草稿状态
      userId: userStore.userInfo?.id
    }

    const response = isEdit.value
      ? await request.put('/posts/update', formData)
      : await request.post('/posts/add', formData)

    if (response.code === 200) {
      lastSaveTime.value = new Date()
      if (!isEdit.value && response.data?.id) {
        // 新建文章成功后，切换到编辑模式
        postForm.value.id = response.data.id
        router.replace(`/user/edit?id=${response.data.id}`)
      }
    }
  } catch (error) {
    console.error('自动保存失败:', error)
  } finally {
    autoSaving.value = false
  }
}

// 返回上一页
const goBack = async () => {
  // 检查是否有未保存的内容
  if (postForm.value.title || postForm.value.content) {
    try {
      await ElMessageBox.confirm(
        '您有未保存的内容，是否保存为草稿？',
        '确认离开',
        {
          confirmButtonText: '保存并离开',
          cancelButtonText: '直接离开',
          distinguishCancelAndClose: true,
          type: 'warning'
        }
      )
      // 用户选择保存
      await saveDraft()
    } catch (action) {
      if (action === 'cancel') {
        // 用户选择直接离开，不做任何操作
      } else {
        // 用户点击了关闭按钮，取消离开
        return
      }
    }
  }
  router.push('/user/posts')
}

// 封面图片相关方法
const onCoverUrlChange = () => {
  coverError.value = false
  showCoverPreview.value = false
}

// 触发文件选择
const triggerCoverUpload = () => {
  coverFileInput.value.click()
}

// 处理文件选择
const handleCoverFileSelect = (event) => {
  const file = event.target.files[0]
  if (file) {
    handleCoverFile(file)
  }
}

// 处理拖拽上传
const handleCoverDrop = (event) => {
  const files = event.dataTransfer.files
  if (files.length > 0) {
    handleCoverFile(files[0])
  }
}

// 处理文件上传 - 简化版本，压缩交给后端处理
const handleCoverFile = (file) => {
  // 验证文件
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    return
  }
  if (file.size / 1024 / 1024 > 2) {
    ElMessage.error('封面图片大小不能超过 2MB!')
    return
  }

  // 开始上传过程
  coverUploading.value = true
  coverUploadProgress.value = 0

  // 直接读取文件为base64，让后端处理压缩
  const reader = new FileReader()
  reader.onload = (e) => {
    // 模拟上传进度
    const interval = setInterval(() => {
      coverUploadProgress.value += 10
      if (coverUploadProgress.value >= 100) {
        clearInterval(interval)
        coverUploading.value = false
        postForm.value.cover = e.target.result
        coverTabActive.value = 'upload'
        ElMessage.success('封面图片上传成功!')
      }
    }, 100)
  }
  reader.onerror = () => {
    coverUploading.value = false
    coverUploadProgress.value = 0
    ElMessage.error('文件读取失败，请重试')
  }
  reader.readAsDataURL(file)
}

const clearCoverUpload = () => {
  postForm.value.cover = ''
  coverUploadProgress.value = 0
  coverError.value = false
  if (coverFileInput.value) {
    coverFileInput.value.value = ''
  }
}

const removeCover = () => {
  ElMessageBox.confirm(
    '确定要移除封面图片吗？',
    '确认操作',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    clearCoverUpload()
    ElMessage.success('封面图片已移除')
  }).catch(() => {
    // 用户取消操作
  })
}

// 页面离开前的确认
const beforeUnload = (event) => {
  // 检查是否有未保存的内容
  if (postForm.value.title || postForm.value.content) {
    event.preventDefault()
    event.returnValue = '您有未保存的内容，确定要离开吗？'
    return '您有未保存的内容，确定要离开吗？'
  }
}

// 初始化
onMounted(async () => {
  // 添加页面离开前的确认
  window.addEventListener('beforeunload', beforeUnload)

  try {
    await Promise.all([
      fetchCategories(),
      fetchTags()
    ])

    // 如果是编辑模式，获取文章详情
    if (isEdit.value) {
      await fetchPostDetail(route.query.id)
    }
  } catch (error) {
    console.error('初始化失败:', error)
    ElMessage.error('页面初始化失败，请刷新重试')
  }
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('beforeunload', beforeUnload)
})
</script>

<style scoped>
.post-edit-page {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--el-color-primary-light-9) 0%, var(--el-color-primary-light-8) 100%);
  padding: 80px 0 40px;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: var(--el-border-radius-large);
  backdrop-filter: blur(10px);
  box-shadow: var(--el-box-shadow-light);
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--el-color-primary);
  margin-bottom: 15px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-subtitle {
  font-size: 1.1rem;
  color: var(--el-text-color-regular);
  margin: 0;
}

.auto-save-status {
  margin-top: 15px;
  font-size: 0.9rem;
  padding: 8px 16px;
  border-radius: var(--el-border-radius-base);
  background: var(--el-fill-color-lighter);
  border: 1px solid var(--el-border-color-light);
}

.auto-save-status .saving {
  color: var(--el-color-primary);
}

.auto-save-status .saved {
  color: var(--el-color-success);
}

.auto-save-status i {
  margin-right: 5px;
}

.edit-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.post-form {
  background: var(--el-bg-color);
  padding: 40px;
  border-radius: var(--el-border-radius-large);
  box-shadow: var(--el-box-shadow);
  border: 1px solid var(--el-border-color-lighter);
}

.form-row {
  margin-bottom: 25px;
}

.form-row:last-child {
  margin-bottom: 0;
}

.title-item :deep(.el-form-item__content) {
  width: 100%;
}

.content-item {
  margin-bottom: 30px;
}

.editor-container {
  border: 1px solid var(--el-border-color);
  border-radius: var(--el-border-radius-base);
  overflow: hidden;
  box-shadow: var(--el-box-shadow-light);
}

.content-textarea :deep(.el-textarea__inner) {
  border: none;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', monospace;
  font-size: 14px;
  line-height: 1.6;
  resize: vertical;
}

.form-actions {
  text-align: center;
  padding-top: 40px;
  border-top: 2px solid var(--el-border-color-lighter);
  margin-top: 40px;
}

.form-actions .el-button {
  min-width: 120px;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
}

.cover-preview {
  margin-top: 12px;
  padding: 16px;
  border: 1px solid var(--el-border-color-light);
  border-radius: var(--el-border-radius-base);
  background: var(--el-fill-color-lighter);
  text-align: center;
}

.cover-preview img {
  max-width: 100%;
  max-height: 200px;
  border-radius: var(--el-border-radius-base);
  box-shadow: var(--el-box-shadow-light);
}

.error-text {
  color: var(--el-color-danger);
  font-size: 14px;
  margin: 8px 0 0 0;
}

/* 封面上传样式 */
.cover-tabs {
  margin-bottom: 16px;
}

.cover-uploader {
  width: 100%;
}

.cover-upload-area {
  width: 100%;
  min-height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px dashed var(--el-border-color);
  border-radius: var(--el-border-radius-base);
  background: var(--el-fill-color-lighter);
  transition: all 0.3s;
  cursor: pointer;
}

.cover-upload-area:hover {
  border-color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
}

.uploaded-cover {
  text-align: center;
  width: 100%;
}

.uploaded-cover img {
  max-width: 100%;
  max-height: 180px;
  border-radius: var(--el-border-radius-base);
  margin-bottom: 12px;
  box-shadow: var(--el-box-shadow-light);
}

.cover-actions {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.upload-icon {
  font-size: 48px;
  color: var(--el-text-color-placeholder);
  margin-bottom: 16px;
}

.upload-text {
  text-align: center;
}

.upload-text p {
  margin: 4px 0;
  color: var(--el-text-color-regular);
}

.upload-text em {
  color: var(--el-color-primary);
  font-style: normal;
}

.upload-tip {
  font-size: 12px;
  color: var(--el-text-color-placeholder);
}

/* 富文本编辑器样式增强 */
:deep(.simple-rich-editor) {
  border-radius: var(--el-border-radius-base);
  overflow: hidden;
  box-shadow: var(--el-box-shadow-light);
  transition: box-shadow 0.3s;
}

:deep(.simple-rich-editor:hover) {
  box-shadow: var(--el-box-shadow);
}

@media (max-width: 768px) {
  .edit-container {
    padding: 0 15px;
  }

  .post-form {
    padding: 24px;
  }

  .page-title {
    font-size: 2rem;
  }

  .form-actions .el-button {
    margin: 8px 0;
    min-width: 100px;
    height: 40px;
    font-size: 14px;
  }

  .cover-preview {
    padding: 12px;
  }
}
</style>
