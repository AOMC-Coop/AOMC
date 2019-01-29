<template>
<v-content>

  <!-- <div class="inner-wrap" fluid fill-height inner-wrap> -->
    <div>
    
    
    <!-- <Message-List class="msg-list"></Message-List> -->

    <Message-List class="msg-list" v-auto-bottom="this.$store.state.received_messages"></Message-List>
    <Message-From v-on:submitMessage="sendMessage" class="msg-form"></Message-From>

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

// var objDiv = document.getElementById("mydiv"); 

//  objDiv.scrollTop = objDiv.scrollHeight;
</script>

<style>
.msg-list {
  /* position: static; */
  position: absolute;
  background-color:white; 
  /* bottom:10; */
  top: 0%;
  width:100%;
  height: 80%;
 
  /* left: 0;
  right: 0; */
  /* bottom: 60px; */
  overflow-y: scroll;
}

.msg-form {
  /* float:initial; */
   /* position:fixed; */
   /* display: table; */
   /* position: absolute; */
   /* min-width: 800px; */
	 /* min-height: 150px; */
  /* bottom:0; */
  /* top: 0; */
  /* height: 10%;
  width:100%; */
    /* height:70px;    */
  /* position: static; */
  background-color:white; 

    position:absolute;

    bottom:0;

    width:100%;

    height:20%;   
  
}
</style>