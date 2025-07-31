import request from '../request'

export const userApi = {
  // 用户登录
  login: (data) => request.post('/user/auth/login', data),

  // 用户注册 (需要验证码)
  register: (data) => request.post('/user/add', data),

  // 发送验证码
  sendVerifyCode: (email) => request.post('/user/sendEmailCode', { email }),

  // 获取用户信息
  getUserInfo: () => request.get('/user/auth/info'),

  // 更新用户信息
  updateProfile: (data) => request.put('/user/update', data),

  // 修改密码
  changePassword: (data) => request.post('/user/changePassword', data),

  // 上传头像
  uploadAvatar: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/user/upload/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 退出登录
  logout: () => request.post('/user/auth/logout'),

  // 获取所有用户
  getUsers: () => request.get('/user'),

  // 根据ID获取用户
  getUserById: (id) => request.get(`/user/${id}`),

  // 删除用户
  deleteUser: (id) => request.delete(`/user/del/${id}`)
}
