<template>
  <div class="post-detail">
    <div class="container" v-loading="loading">
      <div class="post-content" v-if="post">
        <!-- 文章头部 -->
        <header class="post-header">
          <h1 class="post-title">{{ post.title }}</h1>
          <div class="post-meta">
            <span class="meta-item">
              <el-icon><User /></el-icon>
              {{ post.author?.name || '匿名' }}
            </span>
            <span class="meta-item">
              <el-icon><Calendar /></el-icon>
              {{ formatDate(post.createTime) }}
            </span>
            <span class="meta-item" v-if="post.category">
              <el-icon><Folder /></el-icon>
              {{ post.category.name }}
            </span>
          </div>
          <div class="post-tags" v-if="post.tags && post.tags.length">
            <el-tag 
              v-for="tag in post.tags" 
              :key="tag.id" 
              type="primary"
              size="small"
            >
              {{ tag.name }}
            </el-tag>
          </div>
        </header>

        <!-- 文章封面 -->
        <div class="post-cover" v-if="post.cover">
          <img :src="post.cover" :alt="post.title" />
        </div>

        <!-- 文章内容 -->
        <div class="post-body">
          <div class="content" v-html="post.content"></div>
        </div>

        <!-- 文章底部 -->
        <footer class="post-footer">
          <div class="post-stats">
            <div class="stat-item">
              <span class="stat-icon">👁️</span>
              <span class="stat-text">{{ post.viewCount || 0 }} 阅读</span>
            </div>
            <div class="stat-item">
              <button
                class="like-btn"
                :class="{ 'liked': post.isLiked }"
                @click="togglePostLike"
              >
                <span class="like-icon">{{ post.isLiked ? '❤️' : '🤍' }}</span>
                <span class="like-text">{{ post.likeCount || 0 }} 点赞</span>
              </button>
            </div>
          </div>
          <div class="post-actions">
            <el-button @click="goBack">
              <el-icon><ArrowLeft /></el-icon>
              返回
            </el-button>
          </div>
        </footer>
      </div>

      <!-- 评论区 -->
      <div class="comments-section" v-if="post">
        <h3 class="comments-title">评论 ({{ comments.length }})</h3>

        <!-- 评论表单 -->
        <div class="comment-form" v-if="isLoggedIn">
          <div class="comment-form-header" v-if="replyingTo">
            <span>回复 @{{ replyingTo.user?.name }}：</span>
            <el-button text size="small" @click="cancelReply">取消</el-button>
          </div>
          <div class="comment-input-area">
            <div class="user-avatar">
              <img :src="getUserAvatar(currentUser?.avatar)" :alt="currentUser?.name" />
            </div>
            <div class="input-wrapper">
              <el-input
                ref="commentInput"
                v-model="newComment.content"
                type="textarea"
                :rows="3"
                :placeholder="replyingTo ? `回复 @${replyingTo.user?.name}` : '写下你的评论...'"
                maxlength="500"
                show-word-limit
              />
              <div class="form-actions">
                <div class="emoji-section">
                  <el-button
                    text
                    size="small"
                    @click="toggleEmojiPicker"
                    class="emoji-btn"
                  >
                    😊 表情
                  </el-button>
                  <div v-if="showEmojiPicker" class="emoji-picker">
                    <div class="emoji-grid">
                      <span
                        v-for="emoji in commonEmojis"
                        :key="emoji"
                        class="emoji-item"
                        @click="insertEmoji(emoji)"
                      >
                        {{ emoji }}
                      </span>
                    </div>
                  </div>
                </div>
                <el-button type="primary" @click="submitComment" :loading="submitting" size="small">
                  {{ replyingTo ? '回复' : '发表评论' }}
                </el-button>
              </div>
            </div>
          </div>
        </div>
        <div class="login-prompt" v-else>
          <p>请 <router-link to="/user/login">登录</router-link> 后发表评论</p>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list">
          <div
            v-for="comment in topLevelComments"
            :key="comment.id"
            class="comment-item"
          >
            <!-- 主评论 -->
            <div class="comment-main">
              <div class="comment-avatar">
                <img :src="getUserAvatar(comment.user?.avatar)" :alt="comment.user?.name" />
              </div>
              <div class="comment-content">
                <div class="comment-header">
                  <span class="comment-author">{{ comment.user?.name || '匿名' }}</span>
                  <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
                </div>
                <div class="comment-text">{{ comment.content }}</div>
                <div class="comment-actions">
                  <el-button
                    text
                    size="small"
                    @click="toggleLike(comment)"
                    :class="{ 'liked': comment.isLiked }"
                    class="like-btn"
                  >
                    {{ comment.isLiked ? '❤️' : '🤍' }} {{ comment.likeCount || 0 }}
                  </el-button>
                  <el-button
                    text
                    size="small"
                    @click="replyToComment(comment)"
                    v-if="isLoggedIn"
                  >
                    回复
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 回复列表 -->
            <div class="replies-list" v-if="comment.replies && comment.replies.length > 0">
              <div
                v-for="reply in comment.replies"
                :key="reply.id"
                class="reply-item"
              >
                <div class="comment-avatar">
                  <img :src="getUserAvatar(reply.user?.avatar)" :alt="reply.user?.name" />
                </div>
                <div class="comment-content">
                  <div class="comment-header">
                    <span class="comment-author">{{ reply.user?.name || '匿名' }}</span>
                    <span class="reply-target" v-if="getReplyTarget(reply, comment)">
                      回复 @{{ getReplyTarget(reply, comment) }}
                    </span>
                    <span class="comment-time">{{ formatDate(reply.createTime) }}</span>
                  </div>
                  <div class="comment-text">{{ reply.content }}</div>
                  <div class="comment-actions">
                    <el-button
                      text
                      size="small"
                      @click="toggleLike(reply)"
                      :class="{ 'liked': reply.isLiked }"
                      class="like-btn"
                    >
                      {{ reply.isLiked ? '❤️' : '🤍' }} {{ reply.likeCount || 0 }}
                    </el-button>
                    <el-button
                      text
                      size="small"
                      @click="replyToComment(reply)"
                      v-if="isLoggedIn"
                    >
                      回复
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

