import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { adminApi } from '@/utils/api.js'

export const useAdminStore = defineStore('admin', () => {
  // 状态
  const adminInfo = ref(null)
  const token = ref(localStorage.getItem('admin_token') || '')
  const isLoggedIn = computed(() => !!token.value && !!adminInfo.value)
  
  const dashboard = ref({})
  const users = ref([])
  const allPosts = ref([])
  const allComments = ref([])
  const friendLinks = ref([])
  const systemConfig = ref({})
  const about = ref({})
  
  const loading = ref(false)
  const pagination = ref({
    page: 1,
    size: 10,
    total: 0
  })
  
  // 动作
  const login = async (credentials) => {
    try {
      const response = await adminApi.login({
        username: credentials.username,
        password: credentials.password
      })
      token.value = response.data.token
      adminInfo.value = response.data.user // 后端返回的是user字段，不是admin

      // 设置localStorage，与路由守卫保持一致
      localStorage.setItem('token', token.value)
      localStorage.setItem('userType', 'admin')
      localStorage.setItem('admin_token', token.value) // 保留原有的admin_token

      return response
    } catch (error) {
      throw error
    }
  }
  
  const logout = () => {
    token.value = ''
    adminInfo.value = null
    // 清除所有相关的localStorage项
    localStorage.removeItem('token')
    localStorage.removeItem('userType')
    localStorage.removeItem('admin_token')
  }
  
  const fetchDashboard = async () => {
    try {
      const response = await adminApi.getDashboard()
      dashboard.value = response.data
      return response.data
    } catch (error) {
      console.error('获取仪表盘数据失败:', error)
    }
  }
  
  const fetchUsers = async (params = {}) => {
    loading.value = true
    try {
      const response = await adminApi.getUsers({
        page: pagination.value.page,
        size: pagination.value.size,
        ...params
      })
      
      users.value = response.data.users || response.data.records
      pagination.value = {
        page: response.data.page,
        size: response.data.size,
        total: response.data.total
      }
    } catch (error) {
      console.error('获取用户列表失败:', error)
    } finally {
      loading.value = false
    }
  }
  
  const updateUserStatus = async (userId, status) => {
    try {
      await adminApi.updateUserStatus(userId, status)
      const userIndex = users.value.findIndex(user => user.id === userId)
      if (userIndex !== -1) {
        users.value[userIndex].status = status
      }
    } catch (error) {
      throw error
    }
  }
  
  const deleteUser = async (userId) => {
    try {
      await adminApi.deleteUser(userId)
      users.value = users.value.filter(user => user.id !== userId)
    } catch (error) {
      throw error
    }
  }
  
  const fetchAllPosts = async (params = {}) => {
    loading.value = true
    try {
      const response = await adminApi.getAllPosts({
        page: pagination.value.page,
        size: pagination.value.size,
        ...params
      })
      
      allPosts.value = response.data.posts || response.data.records
      pagination.value = {
        page: response.data.page,
        size: response.data.size,
        total: response.data.total
      }
    } catch (error) {
      console.error('获取文章列表失败:', error)
    } finally {
      loading.value = false
    }
  }
  
  const updatePostStatus = async (postId, status) => {
    try {
      await adminApi.updatePostStatus(postId, status)
      const postIndex = allPosts.value.findIndex(post => post.id === postId)
      if (postIndex !== -1) {
        allPosts.value[postIndex].status = status
      }
    } catch (error) {
      throw error
    }
  }
  
  const deletePost = async (postId) => {
    try {
      await adminApi.deletePost(postId)
      allPosts.value = allPosts.value.filter(post => post.id !== postId)
    } catch (error) {
      throw error
    }
  }
  
  const fetchAllComments = async (params = {}) => {
    loading.value = true
    try {
      const response = await adminApi.getAllComments({
        page: pagination.value.page,
        size: pagination.value.size,
        ...params
      })
      
      allComments.value = response.data.comments || response.data.records
      pagination.value = {
        page: response.data.page,
        size: response.data.size,
        total: response.data.total
      }
    } catch (error) {
      console.error('获取评论列表失败:', error)
    } finally {
      loading.value = false
    }
  }
  
  const updateCommentStatus = async (commentId, status) => {
    try {
      await adminApi.updateCommentStatus(commentId, status)
      const commentIndex = allComments.value.findIndex(comment => comment.id === commentId)
      if (commentIndex !== -1) {
        allComments.value[commentIndex].status = status
      }
    } catch (error) {
      throw error
    }
  }
  
  const deleteComment = async (commentId) => {
    try {
      await adminApi.deleteComment(commentId)
      allComments.value = allComments.value.filter(comment => comment.id !== commentId)
    } catch (error) {
      throw error
    }
  }
  
  const fetchFriendLinks = async () => {
    try {
      const response = await adminApi.getFriendLinks()
      friendLinks.value = response.data
      return response.data
    } catch (error) {
      console.error('获取友情链接失败:', error)
    }
  }
  
  const createFriendLink = async (data) => {
    try {
      const response = await adminApi.createFriendLink(data)
      friendLinks.value.push(response.data)
      return response.data
    } catch (error) {
      throw error
    }
  }
  
  const updateFriendLink = async (id, data) => {
    try {
      const response = await adminApi.updateFriendLink(id, data)
      const index = friendLinks.value.findIndex(link => link.id === id)
      if (index !== -1) {
        friendLinks.value[index] = response.data
      }
      return response.data
    } catch (error) {
      throw error
    }
  }
  
  const deleteFriendLink = async (id) => {
    try {
      await adminApi.deleteFriendLink(id)
      friendLinks.value = friendLinks.value.filter(link => link.id !== id)
    } catch (error) {
      throw error
    }
  }
  
  const fetchSystemConfig = async () => {
    try {
      const response = await adminApi.getSystemConfig()
      systemConfig.value = response.data
      return response.data
    } catch (error) {
      console.error('获取系统配置失败:', error)
    }
  }
  
  const updateSystemConfig = async (data) => {
    try {
      const response = await adminApi.updateSystemConfig(data)
      systemConfig.value = { ...systemConfig.value, ...response.data }
      return response.data
    } catch (error) {
      throw error
    }
  }
  
  const fetchAbout = async () => {
    try {
      const response = await adminApi.getAbout()
      about.value = response.data
      return response.data
    } catch (error) {
      console.error('获取关于页面失败:', error)
    }
  }
  
  const updateAbout = async (data) => {
    try {
      const response = await adminApi.updateAbout(data)
      about.value = response.data
      return response.data
    } catch (error) {
      throw error
    }
  }
  
  return {
    adminInfo,
    token,
    isLoggedIn,
    dashboard,
    users,
    allPosts,
    allComments,
    friendLinks,
    systemConfig,
    about,
    loading,
    pagination,
    login,
    logout,
    fetchDashboard,
    fetchUsers,
    updateUserStatus,
    deleteUser,
    fetchAllPosts,
    updatePostStatus,
    deletePost,
    fetchAllComments,
    updateCommentStatus,
    deleteComment,
    fetchFriendLinks,
    createFriendLink,
    updateFriendLink,
    deleteFriendLink,
    fetchSystemConfig,
    updateSystemConfig,
    fetchAbout,
    updateAbout
  }
})
