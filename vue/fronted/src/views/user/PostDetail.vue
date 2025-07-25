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
          <el-form @submit.prevent="submitComment">
            <el-form-item>
              <el-input
                v-model="newComment.content"
                type="textarea"
                :rows="4"
                placeholder="写下你的评论..."
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitComment" :loading="submitting">
                发表评论
              </el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="login-prompt" v-else>
          <p>请 <router-link to="/user/login">登录</router-link> 后发表评论</p>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list">
          <div 
            v-for="comment in comments" 
            :key="comment.id" 
            class="comment-item"
          >
            <div class="comment-avatar">
              <img :src="comment.user?.avatar || '/default-avatar.png'" :alt="comment.user?.name" />
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
                  @click="replyToComment(comment.id)"
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
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { User, Calendar, Folder, ArrowLeft } from '@element-plus/icons-vue'
import { postApi, commentApi } from '@/utils/api'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const post = ref(null)
const comments = ref([])
const newComment = ref({
  content: '',
  postId: null,
  userId: null,
  parentId: null
})

// 计算属性
const isLoggedIn = computed(() => userStore.isLoggedIn)
const currentUser = computed(() => userStore.userInfo)

// 获取文章详情
const fetchPost = async () => {
  try {
    loading.value = true
    const postId = route.params.id
    const response = await postApi.getPostById(postId)
    if (response.code === 200) {
      post.value = response.data
      newComment.value.postId = postId
    } else {
      ElMessage.error('文章不存在')
      router.push('/')
    }
  } catch (error) {
    ElMessage.error('获取文章失败')
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
      comments.value = response.data
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
      ElMessage.success('评论发表成功')
      newComment.value.content = ''
      fetchComments() // 重新获取评论列表
    }
  } catch (error) {
    ElMessage.error('评论发表失败')
  } finally {
    submitting.value = false
  }
}

// 回复评论
const replyToComment = (commentId) => {
  newComment.value.parentId = commentId
  // 可以在这里添加更多回复逻辑
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

// 初始化
onMounted(() => {
  fetchPost()
  fetchComments()
})
</script>

<style scoped>
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
  display: flex;
  gap: 15px;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 8px;
}

.comment-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.comment-author {
  font-weight: bold;
  color: #333;
}

.comment-time {
  color: #999;
  font-size: 0.9rem;
}

.comment-text {
  margin-bottom: 10px;
  line-height: 1.6;
}

.comment-actions {
  display: flex;
  gap: 10px;
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
