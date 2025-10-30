<script>
import HeaderR from "@/components/HeaderR.vue";

export default {
  name: 'PageIndex',
  components: { HeaderR},

};
</script>


<template>
  <el-container style="height: 900px; border: 1px solid #eee">
    <el-container>
      <el-header style="text-align: right; font-size: 12px;border-bottom: #B3C0D1 1px solid; ">

        <HeaderR></HeaderR>

      </el-header>

      <el-main>

        <router-view/>

      </el-main>
    </el-container>
  </el-container>

</template>

<style scoped>
.el-header {
  background-color: #333;
  color: #333;
  line-height: 60px;
}

.el-main {
  padding: 5px;
}

.el-aside {
  color: #333;
}
</style>