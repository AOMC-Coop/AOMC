<template>
<v-content>
  <div>
    <Message-Header></Message-Header>
    <Message-List class="msg-list" v-auto-bottom="this.$store.state.received_messages"></Message-List>
    <Message-From class="msg-form"></Message-From>
    <!-- <Message-From class="msg-form" v-on:submitMessage="sendMessage" ></Message-From> -->
  </div>
  </v-content>
</template>

<script>
import axios from "axios";
import MessageList from '@/components/chat/MessageList.vue';
import MessageForm from '@/components/chat/MessageForm.vue';
import MessageHeader from '@/components/chat/MessageHeader.vue';

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
    'Message-Header': MessageHeader,
    'Message-List': MessageList,
    'Message-From': MessageForm,
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