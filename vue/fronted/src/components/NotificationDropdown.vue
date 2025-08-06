<template>
  <el-dropdown 
    trigger="click" 
    @visible-change="handleDropdownVisible"
    placement="bottom-end"
    :hide-on-click="false"
  >
    <div class="notification-trigger">
      <el-badge :value="unreadCount" :hidden="unreadCount === 0" :max="99">
        <el-icon class="notification-icon" :size="20">
          <Bell />
        </el-icon>
      </el-badge>
    </div>
    
    <template #dropdown>
      <el-dropdown-menu class="notification-dropdown">
        <!-- 头部 -->
        <div class="notification-header">
          <span class="title">消息通知</span>
          <el-button 
            v-if="unreadCount > 0"
            type="text" 
            size="small" 
            @click="markAllAsRead"
            class="mark-all-btn"
          >
            全部已读
          </el-button>
        </div>
        
        <!-- 通知列表 -->
        <div class="notification-list" v-loading="loading">
          <div v-if="notifications.length === 0" class="empty-state">
            <el-icon class="empty-icon"><Bell /></el-icon>
            <p>暂无消息通知</p>
          </div>
          
          <div 
            v-else
            v-for="notification in notifications" 
            :key="notification.id"
            class="notification-item"
            :class="{ 'unread': !notification.isRead }"
            @click="handleNotificationClick(notification)"
          >
            <!-- 通知图标 -->
            <div class="notification-icon-wrapper">
              <div class="notification-type-icon" :class="notification.type.toLowerCase()">
                <el-icon :size="16">
                  <StarFilled v-if="notification.type === 'LIKE'" />
                  <ChatDotRound v-else-if="notification.type === 'COMMENT'" />
                  <ChatLineRound v-else-if="notification.type === 'REPLY'" />
                  <User v-else-if="notification.type === 'FOLLOW'" />
                </el-icon>
              </div>
            </div>
            
            <!-- 通知内容 -->
            <div class="notification-content">
              <div class="notification-text">
                {{ notification.content }}
              </div>
              <div class="notification-time">
                {{ formatTime(notification.createTime) }}
              </div>
            </div>
            
            <!-- 未读标识 -->
            <div v-if="!notification.isRead" class="unread-dot"></div>
            
            <!-- 删除按钮 -->
            <el-button 
              type="text" 
              size="small" 
              class="delete-btn"
              @click.stop="deleteNotification(notification.id)"
            >
              <el-icon><Close /></el-icon>
            </el-button>
          </div>
        </div>
        
        <!-- 底部 -->
        <div class="notification-footer" v-if="notifications.length > 0">
          <el-button type="text" @click="goToNotificationPage">
            查看全部消息
          </el-button>
        </div>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Bell, StarFilled, ChatDotRound, ChatLineRound, User, Close
} from '@element-plus/icons-vue'
import { notificationApi } from '@/utils/api'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const notifications = ref([])
const unreadCount = ref(0)
const loading = ref(false)

// 计算属性
const isLoggedIn = computed(() => userStore.isLoggedIn)

// 获取头像URL
const getAvatarUrl = (avatar) => {
  if (!avatar) return ''
  if (avatar.startsWith('http')) return avatar
  return `http://localhost:9090${avatar}`
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) {
    console.warn('时间字符串为空:', timeStr)
    return '未知时间'
  }

  console.log('原始时间字符串:', timeStr)
  const time = new Date(timeStr)
  console.log('解析后的时间对象:', time)

  if (isNaN(time.getTime())) {
    console.error('时间解析失败:', timeStr)
    return '时间格式错误'
  }

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
    return time.toLocaleDateString()
  }
}

// 获取未读通知数量
const fetchUnreadCount = async () => {
  if (!isLoggedIn.value) return

  try {
    const response = await notificationApi.getUnreadCount()
    if (response.code === 200) {
      // 后端直接返回数字，不是对象
      unreadCount.value = response.data || 0
    }
  } catch (error) {
    console.error('获取未读通知数量失败:', error)
  }
}

