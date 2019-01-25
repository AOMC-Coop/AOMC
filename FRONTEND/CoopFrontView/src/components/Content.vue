<template>
<v-content>

  <div class="inner-wrap" fluid fill-height inner-wrap>
    <Message-List :msgs="msgDatas" class="msg-list"></Message-List>
    <Message-From v-on:submitMessage="sendMessage" class="msg-form" ></Message-From>
  </div>

  
      <!-- <v-container fluid fill-height>
        <v-layout justify-center align-end>

          <v-flex xs12>
            <v-text-field
              label="Outline"
              placeholder="대화를 입력하세요."
              outline
            ></v-text-field>
          </v-flex> -->

          <!-- <v-tooltip right>
            <v-btn
              slot="activator"
              :href="source"
              icon
              large
              target="_blank"
            >
              <v-icon large>code</v-icon>
            </v-btn>
            <span>Source</span>
          </v-tooltip> -->
          <!-- <v-tooltip right>
            <v-btn slot="activator" icon large href="https://codepen.io/johnjleider/pen/EQOYVV" target="_blank">
              <v-icon large>mdi-codepen</v-icon>
            </v-btn>
            <span>Codepen</span>
          </v-tooltip> -->

          
        <!-- </v-layout> -->

        
      <!-- </v-container> -->
    </v-content>

    
  
</template>

<script>

import { mapMutations, mapState } from 'vuex';
import MessageList from '@/components/Chat/MessageList.vue';
import MessageForm from '@/components/Chat/MessageForm.vue';
import Constant from '@/Constant';

export default {
  name: 'Content',
  data() {
    return {
      datas: [],
    };
  },
  components: {
    'Message-List': MessageList,
    'Message-From': MessageForm,
  },
  // computed: {
  //   ...mapState({
  //     'msgDatas': state => state.socket.msgDatas,
  //   }),
  // },
  created() {
    const $ths = this;
    this.$socket.on('chat', (data) => {
      this.pushMsgData(data);
      $ths.datas.push(data);
    });
  },
  methods: {
    ...mapMutations({
      'pushMsgData': Constant.PUSH_MSG_DATA,
    }),
    sendMessage(msg) {
      this.pushMsgData({
        from: {
          name: '나',
        },
        msg,
      });
      this.$sendMessage({
        name: this.$route.params.username,
        msg,
      });
    },
  },
};


// export default {
//   name: 'Content',
//   data () {
//     return {
//       msg: 'Welcome to Your Vue.js App'
//     }
//   }
// }
</script>

<style>
.msg-form {
  bottom: -28px;
  position: absolute;
  left: 0;
  right: 0;
}
.msg-list {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 60px;
  overflow-x: scroll;
}
</style>