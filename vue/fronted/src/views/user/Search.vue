<template>
  <div class="search-page">
    <div class="page-header">
      <h1 class="page-title">搜索结果</h1>
      <p class="page-subtitle" v-if="searchQuery">
        关键词: "{{ searchQuery }}" 
        <span v-if="!loading">找到 {{ total }} 个结果</span>
      </p>
    </div>

    <div class="search-container">
      <!-- 搜索框 -->
      <div class="search-box">
        <el-input
          v-model="searchKeyword"
          placeholder="输入关键词搜索..."
          size="large"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button @click="handleSearch" :loading="loading">搜索</el-button>
          </template>
        </el-input>
      </div>

      <!-- 搜索过滤器 -->
      <div class="search-filters">
        <div class="filter-group">
          <span class="filter-label">排序:</span>
          <el-select v-model="sortBy" @change="handleSearch" size="small">
            <el-option label="相关性" value="relevance" />
            <el-option label="最新发布" value="latest" />
            <el-option label="最多阅读" value="popular" />
          </el-select>
        </div>
        
        <div class="filter-group">
          <span class="filter-label">分类:</span>
          <el-select v-model="categoryFilter" @change="handleSearch" size="small" clearable>
            <el-option label="全部分类" value="" />
            <el-option 
              v-for="category in categories" 
              :key="category.id"
              :label="category.name" 
              :value="category.id" 
            />
          </el-select>
        </div>

        <div class="filter-group">
          <span class="filter-label">时间:</span>
          <el-select v-model="timeFilter" @change="handleSearch" size="small" clearable>
            <el-option label="全部时间" value="" />
            <el-option label="最近一周" value="week" />
            <el-option label="最近一月" value="month" />
            <el-option label="最近一年" value="year" />
          </el-select>
        </div>
      </div>

      <!-- 搜索结果 -->
      <div class="search-results" v-loading="loading">
        <div v-if="searchResults.length > 0" class="results-list">
          <div 
            v-for="result in searchResults" 
            :key="result.id"
            class="result-item"
            @click="goToPost(result.id)"
          >
            <div class="result-content">
              <h3 class="result-title" v-html="highlightKeyword(result.title)"></h3>
              <div class="result-meta">
                <span class="meta-item">
                  <el-icon><User /></el-icon>
                  {{ result.author?.name || '匿名' }}
                </span>
                <span class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  {{ formatDate(result.createTime) }}
                </span>
                <span class="meta-item" v-if="result.category">
                  <el-icon><Folder /></el-icon>
                  {{ result.category.name }}
                </span>
                <span class="meta-item">
                  <el-icon><View /></el-icon>
                  {{ result.viewCount || 0 }} 次阅读
                </span>
              </div>
              <p class="result-summary" v-html="highlightKeyword(result.summary || '暂无摘要...')"></p>
              <div class="result-tags" v-if="result.tags && result.tags.length">
                <el-tag 
                  v-for="tag in result.tags.slice(0, 3)" 
                  :key="tag.id" 
                  size="small"
                  effect="plain"
                >
                  {{ tag.name }}
                </el-tag>
              </div>
            </div>
            <div class="result-cover" v-if="result.cover">
              <img
                :src="processImageUrl(result.cover, 'cover')"
                :alt="result.title"
                @error="(e) => handleImageError(e, 'cover')"
                loading="lazy"
              />
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else-if="!loading && searchQuery" class="empty-results">
          <el-empty description="没有找到相关结果">
            <template #image>
              <el-icon size="100" color="#c0c4cc"><Search /></el-icon>
            </template>
            <div class="empty-actions">
              <p>尝试以下操作:</p>
              <ul>
                <li>检查关键词拼写</li>
                <li>使用更通用的关键词</li>
                <li>减少关键词数量</li>
                <li>尝试不同的搜索条件</li>
              </ul>
            </div>
          </el-empty>
        </div>

        <!-- 初始状态 -->
        <div v-else-if="!searchQuery" class="search-tips">
          <div class="tips-content">
            <el-icon size="80" color="#409eff"><Search /></el-icon>
            <h3>开始搜索</h3>
            <p>输入关键词搜索文章内容</p>
            <div class="search-suggestions">
              <!-- 搜索历史 -->
              <div class="search-history" v-if="searchHistory.length > 0">
                <h4>搜索历史:</h4>
                <div class="history-tags">
                  <el-tag
                    v-for="item in searchHistory.slice(0, 5)"
                    :key="item"
                    @click="searchKeyword = item; handleSearch()"
                    class="history-tag"
                    closable
                    @close="removeSearchHistory(item)"
                  >
                    {{ item }}
                  </el-tag>
                </div>
              </div>

              <!-- 热门搜索 -->
              <div class="popular-searches">
                <h4>热门搜索:</h4>
                <div class="popular-tags">
                  <el-tag
                    v-for="tag in popularSearches"
                    :key="tag"
                    @click="searchKeyword = tag; handleSearch()"
                    class="popular-tag"
                  >
                    {{ tag }}
                  </el-tag>
                </div>
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
          :page-sizes="[10, 20, 50]"
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
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, User, Calendar, Folder, View } from '@element-plus/icons-vue'
import { blogApi, categoryApi } from '@/utils/api'
import { processImageUrl, handleImageError } from '@/utils/imageUtils'

