<template>
  <div class="user-manage">
    <div class="page-header">
      <h1>用户管理</h1>
      <div class="header-actions">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索用户..."
          :prefix-icon="Search"
          clearable
          @input="handleSearch"
          style="width: 250px; margin-right: 10px;"
        />
        <el-button type="primary" @click="refreshData" :icon="Refresh">刷新</el-button>
      </div>
    </div>

    <!-- 用户表格 -->
    <el-card>
      <el-table
        :data="filteredUsers"
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        
        <el-table-column label="头像" width="80">
          <template #default="{ row }">
            <el-avatar :src="getAvatarUrl(row.avatar)" :size="40">
              {{ row.name?.charAt(0) }}
            </el-avatar>
          </template>
        </el-table-column>
        
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="email" label="邮箱" />
        
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="createTime" label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-tooltip content="编辑用户" placement="top">
                <el-button
                  type="primary"
                  size="small"
                  circle
                  @click="editUser(row)"
                  class="action-btn edit-btn"
                >
                  <el-icon><Edit /></el-icon>
                </el-button>
              </el-tooltip>

              <el-tooltip :content="row.status === 1 ? '禁用用户' : '启用用户'" placement="top">
                <el-button
                  :type="row.status === 1 ? 'warning' : 'success'"
                  size="small"
                  circle
                  @click="toggleUserStatus(row)"
                  class="action-btn status-btn"
                >
                  <el-icon>
                    <component :is="row.status === 1 ? 'Lock' : 'Unlock'" />
                  </el-icon>
                </el-button>
              </el-tooltip>

              <el-popconfirm
                title="确定要删除这个用户吗？"
                @confirm="deleteUser(row.id)"
                confirm-button-text="确定"
                cancel-button-text="取消"
              >
                <template #reference>
                  <el-tooltip content="删除用户" placement="top">
                    <el-button
                      type="danger"
                      size="small"
                      circle
                      class="action-btn delete-btn"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </el-tooltip>
                </template>
              </el-popconfirm>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑用户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="editingUser.id ? '编辑用户' : '添加用户'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="editingUser"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editingUser.name" />
        </el-form-item>
        
        <el-form-item label="用户名" prop="username">
          <el-input v-model="editingUser.username" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editingUser.email" />
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-select v-model="editingUser.status" style="width: 100%">
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser" :loading="submitLoading">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Edit, Delete, Lock, Unlock } from '@element-plus/icons-vue'
import { adminApi } from '@/api/modules/admin'
import { getAvatarUrl } from '@/utils/avatar'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const searchKeyword = ref('')
const users = ref([])
const editingUser = ref({
  id: null,
  name: '',
  username: '',
  email: '',
  status: 1
})

// 组件卸载标志
const isUnmounted = ref(false)

// 表单验证规则
const formRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const formRef = ref()

// 计算属性
const filteredUsers = computed(() => {
  if (!searchKeyword.value) return users.value
  return users.value.filter(user =>
    user.name?.includes(searchKeyword.value) ||
    user.username?.includes(searchKeyword.value) ||
    user.email?.includes(searchKeyword.value)
  )
})

// 获取用户列表
const fetchUsers = async () => {
  if (isUnmounted.value) return

  loading.value = true
  try {
    console.log('开始获取用户列表...')
    const response = await adminApi.getUsers()
    console.log('用户列表响应:', response)

    // 检查组件是否已卸载
    if (isUnmounted.value) return

    users.value = response.data || []
    console.log('用户数据:', users.value)
  } catch (error) {
    if (isUnmounted.value) return
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败: ' + (error.message || '未知错误'))
  } finally {
    if (!isUnmounted.value) {
      loading.value = false
    }
  }
}

// 搜索处理
const handleSearch = () => {
  // 搜索逻辑已在计算属性中处理
}

// 刷新数据
const refreshData = () => {
  fetchUsers()
}

// 编辑用户
const editUser = (user) => {
  editingUser.value = { ...user }
  dialogVisible.value = true
}

