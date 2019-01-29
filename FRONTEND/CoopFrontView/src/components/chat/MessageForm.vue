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
            @keyup.13="submitMessageFunc"
            prepend-icon="add"
            clearable
            ></v-textarea>
          </v-flex>
        </v-layout>
      </v-container>

</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
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
      }
    
      // send_message: {
      //     content : '',
      //     nickname: 'mooming'
      // },
      
      // connected: false
      }
  },
  methods: {
    submitMessageFunc() {
      if (this.msg.length === 0) return false;
      // this.$emit('submitMessage', this.msg);
      

      if (this.stompClient) {
        const sendMessage = { content: this.msg , nickname: this.$store.state.userNickName, send_date: send_date, send_time:send_time, send_db_date: send_db_date};//레디스에서 받은 사용자의 nickname을 세팅
        const sendChannel = { idx: this.$store.state.channelInfo.idx};
        console.log("channelInfo.idx = " + this.$store.state.channelInfo.idx);
        console.log("this.channel.idx = " + this.channel.idx);
        debugger;
        this.stompClient.send("/app/chat/" + this.$store.state.channelInfo.idx, JSON.stringify(sendMessage)); //채널번호 붙이고 싶음
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
      this.socket = new SockJS("http://localhost:8083/socketconnect");
      this.stompClient = Stomp.over(this.socket);
      this.stompClient.connect(
        {},
        frame => {
          // this.connected = true;
          // console.log("////////////////////////"+frame);
          this.stompClient.subscribe("/topic/message", tick => { //채널번호 붙이고싶음
            // console.log(tick);
            this.$store.state.received_messages.push(JSON.parse(tick.body));

            var newValue= this.$store.state.received_messages.slice(-1)[0].send_date;

            for(var i=0; i<this.$store.state.received_messages.length-1;i++){
              if(this.$store.state.received_messages[i].send_date===newValue){
                this.$store.state.received_messages.slice(-1)[0].send_date = ''
              }
            }
          });
        },
        error => {
          console.log(error);
          this.connected = false;
        }
      );
    }

};
</script>
<style>
.area {
  padding-top: 50
}
</style>
