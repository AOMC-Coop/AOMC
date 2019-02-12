<template>
<div>
    
     <v-toolbar
      :clipped-left="$vuetify.breakpoint.lgAndUp"
      color="white-text"
      flat
    >
    
      <v-toolbar-title style="width: 130px" class="ml-0 pl-3">
        <!-- <v-icon large class="title font-bold" >#</v-icon> -->
        <span class="title font-weight-bold"># </span>
        <span class="title font-weight-bold">{{this.$store.state.channelInfo.channelName}}</span>
      </v-toolbar-title>
      
      
      
      <v-spacer></v-spacer>
      
      <v-btn icon @click="getChannelUsers">
      <v-avatar size="18px" tile>
        <img src="./../../assets/image/user_black.png" alt="Vuetify">
      </v-avatar>
      <span>{{this.$store.state.channelUserCount}}</span>
      </v-btn>


      <v-btn icon @click="starClick" v-if="this.$store.state.starFlag === false" >
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/star_off.png" alt="Vuetify">
        </v-avatar>
      </v-btn>

      <v-btn icon @click="starClick" v-if="this.$store.state.starFlag === true" >
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/star_on.png" alt="Vuetify">
        </v-avatar>
      </v-btn>

      <!-- <v-text-field
        flat
        hide-details
        prepend-inner-icon="search"
        label="Search"
        class="hidden-sm-and-down"
      ></v-text-field> -->
      
      
        
    
      <!-- <v-btn icon>
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/settings.png" alt="Vuetify">
        </v-avatar>
      </v-btn> -->
      <v-btn icon @click="inviteChannel" v-if="this.$store.state.generalFlag">
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/invite.png" alt="Vuetify">
        </v-avatar>
      </v-btn>      
      <v-btn icon @click="exitChannel" v-if="this.$store.state.generalFlag">
        <v-avatar size="20px" tile>
          <img src="./../../assets/image/exit.png" alt="Vuetify">
        </v-avatar>
      </v-btn>      
    </v-toolbar>
    <!-- <v-card-title>
        <v-icon large left>#</v-icon>
        <span class="title font-weight-light">{{this.$store.state.channelInfo.channelName}}</span>
    </v-card-title> -->

    <v-dialog v-model="createTeamDialog" width="800px" id="chat">
      <v-card>
        <v-card-title class="primary py-4 title white--text">
        # {{this.$store.state.channelInfo.channelName}} users
        </v-card-title>

         <v-container grid-list-md text-xs-center>
            <v-layout row wrap justify-center>

              <v-flex v-for="(child, i) in this.$store.state.channelUsers" :key="`3${i}`" xs3>
                <v-list>
                  <v-avatar size="42px" class="mr-3">
                  <img src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png">
                </v-avatar>
                  <v-list-content class="px-0">{{ child.nickname }}</v-list-content>
                </v-list>
              </v-flex>
          </v-layout>
          
          <v-layout row justify-center>
            <v-flex xs2>
              <v-btn @click="inviteChannel" v-if="this.$store.state.generalFlag">
                    <v-avatar size="30px" tile>
                    <img src="./../../assets/image/invite.png" alt="Vuetify">
                    </v-avatar>
                    <span>Invite Others</span>
                  </v-btn>
            </v-flex>
          </v-layout>
        </v-container>
        
       
        
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn flat color="primary" @click="createTeamDialog = !createTeamDialog">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  

     <modals-container />
</div>

</template>
<script>
import axios from "axios";
import moment from 'moment'
import CreateChannel from './CreateChannel.vue'
import InviteUserEmail from './InviteUserEmail.vue'

var now = new moment();
var today = now.format("dddd, MMMM Do").toString()

