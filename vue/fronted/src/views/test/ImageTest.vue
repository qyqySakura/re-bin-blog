<template>
  <div class="image-test">
    <h2>图片处理工具测试</h2>
    
    <div class="test-section">
      <h3>1. 图片URL处理测试</h3>
      <div class="test-grid">
        <div class="test-item">
          <h4>完整HTTP URL</h4>
          <img :src="processImageUrl('https://images.unsplash.com/photo-1518709268805-4e9042af2176?w=800&h=400&fit=crop')" 
               @error="(e) => handleImageError(e, 'cover')" 
               alt="测试图片1" />
          <p>原始: https://images.unsplash.com/...</p>
          <p>处理后: {{ processImageUrl('https://images.unsplash.com/photo-1518709268805-4e9042af2176?w=800&h=400&fit=crop') }}</p>
        </div>
        
        <div class="test-item">
          <h4>相对路径 (以/开头)</h4>
          <img :src="processImageUrl('/api/files/test.jpg')" 
               @error="(e) => handleImageError(e, 'cover')" 
               alt="测试图片2" />
          <p>原始: /api/files/test.jpg</p>
          <p>处理后: {{ processImageUrl('/api/files/test.jpg') }}</p>
        </div>
        
        <div class="test-item">
          <h4>不带前缀的路径</h4>
          <img :src="processImageUrl('test.jpg', 'cover')" 
               @error="(e) => handleImageError(e, 'cover')" 
               alt="测试图片3" />
          <p>原始: test.jpg</p>
          <p>处理后: {{ processImageUrl('test.jpg', 'cover') }}</p>
        </div>
        
        <div class="test-item">
          <h4>空URL (默认图片)</h4>
          <img :src="processImageUrl('', 'cover')" 
               @error="(e) => handleImageError(e, 'cover')" 
               alt="默认图片" />
          <p>原始: (空)</p>
          <p>处理后: {{ processImageUrl('', 'cover') }}</p>
        </div>
      </div>
    </div>
    
    <div class="test-section">
      <h3>2. 不同类型图片测试</h3>
      <div class="test-grid">
        <div class="test-item">
          <h4>封面图片 (cover)</h4>
          <img :src="processImageUrl('', 'cover')" 
               @error="(e) => handleImageError(e, 'cover')" 
               alt="封面默认图片" />
          <p>类型: cover</p>
        </div>
        
        <div class="test-item">
          <h4>头像图片 (avatar)</h4>
          <img :src="processImageUrl('', 'avatar')" 
               @error="(e) => handleImageError(e, 'avatar')" 
               alt="头像默认图片" />
          <p>类型: avatar</p>
        </div>
        
        <div class="test-item">
          <h4>内容图片 (content)</h4>
          <img :src="processImageUrl('', 'content')" 
               @error="(e) => handleImageError(e, 'content')" 
               alt="内容默认图片" />
          <p>类型: content</p>
        </div>
      </div>
    </div>
    
    <div class="test-section">
      <h3>3. 图片预加载测试</h3>
      <div class="preload-test">
        <el-button @click="testPreload" type="primary">测试预加载</el-button>
        <el-button @click="testBatchPreload" type="success">测试批量预加载</el-button>
        <div class="preload-results">
          <p v-for="result in preloadResults" :key="result.url">
            {{ result.url }}: {{ result.status }}
          </p>
        </div>
      </div>
    </div>
    
    <div class="test-section">
      <h3>4. 图片信息获取测试</h3>
      <div class="info-test">
        <el-button @click="testImageInfo" type="warning">获取图片信息</el-button>
        <div class="image-info" v-if="imageInfo">
          <p>宽度: {{ imageInfo.width }}px</p>
          <p>高度: {{ imageInfo.height }}px</p>
          <p>宽高比: {{ imageInfo.aspectRatio.toFixed(2) }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { 
  processImageUrl, 
  handleImageError, 
  preloadImage, 
  preloadImages, 
  getImageInfo 
} from '@/utils/imageUtils'

const preloadResults = ref([])
const imageInfo = ref(null)

// 测试单个图片预加载
const testPreload = async () => {
  const testUrl = 'https://images.unsplash.com/photo-1518709268805-4e9042af2176?w=800&h=400&fit=crop'
  try {
    await preloadImage(testUrl)
    preloadResults.value.push({ url: testUrl, status: '加载成功' })
  } catch (error) {
    preloadResults.value.push({ url: testUrl, status: '加载失败' })
  }
}

// 测试批量图片预加载
const testBatchPreload = async () => {
  const testUrls = [
    'https://images.unsplash.com/photo-1518709268805-4e9042af2176?w=800&h=400&fit=crop',
    'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=400&fit=crop',
    '/api/files/nonexistent.jpg'
  ]
  
  const results = await preloadImages(testUrls)
  results.forEach((result, index) => {
    preloadResults.value.push({
      url: testUrls[index],
      status: result.status === 'fulfilled' ? '加载成功' : '加载失败'
    })
  })
}

// 测试获取图片信息
const testImageInfo = async () => {
  const testUrl = 'https://images.unsplash.com/photo-1518709268805-4e9042af2176?w=800&h=400&fit=crop'
  try {
    imageInfo.value = await getImageInfo(testUrl)
  } catch (error) {
    console.error('获取图片信息失败:', error)
  }
}
</script>

<style scoped>
.image-test {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h2 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 30px;
}

.test-section {
  margin-bottom: 40px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

h3 {
  color: #34495e;
  margin-bottom: 20px;
  border-bottom: 2px solid #3498db;
  padding-bottom: 10px;
}

.test-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.test-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
}

.test-item h4 {
  color: #2c3e50;
  margin-bottom: 10px;
}

.test-item img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.test-item p {
  font-size: 12px;
  color: #7f8c8d;
  margin: 5px 0;
  word-break: break-all;
}

.preload-test, .info-test {
  text-align: center;
}

.preload-results {
  margin-top: 20px;
  text-align: left;
  max-height: 200px;
  overflow-y: auto;
  background: #f8f9fa;
  padding: 10px;
  border-radius: 4px;
}

.image-info {
  margin-top: 20px;
  background: #f8f9fa;
  padding: 15px;
  border-radius: 4px;
  text-align: left;
}

.image-info p {
  margin: 5px 0;
  color: #2c3e50;
}
</style>