import { postApi, commentApi } from '@/utils/api'
import { API_CONFIG } from '@/config/index.js'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const post = ref(null)
const comments = ref([])
const replyingTo = ref(null)
const showEmojiPicker = ref(false)
const commentInput = ref(null)
const newComment = ref({
  content: '',
  postId: null,
  userId: null,
  parentId: null,
  replyToUserId: null
})

// 常用表情
const commonEmojis = [
  '😊', '😂', '🤣', '😍', '🥰', '😘', '😗', '😙', '😚', '🙂',
  '🤗', '🤔', '😐', '😑', '😶', '🙄', '😏', '😣', '😥', '😮',
  '🤐', '😯', '😪', '😫', '🥱', '😴', '😌', '😛', '😜', '😝',
  '🤤', '😒', '😓', '😔', '😕', '🙃', '🤑', '😲', '☹️', '🙁',
  '😖', '😞', '😟', '😤', '😢', '😭', '😦', '😧', '😨', '😩',
  '🤯', '😬', '😰', '😱', '🥵', '🥶', '😳', '🤪', '😵', '🥴',
  '👍', '👎', '👌', '✌️', '🤞', '🤟', '🤘', '🤙', '👈', '👉',
  '👆', '🖕', '👇', '☝️', '👋', '🤚', '🖐️', '✋', '🖖', '👏',
  '🙌', '🤲', '🤝', '🙏', '✍️', '💪', '🦾', '🦿', '🦵', '🦶',
  '❤️', '🧡', '💛', '💚', '💙', '💜', '🖤', '🤍', '🤎', '💔'
]

// 计算属性
const isLoggedIn = computed(() => userStore.isLoggedIn)
const currentUser = computed(() => userStore.userInfo)

// 处理评论层级结构
const topLevelComments = computed(() => {
  const topLevel = comments.value.filter(comment => !comment.parentId)
  return topLevel.map(comment => ({
    ...comment,
    replies: comments.value.filter(reply => reply.parentId === comment.id)
  }))
})

// 获取文章详情
const fetchPost = async () => {
  try {
    loading.value = true
    const postId = route.params.id
    console.log('开始获取文章详情, ID:', postId)

    const response = await postApi.getPostById(postId)
    console.log('API响应:', response)

    if (response.code === 200) {
      post.value = response.data
      console.log('获取到的文章数据:', post.value)

      // 处理封面图片URL
      if (post.value.cover && !post.value.cover.startsWith('http')) {
        const originalCover = post.value.cover
        post.value.cover = `${API_CONFIG.BASE_URL}${post.value.cover}`
        console.log('图片URL处理:', {
          original: originalCover,
          processed: post.value.cover,
          baseUrl: API_CONFIG.BASE_URL
        })
      }

      newComment.value.postId = postId
      console.log('文章详情获取成功')
    } else {
      console.error('API返回错误:', response)
      ElMessage.error('文章不存在')
      router.push('/')
    }
  } catch (error) {
    console.error('获取文章详情失败:', error)
    ElMessage.error(`获取文章失败: ${error.message || error}`)
    router.push('/')
  } finally {
    loading.value = false
  }
}

