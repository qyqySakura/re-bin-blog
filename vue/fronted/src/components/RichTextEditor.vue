<template>
  <div class="rich-text-editor">
    <!-- 工具栏 -->
    <el-row class="editor-toolbar" :gutter="8">
      <!-- 基础格式化 -->
      <el-col :span="6">
        <el-button-group>
          <el-button
            :type="isActive('bold') ? 'primary' : 'default'"
            @click="formatText('bold')"
            title="粗体 (Ctrl+B)"
            size="small"
          >
            <el-icon><strong>B</strong></el-icon>
          </el-button>
          <el-button
            :type="isActive('italic') ? 'primary' : 'default'"
            @click="formatText('italic')"
            title="斜体 (Ctrl+I)"
            size="small"
          >
            <el-icon><em>I</em></el-icon>
          </el-button>
          <el-button
            :type="isActive('underline') ? 'primary' : 'default'"
            @click="formatText('underline')"
            title="下划线 (Ctrl+U)"
            size="small"
          >
            <el-icon><u>U</u></el-icon>
          </el-button>
          <el-button
            :type="isActive('strike') ? 'primary' : 'default'"
            @click="formatText('strikeThrough')"
            title="删除线"
            size="small"
          >
            <el-icon><s>S</s></el-icon>
          </el-button>
        </el-button-group>
      </el-col>

      <!-- 标题和字号 -->
      <el-col :span="6">
        <el-select
          v-model="currentHeader"
          @change="formatHeader"
          placeholder="标题"
          size="small"
          style="width: 80px; margin-right: 8px;"
        >
          <el-option label="正文" value="" />
          <el-option label="H1" value="1" />
          <el-option label="H2" value="2" />
          <el-option label="H3" value="3" />
          <el-option label="H4" value="4" />
          <el-option label="H5" value="5" />
          <el-option label="H6" value="6" />
        </el-select>

        <el-select
          v-model="currentFontSize"
          @change="formatFontSize"
          placeholder="字号"
          size="small"
          style="width: 70px;"
        >
          <el-option label="小" value="1" />
          <el-option label="正常" value="3" />
          <el-option label="中" value="4" />
          <el-option label="大" value="5" />
          <el-option label="特大" value="6" />
          <el-option label="超大" value="7" />
        </el-select>
      </el-col>

      <!-- 列表和引用 -->
      <el-col :span="4">
        <el-button-group>
          <el-button
            @click="formatList('ordered')"
            title="有序列表"
            size="small"
          >
            <el-icon><i class="fas fa-list-ol"></i></el-icon>
          </el-button>
          <el-button
            @click="formatList('bullet')"
            title="无序列表"
            size="small"
          >
            <el-icon><i class="fas fa-list-ul"></i></el-icon>
          </el-button>
          <el-button
            @click="formatText('formatBlock', 'blockquote')"
            title="引用"
            size="small"
          >
            <el-icon><i class="fas fa-quote-left"></i></el-icon>
          </el-button>
        </el-button-group>
      </el-col>

      <!-- 链接和图片 -->
      <el-col :span="3">
        <el-button-group>
          <el-button
            @click="insertLink"
            title="插入链接"
            size="small"
          >
            <el-icon><i class="fas fa-link"></i></el-icon>
          </el-button>
          <el-button
            @click="insertImage"
            title="插入图片"
            size="small"
          >
            <el-icon><i class="fas fa-image"></i></el-icon>
          </el-button>
        </el-button-group>
      </el-col>

      <!-- 对齐方式 -->
      <el-col :span="4">
        <el-button-group>
          <el-button
            @click="formatAlign('left')"
            title="左对齐"
            size="small"
          >
            <el-icon><i class="fas fa-align-left"></i></el-icon>
          </el-button>
          <el-button
            @click="formatAlign('center')"
            title="居中对齐"
            size="small"
          >
            <el-icon><i class="fas fa-align-center"></i></el-icon>
          </el-button>
          <el-button
            @click="formatAlign('right')"
            title="右对齐"
            size="small"
          >
            <el-icon><i class="fas fa-align-right"></i></el-icon>
          </el-button>
          <el-button
            @click="formatAlign('justify')"
            title="两端对齐"
            size="small"
          >
            <el-icon><i class="fas fa-align-justify"></i></el-icon>
          </el-button>
        </el-button-group>
      </el-col>

      <!-- 撤销重做和清除格式 -->
      <el-col :span="1">
        <el-dropdown trigger="click">
          <el-button size="small">
            更多<el-icon class="el-icon--right"><i class="fas fa-chevron-down"></i></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="formatText('undo')">
                <el-icon><i class="fas fa-undo"></i></el-icon>
                撤销
              </el-dropdown-item>
              <el-dropdown-item @click="formatText('redo')">
                <el-icon><i class="fas fa-redo"></i></el-icon>
                重做
              </el-dropdown-item>
              <el-dropdown-item @click="clearFormat">
                <el-icon><i class="fas fa-remove-format"></i></el-icon>
                清除格式
              </el-dropdown-item>
              <el-dropdown-item @click="formatText('formatBlock', 'pre')">
                <el-icon><i class="fas fa-code"></i></el-icon>
                代码块
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-col>
    </el-row>

    <!-- 编辑器内容区域 -->
    <div
      ref="editorRef"
      class="editor-content"
      contenteditable="true"
      @input="handleInput"
      @keydown="handleKeydown"
      @paste="handlePaste"
      @compositionstart="handleCompositionStart"
      @compositionupdate="handleCompositionUpdate"
      @compositionend="handleCompositionEnd"
      v-html="modelValue"
      placeholder="开始编写您的内容..."
    ></div>

    <!-- 插入链接对话框 -->
    <el-dialog
      v-model="linkDialogVisible"
      title="插入链接"
      width="500px"
      :before-close="handleLinkDialogClose"
    >
      <el-form :model="linkForm" label-width="80px" :rules="linkRules" ref="linkFormRef">
        <el-form-item label="链接文本" prop="text">
          <el-input
            v-model="linkForm.text"
            placeholder="请输入链接显示文本"
            clearable
          />
        </el-form-item>
        <el-form-item label="链接地址" prop="url">
          <el-input
            v-model="linkForm.url"
            placeholder="请输入链接地址，如：https://example.com"
            clearable
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleLinkDialogClose">取消</el-button>
          <el-button type="primary" @click="confirmInsertLink" :loading="linkInserting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 插入图片对话框 -->
    <el-dialog
      v-model="imageDialogVisible"
      title="插入图片"
      width="600px"
      :before-close="handleImageDialogClose"
    >
      <el-tabs v-model="imageTabActive" class="image-tabs">
        <!-- URL方式 -->
        <el-tab-pane label="网络图片" name="url">
          <el-form :model="imageForm" label-width="80px" :rules="imageRules" ref="imageFormRef">
            <el-form-item label="图片地址" prop="url">
              <el-input
                v-model="imageForm.url"
                placeholder="请输入图片URL地址"
                clearable
              />
            </el-form-item>
            <el-form-item label="图片描述">
              <el-input
                v-model="imageForm.alt"
                placeholder="请输入图片描述（可选）"
                clearable
              />
            </el-form-item>
            <el-form-item label="预览" v-if="imageForm.url">
              <div class="image-preview">
                <img
                  :src="imageForm.url"
                  :alt="imageForm.alt"
                  @load="imageLoaded = true"
                  @error="imageLoaded = false"
                  style="max-width: 100%; max-height: 200px; border-radius: 4px;"
                />
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 上传方式 -->
        <el-tab-pane label="上传图片" name="upload">
          <div class="upload-area" @click="triggerImageUpload" @dragover.prevent @drop.prevent="handleImageDrop">
            <el-icon class="upload-icon" v-if="!uploading && !uploadedImageUrl">
              <i class="fas fa-cloud-upload-alt"></i>
            </el-icon>
            <div class="upload-text" v-if="!uploading && !uploadedImageUrl">
              <p>将图片拖拽到此处，或<em>点击上传</em></p>
              <p class="upload-tip">支持 JPG、PNG、GIF 格式，文件大小不超过 5MB</p>
            </div>
            <el-progress
              v-if="uploading"
              :percentage="uploadProgress"
              :stroke-width="6"
              status="success"
            />
            <div v-if="uploadedImageUrl && !uploading" class="uploaded-image">
              <img :src="uploadedImageUrl" alt="上传的图片" />
              <div class="upload-actions">
                <el-button size="small" @click.stop="clearUpload">重新上传</el-button>
              </div>
            </div>
          </div>
          <input
            ref="imageFileInput"
            type="file"
            accept="image/*"
            style="display: none"
            @change="handleImageFileSelect"
          />
          <el-form :model="uploadImageForm" label-width="80px" v-if="uploadedImageUrl">
            <el-form-item label="图片描述">
              <el-input
                v-model="uploadImageForm.alt"
                placeholder="请输入图片描述（可选）"
                clearable
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleImageDialogClose">取消</el-button>
          <el-button
            type="primary"
            @click="confirmInsertImage"
            :loading="imageInserting"
            :disabled="!canInsertImage"
          >
            插入图片
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

