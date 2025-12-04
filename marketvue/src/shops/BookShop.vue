<template>
  <div class="bookshop-container">
    <!-- 页面标题和搜索区域 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">📚 书刊商店</h1>
          <p class="page-subtitle">发现精彩读物，丰富你的阅读时光</p>
        </div>

        <div class="search-section">
          <div class="search-box">
            <el-input
                v-model="goodsname"
                placeholder="搜索书刊名称..."
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
      <!-- 书籍卡片网格 -->
      <div class="books-grid" v-if="tableData.length > 0">
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="book in tableData" :key="book.id">
            <el-card class="book-card" shadow="hover" :body-style="{ padding: '0px' }">
              <!-- 书籍图片 -->
              <div class="book-image">
                <img
                    :src="fixurl(book.image)"
                    :alt="book.goodsname"
                    class="book-cover"
                    v-if="book.image"
                    @error="handleImageError"
                />
                <div class="no-image" v-else>
                  <i class="el-icon-notebook-2"></i>
                  <span>暂无封面</span>
                </div>
                <div class="book-overlay">
                  <el-button
                      type="primary"
                      class="detail-btn"
                      @click="viewDetail(book)"
                      round
                  >
                    查看详情
                  </el-button>
                </div>
              </div>

              <!-- 书籍信息 -->
              <div class="book-info">
                <h3 class="book-title">{{ book.goodsname }}</h3>

                <div class="book-meta">
                  <div class="meta-item">
                    <i class="el-icon-price-tag"></i>
                    <span class="price">¥{{ book.price }}</span>
                  </div>
                  <div class="meta-item">
                    <i class="el-icon-box"></i>
                    <span>库存: {{ book.storage }}</span>
                  </div>
                </div>

                <div class="book-footer">
                  <div class="author-info">
                    <el-avatar :size="24" :src="fixurl(book.authorAvatar)" v-if="book.authorAvatar">
                      {{ book.authorName ? book.authorName.charAt(0) : 'U' }}
                    </el-avatar>
                    <el-avatar :size="24" v-else>{{ book.authorName ? book.authorName.charAt(0) : 'U' }}</el-avatar>
                    <span class="author-name">{{ book.authorName || '未知作者' }}</span>
                  </div>

                  <div class="rating" v-if="book.rateAvg">
                    <el-rate
                        v-model="book.rateAvg"
                        disabled
                        show-score
                        text-color="#ff9900"
                        score-template="{value}">
                    </el-rate>
                  </div>
                </div>

                <div class="contact-info" v-if="book.authorNumber">
                  <i class="el-icon-phone"></i>
                  <span>{{ book.authorNumber }}</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 空状态 -->
      <div class="empty-state" v-else>
        <div class="empty-content">
          <i class="el-icon-notebook-2 empty-icon"></i>
          <h3>暂无书刊数据</h3>
          <p>当前没有找到相关的书刊信息</p>
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
  name: 'BookShop',
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
        tag: 4,
        image: ''
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
          tag: 4
        }
      }).then(res => res.data).then(res => {
        console.log('完整响应:', res);
        if (res.code == 200) {
          this.tableData = res.data.map(book => ({
            ...book,
            rateAvg: book.rateAvg ? parseFloat(book.rateAvg) : 0
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
    },
  },

  mounted() {
    this.loadPost();
  },
}
</script>

<style scoped>
.bookshop-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.search-btn, .reset-btn {
  border-radius: 20px;
  padding: 10px 20px;
  font-weight: 500;
}

/* 主要内容 */
.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

/* 书籍网格 */
.books-grid {
  margin-bottom: 40px;
}

/* 书籍卡片 */
.book-card {
  border: none;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
  margin-bottom: 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.book-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

/* 书籍图片 */
.book-image {
  position: relative;
  height: 280px;
  overflow: hidden;
  background: #f8f9fa;
}

.book-cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.book-card:hover .book-cover {
  transform: scale(1.05);
}

.no-image {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #bdc3c7;
  font-size: 3rem;
}

.no-image span {
  font-size: 1rem;
  margin-top: 10px;
}

.book-overlay {
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

.book-card:hover .book-overlay {
  opacity: 1;
}

.detail-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  padding: 10px 24px;
  font-weight: 500;
}

/* 书籍信息 */
.book-info {
  padding: 20px;
}

.book-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 15px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.book-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #5d6d7e;
  font-size: 0.9rem;
}

.price {
  color: #e74c3c;
  font-weight: 600;
  font-size: 1.1rem;
}

.book-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-name {
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
  color: #bdc3c7;
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

  .books-grid .el-col {
    margin-bottom: 20px;
  }

  .book-image {
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

  .book-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .book-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>