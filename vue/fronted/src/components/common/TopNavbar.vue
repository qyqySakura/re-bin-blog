<template>
  <nav class="top-navbar">
    <div class="nav-container">
      <div class="nav-brand">
        <router-link to="/" class="brand-link">
          <div class="brand-logo">
            <span class="logo-text">RE-BIN</span>
            <span class="logo-subtitle">个人博客</span>
          </div>
        </router-link>
      </div>

      <!-- 桌面端导航菜单 -->
      <div class="nav-menu desktop-menu">
        <router-link
          to="/"
          class="nav-item"
          :class="{ active: isActiveRoute('/') }"
        >
          <el-icon><House /></el-icon>
          <span>首页</span>
        </router-link>
        <router-link
          to="/archive"
          class="nav-item"
          :class="{ active: isActiveRoute('/archive') }"
        >
          <el-icon><Calendar /></el-icon>
          <span>归档</span>
        </router-link>
        <router-link
          to="/categories"
          class="nav-item"
          :class="{ active: isActiveRoute('/categories') }"
        >
          <el-icon><Folder /></el-icon>
          <span>分类</span>
        </router-link>
        <router-link
          to="/tags"
          class="nav-item"
          :class="{ active: isActiveRoute('/tags') }"
        >
          <el-icon><CollectionTag /></el-icon>
          <span>标签</span>
        </router-link>
        <router-link
          to="/friendlinks"
          class="nav-item"
          :class="{ active: isActiveRoute('/friendlinks') }"
        >
          <el-icon><Link /></el-icon>
          <span>友链</span>
        </router-link>
        <router-link
          to="/about"
          class="nav-item"
          :class="{ active: isActiveRoute('/about') }"
        >
          <el-icon><User /></el-icon>
          <span>关于</span>
        </router-link>
      </div>

      <!-- 移动端汉堡菜单按钮 -->
      <div class="mobile-menu-btn" @click="toggleMobileMenu">
        <el-icon><Menu /></el-icon>
      </div>

      <div class="nav-actions">
        <div class="search-container">
          <el-autocomplete
            v-model="searchKeyword"
            :fetch-suggestions="getSearchHistory"
            placeholder="搜索文章..."
            class="search-input"
            @keyup.enter="handleSearch"
            @select="handleHistorySelect"
            @focus="loadSearchHistory"
            clearable
            :trigger-on-focus="true"
            :debounce="100"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
            <template #default="{ item }">
              <div class="history-item">
                <el-icon class="history-icon"><Clock /></el-icon>
                <span class="history-text">{{ item.value }}</span>
              </div>
            </template>
          </el-autocomplete>
        </div>

        <el-switch
          v-model="isDark"
          class="theme-switch"
          inline-prompt
          :active-icon="Moon"
          :inactive-icon="Sunny"
          @change="toggleTheme"
        />

        <!-- 消息通知 (仅登录用户显示) -->
        <NotificationDropdown v-if="isLoggedIn" />

        <div class="auth-section" v-if="!isLoggedIn">
          <el-button type="primary" @click="$router.push('/user/login')">登录</el-button>
          <el-button @click="$router.push('/user/register')">注册</el-button>
        </div>

        <div class="user-section" v-else>
          <el-dropdown @command="handleUserCommand">
            <div class="user-info">
              <el-avatar :src="getAvatarUrl(userInfo?.avatar)" :size="32">
                {{ userInfo?.name?.charAt(0) || 'U' }}
              </el-avatar>
              <span class="username">{{ userInfo?.name || '用户' }}</span>
              <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>个人资料
                </el-dropdown-item>
                <el-dropdown-item command="notifications">
                  <el-icon><Bell /></el-icon>消息通知
                </el-dropdown-item>
                <!-- 只有网站作者才显示文章管理 -->
                <el-dropdown-item v-if="isBlogAuthor" command="posts">
                  <el-icon><Document /></el-icon>我的文章
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 移动端菜单覆盖层 -->
    <div
      v-if="showMobileMenu"
      class="mobile-menu-overlay"
      @click="closeMobileMenu"
    >
      <div class="mobile-menu" @click.stop>
        <div class="mobile-menu-header">
          <div class="mobile-brand">
            <span class="logo-text">RE-BIN</span>
            <span class="logo-subtitle">个人博客</span>
          </div>
          <el-button
            class="close-btn"
            @click="closeMobileMenu"
            :icon="Close"
            circle
            size="small"
          />
        </div>

        <div class="mobile-nav-menu">
          <router-link
            to="/"
            class="mobile-nav-item"
            :class="{ active: isActiveRoute('/') }"
            @click="closeMobileMenu"
          >
            <el-icon><House /></el-icon>
            <span>首页</span>
          </router-link>
          <router-link
            to="/archive"
            class="mobile-nav-item"
            :class="{ active: isActiveRoute('/archive') }"
            @click="closeMobileMenu"
          >
            <el-icon><Calendar /></el-icon>
            <span>归档</span>
          </router-link>
          <router-link
            to="/categories"
            class="mobile-nav-item"
            :class="{ active: isActiveRoute('/categories') }"
            @click="closeMobileMenu"
          >
            <el-icon><Folder /></el-icon>
            <span>分类</span>
          </router-link>
          <router-link
            to="/tags"
            class="mobile-nav-item"
            :class="{ active: isActiveRoute('/tags') }"
            @click="closeMobileMenu"
          >
            <el-icon><CollectionTag /></el-icon>
            <span>标签</span>
          </router-link>
          <router-link
            to="/friendlinks"
            class="mobile-nav-item"
            :class="{ active: isActiveRoute('/friendlinks') }"
            @click="closeMobileMenu"
          >
            <el-icon><Link /></el-icon>
            <span>友链</span>
          </router-link>
          <router-link
            to="/about"
            class="mobile-nav-item"
            :class="{ active: isActiveRoute('/about') }"
            @click="closeMobileMenu"
          >
            <el-icon><User /></el-icon>
            <span>关于</span>
          </router-link>
        </div>

        <div class="mobile-menu-actions">
          <div class="mobile-search">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索文章..."
              @keyup.enter="handleMobileSearch"
              clearable
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>

          <div class="mobile-theme-switch">
            <span>深色模式</span>
            <el-switch
              v-model="isDark"
              :active-icon="Moon"
              :inactive-icon="Sunny"
              @change="toggleTheme"
            />
          </div>

          <div class="mobile-auth" v-if="!isLoggedIn">
            <el-button type="primary" @click="handleMobileAuth('login')">登录</el-button>
            <el-button @click="handleMobileAuth('register')">注册</el-button>
          </div>

          <div class="mobile-user" v-else>
            <div class="mobile-user-info">
              <el-avatar :src="getAvatarUrl(userInfo?.avatar)" :size="40">
                {{ userInfo?.name?.charAt(0) || 'U' }}
              </el-avatar>
              <span class="mobile-username">{{ userInfo?.name || '用户' }}</span>
            </div>
            <div class="mobile-user-actions">
              <el-button @click="handleMobileUserAction('profile')">个人资料</el-button>
              <el-button @click="handleMobileUserAction('notifications')">消息通知</el-button>
              <!-- 只有网站作者才显示文章管理 -->
              <el-button v-if="isBlogAuthor" @click="handleMobileUserAction('posts')">我的文章</el-button>
              <el-button type="danger" @click="handleMobileUserAction('logout')">退出登录</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { getAvatarUrl } from '@/utils/avatar'
