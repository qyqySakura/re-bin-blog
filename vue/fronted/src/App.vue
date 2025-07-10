<template>
  <div>
    <template v-if="route.path === '/login'">
      <router-view />
    </template>
    <template v-else>
      <div class="app-bg">
        <el-container class="main-layout">
          <!-- 侧边栏 -->
          <el-aside width="260px" class="sidebar">
            <div class="sidebar-header">
              <el-icon class="logo-icon"><i-ep-Compass /></el-icon>
              <span class="logo-text">Fantar</span>
            </div>
            <el-menu :default-active="activeMenu" class="sidebar-menu" router background-color="#fff" text-color="#222" active-text-color="#fff">
              <el-menu-item index="/users">
                <el-icon><i-ep-UserFilled /></el-icon>
                <span>用户管理</span>
              </el-menu-item>
              <el-menu-item index="/admins">
                <el-icon><i-ep-Setting /></el-icon>
                <span>管理员管理</span>
              </el-menu-item>
            </el-menu>
            <div class="sidebar-section-title">Quick Action</div>
            <div class="sidebar-quick">
              <el-button text icon="i-ep-Message" class="sidebar-quick-btn">消息</el-button>
              <el-button text icon="i-ep-Document" class="sidebar-quick-btn">请求</el-button>
            </div>
            <div class="sidebar-section-title">Last Orders</div>
            <div class="sidebar-orders">
              <div class="order-item" v-for="item in lastOrders" :key="item.id">
                <el-icon :size="18"><i-ep-Box /></el-icon>
                <span class="order-name">{{ item.name }}</span>
                <span class="order-link">View Order</span>
              </div>
            </div>
            <el-button class="logout-btn" icon="i-ep-SwitchButton" @click="logout">Log Out</el-button>
          </el-aside>

          <!-- 主内容区 -->
          <el-container>
            <!-- 顶部导航 -->
            <el-header class="header-bar">
              <el-input class="search-input" placeholder="Search" prefix-icon="i-ep-Search" clearable />
              <div class="header-actions">
                <el-button class="header-btn" round>Dashboard</el-button>
                <el-button class="header-btn" round>Website</el-button>
                <el-badge :value="2" class="cart-badge">
                  <el-button icon="i-ep-ShoppingCart" circle class="cart-btn" />
                </el-badge>
                <el-avatar :size="36" class="user-avatar" icon="i-ep-UserFilled" />
                <span class="user-name">{{ currentUser?.name || 'Admin' }}</span>
              </div>
            </el-header>
            <el-main class="main-content">
              <router-view />
            </el-main>
          </el-container>
        </el-container>
      </div>
    </template>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  UserFilled, Setting, Compass, Message, Document, Box, SwitchButton, Search, ShoppingCart
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const store = useStore()

const currentUser = computed(() => store.getters.currentUser)
const activeMenu = computed(() => route.path)

const lastOrders = [
  { id: 1, name: 'XYP Sofa...' },
  { id: 2, name: 'CZ Vase...' },
  { id: 3, name: 'Angel sto...' },
  { id: 4, name: 'Betty sto...' }
]

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
.app-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e0e7ef 100%);
}
.main-layout {
  min-height: 100vh;
}
.sidebar {
  background: #fff;
  border-right: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column;
  padding: 0 0 24px 0;
  box-shadow: 2px 0 16px 0 rgba(0,0,0,0.04);
}
.sidebar-header {
  display: flex;
  align-items: center;
  height: 64px;
  padding: 0 24px;
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 2px;
}
.logo-icon {
  font-size: 28px;
  color: #222;
  margin-right: 10px;
}
.logo-text {
  color: #222;
  font-weight: 700;
}
.sidebar-menu {
  border: none;
  margin: 16px 0 0 0;
}
.sidebar-menu .el-menu-item {
  border-radius: 12px;
  margin: 0 12px 8px 12px;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  transition: background 0.2s;
}
.sidebar-menu .el-menu-item.is-active {
  background: #222;
  color: #fff !important;
}
.sidebar-section-title {
  font-size: 13px;
  color: #bbb;
  margin: 18px 0 6px 24px;
  font-weight: 600;
}
.sidebar-quick {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-left: 24px;
}
.sidebar-quick-btn {
  justify-content: flex-start;
  color: #666;
  font-size: 15px;
  padding: 0;
}
.sidebar-orders {
  margin-left: 24px;
  margin-bottom: 16px;
}
.order-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #888;
  margin-bottom: 6px;
}
.order-name {
  margin-left: 8px;
  flex: 1;
}
.order-link {
  color: #409EFF;
  font-size: 13px;
  margin-left: 8px;
  cursor: pointer;
}
.logout-btn {
  margin: 24px 24px 0 24px;
  width: calc(100% - 48px);
  border-radius: 10px;
  font-weight: 600;
  background: #f5f6fa;
  color: #222;
  border: none;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.04);
}
.header-bar {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  padding: 0 32px;
  border-bottom: 1px solid #f0f0f0;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.03);
}
.search-input {
  width: 260px;
  border-radius: 10px;
  background: #f5f6fa;
  border: none;
}
.header-actions {
  display: flex;
  align-items: center;
  gap: 18px;
}
.header-btn {
  background: #f5f6fa;
  color: #222;
  border-radius: 8px;
  font-weight: 600;
  border: none;
  padding: 6px 18px;
}
.cart-badge {
  margin-right: 8px;
}
.cart-btn {
  background: #f5f6fa;
  color: #222;
  border: none;
}
.user-avatar {
  margin-left: 8px;
  background: #eee;
}
.user-name {
  margin-left: 8px;
  font-weight: 600;
  color: #222;
  font-size: 15px;
}
.main-content {
  min-height: 100vh;
  background: transparent;
  padding: 32px 0 0 0;
}
</style>
