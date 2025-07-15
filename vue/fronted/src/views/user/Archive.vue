<template>
  <div class="archive-page">
    <div class="page-header">
      <h1 class="page-title">文章归档</h1>
      <p class="page-subtitle">按时间顺序浏览所有文章</p>
    </div>

    <div class="archive-container">
      <div class="archive-stats">
        <div class="stat-item">
          <span class="stat-number">{{ totalPosts }}</span>
          <span class="stat-label">篇文章</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ Object.keys(archiveData).length }}</span>
          <span class="stat-label">个年份</span>
        </div>
      </div>

      <div class="archive-timeline" v-loading="loading">
        <div 
          v-for="(posts, year) in archiveData" 
          :key="year" 
          class="year-section"
        >
          <div class="year-header">
            <h2 class="year-title">{{ year }}</h2>
            <span class="year-count">{{ posts.length }} 篇</span>
          </div>
          
          <div class="posts-list">
            <div 
              v-for="post in posts" 
              :key="post.id" 
              class="post-item"
              @click="goToPost(post.id)"
            >
              <div class="post-date">
                {{ formatDate(post.createTime) }}
              </div>
              <div class="post-info">
                <h3 class="post-title">{{ post.title }}</h3>
                <div class="post-meta">
                  <span class="post-category" v-if="post.category">
                    <el-icon><Folder /></el-icon>
                    {{ post.category.name }}
                  </span>
                  <span class="post-tags" v-if="post.tags && post.tags.length">
                    <el-icon><CollectionTag /></el-icon>
                    {{ post.tags.map(tag => tag.name).join(', ') }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="empty-state" v-if="!loading && totalPosts === 0">
        <el-empty description="暂无文章" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Folder, CollectionTag } from '@element-plus/icons-vue'
import { postApi } from '@/utils/api'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const posts = ref([])

// 计算属性
const totalPosts = computed(() => posts.value.length)

const archiveData = computed(() => {
  const grouped = {}
  posts.value.forEach(post => {
    const year = new Date(post.createTime).getFullYear()
    if (!grouped[year]) {
      grouped[year] = []
    }
    grouped[year].push(post)
  })
  
  // 按年份降序排列
  const sortedYears = Object.keys(grouped).sort((a, b) => b - a)
  const result = {}
  sortedYears.forEach(year => {
    // 每年内的文章按时间降序排列
    result[year] = grouped[year].sort((a, b) => 
      new Date(b.createTime) - new Date(a.createTime)
    )
  })
  
  return result
})

// 获取所有文章
const fetchPosts = async () => {
  try {
    loading.value = true
    const response = await postApi.getPosts()
    if (response.code === 200) {
      posts.value = response.data.filter(post => post.status === 1) // 只显示已发布的文章
    }
  } catch (error) {
    ElMessage.error('获取文章列表失败')
    console.error('获取文章列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    month: '2-digit',
    day: '2-digit'
  })
}

// 跳转到文章详情
const goToPost = (postId) => {
  router.push(`/post/${postId}`)
}

// 初始化
onMounted(() => {
  fetchPosts()
})
</script>

<style scoped>
.archive-page {
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

.archive-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
}

.archive-stats {
  display: flex;
  justify-content: center;
  gap: 60px;
  margin-bottom: 50px;
  padding: 30px;
  background: white;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
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

.archive-timeline {
  position: relative;
}

.archive-timeline::before {
  content: '';
  position: absolute;
  left: 30px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: linear-gradient(to bottom, #3498db, #e74c3c);
}

.year-section {
  margin-bottom: 50px;
}

.year-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
  position: relative;
}

.year-header::before {
  content: '';
  position: absolute;
  left: 22px;
  width: 18px;
  height: 18px;
  background: #3498db;
  border-radius: 50%;
  border: 3px solid white;
  box-shadow: 0 0 0 3px #3498db;
}

.year-title {
  font-size: 2rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
  margin-left: 60px;
}

.year-count {
  background: #3498db;
  color: white;
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

.posts-list {
  margin-left: 60px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.post-item {
  display: flex;
  gap: 20px;
  padding: 20px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: all 0.3s ease;
  border-left: 4px solid #3498db;
}

.post-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 25px rgba(0,0,0,0.15);
}

.post-date {
  flex-shrink: 0;
  width: 80px;
  font-size: 0.9rem;
  color: #7f8c8d;
  font-weight: 500;
  text-align: center;
  padding: 8px;
  background: #f8f9fa;
  border-radius: 8px;
}

.post-info {
  flex: 1;
}

.post-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
  line-height: 1.4;
}

.post-meta {
  display: flex;
  gap: 20px;
  font-size: 0.85rem;
  color: #7f8c8d;
}

.post-category,
.post-tags {
  display: flex;
  align-items: center;
  gap: 5px;
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
  
  .archive-stats {
    gap: 30px;
    padding: 20px;
  }
  
  .stat-number {
    font-size: 2rem;
  }
  
  .archive-timeline::before {
    left: 15px;
  }
  
  .year-header::before {
    left: 7px;
  }
  
  .year-title {
    font-size: 1.5rem;
    margin-left: 45px;
  }
  
  .posts-list {
    margin-left: 45px;
  }
  
  .post-item {
    flex-direction: column;
    gap: 10px;
  }
  
  .post-date {
    width: auto;
    text-align: left;
  }
}
</style>
