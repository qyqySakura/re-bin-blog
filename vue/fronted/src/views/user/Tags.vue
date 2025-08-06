<template>
  <div class="tags-page">
    <div class="page-header">
      <h1 class="page-title">{{ isSpecificTag ? currentTag?.name : '标签云' }}</h1>
      <p class="page-subtitle">
        {{ isSpecificTag ? `${currentTag?.name} 标签下的所有文章` : '按标签浏览文章' }}
      </p>
    </div>

    <div class="tags-container">
      <!-- 标签云 -->
      <div v-if="!isSpecificTag" class="tags-section" v-loading="loading">
        <div class="tags-stats">
          <div class="stat-item">
            <span class="stat-number">{{ tags.length }}</span>
            <span class="stat-label">个标签</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">{{ totalPosts }}</span>
            <span class="stat-label">篇文章</span>
          </div>
        </div>

        <div class="tag-cloud">
          <div 
            v-for="tag in tags" 
            :key="tag.id"
            class="tag-item"
            :class="getTagSizeClass(tag.postCount)"
            @click="goToTag(tag.id)"
          >
            <span class="tag-name">{{ tag.name }}</span>
            <span class="tag-count">{{ tag.postCount || 0 }}</span>
          </div>
        </div>
      </div>

      <!-- 特定标签的文章列表 -->
      <div v-else class="tag-posts">
        <div class="tag-header">
          <el-button @click="goBack" type="primary" plain>
            <el-icon><ArrowLeft /></el-icon>
            返回标签云
          </el-button>
        </div>

        <div class="posts-grid" v-loading="loading">
          <article 
            v-for="post in posts" 
            :key="post.id"
            class="post-card"
            @click="goToPost(post.id)"
          >
            <div class="post-cover" v-if="post.cover">
              <img
                :src="processImageUrl(post.cover, 'cover')"
                :alt="post.title"
                @error="(e) => handleImageError(e, 'cover')"
                loading="lazy"
              />
              <div class="cover-overlay">
                <el-icon class="read-icon"><View /></el-icon>
              </div>
            </div>
            <div class="post-content">
              <h3 class="post-title">{{ post.title }}</h3>
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
              <p class="post-summary">{{ post.summary || '暂无摘要...' }}</p>
              <div class="post-tags" v-if="post.tags && post.tags.length">
                <el-tag 
                  v-for="postTag in post.tags.slice(0, 3)" 
                  :key="postTag.id" 
                  size="small"
                  effect="plain"
                  :type="postTag.id === currentTag?.id ? 'primary' : ''"
                >
                  {{ postTag.name }}
                </el-tag>
              </div>
            </div>
          </article>
        </div>

        <!-- 分页 -->
        <div class="pagination-container" v-if="total > 0">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[6, 12, 24]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
          />
        </div>
      </div>

      <!-- 空状态 -->
      <div class="empty-state" v-if="!loading && ((!isSpecificTag && tags.length === 0) || (isSpecificTag && posts.length === 0))">
        <el-empty :description="isSpecificTag ? '该标签下暂无文章' : '暂无标签'" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  ArrowLeft, View, User, Calendar, Folder
} from '@element-plus/icons-vue'
import { blogApi } from '@/utils/api'
import { processImageUrl, handleImageError } from '@/utils/imageUtils'

const router = useRouter()
const route = useRoute()

// 响应式数据
const loading = ref(false)
const tags = ref([])
const posts = ref([])
const currentTag = ref(null)
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

// 计算属性
const isSpecificTag = computed(() => !!route.params.id)

const totalPosts = computed(() => {
  return tags.value.reduce((sum, tag) => sum + (tag.postCount || 0), 0)
})

// 获取标签大小样式类
const getTagSizeClass = (postCount) => {
  if (postCount >= 10) return 'tag-xl'
  if (postCount >= 5) return 'tag-lg'
  if (postCount >= 3) return 'tag-md'
  return 'tag-sm'
}

