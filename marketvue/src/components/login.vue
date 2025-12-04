<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="login-content">
        <div class="header-section">
          <h1 class="login-title">用户登录</h1>
          <p class="login-subtitle">欢迎回来，请登录您的账户</p>
        </div>

        <el-form :model="loginForm" label-width="100px" :rules="rules" ref="loginForm">
          <el-form-item label="账号" prop="no">
            <el-input style="width: 100%" type="text" v-model="loginForm.no"
                      autocomplete="off" size="medium" placeholder="请输入账号">
              <i slot="prefix" class="el-input__icon el-icon-user"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input style="width: 100%" type="password" v-model="loginForm.password"
                      show-password autocomplete="off" size="medium"
                      placeholder="请输入密码" @keyup.enter.native="confirm">
              <i slot="prefix" class="el-input__icon el-icon-lock"></i>
            </el-input>
          </el-form-item>
          <el-form-item class="button-group">
            <el-button type="primary" @click="confirm" :disabled="confirm_disabled" class="login-btn">
              <i class="el-icon-s-promotion"></i> 登录
            </el-button>
            <el-button @click="toRegister" class="register-btn">
              <i class="el-icon-user"></i> 去 注 册
            </el-button>
          </el-form-item>
        </el-form>

        <div class="footer-section">
          <p class="forgot-password">
            <a href="#" @click.prevent="forgotPassword">忘记密码?</a>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MyLogin",
  data() {
    return {
      confirm_disabled: false,
      loginForm: {
        no: '',
        password: ''
      },
      rules: {
        no: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    toRegister(){
      this.$router.replace('/register');
    },
    confirm() {
      this.confirm_disabled = true;
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$axios.post(this.$httpUrl+'/user/login',
              this.loginForm,
          ).then(res => res.data).then(res => {
            console.log(res);
            if(res.code == 200) {
              // 存储用户信息
              sessionStorage.setItem("User", JSON.stringify(res.data));
              this.$message({
                message: '登录成功！',
                type: 'success'
              });
              this.$router.replace('/FrontPage')
            } else {
              this.confirm_disabled = false;
              this.$message.error('校验失败，用户名或密码错误！');
              return false;
            }
          }).catch(error => {
            this.confirm_disabled = false;
            console.error('请求失败:', error);
            this.$message.error('网络错误，请稍后重试！');
          });
        } else {
          this.confirm_disabled = false;
          console.log('校验失败');
          return false;
        }
      });
    },
    forgotPassword() {
      this.$message.info('请联系管理员重置密码');
    }
  }
}
</script>

<style scoped>
.loginBody {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: url('../assets/background.jpg');
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
  justify-content: center;
  margin-top: 30px;
  gap: 15px;
}

.login-btn {
  flex: none;
  width: 120px;
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  border: none;
  border-radius: 6px;
  padding: 12px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(106, 17, 203, 0.4);
}

.login-btn:disabled {
  background: #c0c4cc;
  transform: none;
  box-shadow: none;
}

.register-btn {
  flex: none;
  width: 120px;
  border-radius: 6px;
  padding: 12px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
  border: 1px solid #dcdfe6;
}

.register-btn:hover {
  background-color: #f5f7fa;
  transform: translateY(-2px);
  border-color: #c0c4cc;
}

.footer-section {
  margin-top: 25px;
  text-align: center;
}

.forgot-password {
  margin: 0;
}

.forgot-password a {
  color: #2575fc;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
}

.forgot-password a:hover {
  color: #6a11cb;
  text-decoration: underline;
}

/* 美化输入框 */
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
    flex-direction: row;
    justify-content: center;
    gap: 10px;
  }

  .login-btn, .register-btn {
    width: 110px;
  }
}

@media (max-width: 480px) {
  .button-group {
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }

  .login-btn, .register-btn {
    width: 100%;
    max-width: 200px;
  }
}
</style>