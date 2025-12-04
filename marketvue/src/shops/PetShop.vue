<template>
  <div class="petshop-container">
    <!-- 页面标题和搜索区域 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">🐾 萌宠商店</h1>
          <p class="page-subtitle">遇见可爱伙伴，开启温馨时光</p>
        </div>

        <div class="search-section">
          <div class="search-box">
            <el-input
                v-model="goodsname"
                placeholder="搜索宠物或用品..."
                suffix-icon="el-icon-search"
                class="search-input"
                @keyup.enter.native="loadPost">
            </el-input>
            <el-button type="primary" class="search-btn" @click="loadPost">
              <i class="el-icon-search"></i> 搜索
            </el-button>
            <el-button type="default" class="reset-btn" @click="resetParam">
              <i class="el-icon-refresh"></i> 重置
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 宠物商品网格 -->
      <div class="pets-grid" v-if="tableData.length > 0">
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in tableData" :key="item.id">
            <el-card class="pet-card" shadow="hover" :body-style="{ padding: '0px' }">
              <!-- 宠物图片 -->
              <div class="pet-image">
                <img
                    :src="fixurl(item.image)"
                    :alt="item.goodsname"
                    class="pet-photo"
                    v-if="item.image"
                    @error="handleImageError"
                />
                <div class="no-image" v-else>
                  <i class="el-icon-star-off"></i>
                  <span>暂无图片</span>
                </div>
                <div class="pet-overlay">
                  <el-button
                      type="primary"
                      class="detail-btn"
                      @click="viewDetail(item)"
                      round
                  >
                    <i class="el-icon-view"></i> 查看详情
                  </el-button>
                </div>
                <!-- 宠物标签 -->
                <div class="pet-badge">
                  <span class="badge pet">萌宠</span>
                </div>
              </div>

              <!-- 宠物信息 -->
              <div class="pet-info">
                <h3 class="pet-title">{{ item.goodsname }}</h3>

                <div class="pet-meta">
                  <div class="meta-item price">
                    <i class="el-icon-coin"></i>
                    <span class="price-text">¥{{ item.price }}</span>
                  </div>
                  <div class="meta-item stock">
                    <i class="el-icon-box"></i>
                    <span>库存 {{ item.storage }}</span>
                  </div>
                </div>

                <div class="pet-footer">
                  <div class="breeder-info">
                    <el-avatar :size="24" :src="fixurl(item.authorAvatar)" v-if="item.authorAvatar">
                      {{ item.authorName ? item.authorName.charAt(0) : 'B' }}
                    </el-avatar>
                    <el-avatar :size="24" v-else>
                      {{ item.authorName ? item.authorName.charAt(0) : 'B' }}
                    </el-avatar>
                    <span class="breeder-name">{{ item.authorName || '未知卖家' }}</span>
                  </div>

                  <div class="rating" v-if="item.rateAvg">
                    <el-rate
                        v-model="item.rateAvg"
                        disabled
                        show-score
                        text-color="#ff9900"
                        score-template="{value}">
                    </el-rate>
                  </div>
                </div>

                <div class="contact-info" v-if="item.authorNumber">
                  <i class="el-icon-phone-outline"></i>
                  <span>{{ item.authorNumber }}</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 空状态 -->
      <div class="empty-state" v-else>
        <div class="empty-content">
          <i class="el-icon-star-off empty-icon"></i>
          <h3>暂无宠物商品</h3>
          <p>当前没有找到相关的宠物信息</p>
          <el-button type="primary" @click="resetParam">
            <i class="el-icon-refresh"></i> 重新加载
          </el-button>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-section" v-if="tableData.length > 0">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[8, 16, 24, 32]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            background>
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PetShop',
  data() {
    return {
      tableData: [],
      pageSize: 8,
      tableKey: 0,
      pageNum: 1,
      total: 0,
      goodsname: '',
      centerDialogVisible: true,
      form: {
        id: '',
        goodsname: '',
        price: '',
        storage: '',
        authorid: 0,
        tag: 3
      },
      rules: {
        goodsname: [
          { required: true, message: '请输入商品名', trigger: 'blur' }
        ],
      },
    }
  },
  methods: {
    fixurl(fileName){
      return `http://localhost:8095/images/${fileName}`;
    },

    handleImageError(event) {
      event.target.style.display = 'none';
      event.target.nextElementSibling.style.display = 'flex';
    },

    loadPost() {
      this.$axios.post(this.$httpUrl + '/goods/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          goodsname: this.goodsname,
          tag: 3
        }
      }).then(res => res.data).then(res => {
        console.log('完整响应:', res);
        if (res.code == 200) {
          this.tableData = res.data.map(item => ({
            ...item,
            rateAvg: item.rateAvg ? parseFloat(item.rateAvg) : 0
          }));
          this.total = res.total || 0;
          this.tableKey += 1;
        } else {
          console.error('获取数据失败:', res.msg);
          this.tableData = [];
          this.total = 0;
        }
      }).catch(error => {
        console.error('请求出错:', error);
        this.tableData = [];
        this.total = 0;
      })
    },

    resetParam() {
      this.goodsname = '';
      this.pageNum = 1;
      this.loadPost();
    },

    handleSizeChange(val) {
      this.pageNum = 1;
      this.pageSize = val;
      this.loadPost();
    },

    handleCurrentChange(val) {
      this.pageNum = val;
      this.loadPost();
    },

    viewDetail(row) {
      this.$router.push({
        name: 'GoodsDetail',
        query: { id: row.id }
      });
    }
  },

  mounted() {
    this.loadPost();
  },
}
</script>