export default {
  components : {
    'CreateChannel' : CreateChannel
    // 'InviteUserEmail' : InviteUserEmail
  },
  props:[
    'channels',
    'teamMembers'
  ],

  data() {
    return {

      // star: false,

      createTeamDialog: false
     

    };
  },
    
  methods: {

    starClick() {
      debugger
      if(this.$store.state.starFlag === false) { //star_flag가 0에서 1로 바껴야 함
        this.chaneStarFlag(1);
      }else if(this.$store.state.starFlag === true){ //star_flag가 1에서 0로 바껴야 함
        this.chaneStarFlag(0);
      }
      
    },

    chaneStarFlag(starFlag) {
      //  axios.get(this.$store.state.ip + ":8083/api/channel/star?channelIdx=" + this.$store.state.channelInfo.idx + "&userIdx=" + localStorage.getItem("userIdx") + "&starFlag=" + starFlag)
       let token = localStorage.getItem('token');
       axios({
        method: 'get',
        url: this.$store.state.ip + ":8083/api/channel/star?channelIdx=" + this.$store.state.channelInfo.idx + "&userIdx=" + localStorage.getItem("userIdx") + "&starFlag=" + starFlag,
        
        headers: { 'X-Auth-Token': `${token}` },
      })
       .then((response) => {
        debugger
          if (response.data.status==200) {

              for(var i=0;i<this.channels.length;i++){
                   if(this.channels[i].idx==this.$store.state.channelInfo.idx){
                    this.channels[i].star_flag = starFlag;
                   }
                    
              }
              if(starFlag === 1) {
                this.$store.state.starChannelCount = this.$store.state.starChannelCount + 1;
              }
              if(starFlag === 0) {
                this.$store.state.starChannelCount = this.$store.state.starChannelCount - 1;
              }

            this.$store.state.starFlag = !this.$store.state.starFlag;
          }
      })
    
  },

    getChannelUsers(){
      this.createTeamDialog = !this.createTeamDialog;
      
    },
    inviteChannel(){
      this.$modal.show(CreateChannel,{
        teamMembers : this.teamMembers,
        channels: this.channels,
        teamIdx : localStorage.getItem("teamIdx"),
        modal : this.$modal },{
          name: 'dynamic-modal',
          width : '800px',
          height : '80%',
          draggable: true
        })    

    },
    exitChannel() {        
      //   axios.delete(this.$store.state.ip + ":8083/api/channel", {
      //   params: {
      //     channelIdx: this.$store.state.channelInfo.idx,
      //     userIdx: this.$store.state.userIdx
      //   },
      // })
      let token = localStorage.getItem('token');
       axios({
        method: 'delete',
        url: this.$store.state.ip + ":8083/api/channel",
        params: {
          channelIdx: this.$store.state.channelInfo.idx,
          userIdx: this.$store.state.userIdx
        },
        headers: { 'X-Auth-Token': `${token}` },
      })
      .then(response => {
          // debugger;
            if(response.data.status===200) {

                for(var i=0;i<this.channels.length;i++){
                  if(this.channels[i].idx==this.$store.state.channelInfo.idx){
                    this.channels.splice(i,1)
                    this.$store.state.channelInfo.idx= this.channels[i-1].idx
                    this.$store.state.channelInfo.channelName= this.channels[i-1].name

                    this.$store.state.messageStartNum=0
                    this.$store.state.messageLastIdx=0
                    this.getMessage()
                  }
                    
                }

            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });      
    },
    getMessage() {
      debugger
        this.$store.state.received_messages.splice(0);

      //   axios.get(this.$store.state.ip + ":8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx, {
      //    params: {
      //     start: this.$store.state.messageStartNum,
      //     messageLastIdx: this.$store.state.messageLastIdx
      //   },
      // })
      let token = localStorage.getItem('token');
       axios({
        method: 'get',
        url: this.$store.state.ip + ":8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx,
        params: {
           start: this.$store.state.messageStartNum,
          messageLastIdx: this.$store.state.messageLastIdx
        },
        headers: { 'X-Auth-Token': `${token}` },
      })
      .then((response) => {
        debugger
      if (response.data.status==200) {
        // this.start += 10;
        if(response.data.plusData === -3) {
          this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
          this.$store.state.scrollFlag=false
        }
        else if(response.data.plusData === -2) {
          this.$store.state.messageStartNum = -1;
        }
        else if(response.data.plusData === -1) {
          this.$store.state.messageStartNum = 0;
        }else {
          this.$store.state.messageStartNum = response.data.plusData;
          this.$store.state.messageStartNum+=10
        }

        if(response.data.data == null) {
          // this.$refs.infiniteLoading.$emit('$InfiniteLoading:complete');
          // this.$store.state.scrollFlag=false;
          return;
        }
        
        var sendDate = "";
        var result = response.data.data.reverse();

      if(result[0].send_date===today){
        sendDate = 'today'
        result[0].send_date = 'today'
      }else{
        sendDate = result[0].send_date
      }
      
        for(var i=1;i<result.length;i++){
          if(result[i].send_date==today){
            result[i].send_date='today'
          }

          if(result[i].send_date === sendDate){
            result[i].send_date=''
          }else{
            sendDate = result[i].send_date
          }
        }
        debugger;
        this.$store.state.received_messages = result
        this.$store.state.messageLastIdx = this.$store.state.received_messages[this.$store.state.received_messages.length-1].message_idx;
        // console.log("ChatHome - LastIdx = " + this.$store.state.messageLastIdx);
        // console.log("//////////////"+this.$store.state.messageStartNum);
        // console.log(this.$store.state.received_messages);
        this.$store.state.scrollFlag=true
        localStorage.setItem('scrollControlValue', result[0].message_idx)    
        } else {
          alert(response.data.message)
        }
      });
      },
  },

  

  created() {
    // this.star = false; //star_flag 보고 바꾸기

    // for(var i=0; i<this.channels.length; i++) {
    //   if(this.channels[i].star_flag === 1) {
    //     this.$store.state.starFlag = true;
    //   }
    // }
  },

    

  
};
</script>
<style>

</style>
