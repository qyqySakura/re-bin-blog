<template>
  <div class="user-page">
    <el-card class="user-card">
      <div class="header-bar">
        <div class="title-bar">
          <el-icon class="title-icon"><User /></el-icon>
          <span class="page-title">用户管理</span>
        </div>
        <el-button type="primary" @click="handleAdd" :icon="Plus" class="add-btn">新增用户</el-button>
      </div>
      <el-divider />
      <el-table :data="users" border class="user-table" v-loading="loading" stripe>
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
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑用户' : '新增用户'" width="500px">
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
        <!-- <el-form-item label="头像">
          <input type="file" @change="onAvatarChange($event, 'user')" />
          <img v-if="form.avatar" :src="form.avatar" />
        </el-form-item> -->

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

const users = ref([])
const dialogVisible = ref(false)
const loading = ref(false)
const submitLoading = ref(false)
const formRef = ref()
const form = ref({})
const editingUser = ref(null)
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


async function fetchUsers() {
  loading.value = true
  try {
    const res = await axios.get('/user')
    // 确保返回数据包含完整avatar字段
    users.value = res.data.map(user => ({
      ...user,
      // 防止avatar为空时渲染失败
      avatar: user.avatar || null
    }))
  } catch (error) {
    console.error('获取用户列表失败:', error)
  } finally {
    loading.value = false
  }
}


function handleAdd() {
  form.value = {}
  dialogVisible.value = true
}

function handleEdit(user) {
  editingUser.value = { ...user }
  form.value = { ...user }
  dialogVisible.value = true
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定要删除这个用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await axios.delete(`/user/del/${id}`)
    ElMessage.success('删除成功')
    fetchUsers()
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

    // 如果有新上传的头像文件，先上传头像
    if (avatarFile.value && form.value.id) {
      const url = await uploadAvatar(
        avatarFile.value,
        'user',
        form.value.id
      )
      form.value.avatar = url
    }

    // 如果是编辑且没有新头像，保持原头像
    if (form.value.id && !avatarFile.value && editingUser.value?.avatar) {
      form.value.avatar = editingUser.value.avatar
    }

    if (form.value.id) {
      await axios.put('/user/update', form.value)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/user/add', form.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchUsers()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败: ' + (error.response?.data?.message || error.message))
  } finally {
    submitLoading.value = false
  }
}

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
  fetchUsers()
})
</script>

<style scoped>
.user-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e0e7ef 100%);
  padding: 40px 0;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
.user-card {
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
.user-table {
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
