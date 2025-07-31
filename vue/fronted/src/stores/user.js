import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { userApi } from '@/utils/api.js'

export const useUserStore = defineStore('user', () => {
  // 状态
  const userInfo = ref(null)
  const token = ref(localStorage.getItem('token') || '')
  const isLoggedIn = computed(() => !!token.value && !!userInfo.value)
  
  // 动作
  const login = async (credentials) => {
    try {
      const response = await userApi.login({
        username: credentials.username,
        password: credentials.password
      })
      token.value = response.data.token
      userInfo.value = response.data.user

      // 设置localStorage，与路由守卫保持一致
      localStorage.setItem('token', token.value)
      localStorage.setItem('userType', 'user')

      return response
    } catch (error) {
      throw error
    }
  }
  
  const register = async (userData) => {
    try {
      const response = await userApi.register(userData)
      return response
    } catch (error) {
      throw error
    }
  }
  
  const logout = async () => {
    try {
      if (token.value) {
        await userApi.logout()
      }
    } catch (error) {
      console.error('退出登录失败:', error)
    } finally {
      token.value = ''
      userInfo.value = null
      localStorage.removeItem('token')
      localStorage.removeItem('userType')
    }
  }
  
  const updateUserInfo = async (data) => {
    try {
      const response = await userApi.updateProfile(data)
      userInfo.value = { ...userInfo.value, ...response.data }
      return response
    } catch (error) {
      throw error
    }
  }
  
  const changePassword = async (data) => {
    try {
      const response = await userApi.changePassword(data)
      return response
    } catch (error) {
      throw error
    }
  }
  
  const uploadAvatar = async (file) => {
    try {
      const response = await userApi.uploadAvatar(file)
      if (userInfo.value) {
        userInfo.value.avatar = response.data.avatar
      }
      return response
    } catch (error) {
      throw error
    }
  }
  
  const fetchUserInfo = async () => {
    try {
      const response = await userApi.getUserInfo()
      if (response.code === 200) {
        userInfo.value = response.data.user
      }
      return response
    } catch (error) {
      // 如果获取用户信息失败，清除本地token
      logout()
      throw error
    }
  }
  
  // 初始化时如果有token，尝试获取用户信息
  const initUser = async () => {
    if (token.value && !userInfo.value) {
      try {
        await fetchUserInfo()
      } catch (error) {
        console.error('初始化用户信息失败:', error)
      }
    }
  }
  
  return {
    userInfo,
    token,
    isLoggedIn,
    login,
    register,
    logout,
    updateUserInfo,
    changePassword,
    uploadAvatar,
    fetchUserInfo,
    initUser
  }
})