import NotificationDropdown from '@/components/NotificationDropdown.vue'
import {
  House, Calendar, Folder, CollectionTag, User, Search,
  Moon, Sunny, ArrowDown, Document, SwitchButton, Menu, Close, Link, Clock
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 响应式数据
const searchKeyword = ref('')
const isDark = ref(false)
const showMobileMenu = ref(false)

// 计算属性
const isLoggedIn = computed(() => !!userStore.token)
const userInfo = computed(() => userStore.userInfo)
// 判断是否为网站作者（ID=1）
const isBlogAuthor = computed(() => {
  return userInfo.value && userInfo.value.id === 1
})

// 生命周期
onMounted(() => {
  // 初始化主题
  const savedTheme = localStorage.getItem('theme')
  isDark.value = savedTheme === 'dark'
  if (isDark.value) {
    document.documentElement.classList.add('dark')
  }

  // 加载搜索历史
  loadSearchHistory()
})

// 搜索历史数据
const searchHistory = ref([])

// 加载搜索历史
const loadSearchHistory = () => {
  const history = JSON.parse(localStorage.getItem('searchHistory') || '[]')
  searchHistory.value = history
}

// 保存搜索历史
const saveSearchHistory = (keyword) => {
  if (!keyword.trim()) return

  const history = JSON.parse(localStorage.getItem('searchHistory') || '[]')
  const filteredHistory = history.filter(item => item !== keyword)
  filteredHistory.unshift(keyword)

  // 只保留最近8条搜索记录
  const newHistory = filteredHistory.slice(0, 8)
  localStorage.setItem('searchHistory', JSON.stringify(newHistory))
  searchHistory.value = newHistory
}

// 获取搜索历史建议
const getSearchHistory = (queryString, callback) => {
  let results = []

  if (!queryString) {
    // 没有输入时显示所有历史记录
    results = searchHistory.value.map(item => ({ value: item }))
  } else {
    // 有输入时过滤匹配的历史记录
    results = searchHistory.value
      .filter(item => item.toLowerCase().includes(queryString.toLowerCase()))
      .map(item => ({ value: item }))
  }

  callback(results)
}

// 选择历史记录
const handleHistorySelect = (item) => {
  searchKeyword.value = item.value
  handleSearch()
}

// 方法
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    // 保存搜索历史
    saveSearchHistory(searchKeyword.value.trim())

    router.push({
      path: '/search',
      query: { q: searchKeyword.value.trim() }
    })
  }
}

