<template>

  <div v-if="user && user.roleid < 2">
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="请输入你的商品名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
    </div>
    <el-alert
        v-if="user && user.roleid < 2"
        title="您是管理员，可以管理用户列表"
        type="info"
        show-icon        style="margin-bottom: 10px;">
    </el-alert>
    <el-table :data="tableData"
              :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
              border
    >
      <el-table-column prop="id" label="ID" width="180">
      </el-table-column>
      <el-table-column prop="name" label=" 用户名" width="180">
      </el-table-column>
      <el-table-column prop="roleid" label="权限等级" width="180">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-popconfirm
              v-if="scope.row.roleid<=1"
              title="确定封杀该用户吗吗？"
              @confirm="del(scope.row.id)"
              style="margin-left: 5px;"
          >
            <el-button slot="reference" size="small" type="danger" style="margin-left: 5px;" >封杀</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>


    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20,30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

  </div>
  <div v-else>
    <el-alert
        title="您没有权限访问此页面"
        type="error"
        show-icon        style="margin-top: 20px;">
    </el-alert>
  </div>
</template>

<script>



export default {
  name: "StorageManage",
  data() {
    return {
      // 缓存 receiver -> name
      fileList: [],
      tableData: [],
      pageSize:10,
      pageNum:1,
      total:0,
      goodsname:'',
      centerDialogVisible:false,
      form2:{
        no: '',
        name:'',
        password: '',
        account:''
      },
      rules: {
        goodsname: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
      },
      user:JSON.parse(sessionStorage.getItem('User')),
    }
  },
  methods:{
    resetForm() {
      this.$refs.form.resetFields();
    },

    del(id){
      console.log(id)

      this.$axios.get(this.$httpUrl+'/user/delete?id='+id).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code===200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.loadPost()
        }else{
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }

      })

    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageNum=1
      this.pageSize=val
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val
      this.loadPost()
    },
    resetParam(){
      this.name=''
    },
    loadPost(){
      console.log(this.data)
      this.$axios.post(this.$httpUrl+'/user/listPageC1',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){
          this.tableData=res.data
          this.total=res.total
        }else{
          alert('获取数据失败')
        }

      })
    },

  },
  beforeMount() {
    this.loadPost()
  },

}
</script>

<style scoped>

</style>