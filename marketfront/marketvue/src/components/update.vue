<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="login-content">
        <div class="header-section">
          <div class="avatar-section">
            <!-- 使用 el-upload 组件，完全按照商品管理的模式 -->
            <el-upload
                class="avatar-uploader"
                action="http://localhost:8095/upload/uploadit"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
            >
              <div v-if="avatarUrl" class="avatar-container">
                <img :src="avatarUrl" alt="用户头像" class="user-avatar" />
                <div class="avatar-overlay">
                  <i class="el-icon-camera"></i>
                </div>
              </div>
              <div v-else class="avatar-placeholder">
                <i class="el-icon-plus avatar-icon"></i>
                <span class="avatar-text">点击上传头像</span>
              </div>
            </el-upload>
          </div>
          <h1 class="login-title">修改账户信息</h1>
          <p class="login-subtitle">更新您的账号、密码和头像</p>
        </div>

        <el-form :model="loginForm" label-width="80px" :rules="rules" ref="loginForm">
          <el-form-item label="账号" prop="no">
            <el-input
                style="width: 100%"
                type="text"
                v-model="loginForm.no"
                autocomplete="off"
                size="medium"
                placeholder="请输入新账号"
            >
              <i slot="prefix" class="el-input__icon el-icon-user"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
                style="width: 100%"
                type="password"
                v-model="loginForm.password"
                show-password
                autocomplete="off"
                size="medium"
                placeholder="请输入新密码"
                @keyup.enter.native="confirm"
            >
              <i slot="prefix" class="el-input__icon el-icon-lock"></i>
            </el-input>
          </el-form-item>
          <el-form-item class="button-group">
            <el-button
                type="primary"
                @click="confirm"
                :disabled="confirm_disabled"
                :loading="loading"
                class="confirm-btn"
            >
              <i class="el-icon-check"></i> 确认修改
            </el-button>
            <el-button @click="toHome" class="back-btn">
              <i class="el-icon-back"></i> 返回
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MyLogin",
  data() {
    let checkDuplicate = (rule, value, callback) => {
      if (this.loginForm.id) {
        return callback();
      }
      this.$axios.get(this.$httpUrl+"/user/findByName?name=" + this.loginForm.name).then(res=>res.data).then(res => {
        if (res.code !== 200) {
          callback();
        } else {
          callback(new Error('用户名已经存在'));
        }
      });
    };
    return {
      confirm_disabled: false,
      loading: false,
      avatarUrl: '',
      loginForm: {
        id: '',
        no: '',
        password: '',
        name:'',
        account:'',
        roleid:'',
        image: ''  // 关键：添加image字段，用于保存头像文件名
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
  mounted() {
    this.initUserData();
  },
  methods: {
    initUserData() {
      const user = JSON.parse(sessionStorage.getItem('User'));
      if (user) {
        this.loginForm.id = user.id;
        this.loginForm.no = user.no || '';
        this.loginForm.name = user.name || '';
        this.loginForm.account = user.account || '';
        this.loginForm.roleid = user.roleid || '';
        this.loginForm.kindvalue = user.kindvalue;
        this.loginForm.explevel = user.explevel;

        // 关键：设置image字段，确保表单提交时包含头像数据
        if (user.avatar) {
          this.loginForm.image = user.avatar;
          this.avatarUrl = this.fixAvatarUrl(user.avatar);
        } else if (user.image) {
          this.loginForm.image = user.image;
          this.avatarUrl = this.fixAvatarUrl(user.image);
        }

        console.log('初始化用户数据:', this.loginForm);
      }
    },

    // 使用与商品管理相同的fixurl方法
    fixAvatarUrl(fileName) {
      if (!fileName) return '';
      return `http://localhost:8095/images/${fileName}`;
    },

    toHome() {
      this.$router.replace('/FrontPage')
    },

    // 关键：完全按照商品管理的handleSuccess方法
    handleAvatarSuccess(response, file, fileList) {
      console.log('头像上传成功:', response, file, fileList);

      let fileName = '';
      // 根据后端实际返回格式调整
      if (response) {
        if (response.data) {
          fileName = response.data;
        } else if (response.url) {
          fileName = response.url;
        } else {
          fileName = file.name;
        }
      } else {
        fileName = file.name;
      }

      console.log('获取到的文件名:', fileName);

      // 关键：将文件名保存到loginForm.image中，确保提交时包含头像数据
      this.loginForm.image = fileName;

      // 更新头像显示
      this.avatarUrl = this.fixAvatarUrl(fileName);

      // 更新sessionStorage中的用户信息
      const user = JSON.parse(sessionStorage.getItem('User'));
      if (user) {
        user.avatar = fileName;
        user.image = fileName;  // 同时更新image字段
        sessionStorage.setItem("User", JSON.stringify(user));
      }

      this.$message.success('头像上传成功');
    },

    // 上传前验证
    beforeAvatarUpload(file) {
      console.log('上传文件信息:', file);

      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isPNG) {
        this.$message.error('头像图片只能是 JPG/PNG 格式!');
        return false;
      }
      if (!isLt2M) {
        this.$message.error('头像图片大小不能超过 2MB!');
        return false;
      }

      return true;
    },

    confirm() {
      this.confirm_disabled = true;
      this.loading = true;
      this.loginForm.name = this.loginForm.no;

      const user = JSON.parse(sessionStorage.getItem('User'));
      if (user && user.id) {
        this.loginForm.id = user.id;
        this.loginForm.kindvalue = user.kindvalue;
        this.loginForm.explevel = user.explevel;
      } else {
        console.error('User data is invalid or missing');
        this.$message.error('用户信息无效，请重新登录');
        this.confirm_disabled = false;
        this.loading = false;
        return;
      }

      console.log('提交的用户数据（包含头像）:', this.loginForm);

      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$axios.post(this.$httpUrl+'/user/mod', this.loginForm)
              .then(res => res.data)
              .then(res => {
                console.log('用户信息修改响应:', res);
                if(res.code == 200) {
                  // 更新sessionStorage中的用户信息
                  sessionStorage.setItem("User", JSON.stringify(res.data));
                  this.$message({
                    message: '信息修改成功！',
                    type: 'success'
                  });
                  // 跳转到主页
                  this.$router.replace('/FrontPage')
                } else {
                  this.confirm_disabled = false;
                  this.loading = false;
                  this.$message.error(res.msg || '修改失败，请重试');
                }
              }).catch(error => {
            this.confirm_disabled = false;
            this.loading = false;
            console.error('请求失败:', error);
            this.$message.error('网络错误，请稍后重试');
          });
        } else {
          this.confirm_disabled = false;
          this.loading = false;
          console.log('校验失败');
          this.$message.warning('请完善表单信息');
        }
      });
    }
  }
}
</script>

