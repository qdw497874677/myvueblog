<template>
  <div class="mcontaner">
    <Header></Header>

    <div>
      <el-table
              :data="blogs"
              border
              style="width: 100%">
        <el-table-column
                fixed
                prop="created"
                label="创建日期"
                width="150">
        </el-table-column>
        <el-table-column
                prop="title"
                label="标题"
                width="150">
        </el-table-column>

        <el-table-column
                prop="description"
                label="描述"
                width="350">
        </el-table-column>
        <el-table-column
                prop="typeName"
                label="分类"
                width="150">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="180">
          <template slot-scope="scope">
            <el-button @click="deleteBlog(scope.row)" type="text" size="small">删除</el-button>
            <el-button type="text" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>


  </div>
</template>

<script>
  import Header from "../components/Header";

  export default {
    name: "EditBlog.vue",
    components: {Header},
    data() {
      return {

        blogs: [],
        currentPage: 1,
        total: 0,
        pageSize: 5
      }
    },
    methods: {
      deleteBlog(row) {
        const _this = this;
        _this.$axios.get('/admin/blog/delete/'+row.id,{
          headers: {
            "Authorization": localStorage.getItem("token")
          }
        }).then(res => {

          this.$router.go(0);
        })
      },
      page(currentPage) {
        const _this = this;
        _this.$axios.get("/blogs?currentPage=" + currentPage).then(res => {
          console.log(res);

          _this.blogs = res.data.data.records;
          _this.currentPage = res.data.data.current;
          _this.total = res.data.data.total;
          _this.pageSize = res.data.data.size
          console.log("!!!"+_this.blogs[0].id)
        })
      }
    },
    created() {
      this.page(1)
    }
  }
</script>

<style scoped>

  .mpage {
    margin: 0 auto;
    text-align: center;
  }

</style>