const route = useRoute()
const router = useRouter()

// 响应式数据
const loading = ref(false)
const searchKeyword = ref('')
const searchQuery = ref('')
const searchResults = ref([])
const categories = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const sortBy = ref('relevance')
const categoryFilter = ref('')
const timeFilter = ref('')

const popularSearches = ref([
  'Vue3', 'JavaScript', 'TypeScript', 'Node.js', 'React',
  '前端开发', '后端开发', '数据库', 'CSS', 'HTML'
])

const searchHistory = ref([])
const searchSuggestions = ref([])
const showSuggestions = ref(false)

// 获取分类列表
const fetchCategories = async () => {
  try {
    const response = await categoryApi.getCategories()
    if (response.code === 200) {
      categories.value = response.data
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

// 保存搜索历史
const saveSearchHistory = (keyword) => {
  if (!keyword.trim()) return

  const history = JSON.parse(localStorage.getItem('searchHistory') || '[]')
  const filteredHistory = history.filter(item => item !== keyword)
  filteredHistory.unshift(keyword)

  // 只保留最近10条搜索记录
  const newHistory = filteredHistory.slice(0, 10)
  localStorage.setItem('searchHistory', JSON.stringify(newHistory))
  searchHistory.value = newHistory
}

// 加载搜索历史
const loadSearchHistory = () => {
  const history = JSON.parse(localStorage.getItem('searchHistory') || '[]')
  searchHistory.value = history
}

// 删除搜索历史项
const removeSearchHistory = (keyword) => {
  const history = searchHistory.value.filter(item => item !== keyword)
  searchHistory.value = history
  localStorage.setItem('searchHistory', JSON.stringify(history))
}

// 搜索处理
const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }

  try {
    loading.value = true
    searchQuery.value = searchKeyword.value
    showSuggestions.value = false

    // 保存搜索历史
    saveSearchHistory(searchKeyword.value)

    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      sortBy: sortBy.value,
      category: categoryFilter.value,
      timeRange: timeFilter.value
    }

    const response = await blogApi.searchPosts(searchKeyword.value, params)
    if (response.code === 200) {
      searchResults.value = response.data.posts || []
      total.value = response.data.total || 0

      // 更新URL
      router.replace({
        path: '/search',
        query: { q: searchKeyword.value }
      })
    }
  } catch (error) {
    ElMessage.error('搜索失败')
    console.error('搜索失败:', error)
  } finally {
    loading.value = false
  }
}

