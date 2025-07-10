import axios from '@/utils/request'

export async function uploadAvatar(file, role, id) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('role', role)
  formData.append('id', id)
  const token = localStorage.getItem('token')
  const res = await axios.post('/common/upload/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      Authorization: 'Bearer ' + token
    }
  })
  return res.data.data
} 