<template>
  <div class="modern-blog">

    <!-- 主横幅区域 -->
    <section class="hero-banner">
      <div class="hero-background">
        <div class="hero-gradient"></div>
      </div>
      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">欢迎来到 RE-BIN</h1>
          <p class="hero-subtitle">分享技术心得，记录生活点滴</p>
          <p class="hero-description">在这里，我会分享编程技术、生活感悟和学习心得</p>
          <div class="hero-actions">
            <el-button type="primary" size="large" @click="scrollToContent">
              <el-icon><ArrowDown /></el-icon>
              开始阅读
            </el-button>
            <el-button size="large" @click="$router.push('/about')">
              关于我
            </el-button>
          </div>
        </div>
        <div class="hero-stats">
          <div class="stat-card">
            <div class="stat-number">{{ total }}</div>
            <div class="stat-label">文章总数</div>
          </div>
          <div class="stat-card">
            <div class="stat-number">{{ categories.length }}</div>
            <div class="stat-label">分类数量</div>
          </div>
          <div class="stat-card">
            <div class="stat-number">{{ tags.length }}</div>
            <div class="stat-label">标签数量</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 主内容区域 -->
    <main class="main-content">
      <div class="content-container">
        <div class="content-grid">
          <!-- 左侧文章列表 -->
          <div class="articles-section">
            <div class="section-header">
              <h2 class="section-title">最新文章</h2>
              <div class="section-actions">
                <el-select v-model="sortBy" placeholder="排序方式" size="small">
                  <el-option label="最新发布" value="latest" />
                  <el-option label="最多阅读" value="popular" />
                </el-select>
              </div>
            </div>

            <div class="articles-grid" v-loading="loading">
              <article
                v-for="article in articles"
                :key="article.id"
                class="article-card"
                @click="goToPost(article.id)"
              >
                <div class="article-cover" v-if="article.cover">
                  <img
                    :src="article.cover"
                    :alt="article.title"
                    @error="handleImageError"
                    loading="lazy"
                  />
                  <div class="cover-overlay">
                    <el-icon class="read-icon"><View /></el-icon>
                  </div>
                </div>
                <div class="article-content">
                  <div class="article-header">
                    <h3 class="article-title">{{ article.title }}</h3>
                    <div class="article-meta">
                      <span class="meta-item">
                        <el-icon><User /></el-icon>
                        {{ article.author?.name || '匿名' }}
                      </span>
                      <span class="meta-item">
                        <el-icon><Calendar /></el-icon>
                        {{ formatDate(article.createTime) }}
                      </span>
                      <span class="meta-item" v-if="article.category">
                        <el-icon><Folder /></el-icon>
                        {{ article.category.name }}
                      </span>
                    </div>
                  </div>
                  <p class="article-summary">{{ article.summary || '暂无摘要...' }}</p>
                  <div class="article-footer">
                    <div class="article-tags" v-if="article.tags && article.tags.length">
                      <el-tag
                        v-for="tag in article.tags.slice(0, 3)"
                        :key="tag.id"
                        size="small"
                        effect="plain"
                      >
                        {{ tag.name }}
                      </el-tag>
                      <span v-if="article.tags.length > 3" class="more-tags">+{{ article.tags.length - 3 }}</span>
                    </div>
                    <div class="read-more">
                      <el-button type="primary" link>
                        阅读全文 <el-icon><ArrowRight /></el-icon>
                      </el-button>
                    </div>
                  </div>
                </div>
              </article>
            </div>

            <!-- 分页组件 -->
            <div class="pagination-container" v-if="total > 0">
              <el-pagination
                :current-page="currentPage"
                :page-size="pageSize"
                :total="total"
                :page-sizes="[6, 12, 24]"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                background
              />
            </div>
          </div>

          <!-- 右侧边栏 -->
          <aside class="sidebar">
            <!-- 个人信息卡片 -->
            <div class="widget-card author-card">
              <div class="author-avatar">
                <img src="../../assets/default-avatar.png" alt="作者头像" />
              </div>
              <div class="author-info">
                <h3 class="author-name">RE-BIN</h3>
                <p class="author-desc">热爱技术，喜欢分享</p>
                <div class="author-stats">
                  <div class="stat-item">
                    <span class="stat-value">{{ total }}</span>
                    <span class="stat-label">文章</span>
                  </div>
                  <div class="stat-item">
                    <span class="stat-value">{{ categories.length }}</span>
                    <span class="stat-label">分类</span>
                  </div>
                  <div class="stat-item">
                    <span class="stat-value">{{ tags.length }}</span>
                    <span class="stat-label">标签</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 分类卡片 -->
            <div class="widget-card">
              <h3 class="widget-title">文章分类</h3>
              <div class="category-list">
                <div
                  v-for="category in categories"
                  :key="category.id"
                  class="category-item"
                  @click="filterByCategory(category.id)"
                >
                  <span class="category-name">{{ category.name }}</span>
                  <el-icon><ArrowRight /></el-icon>
                </div>
              </div>
            </div>

            <!-- 标签云 -->
            <div class="widget-card">
              <h3 class="widget-title">标签云</h3>
              <div class="tag-cloud">
                <el-tag
                  v-for="tag in tags"
                  :key="tag.id"
                  class="tag-item"
                  @click="filterByTag(tag.id)"
                  effect="plain"
                >
                  {{ tag.name }}
                </el-tag>
              </div>
            </div>

            <!-- 公告卡片 -->
            <div class="widget-card">
              <h3 class="widget-title">网站公告</h3>
              <div class="notice-content">
                <p>欢迎来到我的个人博客！</p>
                <p>这里会分享技术文章、生活感悟和学习心得。</p>
                <p>如有问题，欢迎留言交流。</p>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="site-footer">
      <div class="footer-container">
        <div class="footer-content">
          <div class="footer-section">
            <h4>RE-BIN 博客</h4>
            <p>分享技术，记录生活</p>
          </div>
          <div class="footer-section">
            <h4>友情链接</h4>
            <div class="links">
              <a
                v-for="link in friendLinks.slice(0, 6)"
                :key="link.id"
                @click="openFriendLink(link.url)"
                :title="link.description"
                style="cursor: pointer;"
              >
                {{ link.name }}
              </a>
              <router-link
                to="/friendlinks"
                class="more-link"
                v-if="friendLinks.length > 6"
              >
                更多 →
              </router-link>
            </div>
          </div>
          <div class="footer-section">
            <h4>联系方式</h4>
            <div class="contact">
              <p>Email: contact@rebin.com</p>
              <p>QQ: 123456789</p>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2025 RE-BIN. All rights reserved.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  ArrowDown, View, ArrowRight
} from '@element-plus/icons-vue'
import { useBlogStore } from '@/stores/blog'
import { useUserStore } from '@/stores/user'
import { formatDate } from '@/utils/date'
import { blogApi } from '@/utils/api'

