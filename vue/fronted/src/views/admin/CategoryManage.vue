<template>
  <div class="category-manage">
    <div class="page-header">
      <h2>分类管理</h2>
      <el-button type="primary" @click="showAddDialog">
        <el-icon><Plus /></el-icon>
        新增分类
      </el-button>
    </div>

    <!-- 分类列表 -->
    <el-table :data="categories" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" min-width="200" />
      <el-table-column prop="description" label="描述" min-width="300" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="editCategory(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteCategory(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑分类' : '新增分类'"
      width="500px"
      :before-close="handleClose"
    >
      <el-form :model="categoryForm" :rules="rules" ref="categoryFormRef" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="categoryForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入分类描述"
            maxlength="255"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitCategory" :loading="submitting">
            {{ isEdit ? '更新' : '创建' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { categoryApi } from '@/utils/api'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const categories = ref([])

// 分类表单
const categoryForm = reactive({
  id: null,
  name: '',
  description: ''
})

// 表单引用
const categoryFormRef = ref()

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 1, max: 50, message: '分类名称长度在 1 到 50 个字符', trigger: 'blur' }
  ]
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    loading.value = true
    const response = await categoryApi.getCategories()
    if (response.code === 200) {
      categories.value = response.data
    }
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  } finally {
    loading.value = false
  }
}

// 显示新增对话框
const showAddDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑分类
const editCategory = (category) => {
  isEdit.value = true
  Object.assign(categoryForm, {
    id: category.id,
    name: category.name,
    description: category.description
  })
  dialogVisible.value = true
}

// 删除分类
const deleteCategory = async (category) => {
  try {
    await ElMessageBox.confirm(`确定要删除分类"${category.name}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await categoryApi.deleteCategory(category.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      fetchCategories()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交分类
const submitCategory = async () => {
  try {
    await categoryFormRef.value.validate()
    submitting.value = true
    
    const response = isEdit.value 
      ? await categoryApi.updateCategory(categoryForm)
      : await categoryApi.addCategory(categoryForm)
      
    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      dialogVisible.value = false
      fetchCategories()
    }
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    }
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(categoryForm, {
    id: null,
    name: '',
    description: ''
  })
  categoryFormRef.value?.clearValidate()
}

// 关闭对话框
const handleClose = (done) => {
  resetForm()
  done()
}

// 初始化
onMounted(() => {
  fetchCategories()
})
</script>

<style scoped>
.category-manage {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
