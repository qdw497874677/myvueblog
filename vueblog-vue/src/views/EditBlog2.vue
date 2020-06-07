<template>
    <div class="mcontaner">
        <Header></Header>

        <div>
            <el-table
                    :data="blogs"
                    stripe
                    style="width: 100%">
                <el-table-column
                        prop="title"
                        label="标题"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="姓名"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="address"
                        label="地址">
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="100">
                    <template slot-scope="scope">
                        <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
                        <el-button type="text" size="small">编辑</el-button>
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
        name: "EditBlog",
        components: {Header},
        data() {
            return {
                blogs: {},
                currentPage: 1,
                total: 0,
                pageSize: 5
            }
        },
        methods: {
            handleClick(row) {
                console.log(row);
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
        // created() {
        //     this.page(1)
        // }
    }

</script>

<style scoped>

</style>