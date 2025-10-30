<template>
    <div class="background-container2" >
      <div class="block" style="width: 1000px " >
        <el-carousel :interval="5000" arrow="always" height="320px" >
          <el-carousel-item v-for="item in 4" :key="item">
            <img class="carousel-image" src="../assets/logo.png" alt="u" >
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="block2">
       <el-row :gutter="20">
        <el-col :span="8">
          <el-card :body-style="{ padding: '14px' }">
            <img src="../assets/龙千1.jpg" class="image" height="400">
            <div style="padding: 14px;">
              <span>夏季上新，幻想乡的新宠商品与特价</span>
              <div class="bottom clearfix">
                <el-button type="text" class="button" @click="$router.push('/PetShop')">点击查看</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card :body-style="{ padding: '14px' }">
            <img src="../assets/耄耋1.png" class="image" height="400">
            <div style="padding: 14px;">
              <span>本网站已购进全新萌宠：现推出买耄耋赠优惠卡活动</span>
              <div class="bottom clearfix">
                <el-button type="text" class="button" @click="$router.push('/PetShop')">点击查看</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
         <el-col :span="8">
           <el-card :body-style="{ padding: '14px' }">
             <img src="../assets/周刊1.jpg" class="image" height="400">
             <div style="padding: 14px;">
               <span>本月新刊速览</span>
               <div class="bottom clearfix">
                 <el-button type="text" class="button" @click="$router.push('/BookShop')">点击查看</el-button>
               </div>
             </div>
           </el-card>
         </el-col>
      </el-row>

      </div>
    </div>
</template>

<style>
.block2{
  margin-left: 20px;
}
.background-java{
  background-image: url('../assets/java.jpg');
}
.background-container2{
  width: 100%;
  height: 100vh;
  background-color: blanchedalmond;
}
.fontwrite2{
  font-size: 18px;
  color: #000;
  font-family: 楷体;
}
.fontwrite{
  font-size: 35px;
  color: #000;
  font-weight: bold;
}
.block{
  align: "center";
  margin-left: 200px;
  margin-right: 380px;
  margin-bottom: 20px;
}
.smallblock{
  margin-right: 180px;
  margin-left: 180px;
  margin-top: 20px;
}
.el-carousel__item h3 {
  color: #475600;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  object-position: center;
}
.background-container {
  width: 100%;
  height: 100vh;
  background-image: url('../assets/background.jpg');
  background-size: cover; /* 图片自适应屏幕 */
  background-position: center; /* 图片居中显示 */
  background-repeat: no-repeat; /* 防止图片重复 */
}
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>

<script>

export default {
  components: {},
  data() {
    return {
      currentDate: new Date()
    };
  }
}
</script>