<template>
  <div class="m-content">
    <div >
      <img class="logo" src="../assets/mylogo.png" alt="">
      <h3>欢迎</h3>
    </div>

    <el-menu :default-active="$router.path" class="el-menu-demo" mode="horizontal" @select="handleSelect" router>
      <el-menu-item v-for="(item,i) in navList" :key="i" :index="item.name">
        {{ item.navItem }}
      </el-menu-item>

      <div class="block" style="float: right">
        <div style="float: left; padding: 10px">
          <span v-show="!hasLogin"><el-link type="primary" href="/login" style="text-decoration: none">登录</el-link></span>
          <span v-show="hasLogin"><el-link type="danger" @click="logout" style="text-decoration: none">退出</el-link></span>
        </div>
        <div style="float: left; padding: 10px">{{ user.username }}</div>
        <el-avatar :size="50" :src="user.avatar"></el-avatar>

      </div>
    </el-menu>

    <br>
  </div>
</template>

<script>
  export default {
    name: "Header",
    data() {
      return {
        navList:[
          {name:'/blogs',navItem:'博客列表'},
          {name:'/types',navItem:'分类列表'},
          {name:'/tags',navItem:'标签列表'},
          {name:'/edit',navItem:'发布'},
          {name:'/chatroom',navItem:'聊天室'},
        ],
        // activeIndex: '1',
        // activeIndex2: '2',
        // activeIndex3: '3',
        // activeIndex4: '4',
        user: {
          // username: '请先登录',
          username: '',
          avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
        },
        hasLogin: false
      }
    },
    computed:{
      activeIndex(){
        return this.$route.path.replace('/','')
      }
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },
      index() {
        const _this = this;
        _this.$router.push("/blogs")
      },
      types() {
        const _this = this;
        _this.$router.push("/types")
      },
      edit() {
        const _this = this;
        _this.$router.push("/blog/add")
      },
      logout() {
        const _this = this;
        _this.$axios.get("/logout", {
          headers: {
            "Authorization": localStorage.getItem("token")
          }
        }).then(res => {
          _this.$store.commit("REMOVE_INFO");


          // 如果相同路由位置，就刷新一下
          if (_this.$route.path === '/blogs'){
            console.log(_this.$route.path)
            this.$router.go(0)
          }
          // _this.$router.push("/login")
          _this.$router.push("/")

        })
      }
    },
    created() {
      if(this.$store.getters.getUser.username) {
        this.user.username = this.$store.getters.getUser.username;
        this.user.avatar = this.$store.getters.getUser.avatar;

        this.hasLogin = true
      }

    }
  }
</script>

<style scoped>

  .m-content {
    max-width: 960px;
    margin: 0 auto;
    text-align: center;
  }
  .maction {
    margin: 10px 0;
  }

  .logo {
    width: 10%;

    margin-top: 8px;
  }

</style>