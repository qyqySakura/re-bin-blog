import request from '../request'

export const commentApi = {
  // 获取文章评论列表
  getComments: (postId, params) => request.get(`/comments/post/${postId}`, { params }),

  // 发表评论
  createComment: (postId, data) => request.post('/comments/add', { ...data, postId }),

  // 回复评论 (实际上也是创建评论，但指定parentId)
  replyComment: (postId, commentId, data) => request.post('/comments/add', {
    ...data,
    postId,
    parentId: commentId
  }),

  // 删除评论
  deleteComment: (commentId) => request.delete(`/comments/del/${commentId}`),

  // 点赞评论 (如果后端支持的话)
  likeComment: (commentId) => request.post(`/comments/${commentId}/like`),

  // 获取所有评论 (管理员用)
  getAllComments: () => request.get('/comments')
}