// 获取评论列表
const fetchComments = async () => {
  try {
    const postId = route.params.id
    const response = await commentApi.getCommentsByPostId(postId)
    if (response.code === 200) {
      comments.value = response.data.map(comment => ({
        ...comment,
        // 处理用户头像URL
        user: comment.user ? {
          ...comment.user,
          avatar: getUserAvatar(comment.user.avatar)
        } : null
      }))
    }
  } catch (error) {
    console.error('获取评论失败:', error)
  }
}

// 提交评论
const submitComment = async () => {
  if (!newComment.value.content.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  try {
    submitting.value = true
    newComment.value.userId = currentUser.value?.id
    const response = await commentApi.addComment(newComment.value)
    if (response.code === 200) {
      ElMessage.success(replyingTo.value ? '回复成功' : '评论发表成功')
      newComment.value.content = ''
      newComment.value.parentId = null
      newComment.value.replyToUserId = null
      replyingTo.value = null
      fetchComments() // 重新获取评论列表
    }
  } catch (error) {
    ElMessage.error('评论发表失败')
  } finally {
    submitting.value = false
  }
}

// 回复评论
const replyToComment = (comment) => {
  replyingTo.value = comment
  // 如果是回复主评论，parentId设为主评论ID
  // 如果是回复回复，parentId设为主评论ID（保持扁平结构）
  const mainCommentId = comment.parentId || comment.id
  newComment.value.parentId = mainCommentId
  // 记录被回复的用户ID，用于显示回复目标
  newComment.value.replyToUserId = comment.user?.id

  // 滚动到评论表单
  document.querySelector('.comment-form')?.scrollIntoView({ behavior: 'smooth' })
}

// 取消回复
const cancelReply = () => {
  replyingTo.value = null
  newComment.value.parentId = null
  newComment.value.replyToUserId = null
  newComment.value.content = ''
  showEmojiPicker.value = false
}

// 切换表情选择器
const toggleEmojiPicker = () => {
  showEmojiPicker.value = !showEmojiPicker.value
}

// 插入表情
const insertEmoji = (emoji) => {
  const textarea = commentInput.value?.textarea || commentInput.value?.input
  if (textarea) {
    const start = textarea.selectionStart
    const end = textarea.selectionEnd
    const content = newComment.value.content

    newComment.value.content = content.substring(0, start) + emoji + content.substring(end)

    // 设置光标位置
    nextTick(() => {
      textarea.focus()
      textarea.setSelectionRange(start + emoji.length, start + emoji.length)
    })
  } else {
    // 如果无法获取光标位置，直接追加到末尾
    newComment.value.content += emoji
  }

  showEmojiPicker.value = false
}

// 获取用户头像URL
const getUserAvatar = (avatar) => {
  // 如果没有头像，使用后端提供的默认头像
  if (!avatar) {
    return `${API_CONFIG.BASE_URL}/static/default-avatar.png`
  }

  // 如果已经是完整URL，直接返回
  if (avatar.startsWith('http')) return avatar

  // 如果是头像路径，拼接基础URL
  if (avatar.startsWith('/avatar/')) return `${API_CONFIG.BASE_URL}${avatar}`

  // 如果是API路径，拼接基础URL
  if (avatar.startsWith('/api/')) return `${API_CONFIG.BASE_URL}${avatar}`

  return avatar
}

// 切换点赞状态
const toggleLike = async (comment) => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    const response = await commentApi.toggleLike(comment.id)
    if (response.code === 200) {
      ElMessage.success(response.message)
      // 重新获取评论数据以确保数据同步
      await fetchComments()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 获取回复目标用户名
const getReplyTarget = (reply, mainComment) => {
  // 如果有被回复的用户信息，直接返回
  if (reply.replyToUser?.name) {
    return reply.replyToUser.name
  }

  // 如果回复的是主评论，返回主评论作者
  if (reply.parentId === mainComment.id) {
    return mainComment.user?.name
  }

  // 默认返回主评论作者
  return mainComment.user?.name
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// 返回上一页
const goBack = () => {
  router.go(-1)
}

// 切换文章点赞状态
const togglePostLike = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    const response = await postApi.toggleLike(post.value.id)
    if (response.code === 200) {
      ElMessage.success(response.message)
      // 重新获取文章数据以确保数据同步
      await fetchPost()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 点击外部关闭表情选择器
const handleClickOutside = (event) => {
  const emojiSection = event.target.closest('.emoji-section')
  if (!emojiSection && showEmojiPicker.value) {
    showEmojiPicker.value = false
  }
}

// 初始化
onMounted(() => {
  fetchPost()
  fetchComments()

  // 点击外部关闭表情选择器
  document.addEventListener('click', handleClickOutside)
})
</script>

<style scoped>
/* 页面布局 */
.post-detail {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20px 0;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 文章内容 */
.post-content {
  background: white;
  border-radius: 8px;
  padding: 40px;
  margin-bottom: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.post-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.post-title {
  font-size: 2.5rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  line-height: 1.2;
}

.post-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  color: #666;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.post-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.post-cover {
  margin-bottom: 30px;
  text-align: center;
}

.post-cover img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
}

.post-body {
  margin-bottom: 30px;
}

.content {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #333;
}

.post-footer {
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.post-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  align-items: center;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #666;
  font-size: 0.9rem;
}

.stat-icon {
  font-size: 1.1rem;
}

.like-btn {
  background: none;
  border: 1px solid #ddd;
  padding: 8px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #666;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.9rem;
}

.like-btn:hover {
  color: #ff6b6b;
  border-color: #ff6b6b;
  background: #fff5f5;
}

.like-btn.liked {
  color: #ff6b6b;
  border-color: #ff6b6b;
  background: #fff5f5;
}

.like-icon {
  font-size: 1.1rem;
}

.post-actions {
  display: flex;
  justify-content: flex-start;
}

.comments-section {
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.comments-title {
  font-size: 1.5rem;
  margin-bottom: 20px;
  color: #333;
}

.comment-form {
  margin-bottom: 30px;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
}

.comment-form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px;
  background: #e3f2fd;
  border-radius: 4px;
  font-size: 0.9rem;
  color: #1976d2;
}

.comment-input-area {
  display: flex;
  gap: 12px;
}

.user-avatar {
  flex-shrink: 0;
}

.user-avatar img {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}

.input-wrapper {
  flex: 1;
}

.form-actions {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.emoji-section {
  position: relative;
}

.emoji-btn {
  color: #666;
  font-size: 0.9rem;
}

.emoji-picker {
  position: absolute;
  bottom: 100%;
  left: 0;
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  padding: 10px;
  z-index: 1000;
  margin-bottom: 5px;
}

.emoji-grid {
  display: grid;
  grid-template-columns: repeat(10, 1fr);
  gap: 5px;
  max-width: 300px;
  max-height: 200px;
  overflow-y: auto;
}

.emoji-item {
  font-size: 20px;
  cursor: pointer;
  padding: 5px;
  border-radius: 4px;
  text-align: center;
  transition: background-color 0.2s;
}

.emoji-item:hover {
  background-color: #f0f0f0;
}

.login-prompt {
  text-align: center;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
  margin-bottom: 30px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
}

.comment-main {
  display: flex;
  gap: 12px;
  padding: 15px;
}

.comment-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}

.comment-author {
  font-weight: 600;
  color: #333;
}

.reply-target {
  color: #1976d2;
  font-size: 0.85rem;
  background: #e3f2fd;
  padding: 2px 6px;
  border-radius: 4px;
}

.comment-time {
  color: #999;
  font-size: 0.85rem;
  margin-left: auto;
}

.comment-text {
  margin-bottom: 10px;
  line-height: 1.6;
  color: #555;
}

.comment-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.like-btn {
  color: #666;
  transition: all 0.3s ease;
}

.like-btn:hover {
  color: #ff6b6b;
}

.like-btn.liked {
  color: #ff6b6b;
}

.replies-list {
  background: #f8f9fa;
  border-top: 1px solid #eee;
}

.reply-item {
  display: flex;
  gap: 12px;
  padding: 12px 15px;
  border-bottom: 1px solid #eee;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-item .comment-avatar img {
  width: 32px;
  height: 32px;
}

@media (max-width: 768px) {
  .container {
    padding: 0 15px;
  }
  
  .post-content {
    padding: 20px;
  }
  
  .post-title {
    font-size: 2rem;
  }
  
  .post-meta {
    flex-direction: column;
    gap: 10px;
  }
}
</style>
