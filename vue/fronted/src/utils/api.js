import request from './request'

// 用户相关API
export const userApi = {
  // 用户登录
  login: (data) => request.post('/user/auth/login', data),
  // 用户注册
  register: (data) => request.post('/user/add', data),
  // 发送验证码
  sendCode: (email) => request.post('/user/sendEmailCode', { email }),
  // 获取所有用户
  getUsers: () => request.get('/user'),
  // 根据ID获取用户
  getUserById: (id) => request.get(`/user/${id}`),
  // 更新用户
  updateUser: (data) => request.put('/user/update', data),
  // 删除用户
  deleteUser: (id) => request.delete(`/user/del/${id}`),
  // 修改密码
  changePassword: (data) => request.post('/user/changePassword', data)
}

// 管理员相关API
export const adminApi = {
  // 管理员登录
  login: (data) => request.post('/admins/auth/login', data),
  // 获取所有管理员
  getAdmins: () => request.get('/admins'),
  // 根据ID获取管理员
  getAdminById: (id) => request.get(`/admins/${id}`),
  // 新增管理员
  addAdmin: (data) => request.post('/admins/add', data),
  // 更新管理员
  updateAdmin: (data) => request.put('/admins/update', data),
  // 删除管理员
  deleteAdmin: (id) => request.delete(`/admins/del/${id}`)
}

// 文章相关API
export const postApi = {
  // 获取所有文章
  getPosts: () => request.get('/posts'),
  // 根据ID获取文章
  getPostById: (id) => request.get(`/posts/${id}`),
  // 获取已发布的文章（分页）
  getPublishedPosts: (page = 1, size = 10) => request.get('/posts/published', { params: { page, size } }),
  // 根据用户ID获取文章
  getPostsByUserId: (userId) => request.get(`/posts/user/${userId}`),
  // 根据分类ID获取文章
  getPostsByCategoryId: (categoryId) => request.get(`/posts/category/${categoryId}`),
  // 根据分类获取文章（分页）
  getPostsByCategory: (categoryId, page = 1, size = 10) => request.get(`/posts/category/${categoryId}`, { params: { page, size } }),
  // 根据标签获取文章（分页）
  getPostsByTag: (tagId, page = 1, size = 10) => request.get(`/posts/tag/${tagId}`, { params: { page, size } }),
  // 搜索文章
  searchPosts: (params) => request.get('/posts/search', { params }),
  // 新增文章
  addPost: (data) => request.post('/posts/add', data),
  // 更新文章
  updatePost: (data) => request.put('/posts/update', data),
  // 删除文章
  deletePost: (id) => request.delete(`/posts/del/${id}`)
}

// 分类相关API
export const categoryApi = {
  // 获取所有分类
  getCategories: () => request.get('/categories'),
  // 根据ID获取分类
  getCategoryById: (id) => request.get(`/categories/${id}`),
  // 根据名称获取分类
  getCategoryByName: (name) => request.get(`/categories/name/${name}`),
  // 新增分类
  addCategory: (data) => request.post('/categories/add', data),
  // 更新分类
  updateCategory: (data) => request.put('/categories/update', data),
  // 删除分类
  deleteCategory: (id) => request.delete(`/categories/del/${id}`)
}

// 标签相关API
export const tagApi = {
  // 获取所有标签
  getTags: () => request.get('/tags'),
  // 根据ID获取标签
  getTagById: (id) => request.get(`/tags/${id}`),
  // 根据名称获取标签
  getTagByName: (name) => request.get(`/tags/name/${name}`),
  // 根据文章ID获取标签
  getTagsByPostId: (postId) => request.get(`/tags/post/${postId}`),
  // 新增标签
  addTag: (data) => request.post('/tags/add', data),
  // 更新标签
  updateTag: (data) => request.put('/tags/update', data),
  // 删除标签
  deleteTag: (id) => request.delete(`/tags/del/${id}`)
}

// 评论相关API
export const commentApi = {
  // 获取所有评论
  getComments: () => request.get('/comments'),
  // 根据ID获取评论
  getCommentById: (id) => request.get(`/comments/${id}`),
  // 根据文章ID获取评论
  getCommentsByPostId: (postId) => request.get(`/comments/post/${postId}`),
  // 根据用户ID获取评论
  getCommentsByUserId: (userId) => request.get(`/comments/user/${userId}`),
  // 根据父评论ID获取回复
  getCommentsByParentId: (parentId) => request.get(`/comments/parent/${parentId}`),
  // 新增评论
  addComment: (data) => request.post('/comments/add', data),
  // 更新评论
  updateComment: (data) => request.put('/comments/update', data),
  // 删除评论
  deleteComment: (id) => request.delete(`/comments/del/${id}`)
}

// 通用API
export const commonApi = {
  // 上传头像
  uploadAvatar: (formData) => request.post('/common/upload/avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
  // 获取当前用户信息
  getCurrentUser: () => request.get('/auth/info')
}
