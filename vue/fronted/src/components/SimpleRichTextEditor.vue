<template>
  <div class="simple-rich-editor">
    <!-- 简化工具栏 -->
    <div class="editor-toolbar">
      <el-button-group>
        <el-button 
          :type="isActive('bold') ? 'primary' : 'default'"
          @click="format('bold')"
          size="small"
          title="粗体 (Ctrl+B)"
        >
          <strong>B</strong>
        </el-button>
        <el-button 
          :type="isActive('italic') ? 'primary' : 'default'"
          @click="format('italic')"
          size="small"
          title="斜体 (Ctrl+I)"
        >
          <em>I</em>
        </el-button>
        <el-button 
          :type="isActive('underline') ? 'primary' : 'default'"
          @click="format('underline')"
          size="small"
          title="下划线 (Ctrl+U)"
        >
          <u>U</u>
        </el-button>
      </el-button-group>

      <el-button-group style="margin-left: 8px;">
        <el-button @click="format('insertUnorderedList')" size="small" title="无序列表">
          <i class="fas fa-list-ul"></i>
        </el-button>
        <el-button @click="format('insertOrderedList')" size="small" title="有序列表">
          <i class="fas fa-list-ol"></i>
        </el-button>
      </el-button-group>

      <el-button-group style="margin-left: 8px;">
        <el-button @click="showLinkDialog = true" size="small" title="插入链接">
          <i class="fas fa-link"></i>
        </el-button>
        <el-button @click="showImageDialog = true" size="small" title="插入图片">
          <i class="fas fa-image"></i>
        </el-button>
      </el-button-group>
    </div>

    <!-- 编辑器内容区域 -->
    <div 
      ref="editorRef" 
      class="editor-content"
      contenteditable="true"
      @input="handleInput"
      @keydown="handleKeydown"
      @compositionstart="isComposing = true"
      @compositionend="handleCompositionEnd"
      placeholder="开始编写您的内容..."
    ></div>

    <!-- 插入链接对话框 -->
    <el-dialog v-model="showLinkDialog" title="插入链接" width="400px">
      <el-form :model="linkForm" label-width="80px">
        <el-form-item label="链接文本">
          <el-input v-model="linkForm.text" placeholder="请输入链接显示文本" />
        </el-form-item>
        <el-form-item label="链接地址">
          <el-input v-model="linkForm.url" placeholder="请输入链接地址" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showLinkDialog = false">取消</el-button>
        <el-button type="primary" @click="insertLink" :disabled="!linkForm.text || !linkForm.url">
          插入
        </el-button>
      </template>
    </el-dialog>

    <!-- 插入图片对话框 -->
    <el-dialog v-model="showImageDialog" title="插入图片" width="500px">
      <el-tabs v-model="imageTab">
        <el-tab-pane label="网络图片" name="url">
          <el-input 
            v-model="imageUrl" 
            placeholder="请输入图片URL地址"
            style="margin-bottom: 12px;"
          />
          <el-input 
            v-model="imageAlt" 
            placeholder="图片描述（可选）"
          />
          <div v-if="imageUrl" class="image-preview">
            <img :src="imageUrl" alt="预览" @error="imageError = true" @load="imageError = false" />
            <p v-if="imageError" class="error-text">图片加载失败</p>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="上传图片" name="upload">
          <div class="upload-area" @click="$refs.fileInput.click()" @drop="handleDrop" @dragover.prevent>
            <div v-if="!uploading && !uploadedImage">
              <i class="fas fa-cloud-upload-alt upload-icon"></i>
              <p>点击或拖拽图片到此处上传</p>
              <p class="upload-tip">支持 JPG、PNG、GIF 格式，最大 5MB</p>
            </div>
            <el-progress v-if="uploading" :percentage="uploadProgress" />
            <div v-if="uploadedImage" class="uploaded-image">
              <img :src="uploadedImage" alt="上传的图片" />
              <el-button size="small" @click.stop="clearUpload">重新上传</el-button>
            </div>
          </div>
          <input ref="fileInput" type="file" accept="image/*" style="display: none" @change="handleFileSelect" />
        </el-tab-pane>
      </el-tabs>
      
      <template #footer>
        <el-button @click="showImageDialog = false">取消</el-button>
        <el-button type="primary" @click="insertImage" :disabled="!canInsertImage">
          插入
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

// 基础状态
const editorRef = ref()
const isComposing = ref(false)
const isInternalUpdate = ref(false)

// 对话框状态
const showLinkDialog = ref(false)
const showImageDialog = ref(false)
const imageTab = ref('url')

// 表单数据
const linkForm = ref({ text: '', url: '' })
const imageUrl = ref('')
const imageAlt = ref('')
const imageError = ref(false)

// 上传状态
const uploading = ref(false)
const uploadProgress = ref(0)
const uploadedImage = ref('')

// 计算属性
const canInsertImage = computed(() => {
  return imageTab.value === 'url' ? imageUrl.value && !imageError.value : uploadedImage.value
})

// 格式化命令
const format = (command, value = null) => {
  document.execCommand(command, false, value)
  editorRef.value.focus()
  handleInput()
}

// 检查格式是否激活
const isActive = (command) => {
  return document.queryCommandState(command)
}

// 处理输入
const handleInput = () => {
  if (isComposing.value) return
  
  isInternalUpdate.value = true
  emit('update:modelValue', editorRef.value.innerHTML)
  setTimeout(() => {
    isInternalUpdate.value = false
  }, 50)
}

// 中文输入结束
const handleCompositionEnd = () => {
  isComposing.value = false
  setTimeout(() => {
    handleInput()
  }, 0)
}

