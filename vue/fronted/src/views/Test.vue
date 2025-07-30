<template>
  <div class="test-page">
    <h1>前端功能测试页面</h1>
    
    <div class="test-section">
      <h2>状态管理测试</h2>
      <div class="test-item">
        <h3>用户状态</h3>
        <p>登录状态: {{ userStore.isLoggedIn ? '已登录' : '未登录' }}</p>
        <p>用户信息: {{ userStore.userInfo || '无' }}</p>
        <p>Token: {{ userStore.token ? '存在' : '不存在' }}</p>
      </div>
      
      <div class="test-item">
        <h3>博客状态</h3>
        <p>文章数量: {{ blogStore.posts.length }}</p>
        <p>分类数量: {{ blogStore.categories.length }}</p>
        <p>标签数量: {{ blogStore.tags.length }}</p>
        <p>加载状态: {{ blogStore.loading ? '加载中' : '空闲' }}</p>
      </div>
    </div>
    
    <div class="test-section">
      <h2>API 测试</h2>
      <div class="test-item">
        <el-button @click="testBlogApi" :loading="testing">测试博客API</el-button>
        <el-button @click="testUserApi" :loading="testing">测试用户API</el-button>
        <p v-if="apiResult">API 结果: {{ apiResult }}</p>
      </div>
    </div>
    
    <div class="test-section">
      <h2>工具函数测试</h2>
      <div class="test-item">
        <h3>日期格式化</h3>
        <p>当前时间: {{ formatDate(new Date()) }}</p>
        <p>相对时间: {{ fromNow(new Date(Date.now() - 3600000)) }}</p>
      </div>
      
      <div class="test-item">
        <h3>通用工具</h3>
        <el-button @click="testDebounce">测试防抖</el-button>
        <el-button @click="testCopy">测试复制</el-button>
        <p v-if="toolResult">工具结果: {{ toolResult }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useBlogStore } from '@/stores/blog'
import { useUserStore } from '@/stores/user'
import { blogApi } from '@/utils/api.js'
import { formatDate, fromNow } from '@/utils/date'
import { debounce, copyToClipboard } from '@/utils/common'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const blogStore = useBlogStore()

const testing = ref(false)
const apiResult = ref('')
const toolResult = ref('')

const testBlogApi = async () => {
  testing.value = true
  try {
    const response = await blogApi.getPosts({ page: 1, size: 5 })
    apiResult.value = `成功获取数据`
    ElMessage.success('博客API测试成功')
  } catch (error) {
    apiResult.value = `API错误: ${error.message}`
    ElMessage.error('博客API测试失败')
  } finally {
    testing.value = false
  }
}

const testUserApi = async () => {
  testing.value = true
  try {
    await userStore.fetchUserInfo()
    apiResult.value = '用户API测试成功'
    ElMessage.success('用户API测试成功')
  } catch (error) {
    apiResult.value = `用户API错误: ${error.message}`
    ElMessage.warning('用户API测试失败（可能因为未登录）')
  } finally {
    testing.value = false
  }
}

const testDebounce = debounce(() => {
  toolResult.value = '防抖函数执行成功'
  ElMessage.success('防抖测试成功')
}, 1000)

const testCopy = async () => {
  const success = await copyToClipboard('测试复制内容')
  toolResult.value = success ? '复制成功' : '复制失败'
  ElMessage[success ? 'success' : 'error'](toolResult.value)
}
</script>

<style scoped>
.test-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.test-section {
  margin-bottom: 40px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.test-section h2 {
  color: #409eff;
  margin-bottom: 20px;
  border-bottom: 2px solid #409eff;
  padding-bottom: 10px;
}

.test-item {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 6px;
}

.test-item h3 {
  color: #333;
  margin-bottom: 10px;
}

.test-item p {
  margin: 5px 0;
  color: #666;
}

.test-item .el-button {
  margin-right: 10px;
  margin-bottom: 10px;
}
</style>
