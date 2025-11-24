<template>
  <div class="goods-detail">
    <el-card>
      <div slot="header">
        <span>商品详情</span>
        <el-button style="float: right;" @click="$router.back()">返回</el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-image
              :src="fixurl(goodsInfo.image)"
              fit="contain"
              style="max-height: 400px;">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
              <span>暂无图片</span>
            </div>
          </el-image>
        </el-col>

        <el-col :span="12">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="商品名称">{{ goodsInfo.goodsname }}</el-descriptions-item>
            <el-descriptions-item label="价格">￥{{ goodsInfo.price }}</el-descriptions-item>
            <el-descriptions-item label="库存">{{ goodsInfo.storage }}</el-descriptions-item>
            <el-descriptions-item label="商品类型">
              <el-tag>{{ getTagName(goodsInfo.tag) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="发布人">{{ goodsInfo.authorName }}</el-descriptions-item>
            <el-descriptions-item label="联系方式">{{ goodsInfo.authorNumber }}</el-descriptions-item>
            <el-descriptions-item label="平均评分">
              <el-rate v-model="goodsInfo.rateAvg" disabled text-color="#ff9900" show-score></el-rate>
            </el-descriptions-item>
          </el-descriptions>

          <div style="margin-top: 20px;">
            <el-button type="primary" @click="buyGoods">立即购买</el-button>
            <el-button @click="addToCart">加入购物车</el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 评论区 -->
    <el-divider></el-divider>
    <div class="comments-section">
      <h3>评论区</h3>

      <!-- 发表评论 -->
      <el-card class="comment-input-card" shadow="never">
        <el-form>
          <el-form-item>
            <el-input
                v-model="newComment.message"
                type="textarea"
                :rows="3"
                placeholder="请输入您的评论..."
                maxlength="500"
                show-word-limit>
            </el-input>
          </el-form-item>
          <!-- 添加评分组件 -->
          <el-form-item label="评分">
            <el-rate v-model="newComment.rate" :max="5"></el-rate>
          </el-form-item>
          <el-form-item style="text-align: right;">
            <el-button type="primary" @click="submitComment">发表或修改评论</el-button>
          </el-form-item>
        </el-form>
      </el-card>


      <!-- 评论列表 -->
      <div class="comments-list" v-if="comments.length > 0">
        <el-card
            v-for="comment in comments"
            :key="comment.id"
            class="comment-card"
            shadow="never">
          <div class="comment-header">
            <div class="user-info">
              <el-avatar :src="getUserAvatar(comment.username)" size="medium" icon="el-icon-user-solid" class="user-avatar"></el-avatar>
              <div class="user-details">
                <div class="username">{{ comment.username || '匿名用户' }}</div>
                <div class="comment-time">{{ formatDate(comment.createdAt) }}</div>
              </div>
            </div>
          </div>
          <div class="comment-content">
            {{ comment.message }}
          </div>
        </el-card>

        <!-- 分页 -->
        <div class="pagination-container" v-if="totalComments > commentsPerPage">
          <el-pagination
              background
              layout="prev, pager, next, jumper, ->, total"
              :total="totalComments"
              :page-size="commentsPerPage"
              :current-page="currentPage"
              @current-change="handlePageChange"
              :pager-count="5">
          </el-pagination>
        </div>
      </div>

      <!-- 无评论提示 -->
      <el-empty v-else description="暂无评论，快来抢沙发吧！" class="no-comments"></el-empty>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GoodsDetail',
  data() {
    return {
      goodsInfo: {},
      comments: [],           // 评论列表
      currentPage: 1,         // 当前页码
      totalComments: 0,       // 评论总数
      commentsPerPage: 10,    // 每页评论数
      newComment: {          // 新评论内容
        message: '',
        rate: 1
      },
      avatarCache: {}

    }
  },

  mounted() {
    this.loadGoodsDetail();
    this.loadComments();
  },
  watch: {
    '$route'(to, from) {
      if (to.query.id !== from.query.id) {
        this.loadGoodsDetail();
      }
    }
  },
  methods: {
    loadGoodsDetail() {
      const goodsId = this.$route.query.id;
      console.log('商品ID:', goodsId);
      if (!goodsId) {
        this.$message.error('商品ID无效');
        this.$router.back();
        return;
      }

      this.$axios.post(`${this.$httpUrl}/goods/listPage`, {
        pageSize: 1,
        pageNum: 1,
        param: {
          id: parseInt(goodsId)  // 确保 ID 是数字类型
        }
      }).then(res => res.data)
          .then(res => {
            console.log('响应数据:', res);
            if (res.code === 200 && res.data && res.data.length > 0) {
              this.goodsInfo = res.data[0];  // 取第一个商品数据
            } else {
              this.$message.error('获取商品详情失败');
            }
          })
          .catch(error => {
            console.error('请求错误:', error);
            this.$message.error('请求出错');
          });
    },
    getUserAvatar(username) {
      if (!username) return '';
      return this.avatarCache[username] || '';
    },
    async loadUserAvatar(username) {
      if (!username || this.avatarCache[username]) return;

      try {
        const response = await this.$axios.get(`${this.$httpUrl}/user/findByName?name=${encodeURIComponent(username)}`);
        const res = response.data;

        let avatarUrl = '';
        if (res.code === 200 && res.data && res.data.length > 0) {
          const user = res.data[0];
          avatarUrl = user.image ? `http://localhost:8095/heads/${user.image}` : '';
        }

        this.$set(this.avatarCache, username, avatarUrl);
      } catch (error) {
        console.error('获取用户头像失败:', error);
        this.$set(this.avatarCache, username, '');
      }
    },
    fixurl(fileName) {
      return fileName ? `http://localhost:8095/images/${fileName}` : '';
    },

    getTagName(tag) {
      const tagMap = {
        '1': '日用',
        '2': '游戏',
        '3': '萌宠',
        '4': '书刊',
        '5': '音乐'
      };
      return tagMap[tag] || '未知';
    },

    buyGoods() {
      this.$message.info('购买功能待实现');
    },

    addToCart() {
      this.$message.info('加入购物车功能待实现');
    },
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      const now = new Date();
      const diffMs = now - date;
      const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

      if (diffDays === 0) {
        return '今天 ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
      } else if (diffDays === 1) {
        return '昨天 ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
      } else if (diffDays < 7) {
        return `${diffDays}天前`;
      } else {
        return date.toLocaleDateString('zh-CN');
      }
    },

    /////

    loadComments() {
      const goodsId = this.$route.query.id;
      this.$axios.post(`${this.$httpUrl}/comment/listPageC1`, {
        pageSize: this.commentsPerPage,
        pageNum: this.currentPage,
        param: {
          goodsid: parseInt(goodsId)
        }
      }).then(res => res.data)
          .then(res => {
            if (res.code === 200) {
              this.comments = res.data;
              this.totalComments = res.totalCount;
              this.comments = Array.isArray(res.data) ? res.data : [];
              // 确保 totalCount 不小于实际数据长度
              this.totalComments = Math.max(res.totalCount || 0, this.comments.length);

              this.comments.forEach(comment => {
                if (comment.username) {
                  this.loadUserAvatar(comment.username);
                }
              });
            }
          })
          .catch(error => {
            console.error('获取评论失败:', error);
            this.$message.error('获取评论失败');
            this.comments = [];
            this.totalComments = 0;
          });
    },

    handlePageChange(page) {
      const maxPage = Math.ceil(this.totalComments / this.commentsPerPage) || 1;
      this.currentPage = Math.min(page, maxPage);
      this.loadComments();
    },

    submitComment() {
      if (!this.newComment.message.trim()) {
        this.$message.warning('请输入评论内容');
        return;
      }

      // 从本地存储获取当前用户信息//rate
      const currentUser = JSON.parse(sessionStorage.getItem('User') || '{}');
      const userId = currentUser.id || 114514; // 默认ID为1
      const goodsId = this.$route.query.id;

      this.$axios.post(`${this.$httpUrl}/comment/savemod`, {
        userid: userId,
        goodsid: parseInt(goodsId),
        message: this.newComment.message,
        rate: this.newComment.rate
      }).then(res => res.data)
          .then(res => {
            if (res.code === 200) {
              this.$message.success('评论成功');
              this.newComment.message = '';
              this.newComment.rate = 1;
              this.loadComments(); // 重新加载评论
            } else {
              this.$message.error('评论失败');
            }
          })
          .catch(error => {
            console.error('评论出错:', error);
            this.$message.error('评论出错');
          });
    }


    /////
  }
}
</script>

<style scoped>
.goods-detail {
  padding: 20px;
}

.image-slot {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  background: #f5f7fa;
  color: #909399;
}

.comments-section {
  margin-top: 30px;
}

.comment-input-card {
  margin-bottom: 20px;
  border: 1px solid #ebeef5;
}

.comment-card {
  margin-bottom: 15px;
  border: 1px solid #ebeef5;
  transition: box-shadow 0.3s ease;
}

.comment-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.comment-header {
  padding-bottom: 10px;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  margin-right: 12px;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
  margin-bottom: 2px;
}

.comment-time {
  font-size: 12px;
  color: #909399;
}

.comment-content {
  font-size: 14px;
  line-height: 1.6;
  color: #606266;
  padding-left: 44px;
  white-space: pre-wrap;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.no-comments {
  padding: 40px 0;
}

</style>
