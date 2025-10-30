<template>

  <div>
    <div style="margin-bottom: 5px;">
      <el-input v-model="goodsname" placeholder="请输入你的商品名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>

      <el-button type="primary" style="margin-left: 5px;" @click="add">新增</el-button>

    </div>
    <el-alert
        v-if="user && user.roleid < 2"
        title="您是管理员，可以管理所有商品"
        type="info"
        show-icon        style="margin-bottom: 10px;">
    </el-alert>
    <el-table :data="tableData"
              :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
              border
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="goodsname" label="商品名" width="180">
      </el-table-column>
      <el-table-column prop="price" label="价格" width="180">
      </el-table-column>
      <el-table-column prop="storage" label="库存">
      </el-table-column>
      <el-table-column prop="image" label="图片">
        <template slot-scope="scope">
          <img :src="fixurl(scope.row.image)" alt="fixurl(scope.row.image)" v-if="scope.row.image" style="max-width: 100px; max-height: 100px;">
          <span v-else>暂无图片</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
              style="margin-left: 5px;"
          >
            <el-button slot="reference" size="small" type="danger" style="margin-left: 5px;" >删除</el-button>
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

    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>

      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="商品名" prop="goodsname">
          <el-col :span="20">
            <el-input v-model="form.goodsname"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.price"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="库存" prop="storage">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.storage"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="tag" prop="tag">
          <el-select v-model="form.tag" placeholder="请选择类型" style="width: 200px;">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-model="form.image" label="显示图片" prop="image">
          <el-upload
              class="upload-demo"
              action="http://localhost:8095/upload/uploadit"
              multiple
              :limit="3"
              :file-list="fileList"
              :on-success="handleSuccess"
              :show-file-list="true"
              :before-upload="beforeAvatarUpload"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

import  ELEMENT from "element-ui";

