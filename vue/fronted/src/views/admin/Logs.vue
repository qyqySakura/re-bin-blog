<template>
  <div class="logs">
    <div class="page-header">
      <h1>系统日志</h1>
      <div class="header-actions">
        <el-select v-model="logLevel" placeholder="日志级别" style="width: 120px; margin-right: 10px;">
          <el-option label="全部" value="" />
          <el-option label="INFO" value="INFO" />
          <el-option label="WARN" value="WARN" />
          <el-option label="ERROR" value="ERROR" />
        </el-select>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="margin-right: 10px;"
          @change="handleDateChange"
        />
        <el-button type="primary" @click="refreshLogs" :icon="Refresh">刷新</el-button>
        <el-button type="danger" @click="clearLogs" :icon="Delete">清空日志</el-button>
      </div>
    </div>

    <!-- 日志表格 -->
    <el-card>
      <el-table
        :data="filteredLogs"
        v-loading="loading"
        stripe
        style="width: 100%"
        :default-sort="{ prop: 'timestamp', order: 'descending' }"
      >
        <el-table-column prop="timestamp" label="时间" width="180" sortable>
          <template #default="{ row }">
            {{ formatTime(row.timestamp) }}
          </template>
        </el-table-column>
        
        <el-table-column prop="level" label="级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelTagType(row.level)">
              {{ row.level }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="module" label="模块" width="120" />
        <el-table-column prop="action" label="操作" width="150" />
        <el-table-column prop="message" label="消息" min-width="200" />
        <el-table-column prop="user" label="用户" width="100" />
        <el-table-column prop="ip" label="IP地址" width="130" />
        
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="viewLogDetail(row)"
              :icon="View"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="totalLogs"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 日志详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="日志详情"
      width="600px"
    >
      <div v-if="selectedLog" class="log-detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="时间">
            {{ formatTime(selectedLog.timestamp) }}
          </el-descriptions-item>
          <el-descriptions-item label="级别">
            <el-tag :type="getLevelTagType(selectedLog.level)">
              {{ selectedLog.level }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="模块">
            {{ selectedLog.module }}
          </el-descriptions-item>
          <el-descriptions-item label="操作">
            {{ selectedLog.action }}
          </el-descriptions-item>
          <el-descriptions-item label="用户">
            {{ selectedLog.user || '系统' }}
          </el-descriptions-item>
          <el-descriptions-item label="IP地址">
            {{ selectedLog.ip || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="消息">
            {{ selectedLog.message }}
          </el-descriptions-item>
          <el-descriptions-item label="详细信息" v-if="selectedLog.details">
            <pre class="log-details">{{ selectedLog.details }}</pre>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Delete, View } from '@element-plus/icons-vue'
import axios from 'axios'

// 响应式数据
const loading = ref(false)
const detailDialogVisible = ref(false)
const logLevel = ref('')
const dateRange = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const totalLogs = ref(0)
const selectedLog = ref(null)

// 日志数据
const logs = ref([])

// 计算属性
const filteredLogs = computed(() => {
  let filtered = logs.value

  // 按日志级别过滤
  if (logLevel.value) {
    filtered = filtered.filter(log => log.level === logLevel.value)
  }

  // 按日期范围过滤
  if (dateRange.value && dateRange.value.length === 2) {
    const [startDate, endDate] = dateRange.value
    filtered = filtered.filter(log => {
      const logDate = new Date(log.timestamp)
      return logDate >= startDate && logDate <= endDate
    })
  }

  totalLogs.value = filtered.length
  
  // 分页
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filtered.slice(start, end)
})

// 获取日志级别标签类型
const getLevelTagType = (level) => {
  switch (level) {
    case 'INFO': return 'success'
    case 'WARN': return 'warning'
    case 'ERROR': return 'danger'
    default: return 'info'
  }
}

// 格式化时间
const formatTime = (timestamp) => {
  if (!timestamp) return '未知时间'
  const time = new Date(timestamp)
  if (isNaN(time.getTime())) return '时间格式错误'
  return time.toLocaleString()
}

// 获取日志列表
const fetchLogs = async () => {
  loading.value = true
  try {
    const params = {
      level: logLevel.value,
      startDate: dateRange.value?.[0]?.toISOString(),
      endDate: dateRange.value?.[1]?.toISOString()
    }

    const response = await axios.get('/admin/logs', { params })
    if (response.data.code === 200) {
      logs.value = response.data.data || []
      ElMessage.success('日志已刷新')
    } else {
      ElMessage.error('获取日志失败')
    }
  } catch (error) {
    console.error('获取日志失败:', error)
    ElMessage.error('获取日志失败')
  } finally {
    loading.value = false
  }
}

// 刷新日志
const refreshLogs = () => {
  fetchLogs()
}

// 清空日志
const clearLogs = async () => {
  try {
    await ElMessageBox.confirm('确定要清空所有日志吗？此操作不可恢复！', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const response = await axios.delete('/admin/logs/clear')
    if (response.data.code === 200) {
      await fetchLogs() // 重新获取日志
      ElMessage.success('日志已清空')
    } else {
      ElMessage.error('清空日志失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清空日志失败:', error)
      ElMessage.error('清空日志失败')
    } else {
      ElMessage.info('已取消清空操作')
    }
  }
}

// 查看日志详情
const viewLogDetail = (log) => {
  selectedLog.value = log
  detailDialogVisible.value = true
}

// 日期范围变化处理
const handleDateChange = () => {
  currentPage.value = 1
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}

// 初始化
onMounted(() => {
  fetchLogs()
})
</script>

<style scoped>
.logs {
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
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
}

.header-actions {
  display: flex;
  align-items: center;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}

.log-detail {
  max-height: 500px;
  overflow-y: auto;
}

.log-details {
  background: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
  line-height: 1.4;
  white-space: pre-wrap;
  word-break: break-all;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .header-actions {
    width: 100%;
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .pagination-wrapper {
    text-align: center;
  }
}
</style>
