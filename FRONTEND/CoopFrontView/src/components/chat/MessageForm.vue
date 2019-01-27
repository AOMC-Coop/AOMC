<template>
 <v-container fluid fill-height>
        <v-layout justify-center align-end>

          <v-flex xs12>
            <v-text-field
            placeholder="대화를 입력하세요."
            v-model="msg"
            outline
            @keyup.13="submitMessageFunc"
            ></v-text-field>
          </v-flex>
        </v-layout>


        <form class="form-inline">
                        <div class="form-group">
                            <label for="name">What is your name?</label>
                            <input type="text" id="name" class="form-control" v-model="send_message" placeholder="Your name here...">
                        </div>
                        <button id="send" class="btn btn-default" type="submit" @click.prevent="send">Send</button>
            </form>
        

      </v-container>

  <!-- <div class="inner-wrap">
    <v-text-field
      v-model="msg"
      label="chat"
      placeholder="보낼 메세지를 입력하세요."
      solo
      @keyup.13="submitMessageFunc"
    ></v-text-field>
  </div> -->

</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name: 'MessageForm',
  data() {
    return {
      msg: '',
      send_message:'',
      received_messages:[],
      socket:''
    };
  },
  methods: {
    submitMessageFunc() {
      if (this.msg.length === 0) return false;
      this.$emit('submitMessage', this.msg);
      this.msg = '';
      return true;
    },
    send() {
      debugger
      console.log("Send message:" + this.send_message);
      // if (this.stompClient && this.stompClient.connected) {
      // if (this.stompClient) {
        const msggggg = { msg: this.send_message };
        this.stompClient.send("/app/chat2", JSON.stringify(msggggg), {});
      // }
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
      console.log("create")
      debugger;
      this.socket = new SockJS("http://localhost:8083/chat");
      this.stompClient = Stomp.over(this.socket);
      debugger;
      this.stompClient.connect(
        {},
        frame => {
          this.connected = true;
          debugger
          console.log("////////////////////////"+frame);
          this.stompClient.subscribe("/topic/message", tick => {
            console.log(tick);
            this.received_messages.push(JSON.parse(tick.body).content);
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
