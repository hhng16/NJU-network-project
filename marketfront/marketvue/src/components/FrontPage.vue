<template>
  <div class="home-container">
    <!-- 轮播图部分 -->
    <div class="carousel-section">
      <div class="section-header">
        <h2>天工市场</h2>
        <p>发现美好生活，尽在指尖之间</p>
      </div>

      <div class="carousel-container">
        <div class="carousel-wrapper">
          <el-carousel
              :interval="4000"
              arrow="always"
              height="500px"
              indicator-position="none"
          >
            <el-carousel-item v-for="item in carouselItems" :key="item.id">
              <div class="carousel-item">
                <div class="image-blur-background" :style="{ backgroundImage: `url(${item.image})` }"></div>
                <div class="image-main-container">
                  <img
                      class="carousel-image"
                      :src="item.image"
                      :alt="item.title"
                  >
                </div>
                <div class="carousel-content">
                  <div class="content-wrapper">
                    <h3 class="carousel-title">{{ item.title }}</h3>
                    <p class="carousel-desc">{{ item.description }}</p>
                    <el-button
                        type="primary"
                        class="carousel-btn"
                        round
                        @click="handleCarouselClick(item)"
                    >
                      了解更多
                    </el-button>
                  </div>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
    </div>

    <!-- 商品卡片部分 -->
    <div class="products-section">
      <div class="section-header">
        <h2>热门推荐</h2>
        <p>发现精彩内容，尽在天工市场</p>
      </div>

      <el-row :gutter="30" class="products-grid">
        <el-col :xs="24" :sm="12" :md="8" v-for="product in products" :key="product.id">
          <el-card class="product-card" :body-style="{ padding: '0px' }" shadow="hover">
            <div class="card-image">
              <img :src="product.image" :alt="product.title">
              <div class="image-overlay"></div>
            </div>
            <div class="card-content">
              <h4 class="product-title">{{ product.title }}</h4>
              <p class="product-description">{{ product.description }}</p>
              <div class="card-actions">
                <el-button
                    type="primary"
                    class="action-btn"
                    @click="$router.push(product.link)"
                    round
                >
                  {{ product.buttonText }}
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  min-height: 100vh;
  background-image: url('../assets/background.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  padding: 20px 0;
  position: relative;
}

.home-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  z-index: 0;
}

/* 内容区域 */
.carousel-section,
.products-section {
  position: relative;
  z-index: 1;
}

/* 轮播图样式 */
.carousel-section {
  margin-bottom: 60px;
  padding: 40px 0;
}

.section-header {
  text-align: center;
  margin-bottom: 40px;
}

.section-header h2 {
  font-size: 2.5rem;
  color: white;
  margin-bottom: 10px;
  font-weight: 600;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
}

.section-header p {
  font-size: 1.1rem;
  color: rgba(255, 255, 255, 0.9);
}

.carousel-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.carousel-wrapper {
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.4);
  background: white;
  position: relative;
}

.carousel-item {
  position: relative;
  height: 500px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  overflow: hidden;
  padding: 0 40px;
}

/* 背景虚化效果 */
.image-blur-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-size: cover;
  background-position: center;
  filter: blur(20px) brightness(0.7);
  z-index: 1;
  transform: scale(1.1);
}

/* 主图片容器*/
.image-main-container {
  position: relative;
  z-index: 2;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
  max-width: 60%;
}

.carousel-image {
  height: 100%;
  width: auto;
  max-width: 100%;
  object-fit: contain;
  object-position: center;
  transition: all 0.3s ease;
}

.carousel-item:hover .carousel-image {
  transform: scale(1.03);
}

/* 文字内容区域 - 移到右侧 */
.carousel-content {
  position: relative;
  z-index: 3;
  max-width: 35%; /* 限制文字区域宽度 */
  margin-left: 40px; /* 与图片保持距离 */
}

.content-wrapper {
  text-align: left;
}

.carousel-title {
  font-size: 2.2rem;
  font-weight: 700;
  color: white;
  margin-bottom: 20px;
  line-height: 1.3;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.6);
}

.carousel-desc {
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 30px;
  line-height: 1.6;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.5);
}

.carousel-btn {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
  border: none;
  padding: 12px 28px;
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.4);
}

.carousel-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(255, 107, 107, 0.6);
}

/* 商品部分样式 */
.products-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.products-grid {
  margin: 0 -15px;
}

.product-card {
  border: none;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
  margin-bottom: 30px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
  background: white;
}

.card-image {
  position: relative;
  height: 280px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .card-image img {
  transform: scale(1.05);
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, transparent 60%, rgba(0, 0, 0, 0.3));
}

.card-content {
  padding: 24px;
}

.product-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 12px;
  line-height: 1.4;
}

.product-description {
  color: #5d6d7e;
  font-size: 0.95rem;
  line-height: 1.5;
  margin-bottom: 20px;
  min-height: 60px;
}

.card-actions {
  text-align: center;
}

.action-btn {
  width: 100%;
  padding: 12px 24px;
  font-size: 1rem;
  font-weight: 500;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .carousel-container {
    padding: 0 10px;
  }

  .carousel-item {
    height: 400px;
    flex-direction: column;
    justify-content: center;
    padding: 20px;
  }

  .image-main-container {
    max-width: 100%;
    height: 60%;
    margin-bottom: 20px;
  }

  .carousel-content {
    max-width: 100%;
    margin-left: 0;
    text-align: center;
  }

  .carousel-title {
    font-size: 1.8rem;
  }

  .carousel-desc {
    font-size: 1rem;
  }

  .section-header h2 {
    font-size: 2rem;
  }

  .products-section {
    padding: 0 10px;
  }

  .card-image {
    height: 220px;
  }
}
</style>

<script>
export default {
  name: 'HomePage',
  methods: {
    handleCarouselClick(item) {
      console.log('点击了轮播图:', item.title);
      if (item.id === 2 || item.id === 3) {
        this.$router.push('/PetShop');
      } else if (item.id === 4) {
        this.$router.push('/BookShop');
      }
    }
  },
  data() {
    return {
      carouselItems: [
        {
          id: 1,
          image: require('../assets/龙千1.jpg'),
          title: '欢迎来到天工市场',
          description: '汇聚精品好物，打造品质生活'
        },
        {
          id: 2,
          image: require('../assets/耄耋1.png'),
          title: '夏季新品',
          description: '最新商品火热上线，限时优惠'
        },
        {
          id: 3,
          image: require('../assets/周刊1.jpg'),
          title: '萌宠专区',
          description: '可爱宠物等你来领养'
        },
        {
          id: 4,
          image: require('../assets/logo.png'),
          title: '阅读时光',
          description: '精选书刊，丰富你的精神世界'
        }
      ],
      products: [
        {
          id: 1,
          image: require('../assets/龙千1.jpg'),
          title: '夏季上新',
          description: '幻想乡的新宠商品与特价，带来夏日清凉体验',
          link: '/PetShop',
          buttonText: '立即探索'
        },
        {
          id: 2,
          image: require('../assets/耄耋1.png'),
          title: '萌宠特惠',
          description: '买耄耋赠优惠卡，限量发售，机会难得',
          link: '/PetShop',
          buttonText: '了解详情'
        },
        {
          id: 3,
          image: require('../assets/周刊1.jpg'),
          title: '本月新刊',
          description: '最新期刊速览，掌握前沿资讯',
          link: '/BookShop',
          buttonText: '阅读更多'
        }
      ]
    };
  }
}
</script>