// 编辑器引用
const editorRef = ref()
const linkFormRef = ref()
const imageFormRef = ref()
const imageFileInput = ref()

// 当前状态
const currentHeader = ref('')
const currentFontSize = ref('')

// 对话框状态
const linkDialogVisible = ref(false)
const imageDialogVisible = ref(false)
const imageTabActive = ref('url')

// 上传状态
const uploading = ref(false)
const uploadProgress = ref(0)
const uploadedImageUrl = ref('')
const imageLoaded = ref(false)
const linkInserting = ref(false)
const imageInserting = ref(false)

// 中文输入法状态
const isComposing = ref(false)
const compositionRange = ref(null)
const updateTimer = ref(null)
const isInternalUpdate = ref(false) // 标记是否为内部更新

// 表单数据
const linkForm = ref({
  text: '',
  url: ''
})

const imageForm = ref({
  url: '',
  alt: ''
})

const uploadImageForm = ref({
  alt: ''
})

// 表单验证规则
const linkRules = {
  text: [
    { required: true, message: '请输入链接文本', trigger: 'blur' }
  ],
  url: [
    { required: true, message: '请输入链接地址', trigger: 'blur' },
    { type: 'url', message: '请输入正确的URL格式', trigger: 'blur' }
  ]
}

const imageRules = {
  url: [
    { required: true, message: '请输入图片地址', trigger: 'blur' },
    { type: 'url', message: '请输入正确的URL格式', trigger: 'blur' }
  ]
}

