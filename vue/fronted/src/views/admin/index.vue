<template>
  <div class="admin-page">
    <el-card class="admin-card">
      <div class="header-bar">
        <div class="title-bar">
          <el-icon class="title-icon"><User /></el-icon>
          <span class="page-title">管理员管理</span>
        </div>
        <el-button type="primary" @click="handleAdd" :icon="Plus" class="add-btn">新增管理员</el-button>
      </div>
      <el-divider />
      <el-table :data="admins" border class="admin-table" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center"></el-table-column>
        <el-table-column prop="username" label="用户名" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
        <el-table-column prop="phone" label="电话" align="center"></el-table-column>
        <el-table-column prop="avatar" label="头像" width="100" align="center">
          <template #default="{ row }">
            <img :src="getAvatarUrl(row.avatar)" style="width:40px;height:40px;border-radius:50%;object-fit:cover;" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)" :icon="Edit" class="op-btn">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)" :icon="Delete" class="op-btn">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑管理员' : '新增管理员'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>

<el-form-item label="头像">
  <input type="file" @change="onAvatarChange" />
  <img :src="getAvatarUrl(form.avatar)" style="width:60px;height:60px;border-radius:8px;" />
</el-form-item>

      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, User } from '@element-plus/icons-vue'
import axios from '@/utils/request'
import { uploadAvatar } from '@/utils/avatarUpload'
import { getAvatarUrl } from '@/utils/avatar'

const admins = ref([])
const dialogVisible = ref(false)
const loading = ref(false)
const submitLoading = ref(false)
const formRef = ref()
const form = ref({})
const editingAdmin = ref(null)
const avatarFile = ref(null)

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [{ required: true, message: '请输入电话', trigger: 'blur' }]
}

async function fetchAdmins() {
  loading.value = true
  try {
    const res = await axios.get('/admins')
    admins.value = res.data || []
  } catch (error) {
    console.error('获取管理员列表失败:', error)
  } finally {
    loading.value = false
  }
}

function handleAdd() {
  form.value = { avatar: null } // 保证新增时avatar不是空字符串
  dialogVisible.value = true
}

function handleEdit(admin) {
  editingAdmin.value = { ...admin }
  form.value = { ...admin }
  dialogVisible.value = true
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定要删除这个管理员吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await axios.delete(`/admins/del/${id}`)
    ElMessage.success('删除成功')
    fetchAdmins()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

async function submitForm() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    submitLoading.value = true

    submitLoading.value = true
  
    // 先上传头像（无论新增/编辑）
    if (avatarFile.value) {
      const url = await uploadAvatar(
        avatarFile.value,
        'user',
        // 新增时无id，使用临时标识
        form.value.id || 'temp-user' 
      )
      form.value.avatar = url
    }

    if (
      form.value.id &&
      (!form.value.avatar || form.value.avatar === '' || form.value.avatar === undefined)
    ) {
      form.value.avatar = editingAdmin.value && editingAdmin.value.avatar ? editingAdmin.value.avatar : null
    }
    if (form.value.id) {
      await axios.put('/admins/update', form.value)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/admins/add', form.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchAdmins()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// async function onAvatarChange(e, role) {
//   const file = e.target.files[0]
//   if (!file || !form.value.id) return
//   const url = await uploadAvatar(file, role, form.value.id)
//   form.value.avatar = url
//   ElMessage.success('头像上传成功')
// }

// 重构onAvatarChange方法
function onAvatarChange(e) {
  const file = e.target.files[0]
  if (!file) return
  
  // 存储文件对象用于submitForm上传
  avatarFile.value = file
  
  // 本地预览（非必须但提升体验）
  const previewURL = URL.createObjectURL(file)
  form.value.avatar = previewURL
}

onMounted(() => {
  fetchAdmins()
})
</script>

<style scoped>
.admin-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e0e7ef 100%);
  padding: 40px 0;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
.admin-card {
  width: 100%;
  max-width: 1200px;
  box-shadow: 0 4px 24px 0 rgba(0,0,0,0.08);
  border-radius: 16px;
  padding: 32px 32px 24px 32px;
  background: #fff;
}
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.title-bar {
  display: flex;
  align-items: center;
}
.title-icon {
  color: #409EFF;
  font-size: 28px;
  margin-right: 10px;
}
.page-title {
  font-size: 26px;
  font-weight: 700;
  color: #222;
}
.add-btn {
  font-weight: 600;
  border-radius: 8px;
  padding: 0 18px;
  height: 38px;
}
.admin-table {
  margin-top: 18px;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(64,158,255,0.06);
  overflow: hidden;
}
.op-btn {
  margin: 0 4px;
  border-radius: 6px;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
