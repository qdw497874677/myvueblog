import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Blogs from '../views/Blogs.vue'
import BlogEdit from '../views/BlogEdit.vue'
import BlogDetail from '../views/BlogDetail.vue'
import Types from "../views/Types";
import Tags from "../views/Tags";
import ChatRoom from "../views/ChatRoom";
import EditBlog from "../views/EditBlog";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Index',
    redirect: {name: "Blogs"}
  },
  {
    path: '/blogs',
    name: 'Blogs',
    component: Blogs
  },
  {
    path: '/editblogs',
    name: 'editblogs',
    component: EditBlog
  },
  {
    path: '/types',
    name: 'Types',
    component: Types
  },
  {
    path: '/tags',
    name: 'Tags',
    component: Tags
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/edit',
    name: 'BlogEdit',
    component: BlogEdit,
    meta: {
      requireAuth: true
    }
  },
  // {
  //   path: '/editblogs',
  //   name: 'EditBlog',
  //   component: EditBlog,
  //   meta: {
  //     requireAuth: true
  //   }
  // },
  {
    path: '/chatroom',
    name: 'ChatRoom',
    component: ChatRoom,
    meta: {
      requireAuth: true
    }
  },
  {
    //参数
    path: '/blog/:blogId',
    name: 'BlogDetail',
    component: BlogDetail
  },
  {
    path: '/blog/:blogId/edit',
    name: 'BlogEdit',
    component: BlogEdit,
    meta: {
      requireAuth: true
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
