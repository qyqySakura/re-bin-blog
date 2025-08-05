/**
 * 项目配置文件
 */

// 根据环境变量确定API基础地址
const getApiBaseUrl = () => {
  // 开发环境
  if (import.meta.env.DEV) {
    return 'http://localhost:9090'
  }

  // 生产环境
  if (import.meta.env.PROD) {
    // 生产环境下可以使用相对路径或者配置的域名
    return import.meta.env.VITE_API_BASE_URL || 'http://localhost:9090'
  }
  
  // 默认值
  return 'http://localhost:9090'
}

// API配置
export const API_CONFIG = {
  // 基础URL
  BASE_URL: getApiBaseUrl(),
  
  // 超时时间
  TIMEOUT: 10000,
  
  // 文件上传配置
  UPLOAD: {
    // 图片上传最大大小 (5MB)
    MAX_IMAGE_SIZE: 5 * 1024 * 1024,
    
    // 支持的图片格式
    ALLOWED_IMAGE_TYPES: [
      'image/jpeg',
      'image/png', 
      'image/gif',
      'image/webp'
    ],
    
    // 图片压缩配置
    COMPRESSION: {
      MAX_WIDTH: 1920,
      MAX_HEIGHT: 1080,
      QUALITY: 0.8
    }
  }
}

// 路由配置
export const ROUTE_CONFIG = {
  // 默认路由
  DEFAULT_ROUTE: '/',
  
  // 登录路由
  LOGIN_ROUTE: '/login',
  
  // 管理员登录路由
  ADMIN_LOGIN_ROUTE: '/admin/login'
}

// 存储配置
export const STORAGE_CONFIG = {
  // Token存储键名
  TOKEN_KEY: 'token',
  
  // 用户信息存储键名
  USER_INFO_KEY: 'userInfo',
  
  // 主题存储键名
  THEME_KEY: 'theme'
}

// 分页配置
export const PAGINATION_CONFIG = {
  // 默认页大小
  DEFAULT_PAGE_SIZE: 10,
  
  // 页大小选项
  PAGE_SIZE_OPTIONS: [5, 10, 20, 50, 100]
}

// 编辑器配置
export const EDITOR_CONFIG = {
  // 富文本编辑器工具栏
  TOOLBAR: [
    'bold', 'italic', 'underline',
    'list-ordered', 'list-unordered',
    'link', 'image'
  ],
  
  // 编辑器最小高度
  MIN_HEIGHT: 300
}

// 图片显示配置
export const IMAGE_CONFIG = {
  // 默认封面图片
  DEFAULT_COVER: '/images/default-cover.jpg',
  
  // 默认头像
  DEFAULT_AVATAR: '/images/default-avatar.png',
  
  // 图片懒加载配置
  LAZY_LOAD: {
    THRESHOLD: 0.1,
    ROOT_MARGIN: '50px'
  }
}

// 消息提示配置
export const MESSAGE_CONFIG = {
  // 成功消息持续时间
  SUCCESS_DURATION: 3000,
  
  // 错误消息持续时间
  ERROR_DURATION: 5000,
  
  // 警告消息持续时间
  WARNING_DURATION: 4000
}

// 开发配置
export const DEV_CONFIG = {
  // 是否启用调试模式
  DEBUG: import.meta.env.DEV,
  
  // 是否显示API日志
  LOG_API: import.meta.env.DEV,
  
  // 是否启用Mock数据
  ENABLE_MOCK: false
}

// 导出完整配置
export default {
  API_CONFIG,
  ROUTE_CONFIG,
  STORAGE_CONFIG,
  PAGINATION_CONFIG,
  EDITOR_CONFIG,
  IMAGE_CONFIG,
  MESSAGE_CONFIG,
  DEV_CONFIG
}
