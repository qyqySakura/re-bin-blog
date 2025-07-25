<template>
  <div class="post-list">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <el-skeleton :rows="5" animated />
    </div>
    
    <!-- 空状态 -->
    <div v-else-if="posts.length === 0" class="empty-state">
      <el-empty description="暂无文章" />
    </div>
    
    <!-- 文章列表 -->
    <div v-else class="post-items">
      <article 
        v-for="post in posts" 
        :key="post.id" 
        class="post-item card"
        @click="handlePostClick(post.id)"
      >
        <!-- 封面图 -->
        <div class="post-cover" v-if="post.cover">
          <el-image 
            :src="post.cover" 
            :alt="post.title"
            fit="cover"
            lazy
            :preview-src-list="[post.cover]"
          />
        </div>
        
        <div class="post-content">
          <!-- 标题 -->
          <h2 class="post-title">{{ post.title }}</h2>
          
          <!-- 摘要 -->
          <p class="post-summary" v-if="post.summary">{{ post.summary }}</p>
          
          <!-- 元信息 -->
          <div class="post-meta">
            <div class="meta-left">
              <span class="author">
                <el-avatar :size="20" :src="post.author?.avatar" />
                <span class="author-name">{{ post.author?.name || '匿名' }}</span>
              </span>
              <span class="date">{{ formatDate(post.createTime, 'MM-DD HH:mm') }}</span>
              <span class="category" v-if="post.category">
                <el-tag size="small" type="primary">{{ post.category.name }}</el-tag>
              </span>
            </div>
            
            <div class="meta-right">
              <span class="stats">
                <el-icon><View /></el-icon>
                {{ post.viewCount || 0 }}
              </span>
              <span class="stats">
                <el-icon><ChatDotRound /></el-icon>
                {{ post.commentCount || 0 }}
              </span>
              <span class="stats">
                <el-icon><Star /></el-icon>
                {{ post.likeCount || 0 }}
              </span>
            </div>
          </div>
          
          <!-- 标签 -->
          <div class="post-tags" v-if="post.tags && post.tags.length">
            <el-tag 
              v-for="tag in post.tags" 
              :key="tag.id" 
              size="small" 
              type="info"
              effect="plain"
              class="tag-item"
              @click.stop="handleTagClick(tag.name)"
            >
              {{ tag.name }}
            </el-tag>
          </div>
        </div>
      </article>
    </div>
    
    <!-- 分页 -->
    <div class="pagination" v-if="pagination.totalPages > 1">
      <el-pagination
        v-model:current-page="pagination.page"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="prev, pager, next, jumper, total"
        @current-change="handlePageChange"
        background
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useBlogStore } from '@/stores/blog'
import { formatDate } from '@/utils/date'
import { View, ChatDotRound, Star } from '@element-plus/icons-vue'

const props = defineProps({
  categoryId: {
    type: Number,
    default: null
  },
  keyword: {
    type: String,
    default: ''
  },
  tag: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['tag-click'])

const router = useRouter()
const blogStore = useBlogStore()

const posts = computed(() => blogStore.posts)
const loading = computed(() => blogStore.loading)
const pagination = computed(() => blogStore.pagination)

const handlePostClick = (postId) => {
  router.push(`/post/${postId}`)
}

const handlePageChange = (page) => {
  blogStore.pagination.page = page
  fetchPosts()
  // 滚动到顶部
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const handleTagClick = (tagName) => {
  emit('tag-click', tagName)
}

const fetchPosts = () => {
  const params = {}
  if (props.categoryId) params.categoryId = props.categoryId
  if (props.keyword) params.keyword = props.keyword
  if (props.tag) params.tag = props.tag
  
  if (props.keyword) {
    blogStore.searchPosts(props.keyword, params)
  } else {
    blogStore.fetchPosts(params)
  }
}

// 监听props变化
watch([() => props.categoryId, () => props.keyword, () => props.tag], () => {
  blogStore.pagination.page = 1
  fetchPosts()
}, { immediate: false })

onMounted(() => {
  fetchPosts()
})
</script>

<style scoped>
.post-list {
  max-width: 900px;
  margin: 0 auto;
}

.loading {
  padding: 20px;
}

.post-item {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.post-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  border-color: #409eff;
}

.post-cover {
  flex-shrink: 0;
  width: 200px;
  height: 140px;
  border-radius: 8px;
  overflow: hidden;
}

.post-cover :deep(.el-image) {
  width: 100%;
  height: 100%;
}

.post-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 140px;
}

.post-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 10px 0;
  color: #333;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-summary {
  color: #666;
  line-height: 1.6;
  margin: 0 0 auto 0;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #999;
  margin: 15px 0 10px 0;
}

.meta-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.meta-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.author {
  display: flex;
  align-items: center;
  gap: 6px;
}

.author-name {
  font-weight: 500;
  color: #666;
}

.stats {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #999;
}

.post-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag-item {
  cursor: pointer;
  transition: all 0.2s;
}

.tag-item:hover {
  transform: scale(1.05);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding: 20px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .post-item {
    flex-direction: column;
    gap: 15px;
  }
  
  .post-cover {
    width: 100%;
    height: 200px;
  }
  
  .post-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .meta-left,
  .meta-right {
    width: 100%;
    justify-content: space-between;
  }
}

@media (max-width: 480px) {
  .post-list {
    padding: 0 10px;
  }
  
  .post-item {
    padding: 15px;
  }
  
  .post-title {
    font-size: 16px;
  }
  
  .meta-left,
  .meta-right {
    flex-wrap: wrap;
    gap: 8px;
  }
}
</style>
