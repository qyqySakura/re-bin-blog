<template>
  <div class="notifications-page">
    <div class="page-container">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1 class="page-title">消息通知</h1>
        <div class="header-actions">
          <el-button 
            v-if="unreadCount > 0"
            type="primary" 
            @click="markAllAsRead"
            :loading="markingAll"
          >
            全部标记为已读 ({{ unreadCount }})
          </el-button>
        </div>
      </div>

      <!-- 通知筛选 -->
      <div class="filter-section">
        <el-radio-group v-model="filterType" @change="handleFilterChange">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="unread">未读</el-radio-button>
          <el-radio-button label="LIKE">点赞</el-radio-button>
          <el-radio-button label="COMMENT">评论</el-radio-button>
          <el-radio-button label="REPLY">回复</el-radio-button>
          <!-- <el-radio-button label="FOLLOW">关注</el-radio-button> -->
        </el-radio-group>
      </div>

      <!-- 通知列表 -->
      <div class="notifications-content" v-loading="loading">
        <div v-if="notifications.length === 0" class="empty-state">
          <el-icon class="empty-icon"><Bell /></el-icon>
          <h3>暂无消息通知</h3>
          <p>当有人点赞、评论或关注你时，会在这里显示通知</p>
        </div>

        <div v-else class="notification-list">
          <div 
            v-for="notification in notifications" 
            :key="notification.id"
            class="notification-card"
            :class="{ 'unread': !notification.isRead }"
          >
            <!-- 通知内容 -->
            <div class="notification-main" @click="handleNotificationClick(notification)">
              <!-- 通知类型图标 -->
              <div class="notification-icon-wrapper">
                <div class="notification-type-icon" :class="notification.type.toLowerCase()">
                  <el-icon :size="20">
                    <StarFilled v-if="notification.type === 'LIKE'" />
                    <ChatDotRound v-else-if="notification.type === 'COMMENT'" />
                    <ChatLineRound v-else-if="notification.type === 'REPLY'" />
                    <User v-else-if="notification.type === 'FOLLOW'" />
                  </el-icon>
                </div>
              </div>

              <!-- 通知详情 -->
              <div class="notification-details">
                <div class="notification-content">
                  <h4 class="notification-title">{{ notification.title }}</h4>
                  <p class="notification-text">{{ notification.content }}</p>
                </div>
                <div class="notification-meta">
                  <span class="notification-time">
                    {{ formatTime(notification.createTime) }}
                  </span>
                  <el-tag 
                    :type="getTypeTagType(notification.type)" 
                    size="small"
                    effect="plain"
                  >
                    {{ getTypeText(notification.type) }}
                  </el-tag>
                </div>
              </div>

              <!-- 未读标识 -->
              <div v-if="!notification.isRead" class="unread-indicator">
                <div class="unread-dot"></div>
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="notification-actions">
              <el-button 
                v-if="!notification.isRead"
                type="text" 
                size="small"
                @click="markAsRead(notification)"
              >
                标记已读
              </el-button>
              <el-button 
                type="text" 
                size="small"
                @click="deleteNotification(notification.id)"
                class="delete-btn"
              >
                删除
              </el-button>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-container" v-if="total > 0">
          <el-pagination
            :current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            :page-sizes="[10, 20, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Bell, StarFilled, ChatDotRound, ChatLineRound, User
} from '@element-plus/icons-vue'
import { notificationApi } from '@/utils/api'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const notifications = ref([])
const loading = ref(false)
const markingAll = ref(false)
const filterType = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 计算属性
const unreadCount = computed(() => {
  return notifications.value.filter(n => !n.isRead).length
})

// 获取头像URL
const getAvatarUrl = (avatar) => {
  if (!avatar) return ''
  if (avatar.startsWith('http')) return avatar
  return `http://localhost:9090${avatar}`
}

// 格式化时间
const formatTime = (timeStr) => {
  const time = new Date(timeStr)
  const now = new Date()
  const diff = now - time
  
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  
  if (diff < minute) {
    return '刚刚'
  } else if (diff < hour) {
    return `${Math.floor(diff / minute)}分钟前`
  } else if (diff < day) {
    return `${Math.floor(diff / hour)}小时前`
  } else if (diff < 7 * day) {
    return `${Math.floor(diff / day)}天前`
  } else {
    return time.toLocaleDateString() + ' ' + time.toLocaleTimeString()
  }
}

// 获取类型标签类型
const getTypeTagType = (type) => {
  const typeMap = {
    'LIKE': 'danger',
    'COMMENT': 'primary',
    'REPLY': 'success',
    'FOLLOW': 'warning'
  }
  return typeMap[type] || 'info'
}

// 获取类型文本
const getTypeText = (type) => {
  const typeMap = {
    'LIKE': '点赞',
    'COMMENT': '评论',
    'REPLY': '回复',
    // 'FOLLOW': '关注'
  }
  return typeMap[type] || type
}

