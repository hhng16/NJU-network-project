import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue';
import './assets/global.css'
import axios from "axios";
Vue.prototype.$axios = axios;
Vue.config.productionTip = false
Vue.prototype.$httpUrl="http://localhost:8095";
import router from './router'
Vue.use(VueRouter);
Vue.use(ElementUI);
import VueRouter from "vue-router";

new Vue({
  el: '#app',
  router,
  render: h => h(App),
}).$mount('#app')