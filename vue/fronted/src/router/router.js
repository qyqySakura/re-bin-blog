// src/router/router.js
import { createRouter, createWebHistory } from 'vue-router'
import UserList from '../views/user/index.vue'  // 确认路径正确
import AdminList from '../views/admin/index.vue'
import Login from '../views/login.vue'
import store from '../store'

const routes = [
  { 
    path: '/', 
    redirect: '/users' 
  },
  { 
    path: '/login', 
    component: Login,
    meta: { requiresAuth: false }
  },
  { 
    path: '/users', 
    component: UserList,
    meta: { requiresAuth: true, title: '用户管理' }
  },
  { 
    path: '/admins', 
    component: AdminList,
    meta: { requiresAuth: true, title: '管理员管理' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const isAuthenticated = store.getters.isAuthenticated

  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 后台管理系统`
  }

  if (requiresAuth && !isAuthenticated) {
    next('/login')
  } else if (to.path === '/login' && isAuthenticated) {
    next('/users')
  } else {
    next()
  }
})

export default router  // 导出 router 实例
