<template>
    <div>
        <Header></Header>
        <div style="">
            <el-card class="box-card">
<!--                <div v-for="o in 4" :key="o" class="text item">-->
<!--                    {{'列表内容 ' + o }}-->

<!--                </div>-->
                <div v-for="showtext in showtexts" class="text item">
                    {{ showtext }}
                </div>
<!--                <el-timeline :reverse="reverse">-->
<!--                    <el-timeline-item-->
<!--                            v-for="(activity, index) in activities"-->
<!--                            :key="index"-->
<!--                            :timestamp="activity.timestamp">-->
<!--                        {{activity.content}}-->
<!--                    </el-timeline-item>-->
<!--                </el-timeline>-->
            </el-card>
        </div>
        <br/>
        <el-divider></el-divider>
        <div>
            <el-row :gutter="10">
                <el-col :span="20">

                    <el-switch
                            v-model="value"
                            active-color="#13ce66"
                            inactive-color="#ff4949">
                    </el-switch>
                </el-col>
                <el-col :span="4">
                    <el-button type="primary" v-on:click="send">发送</el-button>
                </el-col>
            </el-row>

            <div style="margin: 20px 0;"></div>
            <el-input
                    type="textarea"
                    placeholder="请输入内容"
                    v-model="textarea"
                    maxlength="30"
                    show-word-limit
            >
            </el-input>
        </div>

    </div>
</template>

<script>
    import Header from "../components/Header";
    export default {
        name: "ChatRoom",
        components: {Header},

        data() {
            return {
                showtexts: [''],
                textarea: '',
                count: 0,
                value: true,
                reverse: true,
                activities: [{
                    content: '活动按期开始',
                    timestamp: '2018-04-15'
                }, {
                    content: '通过审核',
                    timestamp: '2018-04-13'
                }, {
                    content: '创建成功',
                    timestamp: '2018-04-11'
                }],
                wsURL: 'ws://127.0.0.1:8081/ws',
            }
        },
        methods: {
            webSocketInit: function() {
                if(typeof(WebSocket) === "undefined"){
                    alert("您的浏览器不支持socket")
                }else{
                    // 实例化socket
                    this.socket = new WebSocket(this.wsURL)
                    // 监听socket连接
                    this.socket.onopen = this.open
                    // 监听socket错误信息
                    this.socket.onerror = this.error
                    // 监听socket消息
                    this.socket.onmessage = this.getMessage
                }
            },
            open: function() {
                console.log("socket连接成功！")
            },
            error: function() {
                console.log("连接错误")
            },
            getMessage: function(msg) {
                console.log(msg.data);
                this.showtexts.push(msg.data)

            },
            send: function () {
                // let message = document.getElementById('ta').value;
                // this.webSocket.send(message);
                // document.getElementById('ta').value = '';
                this.socket.send(this.textarea);
                this.textarea = ''
            },
            close: function() {
                console.log("socket关闭！")
            },


        },
        created() {
            // let webSocket = null;
            // webSocket = new WebSocket("ws://127.0.0.1:9002/ws");
            // window.onbeforeunload = function () {
            //     webSocket.close();
            // };
            // this.page(1)
            this.webSocketInit()
        },
        destroyed () {
            // 销毁监听
            this.socket.onclose = this.close
        }
    }
</script>

<style scoped>

</style>