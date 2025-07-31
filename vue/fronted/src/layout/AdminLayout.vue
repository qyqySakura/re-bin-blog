<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: sidebarCollapsed }">
      <div class="sidebar-header">
        <div class="logo">
          <el-icon class="logo-icon"><Setting /></el-icon>
          <span v-show="!sidebarCollapsed" class="logo-text">RE-BIN 管理</span>
        </div>
        <el-button
            class="collapse-btn"
            @click="toggleSidebar"
            :icon="sidebarCollapsed ? Expand : Fold"
            circle
            size="small"
        />
      </div>

      <nav class="sidebar-nav">
        <el-menu
            :default-active="activeMenu"
            :collapse="sidebarCollapsed"
            :unique-opened="true"
            router
            class="admin-menu"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><Odometer /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>

          <el-sub-menu index="content">
            <template #title>
              <el-icon><Document /></el-icon>
              <span>内容管理</span>
            </template>
            <el-menu-item index="/admin/posts">
              <el-icon><EditPen /></el-icon>
              <span>文章管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/categories">
              <el-icon><Folder /></el-icon>
              <span>分类管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/tags">
              <el-icon><CollectionTag /></el-icon>
              <span>标签管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/comments">
              <el-icon><ChatDotRound /></el-icon>
              <span>评论管理</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="user">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin/users">
              <el-icon><Avatar /></el-icon>
              <span>用户列表</span>
            </el-menu-item>
            <el-menu-item index="/admin/admins">
              <el-icon><UserFilled /></el-icon>
              <span>管理员</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="system">
            <template #title>
              <el-icon><Tools /></el-icon>
              <span>系统设置</span>
            </template>
            <el-menu-item index="/admin/settings">
              <el-icon><Setting /></el-icon>
              <span>基本设置</span>
            </el-menu-item>
            <el-menu-item index="/admin/logs">
              <el-icon><List /></el-icon>
              <span>系统日志</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </nav>
    </aside>

    <!-- 主内容区 -->
    <div class="main-container">
      <!-- 顶部导航栏 -->
      <header class="top-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <!-- 搜索框 -->
          <div class="search-box">
            <el-input
                v-model="searchKeyword"
                placeholder="搜索..."
                :prefix-icon="Search"
                clearable
                @keyup.enter="handleSearch"
            />
          </div>

          <!-- 通知 -->
          <el-badge :value="notificationCount" class="notification-badge">
            <el-button :icon="Bell" circle @click="showNotifications" />
          </el-badge>

          <!-- 主题切换 -->
          <el-switch
              v-model="isDark"
              :active-icon="Moon"
              :inactive-icon="Sunny"
              @change="toggleTheme"
          />

          <!-- 用户菜单 -->
          <el-dropdown class="user-dropdown" @command="handleUserCommand">
            <div class="user-info">
              <el-avatar :src="getAvatarUrl(currentUser?.avatar)" :size="32">
                {{ currentUser?.name?.charAt(0) }}
              </el-avatar>
              <span class="username">{{ currentUser?.name }}</span>
              <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>个人资料
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <el-icon><Setting /></el-icon>账户设置
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon ><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 页面内容 -->
      <main class="page-content">
        <div class="content-wrapper">
          <router-view />
        </div>
      </main>
    </div>

    <!-- 通知抽屉 -->
    <el-drawer
        v-model="notificationDrawer"
        title="通知中心"
        direction="rtl"
        size="400px"
    >
      <div class="notification-list">
        <div
            v-for="notification in notifications"
            :key="notification.id"
            class="notification-item"
            :class="{ unread: !notification.read }"
        >
          <div class="notification-icon">
            <el-icon><Bell /></el-icon>
          </div>
          <div class="notification-content">
            <h4>{{ notification.title }}</h4>
            <p>{{ notification.content }}</p>
            <span class="notification-time">{{ formatTime(notification.time) }}</span>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAdminStore } from '@/stores/admin'
import { ElMessage } from 'element-plus'
import { getAvatarUrl } from '@/utils/avatar'
import {
  Setting, Expand, Fold, Odometer, Document, EditPen, Folder,
  CollectionTag, ChatDotRound, User, Avatar, UserFilled, Tools,
  List, Search, Bell, Moon, Sunny, ArrowDown, SwitchButton
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const adminStore = useAdminStore()

// 响应式数据
const sidebarCollapsed = ref(false)
const isDark = ref(false)
const searchKeyword = ref('')
const notificationDrawer = ref(false)
const notificationCount = ref(3)
const notifications = ref([
  {
    id: 1,
    title: '新用户注册',
    content: '用户 "张三" 刚刚注册了账号',
    time: new Date(),
    read: false
  },
  {
    id: 2,
    title: '新文章发布',
    content: '文章 "Vue3 开发指南" 已发布',
    time: new Date(Date.now() - 3600000),
    read: false
  },
  {
    id: 3,
    title: '系统更新',
    content: '系统已更新到 v2.1.0',
    time: new Date(Date.now() - 7200000),
    read: true
  }
])

// 计算属性
const currentUser = computed(() => adminStore.adminInfo)
const activeMenu = computed(() => route.path)

const breadcrumbs = computed(() => {
  const pathMap = {
    '/admin': { title: '仪表盘' },
    '/admin/posts': { title: '文章管理' },
    '/admin/categories': { title: '分类管理' },
    '/admin/tags': { title: '标签管理' },
    '/admin/comments': { title: '评论管理' },
    '/admin/users': { title: '用户管理' },
    '/admin/admins': { title: '管理员管理' },
    '/admin/settings': { title: '系统设置' },
    '/admin/logs': { title: '系统日志' }
  }

  const current = pathMap[route.path] || { title: '未知页面' }
  return [
    { title: '管理后台', path: '/admin' },
    current
  ]
})

// 切换侧边栏
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
  localStorage.setItem('sidebarCollapsed', sidebarCollapsed.value)
}

