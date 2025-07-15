<template>
  <div class="user-posts-page">
    <div class="page-header">
      <h1 class="page-title">我的文章</h1>
      <p class="page-subtitle">管理您发布的所有文章</p>
    </div>

    <div class="posts-container">
      <div class="posts-actions">
        <el-button type="primary" @click="$router.push('/user/edit')">
          <el-icon><Plus /></el-icon>
          写新文章
        </el-button>
        
        <div class="filter-actions">
          <el-select v-model="statusFilter" placeholder="状态筛选" clearable>
            <el-option label="全部状态" value="" />
            <el-option label="已发布" value="published" />
            <el-option label="草稿" value="draft" />
          </el-select>
          
          <el-input
            v-model="searchKeyword"
            placeholder="搜索文章标题..."
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>

      <div class="posts-list" v-loading="loading">
        <div v-if="posts.length === 0" class="empty-state">
          <el-empty description="还没有发布任何文章">
            <el-button type="primary" @click="$router.push('/user/edit')">
              开始写作
            </el-button>
          </el-empty>
        </div>

        <div v-else class="posts-grid">
          <div 
            v-for="post in posts" 
            :key="post.id"
            class="post-card"
          >
            <div class="post-cover" v-if="post.cover">
              <img :src="post.cover" :alt="post.title" />
              <div class="post-status" :class="post.status">
                {{ post.status === 'published' ? '已发布' : '草稿' }}
              </div>
            </div>
            
            <div class="post-content">
              <h3 class="post-title">{{ post.title }}</h3>
              <p class="post-summary">{{ post.summary || '暂无摘要...' }}</p>
              
              <div class="post-meta">
                <span class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  {{ formatDate(post.createTime) }}
                </span>
                <span class="meta-item">
                  <el-icon><View /></el-icon>
                  {{ post.viewCount || 0 }} 次阅读
                </span>
                <span class="meta-item" v-if="post.category">
                  <el-icon><Folder /></el-icon>
                  {{ post.category.name }}
                </span>
              </div>
              
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
              
              <div class="post-actions">
                <el-button 
                  type="primary" 
                  link 
                  @click="viewPost(post.id)"
                >
                  <el-icon><View /></el-icon>
                  查看
                </el-button>
                <el-button 
                  type="warning" 
                  link 
                  @click="editPost(post.id)"
                >
                  <el-icon><Edit /></el-icon>
                  编辑
                </el-button>
                <el-button 
                  type="danger" 
                  link 
                  @click="deletePost(post.id)"
                >
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </div>
            </div>
          </div>
        </div>
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Search, Calendar, View, Folder, Edit, Delete 
} from '@element-plus/icons-vue'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const posts = ref([])
const searchKeyword = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(6)
const total = ref(0)

// 模拟数据
const mockPosts = [
  {
    id: 1,
    title: 'Vue 3 Composition API 深度解析',
    summary: '深入了解 Vue 3 的 Composition API，掌握现代 Vue 开发的核心概念和最佳实践。',
    cover: 'https://picsum.photos/400/200?random=1',
    status: 'published',
    category: { name: 'Vue.js' },
    tags: [{ id: 1, name: 'Vue3' }, { id: 2, name: 'JavaScript' }],
    createTime: '2024-01-15',
    viewCount: 1250
  },
  {
    id: 2,
    title: 'TypeScript 进阶技巧与实战',
    summary: '分享 TypeScript 的高级特性和在实际项目中的应用技巧。',
    cover: 'https://picsum.photos/400/200?random=2',
    status: 'draft',
    category: { name: 'TypeScript' },
    tags: [{ id: 3, name: 'TypeScript' }, { id: 4, name: '前端开发' }],
    createTime: '2024-01-10',
    viewCount: 0
  }
]

// 获取文章列表
const fetchPosts = async () => {
  try {
    loading.value = true
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    posts.value = mockPosts
    total.value = mockPosts.length
  } catch (error) {
    ElMessage.error('获取文章列表失败')
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

// 查看文章
const viewPost = (postId) => {
  router.push(`/post/${postId}`)
}

// 编辑文章
const editPost = (postId) => {
  router.push(`/user/edit?id=${postId}`)
}

// 删除文章
const deletePost = async (postId) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这篇文章吗？删除后无法恢复。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    // 模拟删除API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 从列表中移除
    posts.value = posts.value.filter(post => post.id !== postId)
    total.value = posts.value.length
    
    ElMessage.success('文章删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 搜索处理
const handleSearch = () => {
  fetchPosts()
}

// 分页处理
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
  fetchPosts()
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  fetchPosts()
}

// 初始化
onMounted(() => {
  fetchPosts()
})
</script>

<style scoped>
.user-posts-page {
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

.posts-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.posts-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 20px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.08);
}

.filter-actions {
  display: flex;
  gap: 15px;
}

.posts-list {
  min-height: 400px;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
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
}

.post-status {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
  color: white;
}

.post-status.published {
  background: #67c23a;
}

.post-status.draft {
  background: #e6a23c;
}

.post-content {
  padding: 25px;
}

.post-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-summary {
  color: #5a6c7d;
  line-height: 1.6;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-meta {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
  font-size: 0.85rem;
  color: #7f8c8d;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.post-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.post-actions {
  display: flex;
  gap: 15px;
  justify-content: flex-end;
  border-top: 1px solid #f1f2f6;
  padding-top: 15px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 2.5rem;
  }
  
  .posts-actions {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }
  
  .filter-actions {
    flex-direction: column;
    gap: 10px;
  }
  
  .posts-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .post-meta {
    flex-direction: column;
    gap: 8px;
  }
  
  .post-actions {
    justify-content: center;
  }
}
</style>
