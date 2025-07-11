<template>
  <router-view />
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessageBox, ElMessage } from 'element-plus'
const router = useRouter()
const route = useRoute()
const store = useStore()

const currentUser = computed(() => store.getters.currentUser)
const activeMenu = computed(() => route.path)

const lastOrders = [
  { id: 1, name: 'XYP Sofa...' },
  { id: 2, name: 'CZ Vase...' },
  { id: 3, name: 'Angel sto...' },
  { id: 4, name: 'Betty sto...' }
]

function logout() {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    store.dispatch('logout')
    router.push('/login')
    ElMessage.success('已退出登录')
  })
}
</script>

<style scoped>
.sidebar-menu .el-menu-item {
  border-radius: 12px;
  margin: 0 12px 8px 12px;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  transition: background 0.2s;
}
.sidebar-menu .el-menu-item.is-active {
  background: #222;
  color: #fff !important;
}
</style>