// 键盘事件
const handleKeydown = (event) => {
  if (event.ctrlKey || event.metaKey) {
    switch (event.key.toLowerCase()) {
      case 'b':
        event.preventDefault()
        format('bold')
        break
      case 'i':
        event.preventDefault()
        format('italic')
        break
      case 'u':
        event.preventDefault()
        format('underline')
        break
    }
  }
}

// 插入链接
const insertLink = () => {
  if (!linkForm.value.text || !linkForm.value.url) return
  
  const link = `<a href="${linkForm.value.url}" target="_blank">${linkForm.value.text}</a>`
  document.execCommand('insertHTML', false, link)
  
  showLinkDialog.value = false
  linkForm.value = { text: '', url: '' }
  handleInput()
  ElMessage.success('链接插入成功')
}

// 处理文件选择
const handleFileSelect = (event) => {
  const file = event.target.files[0]
  if (file) handleFile(file)
}

// 处理拖拽
const handleDrop = (event) => {
  event.preventDefault()
  const file = event.dataTransfer.files[0]
  if (file) handleFile(file)
}

// 处理文件 - 简化版本，压缩交给后端处理
const handleFile = (file) => {
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    return
  }
  if (file.size / 1024 / 1024 > 5) {
    ElMessage.error('图片大小不能超过 5MB!')
    return
  }

  uploading.value = true
  uploadProgress.value = 0

  // 直接读取文件为base64，让后端处理压缩
  const reader = new FileReader()
  reader.onload = (e) => {
    // 模拟上传进度
    const interval = setInterval(() => {
      uploadProgress.value += 10
      if (uploadProgress.value >= 100) {
        clearInterval(interval)
        uploading.value = false
        uploadedImage.value = e.target.result
        ElMessage.success('图片上传成功!')
      }
    }, 100)
  }
  reader.onerror = () => {
    uploading.value = false
    uploadProgress.value = 0
    ElMessage.error('文件读取失败，请重试')
  }
  reader.readAsDataURL(file)
}

// 清除上传
const clearUpload = () => {
  uploadedImage.value = ''
  uploadProgress.value = 0
}

// 插入图片
const insertImage = () => {
  let src = ''
  let alt = ''
  
  if (imageTab.value === 'url') {
    src = imageUrl.value
    alt = imageAlt.value
  } else {
    src = uploadedImage.value
    alt = '上传的图片'
  }
  
  if (!src) return
  
  const img = `<img src="${src}" alt="${alt}" style="max-width: 100%; height: auto; border-radius: 4px; margin: 8px 0;" />`
  document.execCommand('insertHTML', false, img)
  
  showImageDialog.value = false
  imageUrl.value = ''
  imageAlt.value = ''
  clearUpload()
  handleInput()
  ElMessage.success('图片插入成功')
}

// 监听外部变化
watch(() => props.modelValue, (newValue) => {
  if (isInternalUpdate.value || isComposing.value) return
  
  if (editorRef.value && editorRef.value.innerHTML !== newValue) {
    editorRef.value.innerHTML = newValue || ''
  }
})

// 初始化
onMounted(() => {
  if (editorRef.value) {
    editorRef.value.innerHTML = props.modelValue || ''
  }
})
</script>

<style scoped>
.simple-rich-editor {
  border: 1px solid var(--el-border-color);
  border-radius: var(--el-border-radius-base);
  overflow: hidden;
  background: var(--el-bg-color);
}

.editor-toolbar {
  padding: 12px;
  background: var(--el-bg-color-page);
  border-bottom: 1px solid var(--el-border-color-light);
}

.editor-content {
  min-height: 300px;
  padding: 16px;
  font-size: 14px;
  line-height: 1.6;
  color: var(--el-text-color-primary);
  outline: none;
  overflow-y: auto;
}

.editor-content:empty:before {
  content: attr(placeholder);
  color: var(--el-text-color-placeholder);
  pointer-events: none;
}

.upload-area {
  width: 100%;
  min-height: 150px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px dashed var(--el-border-color);
  border-radius: var(--el-border-radius-base);
  background: var(--el-fill-color-lighter);
  cursor: pointer;
  transition: all 0.3s;
}

.upload-area:hover {
  border-color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
}

.upload-icon {
  font-size: 48px;
  color: var(--el-text-color-placeholder);
  margin-bottom: 16px;
}

.upload-tip {
  font-size: 12px;
  color: var(--el-text-color-placeholder);
  margin: 4px 0;
}

.uploaded-image {
  text-align: center;
}

.uploaded-image img {
  max-width: 100%;
  max-height: 120px;
  border-radius: var(--el-border-radius-base);
  margin-bottom: 8px;
}

.image-preview {
  text-align: center;
  margin-top: 12px;
  padding: 12px;
  border: 1px solid var(--el-border-color-light);
  border-radius: var(--el-border-radius-base);
  background: var(--el-fill-color-lighter);
}

.image-preview img {
  max-width: 100%;
  max-height: 150px;
  border-radius: var(--el-border-radius-base);
}

.error-text {
  color: var(--el-color-danger);
  font-size: 14px;
  margin: 8px 0;
}

/* 内容样式 */
.editor-content h1, .editor-content h2, .editor-content h3,
.editor-content h4, .editor-content h5, .editor-content h6 {
  margin: 16px 0 8px 0;
  font-weight: 600;
}

.editor-content p {
  margin: 8px 0;
}

.editor-content ul, .editor-content ol {
  margin: 8px 0;
  padding-left: 24px;
}

.editor-content li {
  margin: 4px 0;
}

.editor-content a {
  color: var(--el-color-primary);
  text-decoration: none;
}

.editor-content a:hover {
  text-decoration: underline;
}

.editor-content img {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
  margin: 8px 0;
}
</style>
