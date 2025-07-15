<template>
  <div class="about-page">
    <div class="page-header">
      <h1 class="page-title">关于我</h1>
      <p class="page-subtitle">了解更多关于我的信息</p>
    </div>

    <div class="about-container">
      <div class="about-content">
        <!-- 个人介绍 -->
        <section class="intro-section">
          <div class="intro-card">
            <div class="avatar-section">
              <img src="../../assets/default-avatar.png" alt="头像" class="avatar" />
              <div class="avatar-info">
                <h2>RE-BIN</h2>
                <p class="title">持续学习的阿彬</p>
                <div class="social-links">
                  <a href="#" class="social-link github">
                    <el-icon><svg viewBox="0 0 1024 1024"><path d="M512 12.64c-282.752 0-512 229.216-512 512 0 226.208 146.688 418.144 350.08 485.824 25.6 4.736 35.008-11.104 35.008-24.64 0-12.192-0.48-52.544-0.704-95.328-142.464 30.976-172.512-60.416-172.512-60.416-23.296-59.168-56.832-74.912-56.832-74.912-46.464-31.776 3.52-31.136 3.52-31.136 51.392 3.616 78.464 52.768 78.464 52.768 45.664 78.272 119.776 55.648 148.992 42.56 4.576-33.088 17.856-55.68 32.512-68.48-113.728-12.928-233.216-56.864-233.216-253.024 0-55.904 19.936-101.568 52.672-137.44-5.312-12.896-22.848-64.96 4.96-135.488 0 0 42.88-13.76 140.8 52.48 40.832-11.36 84.64-17.024 128.16-17.248 43.488 0.192 87.328 5.888 128.256 17.248 97.728-66.24 140.64-52.48 140.64-52.48 27.872 70.528 10.336 122.592 5.024 135.488 32.832 35.84 52.608 81.536 52.608 137.44 0 196.64-119.776 239.936-233.856 252.64 18.368 15.904 34.72 47.04 34.72 94.816 0 68.512-0.608 123.648-0.608 140.512 0 13.632 9.216 29.6 35.168 24.576C877.472 942.08 1024 750.208 1024 524.64c0-282.784-229.248-512-512-512z"/></svg></el-icon>
                  </a>
                  <a href="#" class="social-link email">
                    <el-icon><Message /></el-icon>
                  </a>
                  <a href="#" class="social-link wechat">
                    <el-icon><ChatDotRound /></el-icon>
                  </a>
                </div>
              </div>
            </div>
            <div class="intro-text">
              <h3>Hello, World! 👋</h3>
              <p>我是一名热爱技术的全栈开发工程师，专注于现代Web开发技术。喜欢探索新技术，分享开发经验，记录学习心得。</p>
              <p>这个博客是我分享技术文章、生活感悟和学习笔记的地方。希望我的文章能对你有所帮助！</p>
            </div>
          </div>
        </section>

        <!-- 技能栈 -->
        <section class="skills-section">
          <h3 class="section-title">技能栈</h3>
          <div class="skills-grid">
            <div class="skill-category">
              <h4>前端技术</h4>
              <div class="skill-tags">
                <el-tag v-for="skill in frontendSkills" :key="skill.name" :type="skill.type">
                  {{ skill.name }}
                </el-tag>
              </div>
            </div>
            <div class="skill-category">
              <h4>后端技术</h4>
              <div class="skill-tags">
                <el-tag v-for="skill in backendSkills" :key="skill.name" :type="skill.type">
                  {{ skill.name }}
                </el-tag>
              </div>
            </div>
            <div class="skill-category">
              <h4>数据库</h4>
              <div class="skill-tags">
                <el-tag v-for="skill in databaseSkills" :key="skill.name" :type="skill.type">
                  {{ skill.name }}
                </el-tag>
              </div>
            </div>
            <div class="skill-category">
              <h4>工具 & 其他</h4>
              <div class="skill-tags">
                <el-tag v-for="skill in toolSkills" :key="skill.name" :type="skill.type">
                  {{ skill.name }}
                </el-tag>
              </div>
            </div>
          </div>
        </section>

        <!-- 项目经历 -->
        <section class="projects-section">
          <h3 class="section-title">项目经历</h3>
          <div class="projects-timeline">
            <div v-for="project in projects" :key="project.id" class="project-item">
              <div class="project-date">{{ project.date }}</div>
              <div class="project-content">
                <h4>{{ project.name }}</h4>
                <p class="project-desc">{{ project.description }}</p>
                <div class="project-tech">
                  <el-tag v-for="tech in project.technologies" :key="tech" size="small">
                    {{ tech }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- 联系方式 -->
        <section class="contact-section">
          <h3 class="section-title">联系我</h3>
          <div class="contact-grid">
            <div class="contact-item">
              <el-icon class="contact-icon"><Message /></el-icon>
              <div class="contact-info">
                <h4>邮箱</h4>
                <p>2692433610@qq.com</p>
              </div>
            </div>
            <div class="contact-item">
              <el-icon class="contact-icon"><ChatDotRound /></el-icon>
              <div class="contact-info">
                <h4>微信</h4>
                <p>fengbinbin6310</p>
              </div>
            </div>
            <div class="contact-item">
              <el-icon class="contact-icon"><Location /></el-icon>
              <div class="contact-info">
                <h4>位置</h4>
                <p>中国 · 湖南</p>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Message, ChatDotRound, Location } from '@element-plus/icons-vue'

// 技能数据
const frontendSkills = ref([
  { name: 'Vue.js', type: 'success' },
  { name: 'React', type: 'primary' },
  { name: 'TypeScript', type: 'info' },
  { name: 'JavaScript', type: 'warning' },
  { name: 'HTML5', type: 'danger' },
  { name: 'CSS3', type: 'success' },
  { name: 'Sass', type: 'primary' }
])

