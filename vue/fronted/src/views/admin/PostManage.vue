<template>
  <div class="post-manage">
    <div class="page-header">
      <h2>文章管理</h2>
      <el-button type="primary" @click="showAddDialog">
        <el-icon><Plus /></el-icon>
        新增文章
      </el-button>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入文章标题" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="0" />
            <el-option label="已发布" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchPosts">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 文章列表 -->
    <el-table :data="posts" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="author.name" label="作者" width="120" />
      <el-table-column prop="category.name" label="分类" width="120" />
      <el-table-column label="标签" width="150">
        <template #default="{ row }">
          <el-tag
            v-for="tag in row.tags"
            :key="tag.id"
            size="small"
            style="margin-right: 5px;"
          >
            {{ tag.name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'warning'">
            {{ row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="viewPost(row.id)">查看</el-button>
          <el-button size="small" type="primary" @click="editPost(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deletePost(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑文章' : '新增文章'"
      width="80%"
      :before-close="handleClose"
    >
      <el-form :model="postForm" :rules="rules" ref="postFormRef" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标题" prop="title">
              <el-input v-model="postForm.title" placeholder="请输入文章标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="postForm.categoryId" placeholder="请选择分类">
                <el-option
                  v-for="category in categories"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="标签">
          <el-select v-model="postForm.tagIds" multiple placeholder="请选择标签">
            <el-option
              v-for="tag in tags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="摘要">
          <el-input
            v-model="postForm.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入文章摘要"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input
            v-model="postForm.content"
            type="textarea"
            :rows="10"
            placeholder="请输入文章内容"
          />
        </el-form-item>

        <el-form-item label="状态">
          <el-radio-group v-model="postForm.status">
            <el-radio :label="0">草稿</el-radio>
            <el-radio :label="1">发布</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitPost" :loading="submitting">
            {{ isEdit ? '更新' : '创建' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { postApi, categoryApi, tagApi } from '@/utils/api'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const posts = ref([])
const categories = ref([])
const tags = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索表单
const searchForm = reactive({
  title: '',
  categoryId: null,
  status: null
})

// 文章表单
const postForm = reactive({
  id: null,
  title: '',
  content: '',
  summary: '',
  categoryId: null,
  tagIds: [],
  status: 1,
  userId: null
})

// 表单引用
const postFormRef = ref()

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入文章内容', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

// 获取文章列表
const fetchPosts = async () => {
  try {
    loading.value = true
    const response = await postApi.getPosts()
    if (response.code === 200) {
      posts.value = response.data
      total.value = response.data.length
    }
  } catch (error) {
    ElMessage.error('获取文章列表失败')
  } finally {
    loading.value = false
  }
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    const response = await categoryApi.getCategories()
    if (response.code === 200) {
      categories.value = response.data
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

// 获取标签列表
const fetchTags = async () => {
  try {
    const response = await tagApi.getTags()
    if (response.code === 200) {
      tags.value = response.data
    }
  } catch (error) {
    console.error('获取标签列表失败:', error)
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// 显示新增对话框
const showAddDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑文章
const editPost = (post) => {
  isEdit.value = true
  Object.assign(postForm, {
    id: post.id,
    title: post.title,
    content: post.content,
    summary: post.summary,
    categoryId: post.categoryId,
    tagIds: post.tags ? post.tags.map(tag => tag.id) : [],
    status: post.status,
    userId: post.userId
  })
  dialogVisible.value = true
}

// 查看文章
const viewPost = (postId) => {
  router.push(`/post/${postId}`)
}

// 删除文章
const deletePost = async (post) => {
  try {
    await ElMessageBox.confirm(`确定要删除文章"${post.title}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await postApi.deletePost(post.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      fetchPosts()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交文章
const submitPost = async () => {
  try {
    await postFormRef.value.validate()
    submitting.value = true
    
    const response = isEdit.value 
      ? await postApi.updatePost(postForm)
      : await postApi.addPost(postForm)
      
    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      dialogVisible.value = false
      fetchPosts()
    }
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    }
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(postForm, {
    id: null,
    title: '',
    content: '',
    summary: '',
    categoryId: null,
    tagIds: [],
    status: 1,
    userId: null
  })
  postFormRef.value?.clearValidate()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    title: '',
    categoryId: null,
    status: null
  })
  fetchPosts()
}

// 关闭对话框
const handleClose = (done) => {
  resetForm()
  done()
}

// 分页处理
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  fetchPosts()
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  fetchPosts()
}

// 初始化
onMounted(() => {
  fetchPosts()
  fetchCategories()
  fetchTags()
})
</script>

<style scoped>
.post-manage {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-bar {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