// 高亮关键词
const highlightKeyword = (text) => {
  if (!searchQuery.value || !text) return text
  const regex = new RegExp(`(${searchQuery.value})`, 'gi')
  return text.replace(regex, '<mark>$1</mark>')
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

// 跳转到文章详情
const goToPost = (postId) => {
  router.push(`/post/${postId}`)
}

// 分页处理
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
  handleSearch()
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  handleSearch()
}

// 监听路由查询参数
watch(() => route.query.q, (newQuery) => {
  if (newQuery) {
    searchKeyword.value = newQuery
    handleSearch()
  }
}, { immediate: true })



// 初始化
onMounted(() => {
  fetchCategories()
  loadSearchHistory()

  // 从URL获取搜索关键词
  if (route.query.q) {
    searchKeyword.value = route.query.q
  }
})
</script>

<style scoped>
.search-page {
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

.search-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.search-box {
  margin-bottom: 30px;
}

.search-filters {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.08);
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-weight: 500;
  color: #2c3e50;
  white-space: nowrap;
}

.search-results {
  min-height: 400px;
}

.results-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.result-item {
  display: flex;
  gap: 20px;
  padding: 25px;
  background: white;
  border-radius: 15px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #f1f2f6;
}

.result-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 25px rgba(0,0,0,0.15);
}

.result-content {
  flex: 1;
}

.result-title {
  font-size: 1.4rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
  line-height: 1.4;
}

.result-meta {
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

.result-summary {
  color: #5a6c7d;
  line-height: 1.6;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.result-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.result-cover {
  flex-shrink: 0;
  width: 120px;
  height: 90px;
  border-radius: 8px;
  overflow: hidden;
}

.result-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.empty-results {
  text-align: center;
  padding: 60px 20px;
}

.empty-actions {
  margin-top: 20px;
  text-align: left;
  display: inline-block;
}

.empty-actions p {
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
}

.empty-actions ul {
  color: #7f8c8d;
  line-height: 1.8;
}

.search-tips {
  text-align: center;
  padding: 80px 20px;
}

.tips-content h3 {
  font-size: 1.8rem;
  color: #2c3e50;
  margin: 20px 0 10px;
}

.tips-content p {
  color: #7f8c8d;
  margin-bottom: 40px;
}

.search-suggestions {
  max-width: 600px;
  margin: 0 auto;
}

.search-history {
  margin-bottom: 30px;
}

.search-history h4,
.popular-searches h4 {
  color: #2c3e50;
  margin-bottom: 15px;
  font-size: 1.1rem;
}

.history-tags,
.popular-tags {
  display: flex;
  gap: 10px;
  justify-content: center;
  flex-wrap: wrap;
}

.history-tag,
.popular-tag {
  cursor: pointer;
  transition: all 0.3s ease;
}

.history-tag {
  background: #e8f4fd;
  color: #409eff;
  border-color: #b3d8ff;
}

.history-tag:hover,
.popular-tag:hover {
  transform: scale(1.05);
}

.pagination-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

/* 高亮样式 */
:deep(mark) {
  background: #fff3cd;
  color: #856404;
  padding: 2px 4px;
  border-radius: 3px;
  font-weight: 600;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 2.5rem;
  }
  
  .search-filters {
    flex-direction: column;
    gap: 15px;
  }
  
  .filter-group {
    justify-content: space-between;
  }
  
  .result-item {
    flex-direction: column;
    gap: 15px;
  }
  
  .result-cover {
    width: 100%;
    height: 150px;
  }
  
  .result-meta {
    gap: 10px;
  }
  
  .popular-tags {
    gap: 8px;
  }
}

@media (max-width: 480px) {
  .search-filters {
    padding: 15px;
  }
  
  .result-item {
    padding: 20px;
  }
  
  .result-title {
    font-size: 1.2rem;
  }
  
  .result-meta {
    flex-direction: column;
    gap: 8px;
  }
}
</style>
