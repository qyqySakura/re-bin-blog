import { API_CONFIG } from '@/config/index.js'

/**
 * 图片URL处理工具
 * 统一处理图片URL，解决图片加载问题
 */

/**
 * 处理图片URL，确保图片能正确加载
 * @param {string} imageUrl - 原始图片URL
 * @param {string} type - 图片类型 ('cover', 'avatar', 'content')
 * @returns {string} - 处理后的完整URL
 */
export const processImageUrl = (imageUrl, type = 'cover') => {
  // 如果没有图片URL，返回默认图片
  if (!imageUrl) {
    return getDefaultImage(type)
  }

  // 如果已经是完整的HTTP/HTTPS URL，直接返回
  if (imageUrl.startsWith('http://') || imageUrl.startsWith('https://')) {
    return imageUrl
  }

  // 如果是相对路径，拼接基础URL
  if (imageUrl.startsWith('/')) {
    return `${API_CONFIG.BASE_URL}${imageUrl}`
  }

  // 如果是不带前缀的路径，根据类型添加前缀
  return `${API_CONFIG.BASE_URL}/${getUrlPrefix(type)}${imageUrl}`
}

/**
 * 获取URL前缀
 * @param {string} type - 图片类型
 * @returns {string} - URL前缀
 */
const getUrlPrefix = (type) => {
  switch (type) {
    case 'cover':
      return 'api/files/'
    case 'avatar':
      return 'avatar/'
    case 'content':
      return 'api/files/'
    default:
      return 'api/files/'
  }
}

/**
 * 获取默认图片
 * @param {string} type - 图片类型
 * @returns {string} - 默认图片URL
 */
const getDefaultImage = (type) => {
  const defaultImages = {
    cover: 'https://images.unsplash.com/photo-1518709268805-4e9042af2176?w=800&h=400&fit=crop',
    avatar: `${API_CONFIG.BASE_URL}/static/default-avatar.png`,
    content: 'https://images.unsplash.com/photo-1518709268805-4e9042af2176?w=800&h=400&fit=crop'
  }
  
  return defaultImages[type] || defaultImages.cover
}

/**
 * 处理图片加载错误
 * @param {Event} event - 错误事件
 * @param {string} type - 图片类型
 */
export const handleImageError = (event, type = 'cover') => {
  const img = event.target
  const originalSrc = img.src
  
  console.warn('图片加载失败:', originalSrc)
  
  // 避免无限循环，检查是否已经是默认图片
  const defaultImg = getDefaultImage(type)
  if (originalSrc !== defaultImg) {
    img.src = defaultImg
  }
}

/**
 * 预加载图片
 * @param {string} imageUrl - 图片URL
 * @returns {Promise} - 加载Promise
 */
export const preloadImage = (imageUrl) => {
  return new Promise((resolve, reject) => {
    const img = new Image()
    img.onload = () => resolve(img)
    img.onerror = reject
    img.src = processImageUrl(imageUrl)
  })
}

/**
 * 批量预加载图片
 * @param {Array} imageUrls - 图片URL数组
 * @returns {Promise} - 加载Promise
 */
export const preloadImages = (imageUrls) => {
  const promises = imageUrls.map(url => preloadImage(url))
  return Promise.allSettled(promises)
}

/**
 * 检查图片是否可访问
 * @param {string} imageUrl - 图片URL
 * @returns {Promise<boolean>} - 是否可访问
 */
export const checkImageAccessible = async (imageUrl) => {
  try {
    await preloadImage(imageUrl)
    return true
  } catch {
    return false
  }
}

/**
 * 获取图片信息
 * @param {string} imageUrl - 图片URL
 * @returns {Promise<Object>} - 图片信息
 */
export const getImageInfo = (imageUrl) => {
  return new Promise((resolve, reject) => {
    const img = new Image()
    
    img.onload = () => {
      resolve({
        width: img.naturalWidth,
        height: img.naturalHeight,
        url: imageUrl,
        aspectRatio: img.naturalWidth / img.naturalHeight
      })
    }
    
    img.onerror = () => {
      reject(new Error('图片加载失败'))
    }
    
    img.src = processImageUrl(imageUrl)
  })
}

/**
 * 压缩图片
 * @param {File} file - 图片文件
 * @param {Object} options - 压缩选项
 * @returns {Promise<Blob>} - 压缩后的图片
 */
export const compressImage = (file, options = {}) => {
  const {
    maxWidth = 1920,
    maxHeight = 1080,
    quality = 0.8
  } = options

  return new Promise((resolve) => {
    const canvas = document.createElement('canvas')
    const ctx = canvas.getContext('2d')
    const img = new Image()

    img.onload = () => {
      // 计算压缩后的尺寸
      let { width, height } = img
      
      if (width > maxWidth) {
        height = (height * maxWidth) / width
        width = maxWidth
      }
      
      if (height > maxHeight) {
        width = (width * maxHeight) / height
        height = maxHeight
      }

      canvas.width = width
      canvas.height = height

      // 绘制并压缩
      ctx.drawImage(img, 0, 0, width, height)
      canvas.toBlob(resolve, 'image/jpeg', quality)
    }

    img.src = URL.createObjectURL(file)
  })
}

/**
 * 图片懒加载观察器
 */
export const createLazyLoadObserver = (callback) => {
  return new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        callback(entry.target)
      }
    })
  }, {
    rootMargin: '50px'
  })
}

/**
 * 应用懒加载
 * @param {HTMLImageElement} img - 图片元素
 * @param {string} src - 图片源
 */
export const applyLazyLoad = (img, src) => {
  const observer = createLazyLoadObserver((target) => {
    target.src = processImageUrl(src)
    observer.unobserve(target)
  })
  
  observer.observe(img)
}

// 导出默认的图片错误处理函数
export default {
  processImageUrl,
  handleImageError,
  preloadImage,
  preloadImages,
  checkImageAccessible,
  getImageInfo,
  compressImage,
  createLazyLoadObserver,
  applyLazyLoad
}
