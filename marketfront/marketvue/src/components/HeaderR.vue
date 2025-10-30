<script>
export default {
  name: 'PageHeaderR',
  data() {
    return {
      user:JSON.parse(sessionStorage.getItem('User')),
      sizeList: ["large", "medium", "small"]
    }
  },
  props: {
    icon: String
  },
  methods: {
    toUer() {
      this.$router.push('/HomePage')
    },
    logout() {

      this.$confirm('您确定要退出登录吗?', '提示', {
        confirmButtonText: '确定', //确认按钮的文字显示
        type: 'warning',
        center: true, //文字居中显示
      })
          .then(() => {
            this.$message({
              type: 'success',
              message: '退出登录成功'
            })
            this.$router.push("/")
            sessionStorage.clear()
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消退出'
            })
          })
    },
    fixavatar(fileName){
      return `http://localhost:8095/heads/${fileName}`;
    },
    toUpdate(){
      this.$router.push('/update')
    },
    toFrontPage(){
      this.$router.push('/FrontPage')
    },
    created(){
      this.$router.push('/HomePage')
    },
    todailyshop(){
      this.$router.push('/DailyShop')
    },
    toBookShop(){
      this.$router.push('/BookShop')
    },
    toPetShop(){
      this.$router.push('/PetShop')
    },
    toVideoShop(){
      this.$router.push('/VideoShop')
    },
    toMusicShop(){
      this.$router.push('/MusicShop')
    },
    toGoodManager(){
      this.$router.push('/GoodsManager')
    },
    toUserManager(){
      this.$router.push('/UserManager')
    }
  }
}
</script>

<template>
  <div>
    <el-menu
        :default-active="activeIndex2"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
    >
      <el-menu-item @click.native="toFrontPage" index="1">主站</el-menu-item>
      <el-submenu index="2">
        <template slot="title">商城精选</template>
        <el-menu-item @click.native="todailyshop" index="2-1">日用</el-menu-item>
        <el-menu-item @click.native="toVideoShop" index="2-2">游戏</el-menu-item>
        <el-menu-item @click.native="toMusicShop" index="2-5">音乐</el-menu-item>
        <el-menu-item @click.native="toBookShop" index="2-4">书刊</el-menu-item>
        <el-menu-item @click.native="toPetShop" index="2-3">萌宠</el-menu-item>
      </el-submenu>
      <el-submenu>
        <template slot="title">个人中心</template>
        <el-menu-item @click.native="toUer" index="3-1">个人主页</el-menu-item>
        <el-menu-item @click.native="toUpdate" index="3-2">修改资料</el-menu-item>
        <el-menu-item @click.native="logout" index="3-3">退出登录</el-menu-item>
      </el-submenu>
      <el-menu-item @click.native="toGoodManager" index="4">商品管理</el-menu-item>
      <el-menu-item @click.native="toUserManager" index="5">用户管理</el-menu-item>
      <div style="position: absolute; top: 0; right: 50px; height: 100%; display: flex; align-items: center;">
        <span style="color: white; margin-right: 15px;">欢迎, {{ user.name }}</span>
        <el-avatar :size="40" :src="fixavatar(user.image)" circle v-if="fixavatar(user.image)"></el-avatar>
        <el-avatar :size="40" v-else>U</el-avatar>
      </div>
    </el-menu>
  </div>
</template>

<style scoped>

</style>