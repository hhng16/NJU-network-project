<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="login-content">
        <div class="header-section">
          <h1 class="login-title">用户注册</h1>
          <p class="login-subtitle">创建您的专属账户</p>
        </div>

        <el-form :model="loginForm" label-width="100px"
                 :rules="rules" ref="loginForm">
          <el-form-item label="用户名" prop="name">
            <el-input style="width: 100%" type="text" v-model="loginForm.name"
                      autocomplete="off" size="medium" placeholder="请输入用户名">
              <i slot="prefix" class="el-input__icon el-icon-user"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input style="width: 100%" type="password" v-model="loginForm.password"
                      show-password autocomplete="off" size="medium"
                      placeholder="请输入密码" @keyup.enter.native="register">
              <i slot="prefix" class="el-input__icon el-icon-lock"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="联系方式" prop="number">
            <el-input style="width: 100%" type="text" v-model="loginForm.number"
                      autocomplete="off" size="medium" placeholder="请输入手机号">
              <i slot="prefix" class="el-input__icon el-icon-mobile-phone"></i>
            </el-input>
          </el-form-item>
          <el-form-item class="button-group">
            <el-button type="primary" @click="register" :disabled="confirm_disabled" class="register-btn">
              <i class="el-icon-check"></i> 注册账户
            </el-button>
            <el-button @click="toLogin" class="login-btn">
              <i class="el-icon-user"></i> 去 登 录
            </el-button>
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
        imgae:'',
        roleid: 3
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
  background-image: url('../assets/loginBackground.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

.loginBody::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 0;
}

.loginDiv {
  width: 480px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  position: relative;
  z-index: 1;
}

.loginDiv:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
}

.login-content {
  padding: 40px;
}

.header-section {
  text-align: center;
  margin-bottom: 30px;
}

.login-title {
  margin: 0 0 10px 0;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  letter-spacing: 1px;
}

.login-subtitle {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.el-form-item {
  margin-bottom: 22px;
}

.el-form-item:last-child {
  margin-bottom: 0;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
}

.register-btn {
  flex: 1;
  margin-right: 10px;
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  border: none;
  border-radius: 6px;
  padding: 12px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(106, 17, 203, 0.4);
}

.login-btn {
  flex: 1;
  margin-left: 10px;
  border-radius: 6px;
  padding: 12px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.login-btn:hover {
  background-color: #f5f7fa;
  transform: translateY(-2px);
}

.footer-section {
  margin-top: 25px;
  text-align: center;
}

.agreement-text {
  font-size: 12px;
  color: #909399;
  margin: 0;
}

.agreement-text a {
  color: #2575fc;
  text-decoration: none;
  transition: color 0.3s ease;
}

.agreement-text a:hover {
  color: #6a11cb;
  text-decoration: underline;
}

:deep(.el-input__inner) {
  border-radius: 6px;
  padding-left: 40px;
  transition: all 0.3s ease;
}

:deep(.el-input__inner:focus) {
  border-color: #6a11cb;
  box-shadow: 0 0 0 2px rgba(106, 17, 203, 0.1);
}

:deep(.el-input__prefix) {
  left: 12px;
  color: #c0c4cc;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .loginDiv {
    width: 90%;
    margin: 0 auto;
  }

  .login-content {
    padding: 30px 25px;
  }

  .button-group {
    flex-direction: column;
  }

  .register-btn, .login-btn {
    margin: 5px 0;
    width: 100%;
  }
}
</style>