<style scoped>
.petshop-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  padding: 0;
}

/* 页面头部 */
.page-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 40px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 30px;
}

.title-section {
  flex: 1;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 8px 0;
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.page-subtitle {
  font-size: 1.1rem;
  color: #5d6d7e;
  margin: 0;
}

.search-section {
  flex-shrink: 0;
}

.search-box {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 300px;
}

.search-input :deep(.el-input__inner) {
  border-radius: 25px;
  border: 2px solid #e4e7ed;
  padding-left: 20px;
  transition: all 0.3s ease;
}

.search-input :deep(.el-input__inner:focus) {
  border-color: #a8edea;
  box-shadow: 0 0 0 2px rgba(168, 237, 234, 0.1);
}

.search-btn, .reset-btn {
  border-radius: 20px;
  padding: 10px 20px;
  font-weight: 500;
}

.search-btn {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  border: none;
}

/* 主要内容 */
.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

/* 宠物网格 */
.pets-grid {
  margin-bottom: 40px;
}

/* 宠物卡片 */
.pet-card {
  border: none;
  border-radius: 20px;
  overflow: hidden;
  transition: all 0.3s ease;
  margin-bottom: 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  position: relative;
}

.pet-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

/* 宠物图片 */
.pet-image {
  position: relative;
  height: 240px;
  overflow: hidden;
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

.pet-photo {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.pet-card:hover .pet-photo {
  transform: scale(1.1);
}

.no-image {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #a8edea;
  font-size: 3rem;
}

.no-image span {
  font-size: 1rem;
  margin-top: 10px;
  color: #5d6d7e;
}

.pet-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.pet-card:hover .pet-overlay {
  opacity: 1;
}

.detail-btn {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  border: none;
  padding: 10px 24px;
  font-weight: 500;
}

/* 宠物标签 */
.pet-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  z-index: 2;
}

.badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  color: white;
}

.badge.pet {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

/* 宠物信息 */
.pet-info {
  padding: 20px;
}

.pet-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 15px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.pet-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.9rem;
}

.meta-item.price {
  color: #e74c3c;
}

.price-text {
  font-weight: 600;
  font-size: 1.2rem;
}

.meta-item.stock {
  color: #5d6d7e;
}

.pet-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.breeder-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.breeder-name {
  font-size: 0.9rem;
  color: #5d6d7e;
}

.rating {
  flex-shrink: 0;
}

.contact-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.85rem;
  color: #7f8c8d;
  padding-top: 12px;
  border-top: 1px solid #ecf0f1;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 80px 20px;
}

.empty-content {
  max-width: 400px;
  margin: 0 auto;
}

.empty-icon {
  font-size: 4rem;
  color: #a8edea;
  margin-bottom: 20px;
}

.empty-content h3 {
  font-size: 1.5rem;
  color: #2c3e50;
  margin-bottom: 10px;
}

.empty-content p {
  color: #7f8c8d;
  margin-bottom: 30px;
}

/* 分页 */
.pagination-section {
  text-align: center;
  padding: 20px 0;
}

.pagination-section :deep(.el-pagination) {
  justify-content: center;
}

.pagination-section :deep(.el-pagination.is-background .el-pager li:not(.disabled).active) {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    text-align: center;
  }

  .page-title {
    font-size: 2rem;
  }

  .search-input {
    width: 100%;
  }

  .search-box {
    width: 100%;
    justify-content: center;
  }

  .pets-grid .el-col {
    margin-bottom: 20px;
  }

  .pet-image {
    height: 200px;
  }
}

@media (max-width: 480px) {
  .main-content {
    padding: 20px 10px;
  }

  .page-header {
    padding: 30px 0;
  }

  .pet-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .pet-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .pet-title {
    font-size: 1rem;
  }
}
</style>