// 获取通知列表
const fetchNotifications = async () => {
  loading.value = true
  try {
    // 构建查询参数
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }

    // 添加筛选参数
    if (filterType.value !== 'all') {
      if (filterType.value === 'unread') {
        params.isRead = false
      } else {
        params.type = filterType.value
      }
    }

    const filters = {}
    if (filterType.value === 'unread') {
      filters.isRead = false
    } else if (filterType.value !== 'all') {
      filters.type = filterType.value
    }

    const response = await notificationApi.getNotifications(params.page, params.size, filters)
    if (response.code === 200) {
      notifications.value = response.data.notifications || []
      total.value = response.data.total || 0
    }
  } catch (error) {
    ElMessage.error('获取通知列表失败')
    console.error('获取通知列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 筛选变化
const handleFilterChange = () => {
  currentPage.value = 1
  fetchNotifications()
}

// 分页变化
const handleSizeChange = () => {
  currentPage.value = 1
  fetchNotifications()
}

const handleCurrentChange = () => {
  fetchNotifications()
}

// 点击通知
const handleNotificationClick = async (notification) => {
  // 标记为已读
  if (!notification.isRead) {
    await markAsRead(notification)
  }
  
  // 跳转到相关页面
  if (notification.relatedType === 'POST' && notification.relatedId) {
    router.push(`/post/${notification.relatedId}`)
  }
}

// 标记单个为已读
const markAsRead = async (notification) => {
  try {
    await notificationApi.markAsRead(notification.id)
    notification.isRead = true
    ElMessage.success('已标记为已读')
  } catch (error) {
    ElMessage.error('操作失败')
    console.error('标记已读失败:', error)
  }
}

// 标记所有为已读
const markAllAsRead = async () => {
  markingAll.value = true
  try {
    await notificationApi.markAllAsRead()
    notifications.value.forEach(n => n.isRead = true)
    ElMessage.success('已标记全部为已读')
  } catch (error) {
    ElMessage.error('操作失败')
    console.error('标记全部已读失败:', error)
  } finally {
    markingAll.value = false
  }
}

// 删除通知
const deleteNotification = async (notificationId) => {
  try {
    await ElMessageBox.confirm('确定要删除这条通知吗？', '确认删除', {
      type: 'warning'
    })
    
    await notificationApi.deleteNotification(notificationId)
    const index = notifications.value.findIndex(n => n.id === notificationId)
    if (index > -1) {
      notifications.value.splice(index, 1)
      total.value--
    }
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除通知失败:', error)
    }
  }
}

// 组件挂载
onMounted(() => {
  if (!userStore.isLoggedIn) {
    router.push('/user/login')
    return
  }
  fetchNotifications()
})
</script>

<style scoped>
.notifications-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20px 0;
}

.page-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px 0;
  border-bottom: 1px solid #ddd;
}

.page-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.filter-section {
  margin-bottom: 20px;
  padding: 15px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.notifications-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #666;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 20px;
  opacity: 0.5;
}

.empty-state h3 {
  font-size: 1.2rem;
  margin-bottom: 8px;
  color: #333;
}

.notification-list {
  background: white;
}

.notification-card {
  border-bottom: 1px solid #eee;
}

.notification-card {
  display: flex;
  padding: 15px;
  transition: background-color 0.3s;
  border-left: 3px solid transparent;
}

.notification-card:hover {
  background-color: #f9f9f9;
}

.notification-card.unread {
  background-color: #f0f8ff;
  border-left-color: #409eff;
}

.notification-main {
  flex: 1;
  display: flex;
  cursor: pointer;
}

.notification-icon-wrapper {
  margin-right: 15px;
  flex-shrink: 0;
}

.notification-type-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.notification-type-icon.like {
  background: linear-gradient(135deg, #ff6b6b, #ee5a52);
}

.notification-type-icon.comment {
  background: linear-gradient(135deg, #4ecdc4, #44a08d);
}

.notification-type-icon.reply {
  background: linear-gradient(135deg, #45b7d1, #96c93d);
}

.notification-type-icon.follow {
  background: linear-gradient(135deg, #a8edea, #fed6e3);
  color: #666;
}

.notification-details {
  flex: 1;
  min-width: 0;
}

.notification-title {
  font-size: 1rem;
  font-weight: 600;
  color: #333;
  margin: 0 0 6px 0;
}

.notification-text {
  color: #666;
  line-height: 1.4;
  margin: 0 0 8px 0;
  font-size: 14px;
}

.notification-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.notification-time {
  font-size: 12px;
  color: #999;
}

.unread-indicator {
  display: flex;
  align-items: flex-start;
  padding-top: 4px;
  margin-left: 12px;
}

.unread-dot {
  width: 8px;
  height: 8px;
  background-color: #409eff;
  border-radius: 50%;
}

.notification-actions {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-left: 12px;
  opacity: 0;
  transition: opacity 0.3s;
}

.notification-card:hover .notification-actions {
  opacity: 1;
}

.delete-btn {
  color: #f56c6c;
}

.pagination-container {
  padding: 20px;
  display: flex;
  justify-content: center;
  border-top: 1px solid #eee;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .notification-card {
    flex-direction: column;
    gap: 12px;
  }

  .notification-main {
    flex-direction: column;
  }

  .notification-actions {
    flex-direction: row;
    opacity: 1;
    margin-left: 0;
  }

  .page-container {
    padding: 0 15px;
  }
}
</style>
