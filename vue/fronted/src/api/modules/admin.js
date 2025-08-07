import request from '../request'

export const adminApi = {
  // 管理员登录
  login: (data) => request.post('/admins/auth/login', data),

  // 管理员管理
  getAllAdmins: () => request.get('/admins'),
  getAdminById: (id) => request.get(`/admins/${id}`),
  createAdmin: (data) => request.post('/admins/add', data),
  updateAdmin: (data) => request.put('/admins/update', data),
  deleteAdmin: (id) => request.delete(`/admins/del/${id}`),

  // 用户管理
  getUsers: () => request.get('/user'),
  getUserById: (id) => request.get(`/user/${id}`),
  createUser: (data) => request.post('/user/add', data),
  updateUser: (data) => request.put('/user/update', data),
  deleteUser: (id) => request.delete(`/user/del/${id}`),

  // 统计数据
  getStats: () => request.get('/admins/stats'),
  getRecentActivities: () => request.get('/admins/activities'),

  // 文章管理
  getAllPosts: () => request.get('/posts'),
  getPostById: (id) => request.get(`/posts/${id}`),
  createPost: (data) => request.post('/posts/add', data),
  updatePost: (data) => request.put('/posts/update', data),
  deletePost: (id) => request.delete(`/posts/del/${id}`),
  getPublishedPosts: (page = 1, size = 10) => request.get('/posts/published', { params: { page, size } }),

  // 评论管理
  getAllComments: () => request.get('/comments'),
  getCommentsByPost: (postId) => request.get(`/comments/post/${postId}`),
  createComment: (data) => request.post('/comments/add', data),
  deleteComment: (id) => request.delete(`/comments/del/${id}`),

  // 分类管理
  getCategories: () => request.get('/categories'),
  createCategory: (data) => request.post('/categories/add', data),
  updateCategory: (data) => request.put('/categories/update', data),
  deleteCategory: (id) => request.delete(`/categories/del/${id}`),

  // 标签管理
  getTags: () => request.get('/tags'),
  getTagsByPost: (postId) => request.get(`/tags/post/${postId}`),
  createTag: (data) => request.post('/tags/add', data),
  updateTag: (data) => request.put('/tags/update', data),
  deleteTag: (id) => request.delete(`/tags/del/${id}`),

  // 友情链接管理
  getFriendLinks: () => request.get('/friend-links'),
  createFriendLink: (data) => request.post('/friend-links/add', data),
  updateFriendLink: (data) => request.put('/friend-links/update', data),
  deleteFriendLink: (id) => request.delete(`/friend-links/del/${id}`),

  // 通用上传
  uploadFile: (file, type, userId) => {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('type', type)
    formData.append('userId', userId)
    return request.post('/common/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}
