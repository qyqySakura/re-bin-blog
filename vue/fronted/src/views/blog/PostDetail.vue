<template>
  <div class="post-detail-page">
    <!-- 导航栏 -->
    <nav class="top-navbar">
      <div class="nav-container">
        <router-link to="/" class="back-btn">
          <el-icon><ArrowLeft /></el-icon>
          返回首页
        </router-link>
        
        <div class="nav-actions">
          <el-button 
            text 
            :icon="Share" 
            @click="sharePost"
          >
            分享
          </el-button>
          
          <div class="user-section" v-if="userStore.isLoggedIn">
            <el-dropdown>
              <div class="user-info">
                <el-avatar :size="32" :src="getAvatarUrl(userStore.userInfo?.avatar)">
                  {{ userStore.userInfo?.name?.charAt(0) }}
                </el-avatar>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/user/profile')">
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item @click="userStore.logout()">
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          
          <div v-else class="auth-buttons">
            <el-button @click="$router.push('/user/login')">登录</el-button>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主内容 -->
    <main class="main-content">
      <div class="container">
        <div class="content-wrapper">
          <!-- 文章内容 -->
          <article class="post-article" v-if="post && !loading">
            <!-- 文章头部 -->
            <header class="post-header">
              <h1 class="post-title">{{ post.title }}</h1>
              
              <div class="post-meta">
                <div class="meta-left">
                  <div class="author-info">
                    <el-avatar :src="getAvatarUrl(post.author?.avatar)" :size="40">
                      {{ post.author?.name?.charAt(0) || '匿' }}
                    </el-avatar>
                    <div class="author-details">
                      <span class="author-name">{{ post.author?.name || '匿名' }}</span>
                      <span class="publish-date">{{ formatDate(post.createTime) }}</span>
                    </div>
                  </div>
                </div>
                
                <div class="meta-right">
                  <div class="post-stats">
                    <span class="stat-item">
                      <el-icon><View /></el-icon>
                      {{ post.viewCount || 0 }}
                    </span>
                    <span class="stat-item">
                      <el-icon><ChatDotRound /></el-icon>
                      {{ post.commentCount || 0 }}
                    </span>
                    <span class="stat-item">
                      <el-icon><Star /></el-icon>
                      {{ post.likeCount || 0 }}
                    </span>
                  </div>
                </div>
              </div>
              
              <!-- 分类和标签 -->
              <div class="post-taxonomy" v-if="post.category || (post.tags && post.tags.length)">
                <div class="category" v-if="post.category">
                  <el-icon><Folder /></el-icon>
                  <el-tag type="primary">{{ post.category.name }}</el-tag>
                </div>
                
                <div class="tags" v-if="post.tags && post.tags.length">
                  <el-icon><CollectionTag /></el-icon>
                  <el-tag 
                    v-for="tag in post.tags" 
                    :key="tag.id"
                    size="small"
                    effect="plain"
                    @click="handleTagClick(tag.name)"
                  >
                    {{ tag.name }}
                  </el-tag>
                </div>
              </div>
            </header>
            
            <!-- 文章内容 -->
            <div class="post-content article-content" v-html="post.content"></div>
            
            <!-- 文章底部操作 -->
            <footer class="post-footer">
              <div class="post-actions">
                <el-button 
                  type="primary" 
                  :icon="Star"
                  @click="toggleLike"
                  :loading="liking"
                  :class="{ 'is-liked': post.isLiked }"
                >
                  {{ post.isLiked ? '已点赞' : '点赞' }} ({{ post.likeCount || 0 }})
                </el-button>
                
                <el-button :icon="Share" @click="sharePost">
                  分享文章
                </el-button>
                
                <el-button :icon="Collection" @click="collectPost">
                  收藏
                </el-button>
              </div>
            </footer>
          </article>
          
          <!-- 加载状态 -->
          <div v-else-if="loading" class="loading-container">
            <el-skeleton :rows="10" animated />
          </div>
          
          <!-- 错误状态 -->
          <div v-else class="error-container">
            <el-result
              icon="warning"
              title="文章不存在"
              sub-title="抱歉，您访问的文章不存在或已被删除"
            >
              <template #extra>
                <el-button type="primary" @click="$router.push('/')">
                  返回首页
                </el-button>
              </template>
            </el-result>
          </div>
          
          <!-- 评论区域 -->
          <section class="comments-section" v-if="post && !loading">
            <div class="comments-header">
              <h3>评论 ({{ comments.length }})</h3>
            </div>
            
            <!-- 发表评论 -->
            <div class="comment-form" v-if="userStore.isLoggedIn">
              <div class="form-header">
                <el-avatar :src="userStore.userInfo?.avatar" :size="40">
                  {{ userStore.userInfo?.name?.charAt(0) }}
                </el-avatar>
                <span class="form-title">发表评论</span>
              </div>
              
              <el-input
                v-model="newComment"
                type="textarea"
                :rows="4"
                placeholder="写下你的评论..."
                maxlength="500"
                show-word-limit
                class="comment-input"
              />
              
              <div class="form-actions">
                <el-button 
                  type="primary" 
                  @click="submitComment"
                  :loading="submitting"
                  :disabled="!newComment.trim()"
                >
                  发表评论
                </el-button>
              </div>
            </div>
            
            <!-- 登录提示 -->
            <div class="login-prompt" v-else>
              <p>
                <router-link to="/user/login">登录</router-link> 
                后发表评论
              </p>
            </div>
            
            <!-- 评论列表 -->
            <div class="comments-list" v-if="comments.length">
              <CommentItem 
                v-for="comment in comments" 
                :key="comment.id"
                :comment="comment"
                :post-id="$route.params.id"
                @reply="handleReply"
                @delete="handleDeleteComment"
                @like="handleLikeComment"
              />
            </div>
            
            <!-- 空评论状态 -->
            <div class="empty-comments" v-else>
              <el-empty description="暂无评论，快来发表第一条评论吧！" />
            </div>
          </section>
        </div>
        
        <!-- 侧边栏 -->
        <aside class="sidebar">
          <!-- 目录 -->
          <div class="widget-card toc-widget" v-if="tocItems.length">
            <h3 class="widget-title">文章目录</h3>
            <div class="toc-list">
              <div 
                v-for="item in tocItems" 
                :key="item.id"
                :class="['toc-item', `toc-level-${item.level}`]"
                @click="scrollToHeading(item.id)"
              >
                {{ item.text }}
              </div>
            </div>
          </div>
          
          <!-- 相关文章 -->
          <div class="widget-card">
            <h3 class="widget-title">相关文章</h3>
            <div class="related-posts" v-if="relatedPosts.length">
              <div 
                v-for="relatedPost in relatedPosts" 
                :key="relatedPost.id"
                class="related-post-item"
                @click="$router.push(`/post/${relatedPost.id}`)"
              >
                <h4 class="related-post-title">{{ relatedPost.title }}</h4>
                <p class="related-post-date">{{ formatDate(relatedPost.createTime, 'MM-DD') }}</p>
              </div>
            </div>
            <div v-else class="no-related">
              <p>暂无相关文章</p>
            </div>
          </div>
        </aside>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useBlogStore } from '@/stores/blog'
