import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

/**
 * 格式化日期
 * @param {string|Date} date 日期
 * @param {string} format 格式
 * @returns {string} 格式化后的日期
 */
export const formatDate = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) return ''
  return dayjs(date).format(format)
}

/**
 * 相对时间
 * @param {string|Date} date 日期
 * @returns {string} 相对时间
 */
export const fromNow = (date) => {
  if (!date) return ''
  return dayjs(date).fromNow()
}

/**
 * 获取日期差
 * @param {string|Date} date1 日期1
 * @param {string|Date} date2 日期2
 * @param {string} unit 单位
 * @returns {number} 差值
 */
export const dateDiff = (date1, date2, unit = 'day') => {
  return dayjs(date1).diff(dayjs(date2), unit)
}

/**
 * 判断是否为今天
 * @param {string|Date} date 日期
 * @returns {boolean} 是否为今天
 */
export const isToday = (date) => {
  return dayjs(date).isSame(dayjs(), 'day')
}

/**
 * 判断是否为本周
 * @param {string|Date} date 日期
 * @returns {boolean} 是否为本周
 */
export const isThisWeek = (date) => {
  return dayjs(date).isSame(dayjs(), 'week')
}

/**
 * 判断是否为本月
 * @param {string|Date} date 日期
 * @returns {boolean} 是否为本月
 */
export const isThisMonth = (date) => {
  return dayjs(date).isSame(dayjs(), 'month')
}
