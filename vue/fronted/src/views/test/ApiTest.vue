<template>
  <div class="api-test">
    <h1>API测试页面</h1>
    
    <div class="test-section">
      <h2>博客文章列表</h2>
      <el-button @click="testGetPosts" type="primary">获取文章列表</el-button>
      <div v-if="postsData" class="result">
        <h3>结果：</h3>
        <pre>{{ JSON.stringify(postsData, null, 2) }}</pre>
      </div>
    </div>

    <div class="test-section">
      <h2>分类列表</h2>
      <el-button @click="testGetCategories" type="primary">获取分类列表</el-button>
      <div v-if="categoriesData" class="result">
        <h3>结果：</h3>
        <pre>{{ JSON.stringify(categoriesData, null, 2) }}</pre>
      </div>
    </div>

    <div class="test-section">
      <h2>标签列表</h2>
      <el-button @click="testGetTags" type="primary">获取标签列表</el-button>
      <div v-if="tagsData" class="result">
        <h3>结果：</h3>
        <pre>{{ JSON.stringify(tagsData, null, 2) }}</pre>
      </div>
    </div>

    <div class="test-section">
      <h2>网站统计</h2>
      <el-button @click="testGetStatistics" type="primary">获取统计信息</el-button>
      <div v-if="statisticsData" class="result">
        <h3>结果：</h3>
        <pre>{{ JSON.stringify(statisticsData, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import * as blogApi from '@/api/blog'
import { ElMessage } from 'element-plus'

const postsData = ref(null)
const categoriesData = ref(null)
const tagsData = ref(null)
const statisticsData = ref(null)

const testGetPosts = async () => {
  try {
    const response = await blogApi.getHomePosts(1, 5)
    postsData.value = response
    ElMessage.success('获取文章列表成功')
  } catch (error) {
    console.error('获取文章列表失败:', error)
    ElMessage.error('获取文章列表失败: ' + error.message)
  }
}

const testGetCategories = async () => {
  try {
    const response = await blogApi.getCategories()
    categoriesData.value = response
    ElMessage.success('获取分类列表成功')
  } catch (error) {
    console.error('获取分类列表失败:', error)
    ElMessage.error('获取分类列表失败: ' + error.message)
  }
}

const testGetTags = async () => {
  try {
    const response = await blogApi.getTags()
    tagsData.value = response
    ElMessage.success('获取标签列表成功')
  } catch (error) {
    console.error('获取标签列表失败:', error)
    ElMessage.error('获取标签列表失败: ' + error.message)
  }
}

const testGetStatistics = async () => {
  try {
    const response = await blogApi.getBlogStatistics()
    statisticsData.value = response
    ElMessage.success('获取统计信息成功')
  } catch (error) {
    console.error('获取统计信息失败:', error)
    ElMessage.error('获取统计信息失败: ' + error.message)
  }
}
</script>

<style scoped>
.api-test {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.test-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.test-section h2 {
  margin-bottom: 15px;
  color: #333;
}

.result {
  margin-top: 15px;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.result h3 {
  margin-bottom: 10px;
  color: #666;
}

.result pre {
  background-color: #fff;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
  overflow-x: auto;
  font-size: 12px;
  line-height: 1.4;
}
</style>
