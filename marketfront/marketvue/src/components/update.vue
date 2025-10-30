<script>
export default {
  name: "MyLogin",
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
        no: '',
        password: '',
        name:'',
        account:'',
        roleid:''
      },
      rules: {
        no: [
          { required: true, message: '请输入账号', trigger: 'blur' },
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
    toHome() {
      this.$router.replace('/FrontPage')
    },
    confirm() {
      this.confirm_disabled = true;
      this.loginForm.name=this.loginForm.no;
      const user = JSON.parse(sessionStorage.getItem('User'));
      if (user && user.id) {
        this.loginForm.id = user.id;
        this.loginForm.kindvalue=user.kindvalue;
        this.loginForm.explevel=user.explevel;
      } else {
        console.error('User data is invalid or missing');
      }

      console.log(this.loginForm.id);
      this.$refs.loginForm.validate((valid) => {
        if (valid) {

          this.$axios.post(this.$httpUrl+'/user/mod',
              this.loginForm,
          ).then(res => res.data).then(res => {
            console.log(res);
            if(res.code == 200) {
              // 存储用户信息
              sessionStorage.setItem("User", JSON.stringify(res.data));
              // 跳转到主页
              this.$router.replace('/FrontPage')
            } else {
              this.confirm_disabled = false;
              alert('校验失败，用户名或密码错误！');
              return false;
            }
          }).catch(error => {
            this.confirm_disabled = false;
            console.error('请求失败:', error);
          });
        } else {
          this.confirm_disabled = false;
          console.log('校验失败');
          return false;
        }
      });
    }
  }
}
</script>

<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="login-content">
        <h1 class="login-title">修改你的账号和密码</h1>
        <el-form :model="loginForm" label-width="100px" :rules="rules" ref="loginForm">
          <el-form-item label="账号" prop="no">
            <el-input style="solid-color: plum" type="text" v-model="loginForm.no" autocomplete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input style="solid-color: plum" type="password" v-model="loginForm.password" show-password autocomplete="off" size="small" @keyup.enter.native="confirm"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="confirm" :disabled="confirm_disabled">确定</el-button>
            <el-button @click="toHome">返回</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.loginBody {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: #30e05b;
}

.loginDiv {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -200px;
  margin-left: -250px;
  width: 450px;
  height: 330px;
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