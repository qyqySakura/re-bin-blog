<template>
  <el-container class="admin-layout">
    <el-aside width="260px" class="sidebar">
      <div class="sidebar-header">
        <el-icon class="logo-icon"><i-ep-Compass /></el-icon>
        <span class="logo-text">后台管理</span>
      </div>
      <el-menu :default-active="$route.path" class="sidebar-menu" router background-color="#fff" text-color="#222" active-text-color="#fff">
        <el-menu-item index="/admin/posts">
          <el-icon><i-ep-Document /></el-icon>
          <span>文章管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/categories">
          <el-icon><i-ep-Folder /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><i-ep-UserFilled /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/admins">
          <el-icon><i-ep-Setting /></el-icon>
          <span>管理员管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header-bar">
        <span class="admin-title">后台管理系统</span>
        <el-button class="logout-btn" icon="i-ep-SwitchButton" @click="logout">退出登录</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
<script setup>
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessageBox, ElMessage } from 'element-plus'
const router = useRouter()
const store = useStore()
function logout() {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    store.dispatch('logout')
    router.push('/login')
    ElMessage.success('已退出登录')
  })
}
</script>
<style scoped>
.admin-layout { min-height: 100vh; }
.sidebar { background: #fff; border-right: 1px solid #f0f0f0; }
.sidebar-header { display: flex; align-items: center; height: 64px; padding: 0 24px; font-size: 22px; font-weight: 700; }
.logo-icon { font-size: 28px; color: #222; margin-right: 10px; }
.logo-text { color: #222; font-weight: 700; }
.sidebar-menu { border: none; margin: 16px 0 0 0; }
.sidebar-menu .el-menu-item { border-radius: 12px; margin: 0 12px 8px 12px; height: 44px; font-size: 16px; font-weight: 500; }
.sidebar-menu .el-menu-item.is-active { background: #222; color: #fff !important; }
.header-bar { background: #fff; display: flex; align-items: center; justify-content: space-between; height: 64px; padding: 0 32px; border-bottom: 1px solid #f0f0f0; }
.admin-title { font-size: 1.3rem; font-weight: bold; color: #222; }
.logout-btn { border-radius: 10px; font-weight: 600; background: #f5f6fa; color: #222; border: none; }
</style> 