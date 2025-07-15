<template>
  <div class="not-found-page">
    <div class="not-found-container">
      <div class="error-animation">
        <div class="error-number">
          <span class="four">4</span>
          <span class="zero">0</span>
          <span class="four">4</span>
        </div>
        <div class="error-icon">
          <el-icon size="100"><Warning /></el-icon>
        </div>
      </div>
      
      <div class="error-content">
        <h1 class="error-title">页面未找到</h1>
        <p class="error-message">抱歉，您访问的页面不存在或已被移除</p>
        
        <div class="error-actions">
          <el-button type="primary" size="large" @click="goHome">
            <el-icon><House /></el-icon>
            返回首页
          </el-button>
          <el-button size="large" @click="goBack">
            <el-icon><Back /></el-icon>
            返回上页
          </el-button>
        </div>
        
        <div class="helpful-links">
          <h3>您可能想要:</h3>
          <div class="links-grid">
            <router-link to="/" class="help-link">
              <el-icon><House /></el-icon>
              <span>网站首页</span>
            </router-link>
            <router-link to="/archive" class="help-link">
              <el-icon><Calendar /></el-icon>
              <span>文章归档</span>
            </router-link>
            <router-link to="/categories" class="help-link">
              <el-icon><Folder /></el-icon>
              <span>文章分类</span>
            </router-link>
            <router-link to="/tags" class="help-link">
              <el-icon><CollectionTag /></el-icon>
              <span>标签云</span>
            </router-link>
            <router-link to="/about" class="help-link">
              <el-icon><User /></el-icon>
              <span>关于我</span>
            </router-link>
            <router-link to="/search" class="help-link">
              <el-icon><Search /></el-icon>
              <span>搜索文章</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>
    
    <div class="floating-elements">
      <div class="floating-shape shape-1"></div>
      <div class="floating-shape shape-2"></div>
      <div class="floating-shape shape-3"></div>
      <div class="floating-shape shape-4"></div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { 
  Warning, House, Back, Calendar, Folder, 
  CollectionTag, User, Search 
} from '@element-plus/icons-vue'

const router = useRouter()

// 返回首页
const goHome = () => {
  router.push('/')
}

// 返回上一页
const goBack = () => {
  if (window.history.length > 1) {
    router.go(-1)
  } else {
    router.push('/')
  }
}
</script>

<style scoped>
.not-found-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.not-found-container {
  max-width: 800px;
  padding: 40px;
  text-align: center;
  position: relative;
  z-index: 10;
}

.error-animation {
  margin-bottom: 40px;
  position: relative;
}

.error-number {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
}

.error-number span {
  font-size: 8rem;
  font-weight: 900;
  color: white;
  text-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  animation: bounce 2s infinite;
}

.four:first-child {
  animation-delay: 0s;
}

.zero {
  animation-delay: 0.2s;
  color: #f39c12;
}

.four:last-child {
  animation-delay: 0.4s;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-20px);
  }
  60% {
    transform: translateY(-10px);
  }
}

.error-icon {
  color: white;
  opacity: 0.8;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.8;
  }
  50% {
    transform: scale(1.1);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 0.8;
  }
}

.error-content {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.error-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 15px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.error-message {
  font-size: 1.2rem;
  color: #7f8c8d;
  margin-bottom: 40px;
  line-height: 1.6;
}

.error-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-bottom: 50px;
  flex-wrap: wrap;
}

.helpful-links h3 {
  font-size: 1.5rem;
  color: #2c3e50;
  margin-bottom: 25px;
}

.links-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
  max-width: 600px;
  margin: 0 auto;
}

.help-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 20px 15px;
  background: #f8f9fa;
  border-radius: 10px;
  text-decoration: none;
  color: #2c3e50;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.help-link:hover {
  background: #3498db;
  color: white;
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(52, 152, 219, 0.3);
}

.help-link span {
  font-size: 0.9rem;
  font-weight: 500;
}

.floating-elements {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.floating-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 60px;
  height: 60px;
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 80px;
  height: 80px;
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.shape-3 {
  width: 40px;
  height: 40px;
  bottom: 30%;
  left: 20%;
  animation-delay: 4s;
}

.shape-4 {
  width: 100px;
  height: 100px;
  top: 10%;
  right: 30%;
  animation-delay: 1s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .not-found-container {
    padding: 20px;
  }
  
  .error-number span {
    font-size: 6rem;
    gap: 10px;
  }
  
  .error-title {
    font-size: 2rem;
  }
  
  .error-message {
    font-size: 1rem;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .links-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }
  
  .help-link {
    padding: 15px 10px;
  }
}

@media (max-width: 480px) {
  .error-number {
    gap: 10px;
  }
  
  .error-number span {
    font-size: 4rem;
  }
  
  .error-content {
    padding: 30px 20px;
  }
  
  .error-title {
    font-size: 1.8rem;
  }
  
  .links-grid {
    grid-template-columns: 1fr;
  }
  
  .helpful-links h3 {
    font-size: 1.3rem;
  }
}

/* 暗色主题适配 */
@media (prefers-color-scheme: dark) {
  .error-content {
    background: rgba(44, 62, 80, 0.95);
  }
  
  .error-title {
    color: #ecf0f1;
  }
  
  .error-message {
    color: #bdc3c7;
  }
  
  .helpful-links h3 {
    color: #ecf0f1;
  }
  
  .help-link {
    background: #34495e;
    color: #ecf0f1;
  }
  
  .help-link:hover {
    background: #3498db;
    color: white;
  }
}
</style>
