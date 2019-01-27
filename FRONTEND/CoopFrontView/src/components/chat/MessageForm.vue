<template>
 <v-container fluid fill-height>
        <v-layout justify-center align-end>

          <v-flex xs12>
            <v-text-field
            multi-line
            :rules="[(v) => v.length <= 500 || 'Max 500 characters']"
            :counter = 500
            placeholder="대화를 입력하세요."
            v-model="msg"
            outline
            @keyup.13="submitMessageFunc"
            prepend-icon="add"
            clearable
            ></v-text-field>
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

export default {
  name: 'MessageForm',
  data() {
    return {
      msg: {
          content : '',
          nickname: '',
          send_date:'',
          send_time:''
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
        const msggggg = { content: this.msg , nickname: 'mooming', send_date: send_date, send_time:send_time};//레디스에서 받은 사용자의 nickname을 세팅
        this.stompClient.send("/app/chat2", JSON.stringify(msggggg), {});
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
          this.stompClient.subscribe("/topic/message", tick => {
            // console.log(tick);
            this.$store.state.received_messages.push(JSON.parse(tick.body));

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

</style>
