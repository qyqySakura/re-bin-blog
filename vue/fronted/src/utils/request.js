import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router/router.js'

const service = axios.create({
  baseURL: 'http://localhost:9090',
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      // 设置Sa-Token配置的token名称
      config.headers['satoken'] = token
      // 同时设置标准的Authorization头，确保兼容性
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const data = response.data
    
    // 如果响应成功但业务状态码不是200
    if (data.code && data.code !== 200) {
      ElMessage.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    
    return data
  },
  error => {
    console.error('响应错误:', error)
    
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          ElMessage.error('登录已过期，请重新登录')
          localStorage.removeItem('token')
          router.push('/login')
          break
        case 403:
          ElMessage.error('没有权限访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(data?.message || '网络错误，请稍后重试')
      }
    } else {
      ElMessage.error('网络连接失败，请检查网络设置')
    }
    
    return Promise.reject(error)
  }
)

export default service