import { useUserStore } from '@/stores/user'
import { commentApi } from '@/utils/api.js'
import { formatDate } from '@/utils/date'
import { copyToClipboard } from '@/utils/common'
import { getAvatarUrl } from '@/utils/avatar'
import { ElMessage } from 'element-plus'
import {
  ArrowLeft, Share, View, ChatDotRound, Star, Folder, 
  CollectionTag, Collection
} from '@element-plus/icons-vue'
import CommentItem from '@/components/blog/CommentItem.vue'

const route = useRoute()
const router = useRouter()
const blogStore = useBlogStore()
const userStore = useUserStore()

const post = computed(() => blogStore.currentPost)
const loading = computed(() => blogStore.loading)

const comments = ref([])
const newComment = ref('')
const submitting = ref(false)
const liking = ref(false)
const tocItems = ref([])
const relatedPosts = ref([])

// 获取文章详情
const fetchPostDetail = async () => {
  try {
    await blogStore.fetchPostDetail(route.params.id)
    await fetchComments()
    await generateTOC()
    await fetchRelatedPosts()
  } catch (error) {
    console.error('获取文章详情失败:', error)
  }
}

// 获取评论列表
const fetchComments = async () => {
  try {
    const response = await commentApi.getComments(route.params.id)
    comments.value = response.data || []
  } catch (error) {
    console.error('获取评论失败:', error)
  }
}

// 生成目录
const generateTOC = async () => {
  await nextTick()
  const headings = document.querySelectorAll('.post-content h1, .post-content h2, .post-content h3, .post-content h4, .post-content h5, .post-content h6')
  tocItems.value = Array.from(headings).map((heading, index) => ({
    id: `heading-${index}`,
    text: heading.textContent,
    level: parseInt(heading.tagName.charAt(1))
  }))
  
  // 为标题添加ID
  headings.forEach((heading, index) => {
    heading.id = `heading-${index}`
  })
}

// 获取相关文章
const fetchRelatedPosts = async () => {
  // 这里可以根据标签或分类获取相关文章
  // 暂时使用热门文章代替
  try {
    await blogStore.fetchHotPosts(5)
    relatedPosts.value = blogStore.hotPosts.filter(p => p.id !== post.value?.id).slice(0, 5)
  } catch (error) {
    console.error('获取相关文章失败:', error)
  }
}

// 提交评论
const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  submitting.value = true
  try {
    await commentApi.createComment(route.params.id, {
      content: newComment.value.trim()
    })
    
    newComment.value = ''
    await fetchComments()
    ElMessage.success('评论发表成功')
  } catch (error) {
    ElMessage.error('评论发表失败')
  } finally {
    submitting.value = false
  }
}

