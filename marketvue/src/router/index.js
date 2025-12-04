import VueRouter    from "vue-router";

const routes = [
  {
    path: "/",
    name: "login",
    component: () => import("../components/login")
  },

  {
    path: '/register',
    name: 'register',
    component: () => import('../components/register')
  },
  {
    path: '/update',
    name: 'update',
    component: () => import('../components/update')
  },
  {
    path: '/goods/detail',
    name: 'GoodsDetail',
    component: () => import('@/views/GoodsDetail.vue')
  },
  {
    path: "/Index",
    name: "index",
    component: () => import("../components/Index.vue")
    ,
    children: [
      {
        path: "/FrontPage",
        name: "frontpage",
        meta: {
          title: "首页"
        },
        component: () => import("../components/FrontPage")
      },
      {
        path: "/HomePage",
        name: "homepage",
        meta: {
          title: "首页"
        },
        component: () => import("../components/HomePage")
      },
      {
        path: "/MainR",
        name: "mainuser",
        meta: {
          title: "用户查询"
        },
        component: () => import("../components/MainR")
      },
      {
        path: '/DailyShop',
        name: 'DailyShop',
        meta: {
          title: '每日商品'
        },
        component: () => import('../shops/DailyShop.vue')
      },
      {
        path: '/MusicShop',
        name: 'MusicShop',
        meta: {
          title: '音乐商品'
        },
        component: () => import('../shops/MusicShop.vue')
      },
      {
        path: '/PetShop',
        name: 'PetShop',
        meta: {
          title: '每日商品'
        },
        component: () => import('../shops/PetShop.vue')
      },
      {
        path: '/VideoShop',
        name: 'VideoShop',
        meta: {
          title: '每日商品'
        },
        component: () => import('../shops/VideoShop.vue')
      },
      {
        path: '/BookShop',
        name: 'BookShop',
        meta: {
          title: '每日商品'
        },
        component: () => import('../shops/BookShop.vue')
      },
      {
        path: '/GoodsManager',
          name: 'GoodsManager',
        meta: {
          title: '商品管理界面'
        },
        component: () => import('../Admins/GoodsManager.vue')
      },
      {
        path: '/UserManager',
        name: 'UserManager',
        meta: {
          title: '用户管理界面'
        },
        component: () => import('../Admins/UserManager.vue')
      }
    ]
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}
export default router;