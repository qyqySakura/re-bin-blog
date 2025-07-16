import axios from '@/utils/request'

/**
 * 上传头像
 * @param {File} file - 头像文件
 * @param {string} role - 角色类型，'user'或'admin'
 * @param {number} id - 用户或管理员ID
 * @returns {Promise<string>} 返回头像URL
 */
export async function uploadAvatar(file, role, id) {
  if (!file || !role || !id) {
    console.error('上传头像参数不完整', { file, role, id })
    throw new Error('上传头像参数不完整')
  }

  const formData = new FormData()
  formData.append('file', file)
  formData.append('role', role)
  formData.append('id', id)

  try {
    const res = await axios.post('/common/upload/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (res.data.code === 200) {
      return res.data.data
    } else {
      throw new Error(res.data.message || '上传失败')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    throw error
  }
}