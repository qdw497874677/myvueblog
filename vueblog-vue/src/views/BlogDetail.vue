<template>
  <div>
    <Header></Header>

    <div class="mblog" >
      <h2> {{ blog.title }}</h2>
      <div >
<!--        <el-link icon="el-icon-edit" v-if="ownBlog" >-->
<!--          <router-link :to="{name: 'BlogEdit', params: {blogId: blog.id}}" style="text-decoration: none;color: #0060B6;">-->
<!--            编辑-->
<!--          </router-link>-->
<!--        </el-link>-->
        <el-row :gutter="5" >
          <el-col :span="3" :offset="18">
            <div class="grid-content bg-purple">
              <el-button type="primary" v-if="ownBlog" style="width: 100%" v-on:click="goEdit">编辑</el-button>
            </div>
          </el-col>
          <el-col :span="3" >
            <div class="grid-content bg-purple">
              <el-button type="danger" v-if="ownBlog" icon="el-icon-delete" style="width: 100%" v-on:click="deleteBlog">移除</el-button>
            </div>
          </el-col>
        </el-row>


<!--        <el-link icon="el-icon-delete" v-if="ownBlog" style="padding: 10px">-->
<!--&lt;!&ndash;          <router-link :to="{name: 'BlogEdit', params: {blogId: blog.id}}" style="text-decoration: none;color: #0060B6;">&ndash;&gt;-->
<!--          <router-link :to="{name: 'BlogEdit', params: {blogId: blog.id}}" style="text-decoration: none;color: #0060B6;">-->
<!--            移除到回收站-->
<!--          </router-link>-->
<!--        </el-link>-->
      </div>

      <el-divider></el-divider>
      <div class="markdown-body" v-html="blog.content"></div>

    </div>

  </div>
</template>

<script>
  import 'github-markdown-css'
  import Header from "../components/Header";

  export default {
    name: "BlogDetail.vue",
    components: {Header},
    data() {
      return {
        blog: {
          id: "",
          title: "",
          content: ""
        },
        ownBlog: false
      }
    },
    methods: {
      goEdit(){
          const _this = this;
          _this.$router.push({name:"BlogEdit",params: { blogId: _this.blog.id }});
      },
      deleteBlog:function(){
        const _this = this;
        _this.$axios.get('/blog/delete/'+_this.blog.id,{
            headers: {
                "Authorization": localStorage.getItem("token")
            }
        }).then(res => {
          _this.$router.push("/")
            console.log("!!!"+_this.blog.id)
        })
      }
    },
    created() {
      const blogId = this.$route.params.blogId
      console.log(blogId)
      const _this = this
      this.$axios.get('/blog/' + blogId).then(res => {
        const blog = res.data.data
        _this.blog.id = blog.id
        _this.blog.title = blog.title

        var MardownIt = require("markdown-it")
        var md = new MardownIt()

        var result = md.render(blog.content)
        _this.blog.content = result
        _this.ownBlog = (blog.userId === _this.$store.getters.getUser.id)

      })
    }
  }
</script>

<style scoped>
  .mblog {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    width: 100%;
    min-height: 700px;
    padding: 20px 15px;
  }

</style>