<template>
  <div class="friend-links-page">
    <div class="page-header">
      <h1 class="page-title">友情链接</h1>
      <p class="page-subtitle">与优秀的朋友们一起成长</p>
    </div>

    <div class="friend-links-container">
      <div class="links-grid" v-loading="loading">
        <div 
          v-for="link in friendLinks" 
          :key="link.id"
          class="link-card"
          @click="openLink(link.url)"
        >
          <div class="link-icon">
            <el-icon><Link /></el-icon>
          </div>
          <div class="link-info">
            <h3 class="link-name">{{ link.name }}</h3>
            <p class="link-desc">{{ link.description || '暂无描述' }}</p>
            <div class="link-url">{{ formatUrl(link.url) }}</div>
          </div>
          <div class="link-arrow">
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div class="empty-state" v-if="!loading && friendLinks.length === 0">
        <el-empty description="暂无友情链接" />
      </div>

      <!-- 申请友链提示 -->
      <div class="apply-section" v-if="friendLinks.length > 0">
        <div class="apply-card">
          <h3>申请友链</h3>
          <p>如果您也有优质的网站，欢迎与我们交换友情链接！</p>
          <div class="contact-info">
            <p><strong>联系方式：</strong>请通过邮件或留言板联系我</p>
            <p><strong>要求：</strong>内容健康、更新活跃、访问稳定</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Link, ArrowRight } from '@element-plus/icons-vue'
import { blogApi } from '@/utils/api'

// 响应式数据
const loading = ref(false)
const friendLinks = ref([])

// 获取友链列表
const fetchFriendLinks = async () => {
  try {
    loading.value = true
    const response = await blogApi.getFriendLinks()
    if (response.code === 200) {
      friendLinks.value = response.data
    }
  } catch (error) {
    ElMessage.error('获取友链列表失败')
    console.error('获取友链列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 格式化URL显示
const formatUrl = (url) => {
  if (!url) return ''
  // 移除协议前缀，只显示域名部分
  return url.replace(/^https?:\/\//, '').replace(/\/$/, '')
}

// 打开链接
const openLink = (url) => {
  if (!url) return
  
  // 确保URL有协议前缀
  let fullUrl = url
  if (!url.startsWith('http://') && !url.startsWith('https://')) {
    fullUrl = 'https://' + url
  }
  
  // 在新窗口打开
  window.open(fullUrl, '_blank')
}

// 初始化
onMounted(() => {
  fetchFriendLinks()
})
</script>

<style scoped>
.friend-links-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 80px 0 40px;
}

.page-header {
  text-align: center;
  margin-bottom: 50px;
}

.page-title {
  font-size: 3rem;
  font-weight: 700;
  color: white;
  margin-bottom: 15px;
  text-shadow: 0 2px 10px rgba(0,0,0,0.3);
}

.page-subtitle {
  font-size: 1.2rem;
  color: rgba(255,255,255,0.8);
  margin: 0;
}

.friend-links-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 友链网格 */
.links-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 25px;
  margin-bottom: 50px;
}

.link-card {
  background: white;
  border-radius: 15px;
  padding: 25px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #f1f2f6;
  display: flex;
  align-items: center;
  gap: 20px;
}

.link-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.15);
}

.link-icon {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.3rem;
  flex-shrink: 0;
}

.link-info {
  flex: 1;
}

.link-name {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 8px;
}

.link-desc {
  color: #7f8c8d;
  line-height: 1.5;
  margin-bottom: 8px;
  font-size: 0.9rem;
}

.link-url {
  color: #667eea;
  font-size: 0.85rem;
  font-weight: 500;
}

.link-arrow {
  color: #bdc3c7;
  font-size: 1.2rem;
  transition: all 0.3s ease;
}

.link-card:hover .link-arrow {
  color: #667eea;
  transform: translateX(5px);
}

/* 申请友链区域 */
.apply-section {
  margin-top: 50px;
}

.apply-card {
  background: rgba(255,255,255,0.95);
  border-radius: 15px;
  padding: 40px;
  text-align: center;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
}

.apply-card h3 {
  font-size: 1.5rem;
  color: #2c3e50;
  margin-bottom: 15px;
}

.apply-card p {
  color: #5a6c7d;
  line-height: 1.6;
  margin-bottom: 20px;
}

.contact-info {
  background: #f8f9fa;
  border-radius: 10px;
  padding: 20px;
  text-align: left;
}

.contact-info p {
  margin-bottom: 10px;
  font-size: 0.9rem;
}

.contact-info strong {
  color: #2c3e50;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: rgba(255,255,255,0.9);
  border-radius: 15px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 2.5rem;
  }
  
  .links-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .link-card {
    margin: 0 10px;
    padding: 20px;
  }
  
  .apply-card {
    margin: 0 10px;
    padding: 30px 20px;
  }
}
</style>
