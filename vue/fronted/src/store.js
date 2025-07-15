// src/store.js
import { createStore } from 'vuex'
import { userApi, adminApi } from './utils/api'

export default createStore({
  state: {
    user: null,
    token: localStorage.getItem('token') || null,
    userType: localStorage.getItem('userType') || null, // 'admin' 或 'user'
    loading: false
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        localStorage.setItem('token', token)
      } else {
        localStorage.removeItem('token')
        localStorage.removeItem('userType')
      }
    },
    SET_USER(state, user) {
      state.user = user
    },
    SET_USER_TYPE(state, userType) {
      state.userType = userType
      if (userType) {
        localStorage.setItem('userType', userType)
      } else {
        localStorage.removeItem('userType')
      }
    },
    SET_LOADING(state, loading) {
      state.loading = loading
    }
  },
  actions: {
    // 用户登录
    async login({ commit }, credentials) {
      try {
        commit('SET_LOADING', true)
        const response = await userApi.login(credentials)

        if (response.code === 200) {
          commit('SET_TOKEN', response.data.token)
          commit('SET_USER', response.data.user)
          commit('SET_USER_TYPE', 'user')
          return response
        }
        return response
      } catch (error) {
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },

    // 管理员登录
    async adminLogin({ commit }, credentials) {
      try {
        commit('SET_LOADING', true)
        const response = await adminApi.login(credentials)

        if (response.code === 200) {
          commit('SET_TOKEN', response.data.token)
          commit('SET_USER', response.data.admin)
          commit('SET_USER_TYPE', 'admin')
          return response
        }
        return response
      } catch (error) {
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },

    // 退出登录
    async logout({ commit }) {
      try {
        // 可以在这里调用后端登出API
        commit('SET_TOKEN', null)
        commit('SET_USER', null)
        commit('SET_USER_TYPE', null)
      } catch (error) {
        // 即使后端调用失败，也要清除本地状态
        commit('SET_TOKEN', null)
        commit('SET_USER', null)
        commit('SET_USER_TYPE', null)
      }
    },

    // 检查认证状态
    async checkAuth({ commit, state }) {
      if (!state.token) {
        throw new Error('No token')
      }

      try {
        // 这里可以调用后端验证token的API
        // const response = await userApi.checkAuth()
        // if (response.code !== 200) {
        //   throw new Error('Token invalid')
        // }
        return true
      } catch (error) {
        commit('SET_TOKEN', null)
        commit('SET_USER', null)
        commit('SET_USER_TYPE', null)
        throw error
      }
    }
  },
  getters: {
    isAuthenticated: state => !!state.token,
    currentUser: state => state.user,
    userType: state => state.userType,
    isAdmin: state => state.userType === 'admin',
    isLoading: state => state.loading
  }
})