// 处理回复
const handleReply = async (replyData) => {
  try {
    await commentApi.replyComment(replyData.postId, replyData.parentId, {
      content: replyData.content
    })
    await fetchComments()
  } catch (error) {
    ElMessage.error('回复失败')
  }
}

// 删除评论
const handleDeleteComment = async (commentId) => {
  try {
    await commentApi.deleteComment(commentId)
    await fetchComments()
    ElMessage.success('评论删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// 点赞评论
const handleLikeComment = async (likeData) => {
  try {
    await commentApi.likeComment(likeData.commentId)
    await fetchComments()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 点赞文章
const toggleLike = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  
  liking.value = true
  try {
    await blogStore.togglePostLike(route.params.id)
    ElMessage.success(post.value.isLiked ? '点赞成功' : '取消点赞')
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    liking.value = false
  }
}

// 分享文章
const sharePost = async () => {
  const url = window.location.href
  const title = post.value?.title || '分享文章'
  
  if (navigator.share) {
    try {
      await navigator.share({
        title,
        url
      })
    } catch (error) {
      // 用户取消分享
    }
  } else {
    // 降级到复制链接
    const success = await copyToClipboard(url)
    if (success) {
      ElMessage.success('链接已复制到剪贴板')
    } else {
      ElMessage.error('复制失败')
    }
  }
}

// 收藏文章
const collectPost = () => {
  ElMessage.info('收藏功能开发中...')
}

// 处理标签点击
const handleTagClick = (tagName) => {
  router.push(`/search?tag=${encodeURIComponent(tagName)}`)
}

// 滚动到标题
const scrollToHeading = (headingId) => {
  const element = document.getElementById(headingId)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' })
  }
}

onMounted(() => {
  fetchPostDetail()
})
</script>

<style scoped>
.post-detail-page {
  min-height: 100vh;
  background: #f8f9fa;
}

.top-navbar {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid #e9ecef;
  padding: 15px 0;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.back-btn:hover {
  color: #409eff;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  cursor: pointer;
}

.auth-buttons {
  display: flex;
  gap: 10px;
}

.main-content {
  padding: 40px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 40px;
}

.post-article {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.post-header {
  margin-bottom: 40px;
  padding-bottom: 30px;
  border-bottom: 1px solid #e9ecef;
}

.post-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #333;
  margin: 0 0 30px 0;
  line-height: 1.3;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-details {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: 600;
  color: #333;
  font-size: 16px;
}

.publish-date {
  font-size: 14px;
  color: #666;
}

.post-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 14px;
}

.post-taxonomy {
  display: flex;
  gap: 20px;
  align-items: center;
}

.category,
.tags {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tags {
  flex-wrap: wrap;
}

.post-content {
  margin-bottom: 40px;
  line-height: 1.8;
}

.post-footer {
  padding-top: 30px;
  border-top: 1px solid #e9ecef;
}

.post-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.post-actions .el-button.is-liked {
  background: #f56c6c;
  border-color: #f56c6c;
  color: white;
}

.loading-container,
.error-container {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.comments-section {
  background: white;
  border-radius: 12px;
  padding: 30px;
  margin-top: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.comments-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e9ecef;
}

.comments-header h3 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.comment-form {
  margin-bottom: 30px;
}

.form-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 15px;
}

.form-title {
  font-weight: 600;
  color: #333;
}

.comment-input {
  margin-bottom: 15px;
}

.form-actions {
  text-align: right;
}

.login-prompt {
  text-align: center;
  padding: 30px;
  color: #666;
}

.login-prompt a {
  color: #409eff;
  text-decoration: none;
}

.comments-list {
  margin-top: 30px;
}

.empty-comments {
  padding: 40px 0;
}

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.widget-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.widget-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
  margin: 0 0 20px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid #409eff;
}

.toc-list {
  max-height: 400px;
  overflow-y: auto;
}

.toc-item {
  padding: 8px 0;
  cursor: pointer;
  color: #666;
  transition: color 0.3s;
  border-left: 2px solid transparent;
  padding-left: 10px;
}

.toc-item:hover {
  color: #409eff;
  border-left-color: #409eff;
}

.toc-level-1 { font-weight: 600; }
.toc-level-2 { padding-left: 20px; }
.toc-level-3 { padding-left: 30px; }
.toc-level-4 { padding-left: 40px; }

.related-posts {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.related-post-item {
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.related-post-item:hover {
  background: #e9ecef;
  transform: translateX(5px);
}

.related-post-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin: 0 0 5px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.related-post-date {
  font-size: 12px;
  color: #666;
  margin: 0;
}

.no-related {
  text-align: center;
  color: #999;
  padding: 20px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    grid-template-columns: 1fr;
    gap: 30px;
  }
  
  .post-article {
    padding: 25px;
  }
  
  .post-title {
    font-size: 2rem;
  }
  
  .post-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .post-taxonomy {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .post-actions {
    flex-direction: column;
  }
  
  .nav-container {
    flex-direction: column;
    gap: 15px;
  }
}
</style>
