<template>
  <div class="user-profile-container">
    <!-- 用户信息头部 -->
    <div class="user-header">
      <el-avatar :size="80" :src="fixavatar(user.image)" class="user-avatar">
        {{ user.name ? user.name.charAt(0) : 'U' }}
      </el-avatar>
      <div class="user-info">
        <h1 class="user-greeting">{{ '欢迎你！' + user.name }}</h1>
        <p class="user-role">
          <el-tag
              :type="
              user.roleid < 2 ? 'danger' :
              user.roleid < 3 ? 'warning' : 'success'
            "
              size="medium">
            {{
              user.roleid < 2 ? '超级管理员' :
                  user.roleid < 3 ? '管理员' : '普通用户'
            }}
          </el-tag>
        </p>
      </div>
    </div>

    <!-- 上部分：个人信息和今日信息 -->
    <div class="top-section">
      <!-- 个人信息卡片 -->
      <el-card class="info-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span class="card-title">个人信息</span>
          </div>
        </template>

        <div class="info-grid">
          <div class="info-item">
            <div class="info-label">
              <i class="el-icon-user"></i>
              用户名
            </div>
            <div class="info-value">{{ user.name }}</div>
          </div>

          <div class="info-item">
            <div class="info-label">
              <i class="el-icon-wallet"></i>
              账户余额
            </div>
            <div class="info-value balance">¥{{ user.account || '0.00' }}</div>
          </div>

          <div class="info-item">
            <div class="info-label">
              <i class="el-icon-phone"></i>
              联系方式
            </div>
            <div class="info-value">{{ user.number || '未设置' }}</div>
          </div>

          <div class="info-item">
            <div class="info-label">
              <i class="el-icon-s-operation"></i>
              用户权限
            </div>
            <div class="info-value">
              <el-tag
                  :type="
                  user.roleid < 2 ? 'danger' :
                  user.roleid < 3 ? 'warning' : 'success'
                "
                  effect="plain">
                {{
                  user.roleid < 2 ? '超级管理员' :
                      user.roleid < 3 ? '管理员' : '普通用户'
                }}
              </el-tag>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 今日信息卡片 -->
      <el-card class="today-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span class="card-title">今日信息</span>
          </div>
        </template>
        <DateUtils></DateUtils>
      </el-card>
    </div>

    <!-- 下部分：日历 -->
    <div class="bottom-section">
      <el-card class="calendar-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span class="card-title">日历</span>
          </div>
        </template>
        <el-calendar v-model="value" class="custom-calendar">
          <template #date-cell="{ data }">
            <div class="calendar-date">
              {{ data.day.split('-').slice(2).join('-') }}
            </div>
          </template>
        </el-calendar>
      </el-card>
    </div>
  </div>
</template>

<script>
import DateUtils from "./DataUtils";
export default {
  name: "HomePage",
  components: {DateUtils},
  data() {
    return {
      user:{},
      value: new Date()
    }
  },
  methods:{
    init(){
      this.user = JSON.parse(sessionStorage.getItem('User')) || {
        name: '用户',
        account: '0.00',
        roleid: 3,
        number: '',
        image: ''
      };
    },
    fixavatar(fileName){
      return fileName ? `http://localhost:8095/heads/${fileName}` : '';
    },
  },
  created(){
    this.init()
  }
}
</script>

<style scoped>
.user-profile-container {
  min-height: 100vh;
  background: #f8f9fa;
  padding: 24px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 用户头部 */
.user-header {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 0 8px;
}

.user-avatar {
  border: 2px solid #e1e5e9;
  background: #fff;
}

.user-info {
  flex: 1;
}

.user-greeting {
  font-size: 28px;
  font-weight: 500;
  color: #1a1a1a;
  margin: 0 0 8px 0;
}

.user-role {
  margin: 0;
}

/* 上部分：个人信息和今日信息 */
.top-section {
  display: grid;
  grid-template-columns: 2fr 1fr; /* 左边占比更多 */
  gap: 24px;
  align-items: start;
}

/* 卡片样式 */
.info-card,
.today-card,
.calendar-card {
  border-radius: 12px;
  border: 1px solid #e1e5e9;
  background: #fff;
}

.info-card {
  min-height: 300px;
}

.today-card {
  min-height: 300px;
  display: flex;
  flex-direction: column;
}

.calendar-card {
  min-height: 500px;
}

.card-header {
  border-bottom: 1px solid #f0f0f0;
  padding: 0 !important;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  padding: 16px 20px;
  display: block;
}

/* 信息网格 */
.info-grid {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 8px 4px;
}

.info-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-radius: 8px;
  transition: background-color 0.2s ease;
}

.info-item:hover {
  background: #f8f9fa;
}

.info-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.info-label i {
  color: #409eff;
  font-size: 16px;
}

.info-value {
  font-size: 15px;
  color: #1a1a1a;
  font-weight: 500;
}

.balance {
  color: #f56c6c;
  font-weight: 600;
}

/* 今日信息卡片 */
:deep(.today-card .el-card__body) {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

/* 日历样式 */
.custom-calendar {
  border: none;
}

:deep(.el-calendar__header) {
  border-bottom: 1px solid #f0f0f0;
  padding: 16px 20px;
  margin-bottom: 0;
}

:deep(.el-calendar-table) {
  border-spacing: 0;
}

:deep(.el-calendar-table thead th) {
  color: #666;
  font-weight: 500;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

:deep(.el-calendar-day) {
  height: 36px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
}

.calendar-date {
  font-size: 13px;
  color: #1a1a1a;
}

:deep(.el-calendar-table .el-calendar-day:hover) {
  background-color: #f0f7ff;
  color: #409eff;
  border-radius: 6px;
}

:deep(.el-calendar-table td.is-selected) {
  background-color: #409eff;
  border-radius: 6px;
}

:deep(.el-calendar-table td.is-selected .calendar-date) {
  color: white;
}

:deep(.el-calendar-table td.is-today) {
  background-color: #f0f7ff;
  border-radius: 6px;
}

:deep(.el-calendar-table td.is-today .calendar-date) {
  color: #409eff;
  font-weight: 600;
}

/* 标签样式 */
:deep(.el-tag) {
  border: none;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-profile-container {
    padding: 16px;
    gap: 20px;
  }

  .top-section {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .user-header {
    flex-direction: column;
    text-align: center;
    gap: 16px;
  }

  .user-greeting {
    font-size: 24px;
  }

  .info-grid {
    gap: 16px;
  }

  .info-item {
    padding: 10px 12px;
  }

  .today-card,
  .info-card {
    min-height: 200px;
  }
}
</style>