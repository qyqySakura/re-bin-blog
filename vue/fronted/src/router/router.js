// src/router/router.js
import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import AdminLayout from '../layout/AdminLayout.vue'
import BlogLayout from '../layout/BlogLayout.vue'
import AdminLogin from '../views/admin/login.vue'
import UserLogin from '../views/user/login.vue'
import UserRegister from '../views/user/register.vue'
import UserHome from '../views/user/Home.vue'
import PostDetail from '../views/user/PostDetail.vue'
import Archive from '../views/user/Archive.vue'
import Categories from '../views/user/Categories.vue'
import Tags from '../views/user/Tags.vue'
import About from '../views/user/About.vue'
import FriendLinks from '../views/user/FriendLinks.vue'
import UserProfile from '../views/user/Profile.vue'
import UserPosts from '../views/user/UserPosts.vue'
import UserNotifications from '../views/user/Notifications.vue'
import Search from '../views/user/Search.vue'
import UserList from '../views/user/index.vue'
import AdminList from '../views/admin/index.vue'
import Dashboard from '../views/admin/Dashboard.vue'
import UserManage from '../views/admin/UserManage.vue'
import AdminManage from '../views/admin/AdminManage.vue'
import PostManage from '../views/admin/PostManage.vue'
import CategoryManage from '../views/admin/CategoryManage.vue'
import TagManage from '../views/admin/TagManage.vue'
import CommentManage from '../views/admin/CommentManage.vue'
import Settings from '../views/admin/Settings.vue'
import Logs from '../views/admin/Logs.vue'
import Test from '../views/Test.vue'


const routes = [
  // 管理员登录
  {
    path: '/login',
    component: AdminLogin,
    meta: { requiresAuth: false, title: '管理员登录' }
  },

  // 管理员后台
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, role: 'admin' },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        component: Dashboard,
        meta: { requiresAuth: true, title: '仪表盘' }
      },
      {
        path: 'posts',
        component: PostManage,
        meta: { requiresAuth: true, title: '文章管理' }
      },
      {
        path: 'categories',
        component: CategoryManage,
        meta: { requiresAuth: true, title: '分类管理' }
      },
      {
        path: 'tags',
        component: TagManage,
        meta: { requiresAuth: true, title: '标签管理' }
      },
      {
        path: 'comments',
        component: CommentManage,
        meta: { requiresAuth: true, title: '评论管理' }
      },
      {
        path: 'users',
        component: UserManage,
        meta: { requiresAuth: true, title: '用户管理' }
      },
      {
        path: 'admins',
        component: AdminManage,
        meta: { requiresAuth: true, title: '管理员管理' }
      },
      {
        path: 'settings',
        component: Settings,
        meta: { requiresAuth: true, title: '系统设置' }
      },
      {
        path: 'logs',
        component: Logs,
        meta: { requiresAuth: true, title: '系统日志' }
      }
    ]
  },

  // 用户认证
  {
    path: '/user/login',
    component: UserLogin,
    meta: { requiresAuth: false, title: '用户登录' }
  },
  {
    path: '/user/register',
    component: UserRegister,
    meta: { requiresAuth: false, title: '用户注册' }
  },

  // 用户个人页面
  {
    path: '/user/profile',
    component: UserProfile,
    meta: { requiresAuth: true, title: '个人中心' }
  },
  {
    path: '/user/posts',
    component: UserPosts,
    meta: { requiresAuth: true, requiresBlogAuthor: true, title: '我的文章' }
  },
  {
    path: '/notifications',
    component: UserNotifications,
    meta: { requiresAuth: true, title: '消息通知' }
  },
  {
    path: '/user/edit',
    component: () => import('../views/user/PostEdit.vue'),
    meta: { requiresAuth: true, requiresBlogAuthor: true, title: '编辑文章' }
  },

  // 博客前台
  {
    path: '/',
    component: BlogLayout,
    children: [
      {
        path: '',
        component: UserHome,
        meta: { title: 'RE-BIN - 个人博客' }
      },
      {
        path: 'post/:id',
        component: PostDetail,
        meta: { title: '文章详情' }
      },
      {
        path: 'archive',
        component: Archive,
        meta: { title: '文章归档' }
      },
      {
        path: 'categories',
        component: Categories,
        meta: { title: '文章分类' }
      },
      {
        path: 'category/:id',
        component: Categories,
        meta: { title: '分类文章' }
      },
      {
        path: 'tags',
        component: Tags,
        meta: { title: '标签云' }
      },
      {
        path: 'tag/:id',
        component: Tags,
        meta: { title: '标签文章' }
      },
      {
        path: 'about',
        component: About,
        meta: { title: '关于我' }
      },
      {
        path: 'friendlinks',
        component: FriendLinks,
        meta: { title: '友情链接' }
      },
      {
        path: 'search',
        component: Search,
        meta: { title: '搜索结果' }
      },
      {
        path: 'test',
        component: Test,
        meta: { title: '功能测试' }
      }
    ]
  },

  // 404页面
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFound.vue'),
    meta: { title: '页面未找到' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title
  } else {
    document.title = 'RE-BIN 个人博客'
  }

  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    const userType = localStorage.getItem('userType') // 'admin' 或 'user'

    if (!token) {
      // 根据目标路径决定跳转到哪个登录页面
      if (to.path.startsWith('/admin')) {
        next('/login')
      } else {
        next('/user/login')
      }
      return
    }

    // 检查角色权限
    if (to.meta.role) {
      if (to.meta.role === 'admin' && userType !== 'admin') {
        // 需要管理员权限但当前是普通用户
        next('/login')
        return
      }
    }

    // 检查是否需要网站作者权限
    if (to.meta.requiresBlogAuthor) {
      try {
        // 获取用户信息
        const userStore = useUserStore()
        await userStore.fetchUserInfo()

        // 检查是否为网站作者(ID=1)或管理员
        const isAdmin = userType === 'admin'
        const isBlogAuthor = userStore.userInfo && userStore.userInfo.id === 1

        if (!isAdmin && !isBlogAuthor) {
          // 不是管理员也不是网站作者，拒绝访问
          next('/')
          return
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        next('/user/login')
        return
      }
    }
  }

  // 如果已登录用户访问登录页面，重定向到相应首页
  if ((to.path === '/login' || to.path === '/user/login') && localStorage.getItem('token')) {
    const userType = localStorage.getItem('userType')
    if (userType === 'admin') {
      next('/admin')
    } else {
      next('/')
    }
    return
  }

  next()
})

// 路由后置守卫
router.afterEach((to, from) => {
  // 滚动到页面顶部
  window.scrollTo(0, 0)
})

export default router