const backendSkills = ref([
  { name: 'Node.js', type: 'success' },
  { name: 'Express', type: 'primary' },
  { name: 'Koa', type: 'info' },
  { name: 'Python', type: 'warning' },
  { name: 'Django', type: 'danger' },
  { name: 'FastAPI', type: 'success' }
])

const databaseSkills = ref([
  { name: 'MySQL', type: 'primary' },
  { name: 'PostgreSQL', type: 'info' },
  { name: 'MongoDB', type: 'success' },
  { name: 'Redis', type: 'danger' }
])

const toolSkills = ref([
  { name: 'Git', type: 'warning' },
  { name: 'Docker', type: 'primary' },
  { name: 'Linux', type: 'info' },
  { name: 'Nginx', type: 'success' },
  { name: 'AWS', type: 'danger' }
])

// 项目数据
const projects = ref([
  {
    id: 1,
    name: 'RE-BIN 个人博客系统',
    description: '基于Vue3和Node.js开发的现代化博客系统，支持文章管理、用户系统、评论功能等。',
    date: '2024',
    technologies: ['Vue3', 'Node.js', 'MySQL', 'Element Plus']
  },
  {
    id: 2,
    name: '企业级后台管理系统',
    description: '为中小企业开发的通用后台管理系统，包含用户管理、权限控制、数据统计等功能。',
    date: '2023',
    technologies: ['React', 'TypeScript', 'Ant Design', 'Express']
  },
  {
    id: 3,
    name: '电商小程序',
    description: '微信小程序电商平台，支持商品展示、购物车、订单管理、支付等完整电商流程。',
    date: '2023',
    technologies: ['微信小程序', 'Python', 'Django', 'MySQL']
  }
])
</script>

<style scoped>
.about-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 80px 0 40px;
}

.page-header {
  text-align: center;
  margin-bottom: 50px;
}

.page-title {
  font-size: 3rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 15px;
  text-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.page-subtitle {
  font-size: 1.2rem;
  color: #7f8c8d;
  margin: 0;
}

.about-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.about-content {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

/* 个人介绍 */
.intro-section {
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
}

.intro-card {
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 40px;
  align-items: center;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  border: 4px solid #3498db;
  margin-bottom: 20px;
}

.avatar-info h2 {
  font-size: 1.8rem;
  color: #2c3e50;
  margin-bottom: 5px;
}

.title {
  color: #7f8c8d;
  margin-bottom: 20px;
}

.social-links {
  display: flex;
  gap: 15px;
}

.social-link {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  text-decoration: none;
  transition: transform 0.3s ease;
}

.social-link:hover {
  transform: scale(1.1);
}

.social-link.github { background: #333; }
.social-link.email { background: #3498db; }
.social-link.wechat { background: #07c160; }

.intro-text h3 {
  font-size: 1.5rem;
  color: #2c3e50;
  margin-bottom: 20px;
}

.intro-text p {
  color: #5a6c7d;
  line-height: 1.8;
  margin-bottom: 15px;
}

/* 技能栈 */
.skills-section,
.projects-section,
.contact-section {
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
}

.section-title {
  font-size: 1.8rem;
  color: #2c3e50;
  margin-bottom: 30px;
  text-align: center;
  position: relative;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 3px;
  background: #3498db;
  border-radius: 2px;
}

.skills-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.skill-category h4 {
  color: #2c3e50;
  margin-bottom: 15px;
  font-size: 1.2rem;
}

.skill-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

/* 项目经历 */
.projects-timeline {
  position: relative;
  padding-left: 30px;
}

.projects-timeline::before {
  content: '';
  position: absolute;
  left: 15px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: #3498db;
}

.project-item {
  position: relative;
  margin-bottom: 30px;
  display: grid;
  grid-template-columns: 100px 1fr;
  gap: 20px;
}

.project-item::before {
  content: '';
  position: absolute;
  left: -23px;
  top: 5px;
  width: 16px;
  height: 16px;
  background: #3498db;
  border-radius: 50%;
  border: 3px solid white;
  box-shadow: 0 0 0 3px #3498db;
}

.project-date {
  color: #7f8c8d;
  font-weight: 600;
  font-size: 0.9rem;
}

.project-content h4 {
  color: #2c3e50;
  margin-bottom: 10px;
  font-size: 1.2rem;
}

.project-desc {
  color: #5a6c7d;
  line-height: 1.6;
  margin-bottom: 15px;
}

.project-tech {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

/* 联系方式 */
.contact-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 10px;
  transition: transform 0.3s ease;
}

.contact-item:hover {
  transform: translateY(-3px);
}

.contact-icon {
  font-size: 2rem;
  color: #3498db;
}

.contact-info h4 {
  color: #2c3e50;
  margin-bottom: 5px;
}

.contact-info p {
  color: #7f8c8d;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 2.5rem;
  }
  
  .intro-card {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 30px;
  }
  
  .skills-grid {
    grid-template-columns: 1fr;
  }
  
  .project-item {
    grid-template-columns: 1fr;
    gap: 10px;
  }
  
  .projects-timeline {
    padding-left: 0;
  }
  
  .projects-timeline::before {
    display: none;
  }
  
  .project-item::before {
    display: none;
  }
  
  .contact-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .intro-section,
  .skills-section,
  .projects-section,
  .contact-section {
    padding: 20px;
  }
  
  .avatar {
    width: 120px;
    height: 120px;
  }
  
  .social-links {
    justify-content: center;
  }
}
</style>