// 是否可以插入图片
const canInsertImage = computed(() => {
  if (imageTabActive.value === 'url') {
    return imageForm.value.url && imageLoaded.value
  } else {
    return uploadedImageUrl.value
  }
})

// 当前选择的范围
let currentRange = null

// 保存选择范围
const saveSelection = () => {
  const selection = window.getSelection()
  if (selection.rangeCount > 0) {
    currentRange = selection.getRangeAt(0)
  }
}

// 恢复选择范围
const restoreSelection = () => {
  if (currentRange) {
    const selection = window.getSelection()
    selection.removeAllRanges()
    selection.addRange(currentRange)
  }
}

// 格式化文本
const formatText = (command, value = null) => {
  document.execCommand(command, false, value)
  editorRef.value.focus()
  handleInput()
}

// 格式化标题
const formatHeader = (level) => {
  if (level) {
    formatText('formatBlock', `h${level}`)
  } else {
    formatText('formatBlock', 'div')
  }
  // 重置选择框
  setTimeout(() => {
    currentHeader.value = ''
  }, 100)
}

// 格式化字号
const formatFontSize = (size) => {
  if (size) {
    formatText('fontSize', size)
  }
  // 重置选择框
  setTimeout(() => {
    currentFontSize.value = ''
  }, 100)
}

// 触发图片上传
const triggerImageUpload = () => {
  imageFileInput.value.click()
}

// 处理文件选择
const handleImageFileSelect = (event) => {
  const file = event.target.files[0]
  if (file) {
    handleImageFile(file)
  }
}

// 处理拖拽上传
const handleImageDrop = (event) => {
  const files = event.dataTransfer.files
  if (files.length > 0) {
    handleImageFile(files[0])
  }
}

