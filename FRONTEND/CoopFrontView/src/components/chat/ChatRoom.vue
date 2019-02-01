<template>
<v-content>

    <div>
    <Message-List class="msg-list"></Message-List>
    <!-- <Message-List class="msg-list" v-auto-bottom="this.$store.state.received_messages"></Message-List> -->
    <Message-From v-on:submitMessage="sendMessage" class="msg-form"></Message-From>

  </div>
  </v-content>
</template>

<script>
import axios from "axios";
import MessageList from '@/components/chat/MessageList.vue';
import MessageForm from '@/components/chat/MessageForm.vue';


export default {
  name: 'Content',
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
.msg-list {
  position: absolute;
  background-color:white; 
  top: 0;
  width:100%;
  height: 80%;
}

.msg-form {
  background-color:white;
  position:absolute;
  bottom:0;
  width:100%;
  height:20%;
}
</style>