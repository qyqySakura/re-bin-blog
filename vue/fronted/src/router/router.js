// src/router/router.js
import { createRouter, createWebHistory } from 'vue-router'
import UserList from '../views/user/index.vue'  // 确认路径正确
import AdminList from '../views/admin/index.vue'
import Login from '../views/login.vue'
import store from '../store'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/users', component: UserList },
  { path: '/admins', component: AdminList }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const isAuthenticated = store.getters.isAuthenticated

  if (requiresAuth && !isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router  // 导出 router 实例
