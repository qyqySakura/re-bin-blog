<template>
  <div class="comment-manage">
    <div class="page-header">
      <h1>评论管理</h1>
      <div class="header-actions">
        <el-select v-model="statusFilter" placeholder="状态筛选" clearable @change="fetchComments">
          <el-option label="全部状态" value="" />
          <el-option label="待审核" value="pending" />
          <el-option label="已通过" value="approved" />
          <el-option label="已拒绝" value="rejected" />
        </el-select>
        <el-input
          v-model="searchKeyword"
          placeholder="搜索评论内容..."
          clearable
          @keyup.enter="fetchComments"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>

    <div class="comment-content">
      <el-table :data="comments" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="评论者" width="120">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :src="row.user?.avatar" :size="32">
                {{ row.user?.name?.charAt(0) }}
              </el-avatar>
              <span>{{ row.user?.name || '匿名' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="评论内容" min-width="200">
          <template #default="{ row }">
            <div class="comment-content-cell">
              <p>{{ row.content }}</p>
              <div class="comment-meta">
                <span>文章: {{ row.post?.title }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag 
              :type="getStatusType(row.status)"
              size="small"
            >
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评论时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button 
              v-if="row.status === 'pending'" 
              type="success" 
              link 
              @click="approveComment(row.id)"
            >
              <el-icon><Check /></el-icon>
              通过
            </el-button>
            <el-button 
              v-if="row.status === 'pending'" 
              type="warning" 
              link 
              @click="rejectComment(row.id)"
            >
              <el-icon><Close /></el-icon>
              拒绝
            </el-button>
            <el-button type="danger" link @click="deleteComment(row.id)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Check, Close, Delete } from '@element-plus/icons-vue'

// 响应式数据
const loading = ref(false)
const comments = ref([])
const searchKeyword = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 模拟数据
const mockComments = [
  {
    id: 1,
    content: '这篇文章写得很好，学到了很多！',
    status: 'pending',
    user: {
      id: 1,
      name: '张三',
      avatar: ''
    },
    post: {
      id: 1,
      title: 'Vue 3 Composition API 深度解析'
    },
    createTime: '2024-01-15 10:30:00'
  },
  {
    id: 2,
    content: '感谢分享，对我很有帮助！',
    status: 'approved',
    user: {
      id: 2,
      name: '李四',
      avatar: ''
    },
    post: {
      id: 2,
      title: 'TypeScript 进阶技巧与实战'
    },
    createTime: '2024-01-14 15:20:00'
  }
]

// 获取评论列表
const fetchComments = async () => {
  try {
    loading.value = true
    await new Promise(resolve => setTimeout(resolve, 500))
    comments.value = mockComments
    total.value = mockComments.length
  } catch (error) {
    ElMessage.error('获取评论列表失败')
  } finally {
    loading.value = false
  }
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    pending: 'warning',
    approved: 'success',
    rejected: 'danger'
  }
  return typeMap[status] || ''
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    pending: '待审核',
    approved: '已通过',
    rejected: '已拒绝'
  }
  return textMap[status] || status
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// 通过评论
const approveComment = async (commentId) => {
  try {
    await new Promise(resolve => setTimeout(resolve, 500))
    
    const comment = comments.value.find(c => c.id === commentId)
    if (comment) {
      comment.status = 'approved'
    }
    
    ElMessage.success('评论已通过')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 拒绝评论
const rejectComment = async (commentId) => {
  try {
    await new Promise(resolve => setTimeout(resolve, 500))
    
    const comment = comments.value.find(c => c.id === commentId)
    if (comment) {
      comment.status = 'rejected'
    }
    
    ElMessage.success('评论已拒绝')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 删除评论
const deleteComment = async (commentId) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条评论吗？',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    await new Promise(resolve => setTimeout(resolve, 500))
    
    comments.value = comments.value.filter(comment => comment.id !== commentId)
    total.value--
    
    ElMessage.success('评论删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 分页处理
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
  fetchComments()
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  fetchComments()
}

// 初始化
onMounted(() => {
  fetchComments()
})
</script>

<style scoped>
.comment-manage {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  color: #2c3e50;
}

.header-actions {
  display: flex;
  gap: 15px;
}

.comment-content {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.comment-content-cell p {
  margin: 0 0 8px 0;
  line-height: 1.5;
}

.comment-meta {
  font-size: 0.85rem;
  color: #7f8c8d;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }
  
  .header-actions {
    flex-direction: column;
    gap: 10px;
  }
}
</style>
