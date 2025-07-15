// src/api/blog.js
import request from '@/utils/request'

// ==================== 文章相关 ====================

/**
 * 获取首页文章列表
 */
export const getHomePosts = (page = 1, size = 10) => {
  return request.get('/api/blog/posts', {
    params: { page, size }
  })
}

/**
 * 获取文章详情
 */
export const getPostDetail = (id) => {
  return request.get(`/api/blog/posts/${id}`)
}

/**
 * 获取最新文章
 */
export const getLatestPosts = (limit = 5) => {
  return request.get('/api/blog/posts/latest', {
    params: { limit }
  })
}

/**
 * 获取热门文章
 */
export const getPopularPosts = (limit = 5) => {
  return request.get('/api/blog/posts/popular', {
    params: { limit }
  })
}

/**
 * 搜索文章
 */
export const searchPosts = (keyword) => {
  return request.get('/api/blog/search', {
    params: { keyword }
  })
}

/**
 * 获取文章归档
 */
export const getPostArchive = () => {
  return request.get('/api/blog/archive')
}

// ==================== 分类相关 ====================

/**
 * 获取所有分类
 */
export const getCategories = () => {
  return request.get('/api/blog/categories')
}

/**
 * 获取分类详情
 */
export const getCategoryDetail = (id) => {
  return request.get(`/api/blog/categories/${id}`)
}

/**
 * 根据分类获取文章
 */
export const getPostsByCategory = (id) => {
  return request.get(`/api/blog/categories/${id}/posts`)
}

// ==================== 标签相关 ====================

/**
 * 获取所有标签
 */
export const getTags = () => {
  return request.get('/api/blog/tags')
}

/**
 * 获取标签云
 */
export const getTagCloud = () => {
  return request.get('/api/blog/tags/cloud')
}

/**
 * 获取标签详情
 */
export const getTagDetail = (id) => {
  return request.get(`/api/blog/tags/${id}`)
}

/**
 * 根据标签获取文章
 */
export const getPostsByTag = (id) => {
  return request.get(`/api/blog/tags/${id}/posts`)
}

// ==================== 评论相关 ====================

/**
 * 获取文章评论
 */
export const getPostComments = (postId) => {
  return request.get(`/api/blog/posts/${postId}/comments`)
}

/**
 * 添加评论
 */
export const addComment = (comment) => {
  return request.post('/api/blog/comments', comment)
}

/**
 * 删除评论
 */
export const deleteComment = (id, userId) => {
  return request.delete(`/api/blog/comments/${id}`, {
    params: { userId }
  })
}

// ==================== 用户相关 ====================

/**
 * 获取用户公开信息
 */
export const getUserInfo = (id) => {
  return request.get(`/api/blog/users/${id}`)
}

/**
 * 获取用户文章列表
 */
export const getUserPosts = (id) => {
  return request.get(`/api/blog/users/${id}/posts`)
}

// ==================== 统计相关 ====================

/**
 * 获取博客统计信息
 */
export const getBlogStatistics = () => {
  return request.get('/api/blog/statistics')
}

/**
 * 获取网站信息
 */
export const getSiteInfo = () => {
  return request.get('/api/blog/site-info')
}

// ==================== 用户认证相关 ====================

/**
 * 用户登录
 */
export const userLogin = (loginData) => {
  return request.post('/user/auth/login', loginData)
}

/**
 * 用户注册
 */
export const userRegister = (userData, code) => {
  return request.post('/user/add', userData, {
    params: { code }
  })
}

/**
 * 发送邮箱验证码
 */
export const sendEmailCode = (email) => {
  return request.post('/user/sendEmailCode', { email })
}

/**
 * 验证邮箱验证码
 */
export const verifyEmailCode = (email, code) => {
  return request.post('/user/verifyEmailCode', { email, code })
}

/**
 * 用户登出
 */
export const userLogout = () => {
  return request.post('/user/auth/logout')
}

/**
 * 获取当前用户信息
 */
export const getCurrentUser = () => {
  return request.get('/user/auth/info')
}

/**
 * 更新用户信息
 */
export const updateUserInfo = (userData) => {
  return request.put('/user/update', userData)
}

/**
 * 上传头像
 */
export const uploadAvatar = (formData) => {
  return request.post('/user/upload/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export default {
  // 文章相关
  getHomePosts,
  getPostDetail,
  getLatestPosts,
  getPopularPosts,
  searchPosts,
  getPostArchive,
  
  // 分类相关
  getCategories,
  getCategoryDetail,
  getPostsByCategory,
  
  // 标签相关
  getTags,
  getTagCloud,
  getTagDetail,
  getPostsByTag,
  
  // 评论相关
  getPostComments,
  addComment,
  deleteComment,
  
  // 用户相关
  getUserInfo,
  getUserPosts,
  
  // 统计相关
  getBlogStatistics,
  getSiteInfo,
  
  // 认证相关
  userLogin,
  userRegister,
  sendEmailCode,
  verifyEmailCode,
  userLogout,
  getCurrentUser,
  updateUserInfo,
  uploadAvatar
}
