// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router/router.js'  // 确认路径正确
import store from './store.js'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)
   .use(store)
   .use(ElementPlus)
   .mount('#app')
