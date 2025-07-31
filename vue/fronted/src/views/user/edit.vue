<template>
  <el-dialog v-model="dialogVisible" title="用户信息" :show-close="false">
    <el-form :model="form" label-width="120px">
      <el-form-item label="头像">
        <el-image style="width: 40px; height: 40px; border-radius: 50%; display: block" v-if="form.avatar"
                  :src="getAvatarUrl(form.avatar)" :preview-src-list="[getAvatarUrl(form.avatar)]" preview-teleported></el-image>
        <el-avatar v-else :size="40">
          {{ form.name?.charAt(0) || form.username?.charAt(0) || '用' }}
        </el-avatar>
      </el-form-item>

      <el-form-item label="姓名">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" show-password />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-button @click="submitForm">提交</el-button>
    </el-form>
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref } from 'vue'
import { getAvatarUrl } from '@/utils/avatar'

const props = defineProps(['formData'])
const emit = defineEmits(['submit'])

const form = ref(props.formData || {})
const dialogVisible = ref(true)

function submitForm() {
  emit('submit', form.value)
}
</script>
