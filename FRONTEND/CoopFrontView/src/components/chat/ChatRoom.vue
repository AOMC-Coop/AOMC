<template>
<v-content>

    <div>
      <v-card-title>
        <v-icon large left>#</v-icon>
        <span class="title font-weight-light">{{this.$store.state.channelInfo.channelName}}</span>
    </v-card-title>
    
    <Message-List class="msg-list" v-auto-bottom="this.$store.state.received_messages"></Message-List>
    <Message-From class="msg-form" v-on:submitMessage="sendMessage" ></Message-From>
    <!-- <Message-Header class="msg-header"></Message-Header> -->

  </div>
  </v-content>
</template>

<script>
import axios from "axios";
import MessageHeader from '@/components/chat/MessageHeader.vue';
import MessageList from '@/components/chat/MessageList.vue';
import MessageForm from '@/components/chat/MessageForm.vue';

export default {
  name: 'ChatRoom',
  data() {
    return {
      // start:10
      // datas: [],
      // msgs: '',
    };
  },
  components: {
    'Message-List': MessageList,
    'Message-From': MessageForm,
    'Message-Header': MessageHeader
  },
    
  methods: {    
    sendMessage(msg) {
      // console.log(msg);
      // this.pushMsgData({
      //   from: {
      //     name: '나',
      //   },
      //   msg,
      // });
      // this.$sendMessage({
      //   name: this.$route.params.username,
      //   msg,
      // });
    },
  },
};

</script>

<style>
/* .msg-header {
  background-color:white;
  position:absolute;
  top:0;
  width:100%;
  height:10%;
} */
.msg-list {
  position: absolute;
  background-color:white; 
  top:20;
  width:100%;
  height:73%;
}
.msg-form {
  background-color:white;
  position:absolute;
  bottom:0;
  width:100%;
  height:20%;
}
</style>