// 获取标签列表
const fetchTags = async () => {
  try {
    loading.value = true
    const response = await blogApi.getTags()
    if (response.code === 200) {
      tags.value = response.data.sort((a, b) => (b.postCount || 0) - (a.postCount || 0))
    }
  } catch (error) {
    ElMessage.error('获取标签列表失败')
    console.error('获取标签列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取特定标签的文章
const fetchTagPosts = async (tagId) => {
  try {
    loading.value = true
    const response = await blogApi.getPostsByTag(tagId, {
      page: currentPage.value,
      size: pageSize.value
    })
    if (response.code === 200) {
      posts.value = response.data.posts
      total.value = response.data.total
      currentTag.value = response.data.tag
    }
  } catch (error) {
    ElMessage.error('获取标签文章失败')
    console.error('获取标签文章失败:', error)
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

// 跳转到标签
const goToTag = (tagId) => {
  router.push(`/tag/${tagId}`)
}

// 跳转到文章详情
const goToPost = (postId) => {
  router.push(`/post/${postId}`)
}

// 返回标签云
const goBack = () => {
  router.push('/tags')
}



// 分页处理
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
  if (isSpecificTag.value) {
    fetchTagPosts(route.params.id)
  }
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  if (isSpecificTag.value) {
    fetchTagPosts(route.params.id)
  }
}

// 监听路由变化
watch(() => route.params.id, (newId) => {
  if (newId) {
    fetchTagPosts(newId)
  } else {
    fetchTags()
  }
}, { immediate: true })

// 初始化
onMounted(() => {
  if (isSpecificTag.value) {
    fetchTagPosts(route.params.id)
  } else {
    fetchTags()
  }
})
</script>

<style scoped>
.tags-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 80px 0 40px;
}

.page-header {
  text-align: center;
  margin-bottom: 50px;
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

.tags-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 标签云部分 */
.tags-section {
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
}

.tags-stats {
  display: flex;
  justify-content: center;
  gap: 60px;
  margin-bottom: 40px;
  padding-bottom: 30px;
  border-bottom: 2px solid #f1f2f6;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 2.5rem;
  font-weight: 700;
  color: #3498db;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 1rem;
  color: #7f8c8d;
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  justify-content: center;
  align-items: center;
}

.tag-item {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  box-shadow: 0 3px 10px rgba(52, 152, 219, 0.3);
}

.tag-item:hover {
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 8px 25px rgba(52, 152, 219, 0.4);
}

.tag-sm {
  font-size: 0.8rem;
}

.tag-md {
  font-size: 0.9rem;
}

.tag-lg {
  font-size: 1rem;
}

.tag-xl {
  font-size: 1.1rem;
  background: linear-gradient(135deg, #e74c3c, #c0392b);
  box-shadow: 0 3px 10px rgba(231, 76, 60, 0.3);
}

.tag-xl:hover {
  box-shadow: 0 8px 25px rgba(231, 76, 60, 0.4);
}

.tag-count {
  background: rgba(255, 255, 255, 0.2);
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

/* 标签文章部分 */
.tag-header {
  margin-bottom: 30px;
}

.posts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 30px;
  margin-bottom: 40px;
}

.post-card {
  background: white;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 5px 20px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #f1f2f6;
}

.post-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0,0,0,0.15);
}

.post-cover {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.post-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.post-card:hover .post-cover img {
  transform: scale(1.05);
}

.cover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.post-card:hover .cover-overlay {
  opacity: 1;
}

.read-icon {
  color: white;
  font-size: 2rem;
}

.post-content {
  padding: 25px;
}

.post-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 15px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-meta {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
  font-size: 0.85rem;
  color: #7f8c8d;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.post-summary {
  color: #5a6c7d;
  line-height: 1.6;
  margin-bottom: 20px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.pagination-container {
  display: flex;
  justify-content: center;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 2.5rem;
  }
  
  .tags-stats {
    gap: 30px;
  }
  
  .stat-number {
    font-size: 2rem;
  }
  
  .tag-cloud {
    gap: 10px;
  }
  
  .posts-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .tags-section {
    padding: 20px;
  }
}
</style>