const router = useRouter()
const blogStore = useBlogStore()
const userStore = useUserStore()

// 响应式数据
const sortBy = ref('latest')
const friendLinks = ref([])

// 计算属性
const loading = computed(() => blogStore.loading)
const articles = computed(() => blogStore.posts)
const categories = computed(() => blogStore.categories)
const tags = computed(() => blogStore.tags)
const currentPage = computed({
  get: () => blogStore.pagination.page,
  set: (value) => blogStore.pagination.page = value
})
const pageSize = computed({
  get: () => blogStore.pagination.size,
  set: (value) => blogStore.pagination.size = value
})
const total = computed(() => blogStore.pagination.total)



// 获取友链数据
const fetchFriendLinks = async () => {
  try {
    const response = await blogApi.getFriendLinks()
    if (response.code === 200) {
      friendLinks.value = response.data
    }
  } catch (error) {
    console.error('获取友链数据失败:', error)
  }
}

// 获取首页数据
const fetchHomeData = async () => {
  try {
    await Promise.all([
      blogStore.fetchPosts({ page: 1, size: pageSize.value }),
      blogStore.fetchCategories(),
      blogStore.fetchTags(),
      fetchFriendLinks()
    ])
  } catch (error) {
    console.error('获取首页数据失败:', error)
    ElMessage.error('获取数据失败，请刷新页面重试')
  }
}