// 检查路由是否激活
const isActiveRoute = (path) => {
  if (path === '/') {
    // 首页只有在完全匹配时才激活
    return route.path === '/'
  }
  // 其他页面使用路径匹配
  return route.path === path || route.path.startsWith(path + '/')
}

const toggleTheme = () => {
  document.documentElement.classList.toggle('dark', isDark.value)
  localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
}

const handleUserCommand = async (command) => {
  switch (command) {
    case 'notifications':
      router.push('/notifications')
      break
    case 'profile':
      router.push('/user/profile')
      break
    case 'posts':
      router.push('/user/posts')
      break
    case 'logout':
      try {
        await userStore.logout()
        ElMessage.success('退出登录成功')
        router.push('/')
      } catch (error) {
        ElMessage.error('退出登录失败')
      }
      break
  }
}

// 移动端菜单相关方法
const toggleMobileMenu = () => {
  showMobileMenu.value = !showMobileMenu.value
}

const closeMobileMenu = () => {
  showMobileMenu.value = false
}

const handleMobileSearch = () => {
  if (searchKeyword.value.trim()) {
    // 保存搜索历史
    saveSearchHistory(searchKeyword.value.trim())

    router.push({
      path: '/search',
      query: { q: searchKeyword.value.trim() }
    })
    closeMobileMenu()
  }
}

const handleMobileAuth = (type) => {
  if (type === 'login') {
    router.push('/user/login')
  } else if (type === 'register') {
    router.push('/user/register')
  }
  closeMobileMenu()
}

const handleMobileUserAction = async (action) => {
  switch (action) {
    case 'notifications':
      router.push('/notifications')
      break
    case 'profile':
      router.push('/user/profile')
      break
    case 'posts':
      router.push('/user/posts')
      break
    case 'logout':
      try {
        await userStore.logout()
        ElMessage.success('退出登录成功')
        router.push('/')
      } catch (error) {
        ElMessage.error('退出登录失败')
      }
      break
  }
  closeMobileMenu()
}
</script>

<style scoped>
/* 顶部导航 */
.top-navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.nav-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
}

.nav-brand .brand-link {
  text-decoration: none;
  color: inherit;
}

