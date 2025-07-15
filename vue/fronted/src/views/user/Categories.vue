<template>
  <div class="categories-page">
    <div class="page-header">
      <h1 class="page-title">{{ isSpecificCategory ? currentCategory?.name : '文章分类' }}</h1>
      <p class="page-subtitle">
        {{ isSpecificCategory ? `${currentCategory?.name} 分类下的所有文章` : '按分类浏览文章' }}
      </p>
    </div>

    <div class="categories-container">
      <!-- 分类总览 -->
      <div v-if="!isSpecificCategory" class="categories-grid" v-loading="loading">
        <div 
          v-for="category in categories" 
          :key="category.id"
          class="category-card"
          @click="goToCategory(category.id)"
        >
          <div class="category-icon">
            <el-icon><Folder /></el-icon>
          </div>
          <div class="category-info">
            <h3 class="category-name">{{ category.name }}</h3>
            <p class="category-desc">{{ category.description || '暂无描述' }}</p>
            <div class="category-stats">
              <span class="post-count">{{ category.postCount || 0 }} 篇文章</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 特定分类的文章列表 -->
      <div v-else class="category-posts">
        <div class="category-header">
          <el-button @click="goBack" type="primary" plain>
            <el-icon><ArrowLeft /></el-icon>
            返回分类列表
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
              <img :src="post.cover" :alt="post.title" />
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
              </div>
              <p class="post-summary">{{ post.summary || '暂无摘要...' }}</p>
              <div class="post-tags" v-if="post.tags && post.tags.length">
                <el-tag 
                  v-for="tag in post.tags.slice(0, 3)" 
                  :key="tag.id" 
                  size="small"
                  effect="plain"
                >
                  {{ tag.name }}
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
      <div class="empty-state" v-if="!loading && ((!isSpecificCategory && categories.length === 0) || (isSpecificCategory && posts.length === 0))">
        <el-empty :description="isSpecificCategory ? '该分类下暂无文章' : '暂无分类'" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  Folder, ArrowLeft, View, User, Calendar 
} from '@element-plus/icons-vue'
import { categoryApi, postApi } from '@/utils/api'

const router = useRouter()
const route = useRoute()

// 响应式数据
const loading = ref(false)
const categories = ref([])
const posts = ref([])
const currentCategory = ref(null)
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

// 计算属性
const isSpecificCategory = computed(() => !!route.params.id)

// 获取分类列表
const fetchCategories = async () => {
  try {
    loading.value = true
    const response = await categoryApi.getCategories()
    if (response.code === 200) {
      categories.value = response.data
    }
  } catch (error) {
    ElMessage.error('获取分类列表失败')
    console.error('获取分类列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取特定分类的文章
const fetchCategoryPosts = async (categoryId) => {
  try {
    loading.value = true
    const response = await postApi.getPostsByCategory(categoryId, currentPage.value, pageSize.value)
    if (response.code === 200) {
      posts.value = response.data.posts
      total.value = response.data.total
      currentCategory.value = response.data.category
    }
  } catch (error) {
    ElMessage.error('获取分类文章失败')
    console.error('获取分类文章失败:', error)
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

// 跳转到分类
const goToCategory = (categoryId) => {
  router.push(`/category/${categoryId}`)
}

// 跳转到文章详情
const goToPost = (postId) => {
  router.push(`/post/${postId}`)
}

// 返回分类列表
const goBack = () => {
  router.push('/categories')
}

// 分页处理
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
  if (isSpecificCategory.value) {
    fetchCategoryPosts(route.params.id)
  }
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  if (isSpecificCategory.value) {
    fetchCategoryPosts(route.params.id)
  }
}

// 监听路由变化
watch(() => route.params.id, (newId) => {
  if (newId) {
    fetchCategoryPosts(newId)
  } else {
    fetchCategories()
  }
}, { immediate: true })

// 初始化
onMounted(() => {
  if (isSpecificCategory.value) {
    fetchCategoryPosts(route.params.id)
  } else {
    fetchCategories()
  }
})
</script>

<style scoped>
.categories-page {
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

.categories-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 分类网格 */
.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 30px;
}

.category-card {
  background: white;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #f1f2f6;
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.15);
}

.category-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  border-radius: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  color: white;
  font-size: 1.5rem;
}

.category-name {
  font-size: 1.5rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
}

.category-desc {
  color: #7f8c8d;
  line-height: 1.6;
  margin-bottom: 15px;
}

.category-stats {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-count {
  background: #3498db;
  color: white;
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

/* 分类文章 */
.category-header {
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
  
  .categories-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .posts-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .category-card,
  .post-card {
    margin: 0 10px;
  }
}
</style>
