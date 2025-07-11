<template>
  <div class="blog-home-bg">
    <!-- 粒子背景（可选 vue-particles 或 canvas） -->
    <div id="particles-bg"></div>

    <!-- 顶部导航 -->
    <header class="blog-header">
      <div class="logo">RE-BIN</div>
      <nav class="nav-menu">
        <a href="#">首页</a>
        <a href="#">归档</a>
        <a href="#">其他</a>
        <a href="#">友链</a>
        <a href="#">音乐</a>
        <a href="#">相册</a>
      </nav>
      <div class="header-actions">
        <el-switch v-model="isDark" active-icon="Moon" inactive-icon="Sunny" />
        <el-input class="search-input" placeholder="搜索" prefix-icon="Search" />
        <el-button class="login-btn" @click="$router.push('/user/login')">登录</el-button>
      </div>
    </header>

    <!-- Banner区 -->
    <section class="blog-banner">
      <h1 class="main-title">RE-BIN</h1>
      <div class="subtitle-bar">
        <span>无聊的并不是时间，而是平庸无奇的我。</span>
      </div>
      <div class="down-arrow" @click="scrollToContent">
        <el-icon><ArrowDown /></el-icon>
      </div>
      <div class="banner-avatars">
        <img src="@/assets/ka.png" alt="avatars" />
      </div>
    </section>

    <!-- 主内容区 -->
    <section class="blog-main">
      <div class="main-left">
        <!-- 推荐轮播 -->
        <div class="recommend-card">
          <el-carousel height="180px">
            <el-carousel-item v-for="item in recommends" :key="item.id">
              <img :src="item.cover" class="carousel-img" />
              <div class="carousel-title">{{ item.title }}</div>
            </el-carousel-item>
          </el-carousel>
        </div>
        <!-- 文章列表 -->
        <div class="article-list">
          <div class="article-card" v-for="item in articles" :key="item.id">
            <img :src="item.cover" class="article-cover" />
            <div class="article-info">
              <h3>{{ item.title }}</h3>
              <div class="article-meta">
                <span>👁 {{ item.views }}</span>
                <span>💬 {{ item.comments }}</span>
                <span>🕒 {{ item.date }}</span>
              </div>
              <p class="article-desc">{{ item.desc }}</p>
              <div class="article-tags">
                <el-tag v-for="tag in item.tags" :key="tag">{{ tag }}</el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="main-right">
        <!-- 个人信息卡片 -->
        <div class="user-card">
          <img class="user-avatar" src="@/assets/default-avatar.png" />
          <div class="user-name">Ruyu</div>
          <div class="user-desc">生活想要活捉了我，不料我是颗种子</div>
          <div class="user-stats">
            <span>11 文章数</span>
            <span>4 分类数</span>
            <span>91 评论数</span>
          </div>
        </div>
        <!-- 公告卡片 -->
        <div class="notice-card">
          <div class="notice-title">公告</div>
          <div class="notice-content">
            本项目github & gitee开源地址：<br>
        <br>
            ...
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ArrowDown, Search, Moon, Sunny } from '@element-plus/icons-vue'
const isDark = ref(false)
const recommends = ref([
  { id: 1, title: '项目部署文档', cover: 'https://image.kuailemao.xyz/blog/article/cover1.jpg' },
  { id: 2, title: '部分问题记录与解决', cover: 'https://image.kuailemao.xyz/blog/article/cover2.jpg' }
])
const articles = ref([
  { id: 1, title: '部分问题记录与解决', cover: 'https://image.kuailemao.xyz/blog/article/cover2.jpg', views: 435, comments: 4, date: '2025-04-01', desc: '长内容公告保存失败公告长度限制是1000!...', tags: ['博客'] },
  { id: 2, title: '项目Minio添加防盗链功能', cover: 'https://image.kuailemao.xyz/blog/article/cover3.jpg', views: 401, comments: 2, date: '2025-03-28', desc: '修复Minio直链外链和各种功能防盗链...', tags: ['技术'] }
])
function scrollToContent() {
  document.querySelector('.blog-main').scrollIntoView({ behavior: 'smooth' })
}
// 粒子背景可用第三方库或canvas实现，这里预留id
onMounted(() => {
  // 可在此初始化粒子动效
})
</script>

