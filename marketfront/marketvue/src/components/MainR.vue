<script>
export default {
  name: 'PageMainR',
  data() {
    // const item = {
    //   date: '2016-05-02',
    //   name: '王小虎',
    //   address: '上海市普陀区金沙江路 1518 弄'
    // };
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      name:'',
    }
  },
  methods:{
    handleSizeChange(val) {
      console.log(`${val} items per page`);
      this.pageSize = val
      this.loadPost()//刷新
    },
    resetForm() {
      this.$refs.form.resetFields();
    },
    handleCurrentChange(val) {
      console.log(`current page: ${val}`);
      this.pageNum = val
      this.loadPost()
    },
    loadGet(){
      this.$axios.get(this.$httpUrl+'/user/list').then(res=>res.data).then(res=>{
        console.log(res)
      })
    },
    loadPost() {
      this.$axios.post(this.$httpUrl + '/user/listPageC1', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        param: {
          name: this.name
        },
        sortOrder: 'asc'  // asc: 升序，desc: 降序
      }).then(res => res.data).then(res => {
        if (res.code == '200') {
          this.tableData = res.data;
          this.total = res.total;
        } else {
          alert('获取失败');
        }
      });
    },

    // loadPost(){
    //   this.$axios.post(this.$httpUrl+'/user/listPageC1',{
    //     pageNum:this.pageNum,
    //     pageSize:this.pageSize,
    //     param: {
    //       name:this.name,//这里是查询条件
    //     },
    //
    //
    //       }
    //
    //   ).then(res=>res.data).then(res=>{
    //     console.log(res)
    //     console.log('AAAAAAAAAAA')
    //     if(res.code=='200'){
    //         this.tableData = res.data
    //         this.total = res.total
    //     }else{
    //       alert('获取失败')
    //     }
    //   })
    // },
  },
  beforeMount(){
    this.loadPost()
  }
}
</script>

<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="name" placeholder="请输入名字" suffix-icon="el-icon-search" style="width: 200px"
                @keyup.enter.native="loadPost"></el-input>

      <el-button type="primary" style="margin-left: 5px" @click="loadPost">查询</el-button>
      <el-button type="warning" @click="resetForm">重置</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background: '#ee00aa' ,color: '#fff'}"
              border
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="120">
      </el-table-column>
      <el-table-column prop="kindvalue" label="善意值" width="120">

      </el-table-column>
      <el-table-column prop="explevel" label="用户等级" >
        <template v-slot="scope">
          <el-tag
              :type="scope.row.explevel < '3' ? 'success' : 'warning' "
              disable-transitions>{{scope.row.explevel}}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[ 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</template>

<style scoped>

</style>