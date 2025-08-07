<template>
  <div class="tag-manage">
    <div class="page-header">
      <h1>标签管理</h1>
      <el-button type="primary" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        添加标签
      </el-button>
    </div>

    <div class="tag-content">
      <el-table :data="tags" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="标签名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="postCount" label="文章数量" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="editTag(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" link @click="deleteTag(row.id)">
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

    <!-- 添加/编辑对话框 -->
    <el-dialog 
      v-model="showAddDialog" 
      :title="editingTag ? '编辑标签' : '添加标签'"
      width="500px"
    >
      <el-form :model="tagForm" :rules="rules" ref="tagFormRef" label-width="80px">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="tagForm.name" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input 
            v-model="tagForm.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入标签描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">
          {{ editingTag ? '更新' : '添加' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import request from '@/api/request'

// 响应式数据
const loading = ref(false)
const saving = ref(false)
const showAddDialog = ref(false)
const editingTag = ref(null)
const tags = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tagFormRef = ref()

const tagForm = reactive({
  name: '',
  description: ''
})

// 验证规则
const rules = {
  name: [
    { required: true, message: '请输入标签名称', trigger: 'blur' },
    { min: 1, max: 20, message: '标签名称长度在 1 到 20 个字符', trigger: 'blur' }
  ]
}

// 获取标签列表
const fetchTags = async () => {
  try {
    loading.value = true
    const response = await request.get('/tags')
    tags.value = response.data || []
    total.value = tags.value.length
  } catch (error) {
    console.error('获取标签列表失败:', error)
    ElMessage.error('获取标签列表失败')
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

// 编辑标签
const editTag = (tag) => {
  editingTag.value = tag
  tagForm.name = tag.name
  tagForm.description = tag.description
  showAddDialog.value = true
}

// 保存标签
const handleSave = async () => {
  if (!tagFormRef.value) return
  
  try {
    await tagFormRef.value.validate()
    saving.value = true

    if (editingTag.value) {
      // 更新标签
      await request.put('/tags/update', {
        id: editingTag.value.id,
        ...tagForm
      })
      await fetchTags() // 重新获取列表
      ElMessage.success('标签更新成功')
    } else {
      // 添加标签
      await request.post('/tags/add', tagForm)
      await fetchTags() // 重新获取列表
      ElMessage.success('标签添加成功')
    }
    
    showAddDialog.value = false
    resetForm()
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// 删除标签
const deleteTag = async (tagId) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个标签吗？',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    await request.delete(`/tags/del/${tagId}`)
    await fetchTags() // 重新获取列表
    ElMessage.success('标签删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除标签失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 重置表单
const resetForm = () => {
  editingTag.value = null
  tagForm.name = ''
  tagForm.description = ''
}

// 分页处理
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
  fetchTags()
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  fetchTags()
}

// 初始化
onMounted(() => {
  fetchTags()
})
</script>

<style scoped>
.tag-manage {
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

.tag-content {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
