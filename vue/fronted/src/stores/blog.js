import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { blogApi } from '@/utils/api.js'
import { API_CONFIG } from '@/config/index.js'

export const useBlogStore = defineStore('blog', () => {
  // 状态
  const posts = ref([])
  const currentPost = ref(null)
  const categories = ref([])
  const tags = ref([])
  const hotPosts = ref([])
  const archives = ref([])
  const statistics = ref({})
  const loading = ref(false)
  const pagination = ref({
    page: 1,
    size: 10,
    total: 0,
    totalPages: 0
  })
  
  // 计算属性
  const publishedPosts = computed(() => 
    posts.value.filter(post => post.status === 1)
  )
  
  const draftPosts = computed(() => 
    posts.value.filter(post => post.status === 0)
  )
  
  // 动作
  const fetchPosts = async (params = {}) => {
    loading.value = true
    try {
      const response = await blogApi.getPosts({
        page: pagination.value.page,
        size: pagination.value.size,
        ...params
      })

      // 根据后端BlogService的返回结构处理数据
      posts.value = (response.data.posts || []).map(post => {
        // 处理封面图片URL
        if (post.cover && !post.cover.startsWith('http')) {
          const originalCover = post.cover
          post.cover = `${API_CONFIG.BASE_URL}${post.cover}`
          console.log('BlogStore图片URL处理:', {
            postId: post.id,
            title: post.title,
            original: originalCover,
            processed: post.cover
          })
        }
        return post
      })
      pagination.value = {
        page: response.data.page || 1,
        size: response.data.size || 10,
        total: response.data.total || 0,
        totalPages: response.data.totalPages || 0
      }
    } catch (error) {
      console.error('获取文章列表失败:', error)
      // 设置空数据避免界面错误
      posts.value = []
      pagination.value = {
        page: 1,
        size: 10,
        total: 0,
        totalPages: 0
      }
    } finally {
      loading.value = false
    }
  }
  
  const fetchPostDetail = async (id) => {
    loading.value = true
    try {
      const response = await blogApi.getPostDetail(id)
      currentPost.value = response.data
      return response.data
    } catch (error) {
      console.error('获取文章详情失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const createPost = async (postData) => {
    try {
      const response = await blogApi.createPost(postData)
      posts.value.unshift(response.data)
      return response.data
    } catch (error) {
      throw error
    }
  }
  
  const updatePost = async (id, postData) => {
    try {
      const response = await blogApi.updatePost(id, postData)
      const index = posts.value.findIndex(post => post.id === id)
      if (index !== -1) {
        posts.value[index] = response.data
      }
      if (currentPost.value?.id === id) {
        currentPost.value = response.data
      }
      return response.data
    } catch (error) {
      throw error
    }
  }
  
  const deletePost = async (id) => {
    try {
      await blogApi.deletePost(id)
      posts.value = posts.value.filter(post => post.id !== id)
      if (currentPost.value?.id === id) {
        currentPost.value = null
      }
    } catch (error) {
      throw error
    }
  }
  
  const searchPosts = async (keyword, params = {}) => {
    loading.value = true
    try {
      const response = await blogApi.searchPosts(keyword, {
        page: pagination.value.page,
        size: pagination.value.size,
        ...params
      })
      
      posts.value = response.data.posts || response.data.records || response.data
      if (response.data.pagination) {
        pagination.value = response.data.pagination
      }
      return response.data
    } catch (error) {
      console.error('搜索文章失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const fetchCategories = async () => {
    try {
      // 使用BlogController的API获取分类（包含文章数量）
      const response = await blogApi.getCategories()
      categories.value = response.data || []
      return response.data
    } catch (error) {
      console.error('获取分类失败:', error)
      categories.value = []
    }
  }

  const fetchTags = async () => {
    try {
      // 使用BlogController的API获取标签（包含文章数量）
      const response = await blogApi.getTags()
      tags.value = response.data || []
      return response.data
    } catch (error) {
      console.error('获取标签失败:', error)
      tags.value = []
    }
  }
  
  const fetchHotPosts = async (limit = 5) => {
    try {
      const response = await blogApi.getHotPosts(limit)
      hotPosts.value = response.data
      return response.data
    } catch (error) {
      console.error('获取热门文章失败:', error)
    }
  }
  
  const fetchArchives = async () => {
    try {
      const response = await blogApi.getArchives()
      archives.value = response.data
      return response.data
    } catch (error) {
      console.error('获取归档失败:', error)
    }
  }
  
  const fetchStatistics = async () => {
    try {
      const response = await blogApi.getStatistics()
      statistics.value = response.data
      return response.data
    } catch (error) {
      console.error('获取统计数据失败:', error)
    }
  }
  
  const togglePostLike = async (postId) => {
    try {
      const response = await blogApi.togglePostLike(postId)
      
      // 更新当前文章的点赞状态
      if (currentPost.value?.id === postId) {
        currentPost.value.isLiked = response.data.isLiked
        currentPost.value.likeCount = response.data.likeCount
      }
      
      // 更新文章列表中的点赞状态
      const postIndex = posts.value.findIndex(post => post.id === postId)
      if (postIndex !== -1) {
        posts.value[postIndex].isLiked = response.data.isLiked
        posts.value[postIndex].likeCount = response.data.likeCount
      }
      
      return response.data
    } catch (error) {
      throw error
    }
  }
  
  const fetchHomeData = async () => {
    try {
      const response = await blogApi.getHomeData()
      const { posts: homePosts, categories: homeCategories, tags: homeTags, hotPosts: homeHotPosts } = response.data
      
      if (homePosts) posts.value = homePosts
      if (homeCategories) categories.value = homeCategories
      if (homeTags) tags.value = homeTags
      if (homeHotPosts) hotPosts.value = homeHotPosts
      
      return response.data
    } catch (error) {
      console.error('获取首页数据失败:', error)
    }
  }
  
  return {
    posts,
    currentPost,
    categories,
    tags,
    hotPosts,
    archives,
    statistics,
    loading,
    pagination,
    publishedPosts,
    draftPosts,
    fetchPosts,
    fetchPostDetail,
    createPost,
    updatePost,
    deletePost,
    searchPosts,
    fetchCategories,
    fetchTags,
    fetchHotPosts,
    fetchArchives,
    fetchStatistics,
    togglePostLike,
    fetchHomeData
  }
})
