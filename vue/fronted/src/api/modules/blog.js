import request from '../request'

export const blogApi = {
  // 获取首页文章列表 (使用BlogController)
  getPosts: (params) => request.get('/api/blog/posts', { params }),

  // 获取文章详情 (使用BlogController)
  getPostDetail: (id) => request.get(`/api/blog/posts/${id}`),

  // 获取已发布文章列表 (直接使用PostController)
  getPublishedPosts: (page = 1, size = 10) => request.get('/posts/published', {
    params: { page, size }
  }),

  // 创建文章 (需要登录)
  createPost: (data) => request.post('/posts/add', data),

  // 更新文章 (需要登录)
  updatePost: (data) => request.put('/posts/update', data),

  // 删除文章 (需要登录)
  deletePost: (id) => request.delete(`/posts/del/${id}`),

  // 搜索文章 (使用BlogController)
  searchPosts: (keyword, params) => request.get('/api/blog/search', {
    params: { keyword, ...params }
  }),

  // 获取分类列表 (使用BlogController，包含文章数量)
  getCategories: () => request.get('/api/blog/categories'),

  // 获取标签列表 (使用BlogController，包含文章数量)
  getTags: () => request.get('/api/blog/tags'),

  // 获取文章标签
  getPostTags: (postId) => request.get(`/tags/post/${postId}`),

  // 获取热门文章 (使用BlogController)
  getHotPosts: (limit = 5) => request.get('/api/blog/posts/popular', {
    params: { limit }
  }),

  // 获取文章归档 (使用BlogController)
  getArchives: () => request.get('/api/blog/archives'),

  // 获取网站统计 (使用BlogController)
  getStatistics: () => request.get('/api/blog/statistics'),

  // 点赞文章 (使用BlogController)
  togglePostLike: (postId) => request.post(`/api/blog/posts/${postId}/like`),

  // 获取首页数据 (使用BlogController)
  getHomeData: () => request.get('/api/blog/home')
}
