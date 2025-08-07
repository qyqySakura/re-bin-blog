<template>
  <div class="dashboard">
    <h1 class="page-title">仪表盘</h1>
    
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon user">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.userCount }}</h3>
          <p>用户总数</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon post">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.postCount }}</h3>
          <p>文章总数</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon comment">
          <el-icon><ChatDotRound /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.commentCount }}</h3>
          <p>评论总数</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon notification">
          <el-icon><Bell /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.notificationCount }}</h3>
          <p>通知总数</p>
        </div>
      </div>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activities">
      <h2>最近活动</h2>
      <el-card>
        <div v-if="loading" class="loading">
          <el-skeleton :rows="5" animated />
        </div>
        <div v-else class="activity-list">
          <div v-for="activity in activities" :key="activity.id" class="activity-item">
            <div class="activity-icon" :class="activity.type">
              <el-icon>
                <User v-if="activity.type === 'user'" />
                <Document v-else-if="activity.type === 'post'" />
                <ChatDotRound v-else-if="activity.type === 'comment'" />
                <Bell v-else />
              </el-icon>
            </div>
            <div class="activity-content">
              <p>{{ activity.content }}</p>
              <span class="activity-time">{{ formatTime(activity.createTime) }}</span>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Document, ChatDotRound, Bell } from '@element-plus/icons-vue'
import { adminApi } from '@/api/modules/admin'

// 响应式数据
const loading = ref(true)
const stats = ref({
  userCount: 0,
  postCount: 0,
  commentCount: 0,
  notificationCount: 0
})
const activities = ref([])

// 获取统计数据
const fetchStats = async () => {
  try {
    const response = await adminApi.getStats()
    if (response.code === 200) {
      stats.value = response.data
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败')
  }
}

// 获取最近活动
const fetchActivities = async () => {
  try {
    const response = await adminApi.getRecentActivities()
    if (response.code === 200) {
      activities.value = response.data
    }
  } catch (error) {
    console.error('获取活动数据失败:', error)
    ElMessage.error('获取活动数据失败')
  } finally {
    loading.value = false
  }
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return '未知时间'
  const time = new Date(timeStr)
  if (isNaN(time.getTime())) return '时间格式错误'
  
  const now = new Date()
  const diff = now - time
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  
  if (diff < minute) return '刚刚'
  if (diff < hour) return `${Math.floor(diff / minute)}分钟前`
  if (diff < day) return `${Math.floor(diff / hour)}小时前`
  if (diff < 7 * day) return `${Math.floor(diff / day)}天前`
  return time.toLocaleDateString()
}

// 初始化
onMounted(async () => {
  await Promise.all([fetchStats(), fetchActivities()])
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.page-title {
  margin: 0 0 30px 0;
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.stat-icon.user { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stat-icon.post { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
.stat-icon.comment { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.stat-icon.notification { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }

.stat-content h3 {
  margin: 0 0 5px 0;
  font-size: 32px;
  font-weight: 700;
  color: #2c3e50;
}

.stat-content p {
  margin: 0;
  font-size: 14px;
  color: #7f8c8d;
}

.recent-activities {
  margin-top: 30px;
}

.recent-activities h2 {
  margin: 0 0 20px 0;
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
}

.loading {
  padding: 20px;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
}

.activity-content {
  flex: 1;
}

.activity-content p {
  margin: 0 0 5px 0;
  font-size: 14px;
  color: #2c3e50;
}

.activity-time {
  font-size: 12px;
  color: #95a5a6;
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .stat-card {
    padding: 20px;
  }
  
  .stat-icon {
    width: 50px;
    height: 50px;
    font-size: 20px;
  }
  
  .stat-content h3 {
    font-size: 24px;
  }
}
</style>
