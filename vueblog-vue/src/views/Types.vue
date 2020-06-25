<template>

    <div class="mcontaner">
        <Header></Header>
        <h3>这里是types</h3>

<!--        <el-row :gutter="5"  >-->
<!--            <el-col :span="3" v-for="type in types">-->
<!--                <div class="grid-content bg-purple">-->
<!--                    <el-button type="primary" plain>-->
<!--                        {{ type.name }} ({{type.c}})-->
<!--                    </el-button>-->
<!--                </div>-->
<!--            </el-col>-->
<!--        </el-row>-->
<!--        <br/>-->
        <el-select v-model="value"  @change="changeType" placeholder="请选择">
            <el-option
                    v-for="(item,index) in types"
                    :key="index"
                    :label="item.name+' ('+item.c+')'"
                    :value="item.id">
            </el-option>
        </el-select>

        <br/>
        <br/>
        <div class="block">
            <el-timeline>

                <el-timeline-item :timestamp="blog.created" placement="top" v-for="blog in blogs">
                    <el-card>
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
    import qs from 'qs';
    export default {
        name: "Types.vue",
        components: {Header},
        data() {
            return {
                types: [],
                blogs: [],
                currentPage: 1,
                total: 0,
                pageSize: 5,
                // options: [{
                //     value: '选项1',
                //     label: '黄金糕'
                // }, {
                //     value: '选项2',
                //     label: '双皮奶'
                // }, {
                //     value: '选项3',
                //     label: '蚵仔煎'
                // }, {
                //     value: '选项4',
                //     label: '龙须面'
                // }, {
                //     value: '选项5',
                //     label: '北京烤鸭'
                // }],
                options: [],
                value: "",
                id: 1,
            }
        },
        methods: {
            page(currentPage,id) {
                const _this = this;
                console.log("id:"+_this.value);
                _this.$axios.get("/blogsType",{
                    params: {
                        'typeId': _this.value,
                        "currentPage": _this.currentPage
                    }

                }).then(res => {
                    console.log(res);
                    _this.blogs = res.data.data.records;
                    _this.currentPage = res.data.data.current;
                    _this.total = res.data.data.total;
                    _this.pageSize = res.data.data.size;

                })
            },
            getTypes(currentPage) {
                console.log("res");
                const _this = this;
                _this.$axios.get("/types",{
                    params: {
                        currentPage: currentPage
                    }
                }).then(res => {
                    console.log(res);
                    _this.types = res.data.data.records;
                    _this.currentPage = res.data.data.current;
                    _this.total = res.data.data.total;
                    _this.pageSize = res.data.data.size;
                    _this.value = _this.types[0].id;
                    console.log("value1:"+_this.value);
                    // _this.id = _this.types[0].id;
                    this.page(_this.currentPage,_this.value)
                })

            },
            changeType() {
                const _this = this;
                _this.blogs = {};
                this.page(_this.currentPage,_this.value);
            }
        },
        created() {
            const _this = this
            this.getTypes(1)
            // console.log("value2:"+_this.value);

        }
    }
</script>

<style scoped>

</style>