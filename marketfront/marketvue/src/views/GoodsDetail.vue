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
            <el-descriptions-item label="平均评分">{{ goodsInfo.rateAvg || '暂无评分' }}</el-descriptions-item>
          </el-descriptions>

          <div style="margin-top: 20px;">
            <el-button type="primary" @click="buyGoods">立即购买</el-button>
            <el-button @click="addToCart">加入购物车</el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'GoodsDetail',
  data() {
    return {
      goodsInfo: {}
    }
  },

  mounted() {
    this.loadGoodsDetail();
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
</style>