// 切换用户状态
const toggleUserStatus = async (user) => {
  try {
    const newStatus = user.status === 1 ? 0 : 1
    await adminApi.updateUser({ ...user, status: newStatus })
    user.status = newStatus
    ElMessage.success(`用户已${newStatus === 1 ? '启用' : '禁用'}`)
  } catch (error) {
    console.error('更新用户状态失败:', error)
    ElMessage.error('更新用户状态失败')
  }
}

// 删除用户
const deleteUser = async (id) => {
  try {
    await adminApi.deleteUser(id)
    users.value = users.value.filter(user => user.id !== id)
    ElMessage.success('删除成功')
  } catch (error) {
    console.error('删除用户失败:', error)
    ElMessage.error('删除用户失败')
  }
}

// 保存用户
const saveUser = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (editingUser.value.id) {
      await adminApi.updateUser(editingUser.value)
      const index = users.value.findIndex(u => u.id === editingUser.value.id)
      if (index !== -1) {
        users.value[index] = { ...editingUser.value }
      }
      ElMessage.success('更新成功')
    } else {
      const response = await adminApi.createUser(editingUser.value)
      if (response.code === 200) {
        await fetchUsers() // 重新获取列表
        ElMessage.success('添加成功')
      }
    }
    
    dialogVisible.value = false
    resetForm()
  } catch (error) {
    console.error('保存用户失败:', error)
    ElMessage.error('保存用户失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  editingUser.value = {
    id: null,
    name: '',
    username: '',
    email: '',
    status: 1
  }
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return '未知时间'
  const time = new Date(timeStr)
  if (isNaN(time.getTime())) return '时间格式错误'
  return time.toLocaleString()
}

// 初始化
onMounted(() => {
  fetchUsers()
})

// 组件卸载时清理
onUnmounted(() => {
  isUnmounted.value = true
})
</script>

<style scoped>
.user-manage {
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

/* 操作按钮样式 */
.action-buttons {
  display: flex;
  gap: 8px;
  align-items: center;
  justify-content: center;
}

.action-btn {
  transition: all 0.3s ease;
  border-width: 1px;
  font-weight: 500;
  position: relative;
  overflow: hidden;
}

.action-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.action-btn:hover::before {
  left: 100%;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.edit-btn {
  background: linear-gradient(135deg, #409eff, #66b1ff);
  border-color: #409eff;
  color: white;
}

.edit-btn:hover {
  background: linear-gradient(135deg, #337ecc, #409eff);
  border-color: #337ecc;
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}

.status-btn.el-button--warning {
  background: linear-gradient(135deg, #e6a23c, #f0c78a);
  border-color: #e6a23c;
  color: white;
}

.status-btn.el-button--warning:hover {
  background: linear-gradient(135deg, #cf9236, #e6a23c);
  border-color: #cf9236;
  box-shadow: 0 6px 16px rgba(230, 162, 60, 0.4);
}

.status-btn.el-button--success {
  background: linear-gradient(135deg, #67c23a, #95d475);
  border-color: #67c23a;
  color: white;
}

.status-btn.el-button--success:hover {
  background: linear-gradient(135deg, #5daf34, #67c23a);
  border-color: #5daf34;
  box-shadow: 0 6px 16px rgba(103, 194, 58, 0.4);
}

.delete-btn {
  background: linear-gradient(135deg, #f56c6c, #f78989);
  border-color: #f56c6c;
  color: white;
}

.delete-btn:hover {
  background: linear-gradient(135deg, #f25656, #f56c6c);
  border-color: #f25656;
  box-shadow: 0 6px 16px rgba(245, 108, 108, 0.4);
}

/* 圆形按钮特殊样式 */
.action-btn.is-circle {
  width: 36px;
  height: 36px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.action-btn.is-circle .el-icon {
  font-size: 16px;
}

/* Tooltip样式优化 */
.el-tooltip__popper {
  background: rgba(0, 0, 0, 0.8);
  color: white;
  border-radius: 6px;
  padding: 8px 12px;
  font-size: 12px;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .header-actions {
    width: 100%;
    justify-content: space-between;
  }

  .action-buttons {
    gap: 6px;
  }

  .action-btn.is-circle {
    width: 32px;
    height: 32px;
  }

  .action-btn.is-circle .el-icon {
    font-size: 14px;
  }
}
</style>
