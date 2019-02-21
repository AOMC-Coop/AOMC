<template>

 <v-container fluid fill-height>

        <div class="container">
          <div class="large-12 medium-12 small-12 cell">
            <label>
              <input type="file" id="file" ref="file" v-on:change="handleFileUpload()"/>
            </label>
            <button v-on:click="submitFile()">Submit</button>
          </div>
        </div>

     <div class="vertical" v-on:change="handleFileUpload()">
        <upload-btn icon title='' large=false id="file" ref="file" type="file">
          <template slot="icon">
            <v-icon class="white--text">add</v-icon>
          </template>
        </upload-btn>

        <v-btn round icon large
            :loading="loading3"
            :disabled="loading3"
            color="secondary"
            class="white--text"
            @click="loader = 'loading3'"
            v-on:click="submitFile()">
            <v-icon right dark left>cloud_upload</v-icon>
        </v-btn>
     </div>

        <v-layout justify-center align-end>

          <v-flex xs12>

              <!-- <form @submit.prevent="sendFile"> 
                <upload-btn icon>
                  <template slot="icon">
                    <v-icon>add</v-icon>
                  </template>
                </upload-btn>
              </form> -->

              <v-textarea class="area"
              :rules="[(v) => v.length <= 500 || 'Max 500 characters']"
              :counter = 500
              placeholder="대화를 입력하세요."
              v-model="msg"
              outline
              @keypress.13.prevent="submitMessageFunc"
              clearable
              ></v-textarea>

          </v-flex>
        </v-layout>
  </v-container>

</template>

<script>
// import SockJS from "sockjs-client";
// import Stomp from "webstomp-client";
import moment from 'moment'
import UploadButton from 'vuetify-upload-button';
import axios from "axios";

  
var now = new moment();
var send_date = now.format("dddd, MMMM Do").toString()
var send_time = now.format("LT").toString()
var send_db_date = moment(new Date()).format("YYYY-MM-DD HH:mm:ss");

export default {

  name: 'MessageForm',
  components: {
      'upload-btn': UploadButton
  },
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
      file: '',
      userIdx: localStorage.getItem("userIdx"),
      userNickName: localStorage.getItem("userNickName"),
      userImage:localStorage.getItem("userImage"),
      loader: null,
      loading: false,
      loading2: false,
      loading3: false,
      loading4: false
    }
  },

  watch: {
    loader () {
      const l = this.loader
      this[l] = !this[l]

      setTimeout(() => (this[l] = false), 3000)

      this.loader = null
     
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
        
        const sendMessage = { content: this.msg , channel_idx: this.$store.state.channelInfo.idx, user_idx: this.userIdx, nickname: this.userNickName, image:this.userImage, send_date: send_date, send_time:send_time, send_db_date: send_db_date};//레디스에서 받은 사용자의 nickname을 세팅
        const sendChannel = { idx: this.$store.state.channelInfo.idx};
        console.log("channelInfo.idx = " + this.$store.state.channelInfo.idx);
        console.log("this.channel.idx = " + this.channel.idx);
        debugger;
        this.$store.state.stompClient.send("/app/chat", JSON.stringify(sendMessage)); //채널번호 붙이고 싶음
      }

      this.msg = '';

      // return true;
    },
    submitFile(){
      // Initialize the form data
      let formData = new FormData();
      // Add the form data we need to submit
      let channel_idx = this.$store.state.channelInfo.idx
      let url =  "http://localhost:8085/api/files/"+ channel_idx
      // let url = this.$store.state.ip + ":8085/api/files/" + channel_idx

      const sendMessage = { 
        content: this.msg , 
        channel_idx: this.$store.state.channelInfo.idx, 
        user_idx: this.userIdx,
        image:this.userImage,
        nickname: this.userNickName, 
        send_date: send_date, 
        send_time:send_time, 
        send_db_date: send_db_date
      };//레디스에서 받은 사용자의 nickname을 세팅
      // this.msg.nickname = 'yunyun',
      // this.msg.send_date
      // this.msg.send_time
      // this.msg.send_db_date    
      // "channel_idx":1,
      // "user_idx":1
      formData.append('file', this.file);
      formData.append('message', JSON.stringify(sendMessage));
      debugger
      // alert(JSON.stringify(sendMessage))
      
      // formData.append('message', sendMessage);
      // Make the request to the POST /single-file URL
      
      axios.post( url,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }).then(function(){
          console.log('Successfully submitted file!');
        })
        .catch(function(){
          console.log('Fail to submit file!');
        });
      },
      // Handles a change on the file upload
      handleFileUpload(){
        debugger
        this.file = this.$refs.file.files[0];
        
      }
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

    // disconnect() {
    //   if (this.stompClient) {
    //     this.stompClient.disconnect();
    //   }
    //   this.connected = false;
    // },
    // tickleConnection() {
    //   this.connected ? this.disconnect() : this.connect();
    // }
</script>
<style>
.area {
  padding-top: 50
}
.left_align{
  float: left;
}
  .custom-loader {
    animation: loader 1s infinite;
    display: flex;
  }
  @-moz-keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
  @-webkit-keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
  @-o-keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
  @keyframes loader {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }
</style>