.brand-logo {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.logo-text {
  font-size: 24px;
  font-weight: 700;
  color: #2c3e50;
  line-height: 1;
}

.logo-subtitle {
  font-size: 12px;
  color: #7f8c8d;
  margin-top: 2px;
}

.nav-menu {
  display: flex;
  gap: 30px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  text-decoration: none;
  color: #5a6c7d;
  font-weight: 500;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.nav-item:hover {
  color: #3b82f6;
  background: rgba(59, 130, 246, 0.1);
}

.nav-item.active {
  color: #3b82f6;
  background: rgba(59, 130, 246, 0.15);
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-container {
  width: 200px;
}

.search-input {
  border-radius: 20px;
}

.history-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  gap: 8px;
}

.history-icon {
  color: #909399;
  font-size: 14px;
}

.history-text {
  color: #2c3e50;
  font-weight: 400;
}

.theme-switch {
  --el-switch-on-color: #3b82f6;
  --el-switch-off-color: #94a3b8;
}

.auth-section {
  display: flex;
  gap: 8px;
}

.user-section .user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.user-section .user-info:hover {
  background: rgba(59, 130, 246, 0.1);
}

.username {
  font-weight: 500;
  color: #374151;
}

.dropdown-icon {
  font-size: 12px;
  color: #9ca3af;
}

/* 暗色主题 */
:global(.dark) .top-navbar {
  background: rgba(17, 24, 39, 0.95);
  border-bottom-color: rgba(255, 255, 255, 0.1);
}

:global(.dark) .logo-text {
  color: #f9fafb;
}

:global(.dark) .logo-subtitle {
  color: #9ca3af;
}

:global(.dark) .nav-item {
  color: #d1d5db;
}

:global(.dark) .nav-item:hover {
  color: #60a5fa;
  background: rgba(96, 165, 250, 0.1);
}

:global(.dark) .nav-item.active {
  color: #60a5fa;
  background: rgba(96, 165, 250, 0.15);
}

:global(.dark) .username {
  color: #f3f4f6;
}

/* 移动端汉堡菜单按钮 */
.mobile-menu-btn {
  display: none;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.mobile-menu-btn:hover {
  background: rgba(59, 130, 246, 0.1);
}

.mobile-menu-btn .el-icon {
  font-size: 20px;
  color: #374151;
}

/* 移动端菜单覆盖层 */
.mobile-menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 2000;
  display: flex;
  justify-content: flex-end;
}

.mobile-menu {
  width: 300px;
  height: 100vh;
  background: white;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  animation: slideInRight 0.3s ease;
}

@keyframes slideInRight {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

.mobile-menu-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border-bottom: 1px solid #e5e7eb;
}

.mobile-brand {
  display: flex;
  flex-direction: column;
}

.mobile-brand .logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #2c3e50;
}

.mobile-brand .logo-subtitle {
  font-size: 12px;
  color: #7f8c8d;
}

.mobile-nav-menu {
  flex: 1;
  padding: 20px 0;
}

.mobile-nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  text-decoration: none;
  color: #374151;
  font-weight: 500;
  transition: all 0.3s ease;
  border-left: 3px solid transparent;
}

.mobile-nav-item:hover {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
  border-left-color: #3b82f6;
}

.mobile-nav-item.active {
  background: rgba(59, 130, 246, 0.15);
  color: #3b82f6;
  border-left-color: #3b82f6;
}

.mobile-menu-actions {
  padding: 20px;
  border-top: 1px solid #e5e7eb;
}

.mobile-search {
  margin-bottom: 20px;
}

.mobile-theme-switch {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  font-weight: 500;
  color: #374151;
}

.mobile-auth {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.mobile-user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  padding: 12px;
  background: #f9fafb;
  border-radius: 8px;
}

.mobile-username {
  font-weight: 500;
  color: #374151;
}

.mobile-user-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-container {
    padding: 0 16px;
  }

  .desktop-menu {
    display: none;
  }

  .mobile-menu-btn {
    display: flex;
  }

  .search-container {
    width: 150px;
  }

  .auth-section {
    display: none;
  }
}

/* 暗色主题 - 移动端菜单 */
:global(.dark) .mobile-menu {
  background: #1f2937;
}

:global(.dark) .mobile-menu-header {
  border-bottom-color: rgba(255, 255, 255, 0.1);
}

:global(.dark) .mobile-brand .logo-text {
  color: #f9fafb;
}

:global(.dark) .mobile-brand .logo-subtitle {
  color: #9ca3af;
}

:global(.dark) .mobile-nav-item {
  color: #d1d5db;
}

:global(.dark) .mobile-nav-item:hover {
  background: rgba(96, 165, 250, 0.1);
  color: #60a5fa;
  border-left-color: #60a5fa;
}

:global(.dark) .mobile-nav-item.active {
  background: rgba(96, 165, 250, 0.15);
  color: #60a5fa;
  border-left-color: #60a5fa;
}

:global(.dark) .mobile-menu-actions {
  border-top-color: rgba(255, 255, 255, 0.1);
}

:global(.dark) .mobile-theme-switch {
  color: #d1d5db;
}

:global(.dark) .mobile-user-info {
  background: #374151;
}

:global(.dark) .mobile-username {
  color: #f3f4f6;
}

:global(.dark) .mobile-menu-btn .el-icon {
  color: #d1d5db;
}
</style>