export default {
  name: "StorageManage",
  data() {
    return {
      options: [{
        value: '1',
        label: '日用'
      }, {
        value: '2',
        label: '游戏'
      }, {
        value: '3',
        label: '萌宠'
      }, {
        value: '4',
        label: '书刊'
      }, {
        value: '5',
        label: '音乐'
      }],
      // 缓存 receiver -> name
      fileList: [],
      tableData: [],
      pageSize:10,
      pageNum:1,
      total:0,
      goodsname:'',
      centerDialogVisible:false,
      form:{
        id:'',
        goodsname:'',
        price:'',
        storage:'',
        authorid:'',
        image:'',
        tag:'',
      },
      form2:{
        no: '',
        name:'',
        password: '',
        account:''
      },
      rules: {
        goodsname: [
          {required: true, message: '请输入商品名', trigger: 'blur'}
        ],
        price: [
          {required: true, message: '请输入价格', trigger: 'blur'}
        ]
      },
      user:JSON.parse(sessionStorage.getItem('User')),
    }
  },
  methods:{
    resetForm() {
      this.$refs.form.resetFields();
      this.form.fileList = {};
    },
    //  getname(receiver){
    //   if(receiver==0){
    //     console.log('无')
    //     return '无'
    //   }else {
    //     let name =this.$axios.get(this.$httpUrl + '/user/findById?id=' + receiver)
    //     name=name.data
    //     return name
    //   }
    // },

    del(id){
      console.log(id)

      this.$axios.get(this.$httpUrl+'/goods/delete?id='+id).then(res=>res.data).then(res=>{
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
    fixurl(fileName){
        return `http://localhost:8095/images/${fileName}`;
    },
    // kick(row) {
    //   this.form.id = row.id
    //   this.form.name = row.name
    //   this.form.remark = row.remark
    //   this.form.level = row.level || 1;
    //   this.form.author = row.author
    //   this.form.receiver=0;
    //   this.$axios.post(this.$httpUrl+'/storage/update',this.form).then(res=>res.data).then(res=>{
    //     console.log(res.code)
    //     if(res.code==200){
    //       this.$message({
    //         message: '操作成功！',
    //         type: 'success'
    //       });
    //       this.centerDialogVisible = false
    //       this.loadPost()
    //     }else{
    //       this.$message({
    //         message: '操作失败！',
    //         type: 'error'
    //       });
    //     }
    //
    //   })
    //
    // },
    // async cancel(id,leveld,receiver) {
    //   console.log(receiver+'QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ')
    //   console.log(typeof receiver)
    //   let username = await this.$axios.get(this.$httpUrl+'/user/findById?id='+receiver)
    //   username=username.data
    //   if (!username) {
    //     this.$message.warning("用户名不能为空！");
    //     return;
    //   }
    //   let kindres=await this.$axios.post(this.$httpUrl+'/user/getk',{
    //     name: username,
    //   })
    //   let kind=kindres.data
    //   console.log('AAA'+kind+'BBB')
    //   kind=parseInt(kind,10)
    //   console.log('BBB'+kind+'AAA')
    //   console.log(typeof kind, kind)
    //
    //   try {
    //     // 调用接口查询用户是否存在
    //     console.log(kind)
    //
    //     if(kind==-245678){
    //       this.$message.warning("用户不存在！");
    //       return;
    //     }else{
    //       kind+=leveld*100
    //       let exp=kind/1000+1
    //       this.$axios.post(this.$httpUrl+'/user/cancel',{
    //         name: username,
    //         kindvalue: kind,
    //         explevel: exp,
    //       }).then(res=>res.data).then(res=>{
    //         console.log(res)
    //         if(res.code==200){
    //
    //           this.$message({
    //             message: '操作成功！',
    //             type: 'success'
    //           });
    //           this.del(id)
    //           this.loadPost()
    //         }else{
    //           this.$message({
    //             message: '操作失败！',
    //             type: 'error'
    //           });
    //         }
    //
    //       })
    //
    //     }
    //   } catch (error) {
    //     console.error(error);
    //     this.$message.error("请求失败，请重试");
    //   }
    // },

    mod(row){
      this.centerDialogVisible = true
      this.$nextTick(()=>{
        //赋值到表单
        this.form.id = row.id
        this.form.goodsname = row.goodsname
        this.form.authorid = row.authorid
        this.form.storage = row.storage
        this.form.price = row.price
        this.form.tag = row.tag || 1;
      })
    },
    handleSuccess(response, file, fileList) {
      console.log('上传成功:', response, file, fileList);
      // 根据后端实际返回格式调整
      if (response) {
        if (response.data) {
          this.form.image = response.data;
        } else if (response.url) {
          this.form.image = response.url;
        } else {
          this.form.image = file.name;
        }
      } else {
        this.form.image = file.name;
      }
      this.fileList = fileList;
      this.$message.success('上传成功');
    },


    add(){

      this.centerDialogVisible = true
      this.$nextTick(()=>{
        this.resetForm()
      })

    },
    generateRandomId() {
      // 结合时间戳和随机数生成更唯一的ID
      const timestamp = Date.now() % 1000000; // 获取时间戳的后6位
      const randomNum = Math.floor(Math.random() * 1000); // 三位随机数
      // 组合生成六位数ID
      return (timestamp + randomNum) % 900000 + 100000;
    },
    doSave(){
      this.form.authorid = this.user.id;
      this.form.id=this.generateRandomId()
      this.$axios.post(this.$httpUrl+'/goods/save',this.form).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code===200){

          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
          this.resetForm()
        }else{
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }

      })
    },
    doMod(){
      this.form.author = this.user?.name;
      this.$axios.post(this.$httpUrl+'/goods/update',this.form).then(res=>res.data).then(res=>{
        console.log(res+"ERER")
        if(res.code===200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
          this. resetForm()
        }else{
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }

      })
    },
    save(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          if(this.form.id){
            this.doMod();
          }else{
            this.doSave();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });

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
      let authorid = this.user.id
      if(this.user.roleid===1){
        authorid=-1
      }
      this.$axios.post(this.$httpUrl+'/goods/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          goodsname:this.goodsname,
          authorid:authorid,
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
    beforeAvatarUpload(file){
      console.log( file,"file")
      this.form.image = file.name
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isPDF = file.type === 'application/pdf';
      const isXLSX = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';  // 原代码不完整
      const isXLS = file.type === 'application/vnd.ms-excel';
      const islt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isPNG && !isPDF && !isXLSX && !isXLS) {  // 原代码逻辑错误，少了!
        ELEMENT.Message.error("上传文件格式只能是 JPG/PNG/PDF/XLS/XLSX 格式!");
      }

      if (!islt2M) {
        ELEMENT.Message.error("上传头像图片大小不能超过 2MB!");
      }

      return (isJPG || isPNG || isPDF || isXLSX || isXLS) && islt2M;
    }
  },
  beforeMount() {
    this.loadPost()
  },

}
</script>

<style scoped>

</style>