// 处理图片文件
const handleImageFile = (file) => {
  // 验证文件
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    return
  }
  if (file.size / 1024 / 1024 > 5) {
    ElMessage.error('图片大小不能超过 5MB!')
    return
  }

  // 开始上传过程
  uploading.value = true
  uploadProgress.value = 0

  // 使用FileReader读取文件并转换为base64
  const reader = new FileReader()
  reader.onload = (e) => {
    // 模拟上传进度
    const interval = setInterval(() => {
      uploadProgress.value += 10
      if (uploadProgress.value >= 100) {
        clearInterval(interval)
        uploading.value = false
        uploadedImageUrl.value = e.target.result
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
  uploadedImageUrl.value = ''
  uploadImageForm.value.alt = ''
  uploadProgress.value = 0
  if (imageFileInput.value) {
    imageFileInput.value.value = ''
  }
}

// 关闭链接对话框
const handleLinkDialogClose = () => {
  linkDialogVisible.value = false
  linkForm.value = { text: '', url: '' }
  linkFormRef.value?.clearValidate()
}

// 关闭图片对话框
const handleImageDialogClose = () => {
  imageDialogVisible.value = false
  imageForm.value = { url: '', alt: '' }
  uploadImageForm.value = { alt: '' }
  clearUpload()
  imageFormRef.value?.clearValidate()
}

// 格式化列表
const formatList = (type) => {
  if (type === 'ordered') {
    formatText('insertOrderedList')
  } else {
    formatText('insertUnorderedList')
  }
}

// 格式化对齐
const formatAlign = (align) => {
  formatText(`justify${align.charAt(0).toUpperCase() + align.slice(1)}`)
}

// 清除格式
const clearFormat = () => {
  formatText('removeFormat')
  formatText('formatBlock', 'div')
}

// 检查格式是否激活
const isActive = (command) => {
  return document.queryCommandState(command)
}

// 插入链接
const insertLink = () => {
  saveSelection()
  const selectedText = window.getSelection().toString()
  linkForm.value.text = selectedText || ''
  linkForm.value.url = ''
  linkDialogVisible.value = true
}

// 确认插入链接
const confirmInsertLink = async () => {
  try {
    await linkFormRef.value.validate()
    linkInserting.value = true

    restoreSelection()
    const { text, url } = linkForm.value

    if (text && url) {
      const link = `<a href="${url}" target="_blank">${text}</a>`
      document.execCommand('insertHTML', false, link)
      ElMessage.success('链接插入成功')
      handleLinkDialogClose()
      handleInput()
    }
  } catch (error) {
    console.error('插入链接失败:', error)
  } finally {
    linkInserting.value = false
  }
}

// 插入图片
const insertImage = () => {
  saveSelection()
  imageForm.value = { url: '', alt: '' }
  uploadImageForm.value = { alt: '' }
  clearUpload()
  imageTabActive.value = 'url'
  imageDialogVisible.value = true
}

// 确认插入图片
const confirmInsertImage = async () => {
  try {
    imageInserting.value = true
    restoreSelection()

    let imageUrl = ''
    let imageAlt = ''

    if (imageTabActive.value === 'url') {
      await imageFormRef.value.validate()
      imageUrl = imageForm.value.url
      imageAlt = imageForm.value.alt
    } else {
      imageUrl = uploadedImageUrl.value
      imageAlt = uploadImageForm.value.alt
    }

    if (imageUrl) {
      const img = `<img src="${imageUrl}" alt="${imageAlt || ''}" style="max-width: 100%; height: auto; border-radius: 4px; margin: 8px 0;" />`
      document.execCommand('insertHTML', false, img)
      ElMessage.success('图片插入成功')
      handleImageDialogClose()
      handleInput()
    }
  } catch (error) {
    console.error('插入图片失败:', error)
  } finally {
    imageInserting.value = false
  }
}

// 防抖更新内容
const debouncedUpdate = (content) => {
  if (updateTimer.value) {
    clearTimeout(updateTimer.value)
  }

  updateTimer.value = setTimeout(() => {
    console.log('防抖更新内容')
    isInternalUpdate.value = true
    emit('update:modelValue', content)
    setTimeout(() => {
      isInternalUpdate.value = false
    }, 50)
  }, 100) // 100ms防抖
}

// 处理输入
const handleInput = () => {
  // 如果正在输入中文，不处理
  if (isComposing.value) {
    console.log('跳过输入处理：正在输入中文')
    return
  }

  const content = editorRef.value.innerHTML
  console.log('处理输入事件，内容长度:', content.length)

  // 使用防抖更新，避免频繁触发
  debouncedUpdate(content)
}

// 中文输入开始
const handleCompositionStart = () => {
  console.log('中文输入开始')
  isComposing.value = true
  // 保存当前光标位置
  const selection = window.getSelection()
  if (selection.rangeCount > 0) {
    compositionRange.value = selection.getRangeAt(0).cloneRange()
    console.log('保存输入法光标位置:', compositionRange.value.startOffset)
  }
}

// 中文输入更新
const handleCompositionUpdate = () => {
  console.log('中文输入更新')
  // 输入过程中不做任何处理，保持光标位置
}

// 中文输入结束
const handleCompositionEnd = () => {
  console.log('中文输入结束')
  isComposing.value = false
  compositionRange.value = null

  // 延迟处理，确保输入法完成
  setTimeout(() => {
    const content = editorRef.value.innerHTML
    console.log('中文输入完成，发送内容更新')
    // 直接发送更新，不使用防抖
    emit('update:modelValue', content)
  }, 50) // 稍微延迟确保输入法完成
}

// 处理键盘事件
const handleKeydown = (event) => {
  // Ctrl+B 粗体
  if (event.ctrlKey && event.key === 'b') {
    event.preventDefault()
    formatText('bold')
  }
  // Ctrl+I 斜体
  else if (event.ctrlKey && event.key === 'i') {
    event.preventDefault()
    formatText('italic')
  }
  // Ctrl+U 下划线
  else if (event.ctrlKey && event.key === 'u') {
    event.preventDefault()
    formatText('underline')
  }

  // 处理回车键，确保正确的段落结构
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    const selection = window.getSelection()
    const range = selection.getRangeAt(0)

    // 插入换行
    const br1 = document.createElement('br')
    const br2 = document.createElement('br')
    range.insertNode(br2)
    range.insertNode(br1)

    // 移动光标到新行
    range.setStartAfter(br2)
    range.collapse(true)
    selection.removeAllRanges()
    selection.addRange(range)

    handleInput()
    return
  }

  // 处理退格键，防止删除所有内容时出现问题
  if (event.key === 'Backspace') {
    setTimeout(() => {
      const content = editorRef.value.textContent || editorRef.value.innerText
      if (!content.trim()) {
        editorRef.value.innerHTML = '<br>'
        const range = document.createRange()
        const selection = window.getSelection()
        range.setStart(editorRef.value, 0)
        range.collapse(true)
        selection.removeAllRanges()
        selection.addRange(range)
      }
    }, 0)
  }
}

// 处理粘贴
const handlePaste = (event) => {
  event.preventDefault()
  const text = event.clipboardData.getData('text/plain')

  // 使用现代API插入文本
  const selection = window.getSelection()
  if (selection.rangeCount > 0) {
    const range = selection.getRangeAt(0)
    range.deleteContents()

    // 创建文本节点并插入
    const textNode = document.createTextNode(text)
    range.insertNode(textNode)

    // 移动光标到插入文本的末尾
    range.setStartAfter(textNode)
    range.collapse(true)
    selection.removeAllRanges()
    selection.addRange(range)
  }

  handleInput()
}

// 初始化编辑器
const initEditor = () => {
  if (editorRef.value) {
    // 设置初始内容
    const initialContent = props.modelValue || ''
    editorRef.value.innerHTML = initialContent

    // 如果没有内容，添加一个br标签以确保光标可见
    if (!initialContent.trim()) {
      editorRef.value.innerHTML = '<br>'
    }
  }
}

// 监听modelValue变化
watch(() => props.modelValue, (newValue, oldValue) => {
  // 如果是内部更新触发的，忽略
  if (isInternalUpdate.value) {
    console.log('跳过更新：内部更新触发')
    return
  }

  // 如果正在输入中文，不更新内容
  if (isComposing.value) {
    console.log('跳过更新：正在输入中文')
    return
  }

  // 如果内容没有实际变化，不更新
  if (editorRef.value && editorRef.value.innerHTML === newValue) {
    console.log('跳过更新：内容相同')
    return
  }

  console.log('modelValue变化:', {
    oldValue: oldValue?.substring(0, 50) + '...',
    newValue: newValue?.substring(0, 50) + '...',
    current: editorRef.value?.innerHTML?.substring(0, 50) + '...'
  })

  if (editorRef.value) {
    const hadFocus = document.activeElement === editorRef.value

    // 只有在内容确实不同时才更新
    if (editorRef.value.innerHTML !== newValue) {
      editorRef.value.innerHTML = newValue || '<br>'
      console.log('更新编辑器内容')

      // 如果之前有焦点，恢复焦点并将光标移到末尾
      if (hadFocus) {
        setTimeout(() => {
          editorRef.value.focus()
          // 将光标移到内容末尾
          const range = document.createRange()
          const selection = window.getSelection()

          if (editorRef.value.childNodes.length > 0) {
            const lastChild = editorRef.value.lastChild
            if (lastChild.nodeType === Node.TEXT_NODE) {
              range.setStart(lastChild, lastChild.textContent.length)
            } else {
              range.setStartAfter(lastChild)
            }
          } else {
            range.setStart(editorRef.value, 0)
          }

          range.collapse(true)
          selection.removeAllRanges()
          selection.addRange(range)
          console.log('光标移到末尾')
        }, 0)
      }
    }
  }
})

onMounted(() => {
  initEditor()
})
</script>

<style scoped>
.rich-text-editor {
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
  background: var(--el-bg-color);
}

.editor-content:focus {
  outline: none;
}

.editor-content:empty:before {
  content: attr(placeholder);
  color: var(--el-text-color-placeholder);
  pointer-events: none;
}

/* 图片上传样式 */
.image-uploader {
  width: 100%;
}

.upload-area {
  width: 100%;
  min-height: 180px;
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

.upload-area:hover {
  border-color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
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

.uploaded-image {
  text-align: center;
}

.uploaded-image img {
  max-width: 100%;
  max-height: 150px;
  border-radius: var(--el-border-radius-base);
  margin-bottom: 12px;
}

.upload-actions {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.image-preview {
  text-align: center;
  padding: 8px;
  border: 1px solid var(--el-border-color-light);
  border-radius: var(--el-border-radius-base);
  background: var(--el-fill-color-lighter);
}

.image-tabs {
  margin-bottom: 16px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

/* 编辑器内容样式 */
.editor-content h1,
.editor-content h2,
.editor-content h3,
.editor-content h4,
.editor-content h5,
.editor-content h6 {
  margin: 16px 0 8px 0;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.editor-content h1 { font-size: 2em; }
.editor-content h2 { font-size: 1.5em; }
.editor-content h3 { font-size: 1.25em; }
.editor-content h4 { font-size: 1.1em; }
.editor-content h5 { font-size: 1em; }
.editor-content h6 { font-size: 0.9em; }

.editor-content p {
  margin: 8px 0;
  line-height: 1.6;
}

.editor-content blockquote {
  margin: 16px 0;
  padding: 12px 16px;
  border-left: 4px solid var(--el-color-primary);
  background: var(--el-color-primary-light-9);
  color: var(--el-text-color-regular);
  border-radius: 0 var(--el-border-radius-base) var(--el-border-radius-base) 0;
}

.editor-content pre {
  margin: 16px 0;
  padding: 16px;
  background: var(--el-fill-color-light);
  border: 1px solid var(--el-border-color-light);
  border-radius: var(--el-border-radius-base);
  overflow-x: auto;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', monospace;
  font-size: 13px;
  line-height: 1.4;
}

.editor-content ul,
.editor-content ol {
  margin: 8px 0;
  padding-left: 24px;
}

.editor-content li {
  margin: 4px 0;
  line-height: 1.6;
}

.editor-content a {
  color: var(--el-color-primary);
  text-decoration: none;
  transition: color 0.3s;
}

.editor-content a:hover {
  color: var(--el-color-primary-light-3);
  text-decoration: underline;
}

.editor-content img {
  max-width: 100%;
  height: auto;
  border-radius: var(--el-border-radius-base);
  margin: 8px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.editor-content img:hover {
  transform: scale(1.02);
}

.editor-content strong {
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.editor-content em {
  font-style: italic;
  color: var(--el-text-color-regular);
}

.editor-content u {
  text-decoration: underline;
}

.editor-content s {
  text-decoration: line-through;
  color: var(--el-text-color-placeholder);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .editor-toolbar {
    padding: 8px;
  }

  .editor-content {
    padding: 12px;
    font-size: 13px;
  }

  .upload-area {
    min-height: 120px;
  }

  .upload-icon {
    font-size: 32px;
    margin-bottom: 8px;
  }
}
</style>
