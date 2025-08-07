<template>
  <div class="admin-manage">
    <div class="page-header">
      <h1>管理员管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="addAdmin" :icon="Plus">添加管理员</el-button>
        <el-button @click="refreshData" :icon="Refresh">刷新</el-button>
      </div>
    </div>

    <!-- 管理员表格 -->
    <el-card>
      <el-table
        :data="admins"
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
        
        <el-table-column label="角色" width="100">
          <template #default="{ row }">
            <el-tag type="warning">管理员</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-tooltip content="编辑管理员" placement="top">
                <el-button
                  type="primary"
                  size="small"
                  circle
                  @click="editAdmin(row)"
                  class="action-btn edit-btn"
                >
                  <el-icon><Edit /></el-icon>
                </el-button>
              </el-tooltip>

              <el-popconfirm
                title="确定要删除这个管理员吗？"
                @confirm="deleteAdmin(row.id)"
                confirm-button-text="确定"
                cancel-button-text="取消"
              >
                <template #reference>
                  <el-tooltip content="删除管理员" placement="top">
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

    <!-- 编辑管理员对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="editingAdmin.id ? '编辑管理员' : '添加管理员'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="editingAdmin"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editingAdmin.name" />
        </el-form-item>
        
        <el-form-item label="用户名" prop="username">
          <el-input v-model="editingAdmin.username" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editingAdmin.email" />
        </el-form-item>
        
        <el-form-item label="密码" prop="password" v-if="!editingAdmin.id">
          <el-input v-model="editingAdmin.password" type="password" show-password />
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword" v-if="editingAdmin.id">
          <el-input v-model="editingAdmin.newPassword" type="password" show-password placeholder="留空则不修改密码" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAdmin" :loading="submitLoading">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Refresh, Edit, Delete } from '@element-plus/icons-vue'
import { adminApi } from '@/api/modules/admin'
import { getAvatarUrl } from '@/utils/avatar'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const admins = ref([])
const editingAdmin = ref({
  id: null,
  name: '',
  username: '',
  email: '',
  password: '',
  newPassword: ''
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
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const formRef = ref()

// 获取管理员列表
const fetchAdmins = async () => {
  if (isUnmounted.value) return

  loading.value = true
  try {
    console.log('开始获取管理员列表...')
    const response = await adminApi.getAllAdmins()
    console.log('管理员列表响应:', response)

    // 检查组件是否已卸载
    if (isUnmounted.value) return

    admins.value = response.data || []
    console.log('管理员数据:', admins.value)
  } catch (error) {
    if (isUnmounted.value) return
    console.error('获取管理员列表失败:', error)
    ElMessage.error('获取管理员列表失败: ' + (error.message || '未知错误'))
  } finally {
    if (!isUnmounted.value) {
      loading.value = false
    }
  }
}

// 刷新数据
const refreshData = () => {
  fetchAdmins()
}

// 添加管理员
const addAdmin = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑管理员
const editAdmin = (admin) => {
  editingAdmin.value = { ...admin, newPassword: '' }
  dialogVisible.value = true
}

// 删除管理员
const deleteAdmin = async (id) => {
  try {
    await adminApi.deleteAdmin(id)
    admins.value = admins.value.filter(admin => admin.id !== id)
    ElMessage.success('删除成功')
  } catch (error) {
    console.error('删除管理员失败:', error)
    ElMessage.error('删除管理员失败')
  }
}

// 保存管理员
const saveAdmin = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const adminData = { ...editingAdmin.value }
    
    // 如果是编辑且有新密码，使用新密码
    if (adminData.id && adminData.newPassword) {
      adminData.password = adminData.newPassword
    }
    
    // 删除临时字段
    delete adminData.newPassword
    
    if (adminData.id) {
      await adminApi.updateAdmin(adminData)
      const index = admins.value.findIndex(a => a.id === adminData.id)
      if (index !== -1) {
        admins.value[index] = { ...adminData }
      }
      ElMessage.success('更新成功')
    } else {
      const response = await adminApi.createAdmin(adminData)
      if (response.code === 200) {
        await fetchAdmins() // 重新获取列表
        ElMessage.success('添加成功')
      }
    }
    
    dialogVisible.value = false
    resetForm()
  } catch (error) {
    console.error('保存管理员失败:', error)
    ElMessage.error('保存管理员失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  editingAdmin.value = {
    id: null,
    name: '',
    username: '',
    email: '',
    password: '',
    newPassword: ''
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
  fetchAdmins()
})

// 组件卸载时清理
onUnmounted(() => {
  isUnmounted.value = true
})
</script>

<style scoped>
.admin-manage {
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
  gap: 10px;
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

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .header-actions {
    width: 100%;
    justify-content: flex-end;
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
