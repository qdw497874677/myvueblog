<template>
  <div class="mcontaner">
    <Header></Header>

    <div>
      <el-table
              :data="tableData"
              style="width: 100%"
              max-height="250">
        <el-table-column
                fixed
                prop="date"
                label="日期"
                width="150">
        </el-table-column>
        <el-table-column
                prop="name"
                label="姓名"
                width="120">
        </el-table-column>
        <el-table-column
                prop="province"
                label="省份"
                width="120">
        </el-table-column>
        <el-table-column
                prop="city"
                label="市区"
                width="120">
        </el-table-column>
        <el-table-column
                prop="address"
                label="地址"
                width="300">
        </el-table-column>
        <el-table-column
                prop="zip"
                label="邮编"
                width="120">
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="120">
          <template slot-scope="scope">
            <el-button
                    @click.native.prevent="deleteRow(scope.$index, tableData)"
                    type="text"
                    size="small">
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="block">
      <el-timeline>

        <el-timeline-item :timestamp="blog.created" placement="top" v-for="blog in blogs" v-if="!blog.isPublished">
          <el-card >
            <h4>
              <router-link :to="{name: 'BlogDetail', params: {blogId: blog.id}}" style="text-decoration: none;color: #0060B6;font-size: x-large">
                {{blog.title}}
              </router-link>
            </h4>
            <p>{{blog.description}}</p>
          </el-card>
        </el-timeline-item>

      </el-timeline>

      <el-pagination class="mpage"
                     background
                     layout="prev, pager, next"
                     :current-page="currentPage"
                     :page-size="pageSize"
                     :total="total"
                     @current-change=page>
      </el-pagination>

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
        tableData: [{
          date: '2016-05-03',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-02',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-04',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-01',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-08',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-06',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-07',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }],
        blogs: {},
        currentPage: 1,
        total: 0,
        pageSize: 5
      }
    },
    methods: {
      deleteRow(index, rows) {
        rows.splice(index, 1);
      },
      page(currentPage) {
        const _this = this;
        _this.$axios.get("/blogs?currentPage=" + currentPage).then(res => {
          console.log(res);
          
          _this.blogs = res.data.data.records;
          _this.currentPage = res.data.data.current;
          _this.total = res.data.data.total;
          _this.pageSize = res.data.data.size

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