<style scoped>
.loginBody {
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

.loginDiv {
  width: 500px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.loginDiv:hover {
  transform: translateY(-5px);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.2);
}

.login-content {
  padding: 40px;
}

.header-section {
  text-align: center;
  margin-bottom: 30px;
}

.avatar-section {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

/* 头像上传样式 */
.avatar-uploader {
  text-align: center;
  cursor: pointer;
}

.avatar-container {
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px dashed #dcdfe6;
  transition: all 0.3s ease;
}

.avatar-container:hover {
  border-color: #667eea;
  transform: scale(1.05);
}

.user-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-container:hover .avatar-overlay {
  opacity: 1;
}

.avatar-overlay i {
  color: white;
  font-size: 24px;
}

.avatar-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #c0c4cc;
  transition: color 0.3s ease;
  width: 120px;
  height: 120px;
  border: 2px dashed #dcdfe6;
  border-radius: 50%;
}

.avatar-uploader:hover .avatar-placeholder {
  color: #667eea;
  border-color: #667eea;
}

.avatar-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.avatar-text {
  font-size: 12px;
  text-align: center;
}

.login-title {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.login-subtitle {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.el-form-item {
  margin-bottom: 20px;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 30px;
}

.confirm-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  padding: 12px 24px;
  font-weight: 500;
  transition: all 0.3s ease;
  min-width: 120px;
}

.confirm-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.confirm-btn:disabled {
  background: #c0c4cc;
  transform: none;
  box-shadow: none;
}

.back-btn {
  border-radius: 8px;
  padding: 12px 24px;
  font-weight: 500;
  transition: all 0.3s ease;
  min-width: 120px;
  border: 1px solid #dcdfe6;
}

.back-btn:hover {
  background-color: #f5f7fa;
  transform: translateY(-2px);
  border-color: #c0c4cc;
}

/* 美化输入框 */
:deep(.el-input__inner) {
  border-radius: 8px;
  padding-left: 40px;
  transition: all 0.3s ease;
  border: 1px solid #e4e7ed;
}

:deep(.el-input__inner:focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
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
    align-items: center;
  }

  .confirm-btn, .back-btn {
    width: 100%;
    max-width: 200px;
  }

  .avatar-container, .avatar-placeholder {
    width: 100px;
    height: 100px;
  }
}
</style>