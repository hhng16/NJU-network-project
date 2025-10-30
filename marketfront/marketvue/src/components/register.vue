<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="login-content">
        <h1 class="login-title">用户注册</h1>
        <el-form :model="loginForm" label-width="100px"
                 :rules="rules" ref="loginForm">
          <el-form-item label="用户名" prop="name">
            <el-input style="width: 200px" type="text" v-model="loginForm.name"
                      autocomplete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input style="width: 200px" type="password" v-model="loginForm.password"
                      show-password autocomplete="off" size="small" @keyup.enter.native="register"></el-input>
          </el-form-item>
          <el-form-item label="联系方式" prop="number">
            <el-input style="width: 200px" type="text" v-model="loginForm.number"
                      autocomplete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="register" :disabled="confirm_disabled">确 定</el-button>
            <el-button @click="toLogin">去 登 录</el-button>
          </el-form-item>

        </el-form>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  name: "AppRegister",
  data() {
    let checkDuplicate = (rule, value, callback) => {
      if (this.loginForm.id) {
        return callback();
      }
      this.$axios.get(this.$httpUrl+"/user/findByName?name=" + this.loginForm.name).then(res=>res.data).then(res => {
        if (res.code !== 200) {//es6解构也可以
          callback();
        } else {
          callback(new Error('用户名已经存在'));
        }
      });
    };
    return {
      confirm_disabled: false,
      loginForm: {
        id: '',
        name: '',
        password: '',
        account:0,
        number: '',
        imgae:''
      },
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          {validator: checkDuplicate, trigger: 'blur'},
          { min: 1, max: 20, message: '用户名长度在1-20之间', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, message: '密码至少1位', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    toLogin() {
      this.$router.push('/');
    },

    register(){
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.doRegister();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },


    doRegister() {
      this.loginForm.no  = this.loginForm.name;
      this.$axios.post(this.$httpUrl + '/user/register', this.loginForm).then(res => res.data).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$message({
            message: '操作成功!',
            type: 'success'
          });

        } else {
          this.$message({
            message: '操作失败!请返回重新操作...',
            type: 'error'
          });
        }
      });
    },
  }
}
</script>

<style scoped>
.loginBody {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: #B3C0D1;
}

.loginDiv {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -200px;
  margin-left: -250px;
  width: 450px;
  height: 400px;
  background: #fff;
  border-radius: 5%;

}

.login-title {
  margin: 20px 0;
  text-align: center;
}

.login-content {
  width: 400px;
  height: 250px;
  position: absolute;
  top: 25px;
  left: 25px;
}
</style>