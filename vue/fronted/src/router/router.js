// src/router/router.js
import { createRouter, createWebHistory } from 'vue-router'
import AdminLayout from '../layout/AdminLayout.vue'
import BlogLayout from '../layout/BlogLayout.vue'
import AdminLogin from '../views/admin/login.vue'
import UserLogin from '../views/user/login.vue'
import UserRegister from '../views/user/register.vue'
import UserHome from '../views/user/Home.vue'
import UserList from '../views/user/index.vue'
import AdminList from '../views/admin/index.vue'
import store from '../store'

const routes = [
  { path: '/login', component: AdminLogin, meta: { requiresAuth: false } },
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      { path: 'users', component: UserList, meta: { requiresAuth: true, title: '用户管理' } },
      { path: 'admins', component: AdminList, meta: { requiresAuth: true, title: '管理员管理' } }
    ]
  },
  { path: '/user/login', component: UserLogin, meta: { requiresAuth: false } },
  { path: '/user/register', component: UserRegister, meta: { requiresAuth: false } },
  {
    path: '/',
    component: BlogLayout,
    children: [
      { path: '', component: UserHome, meta: { title: 'RE-BIN' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const isAuthenticated = store.getters.isAuthenticated
  if (to.meta.title) {
    document.title = `${to.meta.title} - RE-BIN`
  }
  if (requiresAuth && !isAuthenticated) {
    next('/login')
  } else if (to.path === '/login' && isAuthenticated) {
    next('/admin/users')
  } else {
    next()
  }
})

export default router
