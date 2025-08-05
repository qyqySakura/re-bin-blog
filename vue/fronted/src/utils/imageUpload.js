import { fileApi } from './api'
import { ElMessage } from 'element-plus'
import { API_CONFIG } from '../config/index.js'

/**
 * 图片上传工具函数
 */

/**
 * 将文件转换为base64
 * @param {File} file 文件对象
 * @returns {Promise<string>} base64字符串
 */
export const fileToBase64 = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => resolve(reader.result)
    reader.onerror = reject
    reader.readAsDataURL(file)
  })
}

/**
 * 验证图片文件
 * @param {File} file 文件对象
 * @param {Object} options 验证选项
 * @returns {boolean} 是否通过验证
 */
export const validateImageFile = (file, options = {}) => {
  const {
    maxSize = API_CONFIG.UPLOAD.MAX_IMAGE_SIZE,
    allowedTypes = API_CONFIG.UPLOAD.ALLOWED_IMAGE_TYPES
  } = options

  // 检查文件类型
  if (!allowedTypes.includes(file.type)) {
    ElMessage.error('只支持 JPG、PNG、GIF、WebP 格式的图片!')
    return false
  }

  // 检查文件大小
  if (file.size > maxSize) {
    const maxSizeMB = (maxSize / 1024 / 1024).toFixed(1)
    ElMessage.error(`图片大小不能超过 ${maxSizeMB}MB!`)
    return false
  }

  return true
}

/**
 * 上传图片到后端
 * @param {File} file 图片文件
 * @param {Object} options 上传选项
 * @returns {Promise<string>} 图片URL
 */
export const uploadImage = async (file, options = {}) => {
  // 验证文件
  if (!validateImageFile(file, options)) {
    throw new Error('文件验证失败')
  }

  try {
    // 转换为base64
    const base64Data = await fileToBase64(file)
    
    // 上传到后端
    const response = await fileApi.uploadImage(base64Data)
    
    if (response.code === 200) {
      // 构建完整的图片URL
      const imageUrl = `${API_CONFIG.BASE_URL}${response.data.url}`
      ElMessage.success('图片上传成功!')
      return imageUrl
    } else {
      throw new Error(response.message || '上传失败')
    }
  } catch (error) {
    ElMessage.error(`图片上传失败: ${error.message}`)
    throw error
  }
}

/**
 * 删除图片
 * @param {string} imageUrl 图片URL
 * @returns {Promise<boolean>} 是否删除成功
 */
export const deleteImage = async (imageUrl) => {
  try {
    const response = await fileApi.deleteImage(imageUrl)
    if (response.code === 200) {
      ElMessage.success('图片删除成功!')
      return true
    } else {
      throw new Error(response.message || '删除失败')
    }
  } catch (error) {
    ElMessage.error(`图片删除失败: ${error.message}`)
    return false
  }
}

/**
 * 批量上传图片
 * @param {FileList} files 文件列表
 * @param {Object} options 上传选项
 * @returns {Promise<Array<string>>} 图片URL数组
 */
export const uploadMultipleImages = async (files, options = {}) => {
  const uploadPromises = Array.from(files).map(file => uploadImage(file, options))
  
  try {
    const results = await Promise.allSettled(uploadPromises)
    const successUrls = []
    const failedCount = results.filter(result => result.status === 'rejected').length
    
    results.forEach(result => {
      if (result.status === 'fulfilled') {
        successUrls.push(result.value)
      }
    })
    
    if (failedCount > 0) {
      ElMessage.warning(`${successUrls.length} 张图片上传成功，${failedCount} 张失败`)
    } else {
      ElMessage.success(`${successUrls.length} 张图片全部上传成功!`)
    }
    
    return successUrls
  } catch (error) {
    ElMessage.error('批量上传失败')
    throw error
  }
}

/**
 * 获取图片信息
 * @param {string} imageUrl 图片URL
 * @returns {Promise<Object>} 图片信息
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
    
    img.src = imageUrl
  })
}

/**
 * 压缩图片（前端压缩，作为后端压缩的补充）
 * @param {File} file 原始文件
 * @param {Object} options 压缩选项
 * @returns {Promise<File>} 压缩后的文件
 */
export const compressImage = (file, options = {}) => {
  const {
    maxWidth = API_CONFIG.UPLOAD.COMPRESSION.MAX_WIDTH,
    maxHeight = API_CONFIG.UPLOAD.COMPRESSION.MAX_HEIGHT,
    quality = API_CONFIG.UPLOAD.COMPRESSION.QUALITY
  } = options

  return new Promise((resolve) => {
    const canvas = document.createElement('canvas')
    const ctx = canvas.getContext('2d')
    const img = new Image()
    
    img.onload = () => {
      // 计算新尺寸
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
      
      // 绘制压缩图片
      ctx.drawImage(img, 0, 0, width, height)
      
      // 转换为Blob
      canvas.toBlob((blob) => {
        const compressedFile = new File([blob], file.name, {
          type: 'image/jpeg',
          lastModified: Date.now()
        })
        resolve(compressedFile)
      }, 'image/jpeg', quality)
    }
    
    img.src = URL.createObjectURL(file)
  })
}