// 获取最近通知
const fetchRecentNotifications = async () => {
  if (!isLoggedIn.value) return

  loading.value = true
  try {
    const response = await notificationApi.getRecentNotifications(10)
    if (response.code === 200) {
      notifications.value = response.data
      // 调试：打印通知数据
      console.log('通知数据:', notifications.value)
    }
  } catch (error) {
    console.error('获取通知列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 下拉框显示/隐藏事件
const handleDropdownVisible = (visible) => {
  if (visible && isLoggedIn.value) {
    fetchRecentNotifications()
  }
}

// 点击通知项
const handleNotificationClick = async (notification) => {
  // 标记为已读
  if (!notification.isRead) {
    try {
      await notificationApi.markAsRead(notification.id)
      notification.isRead = true
      unreadCount.value = Math.max(0, unreadCount.value - 1)
    } catch (error) {
      console.error('标记通知已读失败:', error)
    }
  }
  
  // 跳转到相关页面
  if (notification.relatedType === 'POST' && notification.relatedId) {
    router.push(`/post/${notification.relatedId}`)
  }
}

// 标记所有为已读
const markAllAsRead = async () => {
  try {
    await notificationApi.markAllAsRead()
    notifications.value.forEach(n => n.isRead = true)
    unreadCount.value = 0
    ElMessage.success('已标记全部为已读')
  } catch (error) {
    ElMessage.error('操作失败')
    console.error('标记全部已读失败:', error)
  }
}

// 删除通知
const deleteNotification = async (notificationId) => {
  try {
    await notificationApi.deleteNotification(notificationId)
    const index = notifications.value.findIndex(n => n.id === notificationId)
    if (index > -1) {
      const notification = notifications.value[index]
      if (!notification.isRead) {
        unreadCount.value = Math.max(0, unreadCount.value - 1)
      }
      notifications.value.splice(index, 1)
    }
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
    console.error('删除通知失败:', error)
  }
}

// 跳转到通知页面
const goToNotificationPage = () => {
  router.push('/notifications')
}

// 定期刷新未读数量
let refreshTimer = null

const startRefreshTimer = () => {
  if (refreshTimer) clearInterval(refreshTimer)
  refreshTimer = setInterval(() => {
    if (isLoggedIn.value) {
      fetchUnreadCount()
    }
  }, 30000) // 30秒刷新一次
}

const stopRefreshTimer = () => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
    refreshTimer = null
  }
}

// 组件挂载
onMounted(() => {
  if (isLoggedIn.value) {
    fetchUnreadCount()
    startRefreshTimer()
  }
})

// 监听登录状态变化
userStore.$subscribe((mutation, state) => {
  if (state.isLoggedIn) {
    fetchUnreadCount()
    startRefreshTimer()
  } else {
    unreadCount.value = 0
    notifications.value = []
    stopRefreshTimer()
  }
})

// 暴露方法供外部调用
defineExpose({
  fetchUnreadCount,
  fetchRecentNotifications
})
</script>

<style scoped>
.notification-trigger {
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.notification-trigger:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.notification-icon {
  color: #606266;
}

.notification-dropdown {
  width: 360px;
  max-height: 500px;
  padding: 0;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #ebeef5;
  background: #fafafa;
}

.notification-header .title {
  font-weight: 600;
  color: #303133;
}

.mark-all-btn {
  color: #409eff;
  padding: 0;
}

.notification-list {
  max-height: 400px;
  overflow-y: auto;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s;
  position: relative;
}

.notification-item:hover {
  background-color: #f5f7fa;
}

.notification-item.unread {
  background-color: #f0f9ff;
}

.notification-icon-wrapper {
  margin-right: 12px;
  flex-shrink: 0;
}

.notification-type-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 500;
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

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-text {
  font-size: 14px;
  color: #303133;
  line-height: 1.4;
  margin-bottom: 4px;
}

.notification-time {
  font-size: 12px;
  color: #909399;
}

.unread-dot {
  width: 8px;
  height: 8px;
  background-color: #f56565;
  border-radius: 50%;
  margin-left: 8px;
  flex-shrink: 0;
}

.delete-btn {
  opacity: 0;
  transition: opacity 0.3s;
  margin-left: 8px;
  color: #c0c4cc;
}

.notification-item:hover .delete-btn {
  opacity: 1;
}

.notification-footer {
  padding: 12px 16px;
  text-align: center;
  border-top: 1px solid #ebeef5;
  background: #fafafa;
}

.notification-footer .el-button {
  color: #409eff;
}
</style>
