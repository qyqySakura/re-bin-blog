/**
 * localStorage 封装
 */
export const storage = {
  /**
   * 设置存储
   * @param {string} key 键
   * @param {any} value 值
   * @param {number} expire 过期时间（毫秒）
   */
  set(key, value, expire = null) {
    const data = {
      value,
      expire: expire ? Date.now() + expire : null
    }
    localStorage.setItem(key, JSON.stringify(data))
  },

  /**
   * 获取存储
   * @param {string} key 键
   * @returns {any} 值
   */
  get(key) {
    try {
      const item = localStorage.getItem(key)
      if (!item) return null

      const data = JSON.parse(item)
      
      // 检查是否过期
      if (data.expire && Date.now() > data.expire) {
        localStorage.removeItem(key)
        return null
      }

      return data.value
    } catch (error) {
      console.error('获取localStorage失败:', error)
      return null
    }
  },

  /**
   * 删除存储
   * @param {string} key 键
   */
  remove(key) {
    localStorage.removeItem(key)
  },

  /**
   * 清空存储
   */
  clear() {
    localStorage.clear()
  },

  /**
   * 获取所有键
   * @returns {string[]} 键数组
   */
  keys() {
    return Object.keys(localStorage)
  }
}

/**
 * sessionStorage 封装
 */
export const sessionStorage = {
  /**
   * 设置存储
   * @param {string} key 键
   * @param {any} value 值
   */
  set(key, value) {
    try {
      window.sessionStorage.setItem(key, JSON.stringify(value))
    } catch (error) {
      console.error('设置sessionStorage失败:', error)
    }
  },

  /**
   * 获取存储
   * @param {string} key 键
   * @returns {any} 值
   */
  get(key) {
    try {
      const item = window.sessionStorage.getItem(key)
      return item ? JSON.parse(item) : null
    } catch (error) {
      console.error('获取sessionStorage失败:', error)
      return null
    }
  },

  /**
   * 删除存储
   * @param {string} key 键
   */
  remove(key) {
    window.sessionStorage.removeItem(key)
  },

  /**
   * 清空存储
   */
  clear() {
    window.sessionStorage.clear()
  }
}