<style scoped>
.blog-home-bg { min-height: 100vh; background: #23272e; position: relative; }
#particles-bg { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 0; pointer-events: none; }
.blog-header { width: 100%; height: 64px; display: flex; align-items: center; justify-content: space-between; position: fixed; top: 0; left: 0; z-index: 10; background: rgba(34,39,46,0.95); box-shadow: 0 2px 8px 0 rgba(0,0,0,0.08); padding: 0 48px; }
.logo { font-size: 2rem; font-weight: bold; color: #fff; letter-spacing: 2px; }
.nav-menu { display: flex; gap: 32px; align-items: center; }
.nav-menu a { color: #fff; font-size: 1.1rem; text-decoration: none; padding: 6px 16px; border-radius: 8px; transition: background 0.2s, color 0.2s; }
.nav-menu a:hover, .nav-login { background: #ff7e7e; color: #fff; }
.header-actions { display: flex; align-items: center; gap: 18px; }
.search-input { width: 180px; border-radius: 10px; background: #23272e; border: none; color: #fff; }
.login-btn { background: #ff7e7e; color: #fff; border-radius: 8px; font-weight: 600; border: none; padding: 6px 18px; }
.blog-banner { width: 100%; height: 420px; display: flex; flex-direction: column; align-items: center; justify-content: center; margin-top: 64px; position: relative; background: transparent; z-index: 1; }
.main-title { font-size: 3.2rem; color: #fff; font-weight: bold; text-shadow: 0 4px 24px #000, 0 1px 0 #fff; margin-bottom: 18px; }
.subtitle-bar { background: linear-gradient(90deg, #ff7e7e 0%, #6dd5fa 100%); color: #fff; font-size: 1.2rem; padding: 8px 32px; border-radius: 16px; margin-bottom: 24px; box-shadow: 0 2px 12px 0 rgba(255,126,126,0.12); }
.down-arrow { font-size: 2.2rem; color: #fff; margin-bottom: 12px; cursor: pointer; animation: bounce 1.5s infinite; }
@keyframes bounce { 0%, 100% { transform: translateY(0); } 50% { transform: translateY(16px); } }
.banner-avatars { position: absolute; left: 50%; bottom: -32px; transform: translateX(-50%); z-index: 2; }
.banner-avatars img { height: 80px; filter: drop-shadow(0 4px 16px rgba(0,0,0,0.18)); }
.blog-main { display: flex; max-width: 1200px; margin: 0 auto; gap: 32px; padding-top: 32px; z-index: 2; position: relative; }
.main-left { flex: 2; display: flex; flex-direction: column; gap: 24px; }
.main-right { flex: 1; display: flex; flex-direction: column; gap: 24px; }
.recommend-card { background: #23272e; border-radius: 18px; box-shadow: 0 4px 24px 0 rgba(255,126,126,0.08); overflow: hidden; margin-bottom: 12px; }
.carousel-img { width: 100%; height: 180px; object-fit: cover; border-radius: 18px; }
.carousel-title { position: absolute; left: 24px; bottom: 18px; color: #fff; font-size: 1.3rem; font-weight: bold; text-shadow: 0 2px 8px #000; }
.article-list { display: flex; flex-direction: column; gap: 18px; }
.article-card { background: #23272e; border-radius: 18px; box-shadow: 0 4px 24px 0 rgba(255,126,126,0.08); overflow: hidden; display: flex; gap: 18px; padding: 18px; align-items: flex-start; }
.article-cover { width: 120px; height: 90px; object-fit: cover; border-radius: 12px; }
.article-info { flex: 1; }
.article-info h3 { font-size: 1.2rem; color: #ff7e7e; margin-bottom: 8px; font-weight: bold; }
.article-meta { font-size: 0.95rem; color: #aaa; display: flex; gap: 18px; margin-bottom: 6px; }
.article-desc { color: #ccc; font-size: 1rem; margin-bottom: 8px; }
.article-tags { display: flex; gap: 8px; }
.user-card { background: #23272e; border-radius: 18px; box-shadow: 0 4px 24px 0 rgba(255,126,126,0.08); padding: 24px 18px; display: flex; flex-direction: column; align-items: center; }
.user-avatar { width: 72px; height: 72px; border-radius: 50%; margin-bottom: 12px; }
.user-name { font-size: 1.2rem; color: #fff; font-weight: bold; margin-bottom: 6px; }
.user-desc { color: #aaa; font-size: 1rem; margin-bottom: 12px; text-align: center; }
.user-stats { display: flex; gap: 12px; color: #ffd04b; font-size: 0.98rem; margin-bottom: 8px; }
.notice-card { background: #23272e; border-radius: 18px; box-shadow: 0 4px 24px 0 rgba(255,126,126,0.08); padding: 18px 16px; color: #fff; }
.notice-title { font-size: 1.1rem; font-weight: bold; margin-bottom: 8px; }
.notice-content { color: #ccc; font-size: 0.98rem; }
@media (max-width: 900px) {
  .blog-main { flex-direction: column; padding: 0 8px; }
  .main-left, .main-right { width: 100%; }
}
</style> 