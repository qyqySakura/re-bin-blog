<template>
  <div class="comment-item">
    <div class="comment-main">
      <!-- 头像 -->
      <div class="comment-avatar">
        <el-avatar :size="40" :src="comment.user?.avatar">
          {{ comment.user?.name?.charAt(0) || '匿' }}
        </el-avatar>
      </div>
      
      <!-- 内容区域 -->
      <div class="comment-content">
        <!-- 用户信息 -->
        <div class="comment-header">
          <span class="comment-author">{{ comment.user?.name || '匿名用户' }}</span>
          <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
          <span class="comment-floor" v-if="comment.floor">#{{ comment.floor }}</span>
        </div>
        
        <!-- 回复信息 -->
        <div class="reply-to" v-if="comment.parentUser">
          回复 <span class="reply-user">@{{ comment.parentUser.name }}</span>
        </div>
        
        <!-- 评论内容 -->
        <div class="comment-text" v-html="formatContent(comment.content)"></div>
        
        <!-- 操作按钮 -->
        <div class="comment-actions">
          <el-button 
            text 
            size="small" 
            :icon="Star"
            @click="handleLike"
            :loading="liking"
            :class="{ 'is-liked': comment.isLiked }"
          >
            {{ comment.likeCount || 0 }}
          </el-button>
          
          <el-button 
            text 
            size="small" 
            :icon="ChatDotRound"
            @click="handleReply"
          >
            回复
          </el-button>
          
          <el-button 
            text 
            size="small" 
            type="danger"
            :icon="Delete"
            @click="handleDelete"
            v-if="canDelete"
          >
            删除
          </el-button>
        </div>
        
        <!-- 回复框 -->
        <div class="reply-form" v-if="showReplyForm">
          <el-input
            v-model="replyContent"
            type="textarea"
            :rows="3"
            :placeholder="`回复 @${comment.user?.name}:`"
            maxlength="500"
            show-word-limit
          />
          <div class="reply-actions">
            <el-button size="small" @click="cancelReply">取消</el-button>
            <el-button 
              type="primary" 
              size="small" 
              @click="submitReply"
              :loading="replying"
              :disabled="!replyContent.trim()"
            >
              发表回复
            </el-button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 子评论 -->
    <div class="comment-children" v-if="comment.children && comment.children.length">
      <CommentItem
        v-for="child in comment.children"
        :key="child.id"
        :comment="child"
        :post-id="postId"
        @reply="$emit('reply', $event)"
        @delete="$emit('delete', $event)"
        @like="$emit('like', $event)"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { formatDate, fromNow } from '@/utils/date'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Star, ChatDotRound, Delete } from '@element-plus/icons-vue'

const props = defineProps({
  comment: {
    type: Object,
    required: true
  },
  postId: {
    type: [String, Number],
    required: true
  }
})

const emit = defineEmits(['reply', 'delete', 'like'])

const userStore = useUserStore()

const showReplyForm = ref(false)
const replyContent = ref('')
const replying = ref(false)
const liking = ref(false)

const canDelete = computed(() => {
  return userStore.isLoggedIn && (
    userStore.userInfo?.id === props.comment.user?.id ||
    userStore.userInfo?.role === 'admin'
  )
})

const formatContent = (content) => {
  if (!content) return ''
  
  // 简单的文本格式化，防止XSS
  return content
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/\n/g, '<br>')
    .replace(/@(\w+)/g, '<span class="mention">@$1</span>')
}

const handleLike = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  
  liking.value = true
  try {
    emit('like', {
      commentId: props.comment.id,
      isLiked: !props.comment.isLiked
    })
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    liking.value = false
  }
}

const handleReply = () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  
  showReplyForm.value = !showReplyForm.value
  if (showReplyForm.value) {
    replyContent.value = ''
  }
}

const submitReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  
  replying.value = true
  try {
    emit('reply', {
      postId: props.postId,
      parentId: props.comment.id,
      content: replyContent.value.trim(),
      parentUser: props.comment.user
    })
    
    replyContent.value = ''
    showReplyForm.value = false
    ElMessage.success('回复成功')
  } catch (error) {
    ElMessage.error('回复失败')
  } finally {
    replying.value = false
  }
}

const cancelReply = () => {
  showReplyForm.value = false
  replyContent.value = ''
}

const handleDelete = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条评论吗？',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    emit('delete', props.comment.id)
  } catch (error) {
    // 用户取消删除
  }
}
</script>

<style scoped>
.comment-item {
  margin-bottom: 20px;
}

.comment-main {
  display: flex;
  gap: 12px;
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
  min-width: 0;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
  font-size: 13px;
}

.comment-author {
  font-weight: 600;
  color: #333;
}

.comment-time {
  color: #999;
}

.comment-floor {
  color: #409eff;
  font-size: 12px;
}

.reply-to {
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;
}

.reply-user {
  color: #409eff;
  font-weight: 500;
}

.comment-text {
  line-height: 1.6;
  color: #333;
  margin-bottom: 12px;
  word-break: break-word;
}

.comment-text :deep(.mention) {
  color: #409eff;
  font-weight: 500;
}

.comment-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.comment-actions .el-button {
  padding: 4px 8px;
  font-size: 12px;
  color: #999;
}

.comment-actions .el-button:hover {
  color: #409eff;
}

.comment-actions .el-button.is-liked {
  color: #f56c6c;
}

.reply-form {
  margin-top: 12px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 6px;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 8px;
}

.comment-children {
  margin-left: 52px;
  margin-top: 16px;
  padding-left: 16px;
  border-left: 2px solid #f0f0f0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .comment-main {
    gap: 8px;
  }
  
  .comment-avatar :deep(.el-avatar) {
    width: 32px;
    height: 32px;
  }
  
  .comment-children {
    margin-left: 40px;
    padding-left: 12px;
  }
  
  .comment-header {
    flex-wrap: wrap;
    gap: 8px;
  }
  
  .reply-form {
    padding: 8px;
  }
}

@media (max-width: 480px) {
  .comment-actions {
    flex-wrap: wrap;
  }
  
  .reply-actions {
    flex-direction: column;
  }
  
  .reply-actions .el-button {
    width: 100%;
  }
}
</style>
