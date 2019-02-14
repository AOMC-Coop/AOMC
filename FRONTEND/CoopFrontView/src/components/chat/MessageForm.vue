<template>
 <v-container fluid fill-height>
        <v-layout justify-center align-end>

          <v-flex xs12>
            <v-textarea class="area"
            :rules="[(v) => v.length <= 500 || 'Max 500 characters']"
            :counter = 500
            placeholder="대화를 입력하세요."
            v-model="msg"
            outline
            @keypress.13.prevent="submitMessageFunc"
            prepend-icon="add"
            clearable
            ></v-textarea>
            <upload-btn icon>
              <template slot="icon">
                <v-icon>add</v-icon>
              </template>
            </upload-btn>
          </v-flex>
        </v-layout>
      </v-container>

</template>

<script>
// import SockJS from "sockjs-client";
// import Stomp from "webstomp-client";
import moment from 'moment'

var now = new moment();
var send_date = now.format("dddd, MMMM Do").toString()
var send_time = now.format("LT").toString()
var send_db_date = moment(new Date()).format("YYYY-MM-DD HH:mm:ss");


export default {
  name: 'MessageForm',
  data() {
    return {
      msg: {
        content : '',
        nickname: '',
        send_date:'',
        send_time:'',
        send_db_date:''
      },
      channel: {
        idx: this.$store.state.channelInfo.idx
      },
      userIdx: localStorage.getItem("userIdx"),
      userNickName: localStorage.getItem("userNickName")
    }
  },
  methods: {
    submitMessageFunc() {
      debugger;
      console.log(this.msg.length);
      if (this.msg.length === 0 || this.msg.length > 500 || this.msg == '/\n/') {
        this.msg = '';
        return;
      }
      // if(this.msg.length === 1 && this.msg === '/\n/') return false;
      // this.$emit('submitMessage', this.msg);
      console.log(this.$store.state.stompClient);

      if (this.$store.state.stompClient) {
        
        const sendMessage = { content: this.msg , channel_idx: this.$store.state.channelInfo.idx, user_idx: this.userIdx, nickname: this.userNickName, send_date: send_date, send_time:send_time, send_db_date: send_db_date};//레디스에서 받은 사용자의 nickname을 세팅
        const sendChannel = { idx: this.$store.state.channelInfo.idx};
        console.log("channelInfo.idx = " + this.$store.state.channelInfo.idx);
        console.log("this.channel.idx = " + this.channel.idx);
        debugger;
        this.$store.state.stompClient.send("/app/chat", JSON.stringify(sendMessage)); //채널번호 붙이고 싶음
      }

      this.msg = '';

      // return true;
    },

    // disconnect() {
    //   if (this.stompClient) {
    //     this.stompClient.disconnect();
    //   }
    //   this.connected = false;
    // },
    // tickleConnection() {
    //   this.connected ? this.disconnect() : this.connect();
    // }
  },



   created() {
       this.msg = '';
      // this.socket = new SockJS("http://localhost:8083/socketconnect");
      // this.stompClient = Stomp.over(this.socket);
      // this.stompClient.connect(
      //   {},
      //   frame => {
      //     debugger
      //     // this.connected = true;
      //     // console.log("////////////////////////"+frame);
      //     this.stompClient.subscribe("/topic/message", tick => {
      //       // console.log(tick);
      //       debugger
      //       this.$store.state.received_messages.push(JSON.parse(tick.body));

      //       console.log("subcribe = " + tick.body);

      //       var newValue= this.$store.state.received_messages.slice(-1)[0].send_date;

      //       for(var i=0; i<this.$store.state.received_messages.length-1;i++){
      //         if(this.$store.state.received_messages[i].send_date===now.format("dddd, MMMM Do").toString()||this.$store.state.received_messages[i].send_date==='today'){
      //           this.$store.state.received_messages.slice(-1)[0].send_date = 'today'
      //           newValue='today'
      //         }
      //         if(this.$store.state.received_messages[i].send_date===newValue){
      //           this.$store.state.received_messages.slice(-1)[0].send_date = ''
      //         }
      //       }
      //     });
      //   },
      //   error => {
      //     console.log(error);
      //     this.connected = false;
      //   }
      // )

      // this.stompClient.disconnect(distick => {
      //   console.log("socket disconnect");
      // });
    }

};
</script>
<style>
.area {
  padding-top: 50
}
</style>