// 跳转到文章详情
const goToPost = (postId) => {
  router.push(`/post/${postId}`)
}

// 按分类筛选
const filterByCategory = (categoryId) => {
  router.push(`/category/${categoryId}`)
}

// 按标签筛选
const filterByTag = (tagId) => {
  router.push(`/tag/${tagId}`)
}

// 打开友链
const openFriendLink = (url) => {
  if (!url) return

  // 确保URL有协议前缀
  let fullUrl = url
  if (!url.startsWith('http://') && !url.startsWith('https://')) {
    fullUrl = 'https://' + url
  }

  // 在新窗口打开
  window.open(fullUrl, '_blank')
}

// 处理图片加载错误
const handleImageError = (event) => {
  console.error('图片加载失败:', event.target.src)
  // 设置默认图片
  event.target.src = 'https://images.unsplash.com/photo-1518709268805-4e9042af2176?w=800&h=400&fit=crop'
}



// 滚动到内容区域
const scrollToContent = () => {
  document.querySelector('.main-content').scrollIntoView({
    behavior: 'smooth'
  })
}

// 分页处理
const handleSizeChange = (newSize) => {
  blogStore.pagination.size = newSize
  blogStore.pagination.page = 1
  fetchHomeData()
}

const handleCurrentChange = (newPage) => {
  blogStore.pagination.page = newPage
  fetchHomeData()
}

// 初始化
onMounted(async () => {
  await fetchHomeData()
  // 如果用户已登录但没有用户信息，获取用户信息
  if (userStore.token && !userStore.userInfo) {
    await userStore.initUser()
  }
})
</script>

<style scoped>
/* 全局样式 */
.modern-blog {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}



/* 主横幅 */
.hero-banner {
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.hero-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.hero-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, rgba(0,0,0,0.1) 0%, rgba(255,255,255,0.1) 100%);
}

.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  color: white;
  max-width: 800px;
  padding: 0 20px;
}

.hero-title {
  font-size: 4rem;
  font-weight: 700;
  margin-bottom: 20px;
  text-shadow: 0 4px 20px rgba(0,0,0,0.3);
  animation: fadeInUp 1s ease-out;
}

.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 10px;
  opacity: 0.9;
  animation: fadeInUp 1s ease-out 0.2s both;
}

.hero-description {
  font-size: 1.1rem;
  margin-bottom: 40px;
  opacity: 0.8;
  animation: fadeInUp 1s ease-out 0.4s both;
}

.hero-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-bottom: 60px;
  animation: fadeInUp 1s ease-out 0.6s both;
}

.hero-stats {
  display: flex;
  gap: 40px;
  justify-content: center;
  animation: fadeInUp 1s ease-out 0.8s both;
}

.stat-card {
  text-align: center;
  padding: 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: transform 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-number {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 主内容区域 */
.main-content {
  background: #f8f9fa;
  min-height: 100vh;
  padding: 80px 0;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 40px;
}

/* 文章区域 */
.articles-section {
  background: white;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f1f2f6;
}

.section-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.articles-grid {
  display: grid;
  gap: 30px;
}

.article-card {
  background: white;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 5px 20px rgba(0,0,0,0.08);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #f1f2f6;
}

.article-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0,0,0,0.15);
}