// 主题切换
const toggleTheme = () => {
  document.documentElement.classList.toggle('dark', isDark.value)
  localStorage.setItem('adminTheme', isDark.value ? 'dark' : 'light')
}

// 搜索处理
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    ElMessage.info(`搜索: ${searchKeyword.value}`)
  }
}

// 显示通知
const showNotifications = () => {
  notificationDrawer.value = true
}

// 用户菜单处理
const handleUserCommand = async (command) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人资料功能开发中...')
      break
    case 'settings':
      ElMessage.info('账户设置功能开发中...')
      break
    case 'logout':
      try {
        await adminStore.logout()
        ElMessage.success('退出登录成功')
        router.push('/login')
      } catch (error) {
        ElMessage.error('退出登录失败')
      }
      break
  }
}

// 格式化时间
const formatTime = (time) => {
  const now = new Date()
  const diff = now - time
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  return `${days}天前`
}

// 初始化
onMounted(() => {
  // 恢复侧边栏状态
  const savedCollapsed = localStorage.getItem('sidebarCollapsed')
  if (savedCollapsed !== null) {
    sidebarCollapsed.value = savedCollapsed === 'true'
  }

  // 恢复主题
  const savedTheme = localStorage.getItem('adminTheme')
  isDark.value = savedTheme === 'dark'
  document.documentElement.classList.toggle('dark', isDark.value)
})
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
  background: #f5f7fa;
}

/* 侧边栏 */
.sidebar {
  width: 250px;
  background: white;
  border-right: 1px solid #e4e7ed;
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  border-bottom: 1px solid #e4e7ed;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: 600;
  color: #2c3e50;
}

.logo-icon {
  font-size: 24px;
  color: #3498db;
}

.logo-text {
  font-size: 18px;
}

.collapse-btn {
  border: none;
  background: #f8f9fa;
}

.sidebar-nav {
  flex: 1;
  overflow-y: auto;
}

.admin-menu {
  border: none;
}

/* 主内容区 */
.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.top-header {
  height: 60px;
  background: white;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-left {
  flex: 1;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.search-box {
  width: 250px;
}

.notification-badge {
  cursor: pointer;
}

.user-dropdown .user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 6px;
  transition: background 0.3s ease;
}

.user-dropdown .user-info:hover {
  background: #f5f7fa;
}

.username {
  font-weight: 500;
  color: #2c3e50;
}

.dropdown-icon {
  font-size: 12px;
  color: #909399;
}

.page-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
}

/* 通知列表 */
.notification-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.notification-item {
  display: flex;
  gap: 12px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  border-left: 3px solid #e4e7ed;
  transition: all 0.3s ease;
}

.notification-item.unread {
  background: #e8f4fd;
  border-left-color: #3498db;
}

.notification-icon {
  flex-shrink: 0;
  width: 32px;
  height: 32px;
  background: #3498db;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.notification-content h4 {
  margin: 0 0 5px 0;
  font-size: 14px;
  font-weight: 600;
  color: #2c3e50;
}

.notification-content p {
  margin: 0 0 8px 0;
  font-size: 13px;
  color: #5a6c7d;
  line-height: 1.4;
}

.notification-time {
  font-size: 12px;
  color: #909399;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    left: 0;
    top: 0;
    z-index: 1000;
    height: 100vh;
  }

  .sidebar.collapsed {
    transform: translateX(-100%);
  }

  .main-container {
    margin-left: 0;
  }

  .search-box {
    width: 150px;
  }

  .header-right {
    gap: 10px;
  }
}

/* 暗色主题 */
:global(.dark) .admin-layout {
  background: #1a1a1a;
}

:global(.dark) .sidebar {
  background: #2c2c2c;
  border-right-color: #404040;
}

:global(.dark) .top-header {
  background: #2c2c2c;
  border-bottom-color: #404040;
}

:global(.dark) .logo,
:global(.dark) .username {
  color: #e4e7ed;
}

:global(.dark) .user-dropdown .user-info:hover {
  background: #404040;
}

:global(.dark) .notification-item {
  background: #404040;
}

:global(.dark) .notification-item.unread {
  background: #2c3e50;
}
</style>
