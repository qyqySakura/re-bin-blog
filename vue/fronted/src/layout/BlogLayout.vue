<template>
  <div class="blog-layout">
    <!-- 使用统一的顶部导航栏 -->
    <TopNavbar />

    <!-- 主内容区域 -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 底部 -->
    <footer class="blog-footer">
      <div class="footer-content">
        <div class="footer-info">
          <p>© 2025 RE-BIN 个人博客 | 分享技术心得，记录生活点滴</p>
          <p>邮箱：f2692433610@outlook.com</p>
        </div>
        <div class="footer-links">
          <a href="/about">关于我</a>
          <a href="https://gitee.com/sakurafengbinbin">联系我</a>
          <a href="https://gitee.com/sakurafengbinbin">github主页</a>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import TopNavbar from '@/components/common/TopNavbar.vue'
</script>
<style scoped>
.blog-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f8fafc 0%, #e0e7ef 100%);
  font-family: 'HarmonyOS Sans', 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.main-content {
  flex: 1;
  padding-top: 70px; /* 为固定导航栏留出空间 */
  min-height: calc(100vh - 70px);
}

.blog-footer {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
  color: white;
  padding: 3rem 0 2rem;
  margin-top: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 2rem;
}

.footer-info {
  flex: 1;
}

.footer-info p {
  margin: 0.5rem 0;
  color: #bdc3c7;
  font-size: 14px;
}

.footer-info a {
  color: #3b82f6;
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-info a:hover {
  color: #60a5fa;
}

.footer-links {
  display: flex;
  gap: 2rem;
}

.footer-links a {
  color: #ecf0f1;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: #3b82f6;
}

/* 暗色主题 */
:global(.dark) .blog-layout {
  background: linear-gradient(135deg, #111827 0%, #1f2937 100%);
}

:global(.dark) .footer-info p {
  color: #9ca3af;
}

:global(.dark) .footer-links a {
  color: #d1d5db;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    text-align: center;
  }

  .footer-links {
    justify-content: center;
  }
}
</style>