.article-cover {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.article-card:hover .article-cover img {
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

.article-card:hover .cover-overlay {
  opacity: 1;
}

.read-icon {
  color: white;
  font-size: 2rem;
}

.article-content {
  padding: 25px;
}

.article-title {
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

.article-meta {
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

.article-summary {
  color: #5a6c7d;
  line-height: 1.6;
  margin-bottom: 20px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-tags {
  display: flex;
  gap: 8px;
  align-items: center;
}

.more-tags {
  font-size: 0.8rem;
  color: #7f8c8d;
}

.pagination-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

/* 侧边栏 */
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.widget-card {
  background: white;
  border-radius: 15px;
  padding: 25px;
  box-shadow: 0 5px 20px rgba(0,0,0,0.08);
  border: 1px solid #f1f2f6;
}

.widget-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #3498db;
}

/* 作者卡片 */
.author-card {
  text-align: center;
}

.author-avatar img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-bottom: 15px;
  border: 3px solid #3498db;
}

.author-name {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 8px;
}

.author-desc {
  color: #7f8c8d;
  margin-bottom: 20px;
}

.author-stats {
  display: flex;
  justify-content: space-around;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 1.5rem;
  font-weight: 600;
  color: #3498db;
}

.stat-label {
  font-size: 0.85rem;
  color: #7f8c8d;
}

/* 分类列表 */
.category-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.category-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.category-item:hover {
  background: #3498db;
  color: white;
  transform: translateX(5px);
}

.category-name {
  font-weight: 500;
}

/* 标签云 */
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag-item {
  cursor: pointer;
  transition: all 0.3s ease;
}

.tag-item:hover {
  transform: scale(1.05);
}

/* 公告内容 */
.notice-content {
  color: #5a6c7d;
  line-height: 1.6;
}

.notice-content p {
  margin-bottom: 10px;
}

/* 页脚 */
.site-footer {
  background: #2c3e50;
  color: white;
  padding: 50px 0 20px;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 40px;
  margin-bottom: 30px;
}

.footer-section h4 {
  font-size: 1.2rem;
  margin-bottom: 15px;
  color: #3498db;
}

.footer-section p {
  color: #bdc3c7;
  line-height: 1.6;
}

.links {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.links a {
  color: #bdc3c7;
  text-decoration: none;
  transition: color 0.3s ease;
}

.links a:hover {
  color: #3498db;
}

.more-link {
  color: #3498db !important;
  font-weight: 500;
  margin-top: 5px;
}

.more-link:hover {
  color: #2980b9 !important;
}

.contact p {
  color: #bdc3c7;
  margin-bottom: 8px;
}

.footer-bottom {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #34495e;
  color: #95a5a6;
}

/* 动画 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    height: auto;
    padding: 15px 20px;
  }

  .nav-menu {
    margin: 15px 0;
    gap: 15px;
  }

  .nav-actions {
    gap: 10px;
  }

  .search-container {
    width: 200px;
  }

  .hero-title {
    font-size: 2.5rem;
  }

  .hero-stats {
    flex-direction: column;
    gap: 20px;
  }

  .content-grid {
    grid-template-columns: 1fr;
    gap: 30px;
  }

  .sidebar {
    order: -1;
  }

  .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 2rem;
  }

  .hero-actions {
    flex-direction: column;
    gap: 15px;
  }

  .articles-section,
  .widget-card {
    padding: 20px;
  }
}

/* 暗色主题 */
:global(.dark) .modern-blog {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
}

:global(.dark) .top-navbar {
  background: rgba(44, 62, 80, 0.95);
  border-bottom-color: rgba(255, 255, 255, 0.1);
}

:global(.dark) .logo-text,
:global(.dark) .nav-item,
:global(.dark) .username {
  color: #ecf0f1;
}

:global(.dark) .logo-subtitle {
  color: #bdc3c7;
}

:global(.dark) .main-content {
  background: #34495e;
}

:global(.dark) .articles-section,
:global(.dark) .widget-card {
  background: #2c3e50;
  border-color: #34495e;
}

:global(.dark) .section-title,
:global(.dark) .article-title,
:global(.dark) .widget-title,
:global(.dark) .author-name {
  color: #ecf0f1;
}

:global(.dark) .article-summary,
:global(.dark) .notice-content {
  color: #bdc3c7;
}

:global(.dark) .category-item {
  background: #34495e;
  color: #ecf0f1;
}

:global(.dark) .category-item:hover {
  background: #3498db;
}
</style>