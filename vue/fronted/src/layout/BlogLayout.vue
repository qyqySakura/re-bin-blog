<template>
  <div class="blog-layout">
    <header class="blog-header">
      <div class="logo">FantBlog</div>
      <nav class="nav-menu">
        <router-link to="/">首页</router-link>
        <router-link to="/articles">文章</router-link>
        <router-link to="/about">关于</router-link>
        <router-link to="/user/login" class="nav-login">登录</router-link>
      </nav>
    </header>
    <main>
      <router-view />
    </main>
    <footer class="blog-footer">
      <div>© 2024 FantBlog | 备案号：xxxxxx</div>
    </footer>
  </div>
</template>
<script setup>
</script>
<style scoped>
.blog-layout { min-height: 100vh; background: linear-gradient(135deg, #f8fafc 0%, #e0e7ef 100%); font-family: 'HarmonyOS Sans', 'PingFang SC', 'Microsoft YaHei', sans-serif; }
.blog-header { width: 100%; height: 64px; display: flex; align-items: center; justify-content: space-between; position: fixed; top: 0; left: 0; z-index: 10; background: rgba(255,255,255,0.85); box-shadow: 0 2px 8px 0 rgba(0,0,0,0.03); padding: 0 48px; }
.logo { font-size: 2rem; font-weight: bold; color: #ff7e7e; letter-spacing: 2px; }
.nav-menu { display: flex; gap: 32px; align-items: center; }
.nav-menu a { color: #333; font-size: 1.1rem; text-decoration: none; padding: 6px 16px; border-radius: 8px; transition: background 0.2s, color 0.2s; }
.nav-menu a:hover, .nav-login { background: #ff7e7e; color: #fff; }
main { padding-top: 80px; min-height: 70vh; }
.blog-footer { width: 100%; text-align: center; color: #aaa; font-size: 1rem; padding: 32px 0 18px 0; margin-top: 48px; }
</style> 