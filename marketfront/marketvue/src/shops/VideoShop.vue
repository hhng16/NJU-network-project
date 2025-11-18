<script>

export default {
  name: 'VedioShop',
  data() {
    return {
      tableData: [],
      pageSize: 10,
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
        tag: 4
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
    loadPost() {
      this.$axios.post(this.$httpUrl + '/goods/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          goodsname: this.goodsname,
          tag: 2
        }
      }).then(res => res.data).then(res => {
        console.log('完整响应:', res);
        if (res.code == 200) {
          // 处理数据，移除Blob对象
          this.tableData = res.data;
          this.total = res.total || 0;
          this.tableKey += 1;

          console.log('处理后的数据:', this.tableData);
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
      console.log(`每页 ${val} 条`);
      this.pageNum = 1;
      this.pageSize = val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
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

<template>
  <div style="padding: 20px;">
    <div style="margin-bottom: 15px;">
      <el-input
          v-model="goodsname"
          placeholder="查询"
          suffix-icon="el-icon-search"
          style="width: 200px;"
          @keyup.enter.native="loadPost">
      </el-input>
      <el-button type="primary" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
    </div>

    <!-- 表格容器 -->
    <div style="border: 1px solid #ebeef5; border-radius: 4px;">
      <el-table
          :data="tableData"
          :header-cell-style="{ background: '#204', color: '#fff' }"
          border
          style="width: 100%;">

        <el-table-column prop="id" label="ID" width="80" align="center">
        </el-table-column>
        <el-table-column prop="goodsname" label="商品名" wi  dth="180"></el-table-column>
        <el-table-column prop="price" label="价格" width="100" align="center"></el-table-column>
        <el-table-column prop="storage" label="库存" width="100" align="center"></el-table-column>
        <el-table-column prop="authorName" label="发布人" width="120" align="center"></el-table-column>
        <el-table-column label="类型" width="100" align="center">
          <span>视频</span>
        </el-table-column>
        <el-table-column prop="image" label="图片">
          <template slot-scope="scope">
            <img :src="fixurl(scope.row.image)" alt="fixurl(scope.row.image)" v-if="scope.row.image" style="max-width: 100px; max-height: 100px;">
            <span v-else>暂无图片</span>
          </template>
        </el-table-column>
        <el-table-column prop="authorNumber" label="联系方式" width="120" align="center"></el-table-column>
        <el-table-column prop="rateAvg" label="评分" width="120" align="center"></el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button size="small" type="primary" @click="viewDetail(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
        <!-- 空数据插槽 -->
        <template slot="empty">
          <div style="padding: 40px; text-align: center; color: #909399;">
            <i class="el-icon-picture-outline" style="font-size: 40px; margin-bottom: 10px;"></i>
            <p>暂无数据</p>
          </div>
        </template>
      </el-table>
    </div>

    <!-- 分页 -->
    <div style="margin-top: 20px; text-align: right;">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20, 30]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<style scoped>
</style>
