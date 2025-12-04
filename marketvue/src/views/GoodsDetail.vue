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
            <el-button type="primary" @click="openReserveDialog">预订</el-button>
            <el-button @click="handleCancel">退订</el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-dialog title="预订商品" :visible.sync="reserveDialogVisible" width="30%">
      <el-input-number
          v-model="reserveNum"
          :min="1"
          label="预订数量"
          style="width: 100%;"
      ></el-input-number>
      <div slot="footer">
        <el-button @click="reserveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReserve">确认预订</el-button>
      </div>
    </el-dialog>

    <el-divider></el-divider>
    <div class="comments-section">
      <h3>评论区</h3>

      <el-card class="comment-input-card" shadow="never">
        <el-form>
          <el-form-item>
            <el-input
                v-model="newComment.message"
                type="textarea"
                :rows="3"
                placeholder="请先预订商品再评论..."
                maxlength="500"
                show-word-limit>
            </el-input>
          </el-form-item>
          <el-form-item label="评分">
            <el-rate v-model="newComment.rate" :max="5"></el-rate>
          </el-form-item>
          <el-form-item style="text-align: right;">
            <el-button type="primary" @click="submitComment">发表或修改评论</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <div class="comments-list" v-if="comments.length > 0">
        <el-card
            v-for="comment in comments"
            :key="comment.userid + '-' + comment.goodsid"
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
            <el-rate v-model="comment.rate" disabled text-color="#ff9900" :max="5" style="margin-left: auto;"></el-rate>
          </div>
          <div class="comment-content">
            {{ comment.message }}
          </div>
        </el-card>

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
      comments: [],
      currentPage: 1,
      totalComments: 0,
      commentsPerPage: 10,
      newComment: {
        message: '',
        rate: 1
      },
      avatarCache: {},
      reserveDialogVisible: false,
      reserveNum: 1,
      hasCommentPerm: false
    }
  },

  mounted() {
    this.loadGoodsDetail();
    this.loadComments();
    this.checkCommentPermission();
  },
  watch: {
    '$route'(to, from) {
      if (to.query.id !== from.query.id) {
        this.loadGoodsDetail();
        this.checkCommentPermission();
      }
    }
  },
  methods: {
    loadGoodsDetail() {
      const goodsId = this.$route.query.id;
      if (!goodsId) {
        this.$message.error('商品ID无效');
        this.$router.back();
        return;
      }

      this.$axios.post(`${this.$httpUrl}/goods/listPage`, {
        pageSize: 1,
        pageNum: 1,
        param: {
          id: parseInt(goodsId)
        }
      }).then(res => res.data)
          .then(res => {
            if (res.code === 200 && res.data && res.data.length > 0) {
              this.goodsInfo = res.data[0];
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

    openReserveDialog() {
      const currentUser = JSON.parse(sessionStorage.getItem('User') || '{}');

      if (!currentUser.id) {
        this.$message.warning('请先登录');
        return;
      }
      if (this.goodsInfo.storage <= 0) {
        this.$message.error('该商品已无库存，无法预订');
        return;
      }
      this.reserveDialogVisible = true;
    },

    // --- 核心修改：美化后的预订方法 ---
    confirmReserve() {
      const currentUser = JSON.parse(sessionStorage.getItem('User') || '{}');
      const userId = currentUser.id;
      const goodsId = this.$route.query.id;

      const requestData = {
        userId: userId,
        goodsId: parseInt(goodsId),
        reserveNum: this.reserveNum
      };

      this.$axios.post(`${this.$httpUrl}/reservation/reserve`, requestData)
          .then(res => res.data)
          .then(res => {
            if (res.code === 200) {
              this.$alert(
                  `<div style="text-align: center;">
                    <div style="font-size: 18px; color: #67C23A; font-weight: bold; margin-bottom: 8px;">🎉 预订成功！</div>
                    <div style="font-size: 14px; color: #606266;">${''}</div>
                    <div style="font-size: 13px; color: #909399; margin-top: 5px;">${''}</div>
                   </div>`,
                  {
                    dangerouslyUseHTMLString: true, // 开启HTML解析
                    confirmButtonText: '确定',
                    type: 'success', // 绿色勾选图标
                    center: true,    // 居中布局
                    showClose: false // 隐藏右上角叉号
                  }
              ).then(() => {
                // 点击按钮后的回调
                this.reserveDialogVisible = false;
                this.loadGoodsDetail();
                this.checkCommentPermission();
              });

            } else {
              // 🌑 美化后的失败弹窗
              this.$alert(
                  `<div style="text-align: center;">
                    <div style="font-size: 14px; color: #606266;">${res.data || '当前库存不足或网络异常'}</div>
                   </div>`,
                  '预定失败',
                  {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '关闭',
                    type: 'error', // 红色错误图标
                    center: true
                  }
              );
            }
          })
          .catch(error => {
            console.error('预订请求错误:', error);
            this.$message.error('网络错误，请重试');
          });
      this.loadGoodsDetail();
    },

    // --- 核心修改：美化后的退订方法（保持风格一致） ---
    handleCancel() {
      const currentUser = JSON.parse(sessionStorage.getItem('User') || '{}');
      const userId = currentUser.id;
      const goodsId = this.$route.query.id;

      if (!userId) {
        this.$message.warning('请先登录');
        return;
      }

      this.$confirm('确定要取消该商品的预订吗？', '退订确认', {
        confirmButtonText: '确定退订',
        cancelButtonText: '我再想想',
        type: 'warning'
      }).then(() => {
        // 用户点击确定后发送请求
        this.$axios.post(`${this.$httpUrl}/reservation/cancel`, {
          userId: userId,
          goodsId: parseInt(goodsId)
        }).then(res => res.data)
            .then(res => {
              if (res.code === 200) {
                // 🌸 退订成功美化
                this.$alert(
                    `<div style="text-align: center; color: #67C23A; font-weight: bold;">退订成功</div>`,
                    {
                      dangerouslyUseHTMLString: true,
                      type: 'success',
                      center: true
                    }
                );
                this.loadGoodsDetail();
                this.checkCommentPermission();
              } else {
                // 🌑 退订失败美化
                this.$alert(
                    `<div style="text-align: center;">
                      <div style="font-size: 13px; color: #909399;">${res.data}</div>
                     </div>`,
                    '退订失败',
                    {
                      dangerouslyUseHTMLString: true,
                      type: 'error',
                      center: true
                    }
                );
              }
            })
            .catch(error => {
              console.error('退订请求错误:', error);
              this.$message.error('网络错误，请重试');
            });
        this.loadGoodsDetail();
      }).catch(() => {
        // 用户点击取消，不做任何事
      });
    },

    checkCommentPermission() {
      const currentUser = JSON.parse(sessionStorage.getItem('User') || '{}');
      const userId = currentUser.id;
      const goodsId = this.$route.query.id;

      if (!userId || !goodsId) {
        this.hasCommentPerm = false;
        return;
      }

      this.$axios.get(`${this.$httpUrl}/reservation/check-comment-perm`, {
        params: {
          userId: userId,
          goodsId: parseInt(goodsId)
        }
      }).then(res => res.data)
          .then(res => {
            if (res.code === 200) {
              this.hasCommentPerm = true;
            } else {
              this.hasCommentPerm = false;
            }
          })
          .catch(error => {
            console.error('权限校验错误:', error);
            this.hasCommentPerm = false;
          });
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
              this.comments = Array.isArray(res.data) ? res.data : [];
              this.totalComments = res.totalCount || this.comments.length;

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
      if (!this.hasCommentPerm) {
        this.$message.error('请先预订该商品再评论');
        return;
      }

      if (!this.newComment.message.trim()) {
        this.$message.warning('请输入评论内容');
        return;
      }

      const currentUser = JSON.parse(sessionStorage.getItem('User') || '{}');
      const userId = currentUser.id;
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
              this.$message.error(res.msg || '评论失败');
            }
          })
          .catch(error => {
            console.error('评论出错:', error);
            this.$message.error('评论出错');
          });
      this.loadComments();
      this.loadGoodsDetail();
      this.loadUserAvatar();
    }
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
  display: flex;
  align-items: center;
  border-bottom: 1px solid #f5f5f5;
  margin-bottom: 10px;
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