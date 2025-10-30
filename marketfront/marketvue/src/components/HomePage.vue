<template>
  <div style="text-align: center;background-color: #eeeeee;height: 100%;padding: 0px;margin: 0px;">
    <h1 style="font-size: 50px;">{{'欢迎你！'+user.name}}</h1>
    <el-descriptions  title="个人中心" :column="2" size="40" border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-custom"></i>
          用户名
        </template>
        <div style="text-align: center;">{{user.name}}</div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-apple"></i>
          余额
        </template>
        <div style="text-align: center;">{{user.account}}</div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          权限
        </template>
        <div style="text-align: center;">
          <el-tag
              :type="
                user.roleid < 2 ? 'danger' :
                user.roleid < 3 ? 'warning' : 'success'
              "              style="align-content: center"
              disable-transitions>
            {{
              user.roleid < 2 ? '超级管理员' :
                  user.roleid < 3 ? '管理员' : '用户'
            }}
          </el-tag>
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label" >
          <i class="el-icon-magic-stick"></i>
          联系方式
        </template>
        <div style="text-align: center;">{{user.number}}</div>
      </el-descriptions-item>
    </el-descriptions>
    <DateUtils></DateUtils>
    <el-calendar v-model="value">
    </el-calendar>
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
  computed:{

  },
  methods:{
    init(){
      this.user = JSON.parse(sessionStorage.getItem('User'))
    },
    fixavatar(fileName){
      return `http://localhost:8095/heads/${fileName}`;
    },
  },
  created(){
    this.init()
  }
}
</script>

<style scoped>
.el-descriptions{
  width:90%;

  margin: 0 auto;
  text-align: center;
}
</style>