<template>
<div>
    
     <v-toolbar
      :clipped-left="$vuetify.breakpoint.lgAndUp"
      color="white-text"
      flat
    >
    
      <v-toolbar-title class="ml-0 pl-3">
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
      <v-btn icon @click="popupInviteChannelDialog" v-if="this.$store.state.generalFlag">
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

    <v-dialog v-model="getUserDialog" width="800px" id="chat">
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
          <v-btn flat color="primary" @click="getUserDialog = !getUserDialog">Cancel</v-btn>
        </v-card-actions>
      </v-card>

    </v-dialog>
      

    <v-dialog v-model="inviteChannelDialog" width="800px">

       <v-card>
        <v-card-title 
         class="primary py-4 title white--text"
        >
        Invite to # {{this.$store.state.channelInfo.channelName}}
        </v-card-title>
        <v-container grid-list-sm class="pa-4">
            <v-flex xs12>
                <v-text-field
                  prepend-icon="search"
                  placeholder="Search  user's  nickname"
                  v-model="searchName"
                ></v-text-field>
            </v-flex>
            
        <v-layout row wrap>
            <v-flex xs12 align-center>
                <v-subheader>
                    <v-text style = "fontSize : 20px; font-weight:bold">Invite Others</v-text>
                </v-subheader>
            </v-flex>

            <!-- <v-flex v-for="(child, i) in this.$store.state.channelUsers" :key="`3${i}`" xs3>
                <v-list>
                    <v-avatar size="42px" class="mr-3">
                        <img src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png">
                    </v-avatar>
                <v-list-content class="px-0">{{ child.nickname }}</v-list-content>
                <v-list-tile-action>
                    <v-icon right fab @click="clickInviteUserInChannel(child.idx, child.nickname)">add</v-icon>
                </v-list-tile-action>
                </v-list>
            </v-flex> -->

            <v-flex
              v-for="(child, i) in searchList"
              :key="i" xs4
              
              style = "margin:10px 50px 10px 7px;"
              >

              <v-flex xs12 align-center justify-space-between>
                  <v-layout align-center>
                    <v-list-tile-action>
                        <v-avatar size="40px" class="mr-3">
                            <img
                                src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                                alt=""
                            >
                        </v-avatar>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title justify-space-between> {{ child.nickname }} </v-list-tile-title>
                    </v-list-tile-content>
                    <v-list-tile-action>
                        <v-icon right fab @click="clickInviteUserInChannel(child.idx, child.nickname)">add</v-icon>
                    </v-list-tile-action>
                  </v-layout>
                </v-flex>
            </v-flex>

        </v-layout>

        <!-- <br> -->

        <v-layout row wrap>
            <v-flex xs12 align-center>
                <v-subheader>
                    <v-text style = "fontSize : 20px; font-weight:bold">Invite Lists</v-text>
                </v-subheader>
            </v-flex>

            <v-flex 
              v-for="(child, i) in this.channel.users"
              :key="i" xs4
              
              style = "margin:10px 50px 10px 7px;"
              >

              <v-flex xs12 align-center justify-space-between>
                  <v-layout align-center>
                    <v-list-tile-action>
                        <v-avatar size="40px" class="mr-3">
                            <img
                                src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                                alt=""
                            >
                        </v-avatar>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title justify-space-between> {{ child.nickname }} </v-list-tile-title>
                    </v-list-tile-content>
                    <v-list-tile-action>
                        <v-icon right fab @click="cancleUserInChannel(child.idx, child.nickname)">cancel</v-icon>
                    </v-list-tile-action>
                  </v-layout>
                </v-flex>
            </v-flex>

            <!-- <v-flex v-for="(child, i) in this.channel.users" :key="`3${i}`" xs3 style = "margin:5px 0px 5px 5px;">
                <v-list>
                    <v-avatar size="42px" class="mr-3">
                        <img src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png">
                    </v-avatar>
                <v-list-content class="px-0">{{ child.nickname }}</v-list-content>
                </v-list>
            </v-flex> -->
        </v-layout>

        <v-layout row wrap>
            <v-flex xs12 align-center>
                <v-subheader>
                    <v-text style = "fontSize : 20px; font-weight:bold">Already Users</v-text>
                </v-subheader>
            </v-flex>

             <v-flex
              v-for="(child, i) in this.$store.state.channelUsers"
              :key="i" xs4
              
              style = "margin:10px 50px 10px 7px;"
              >

              <v-flex xs12 align-center justify-space-between>
                  <v-layout align-center>
                    <v-list-tile-action>
                        <v-avatar size="40px" class="mr-3">
                            <img
                                src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                                alt=""
                            >
                        </v-avatar>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title justify-space-between> {{ child.nickname }} </v-list-tile-title>
                    </v-list-tile-content>
                    <v-list-tile-action>
                        <v-icon right fab></v-icon>
                    </v-list-tile-action>
                  </v-layout>
                </v-flex>
            </v-flex>

            <!-- <v-flex v-for="(child, i) in this.$store.state.channelUsers" :key="`3${i}`" xs2 style = "margin:5px 0px 5px 5px;">
                <v-list>
                    <v-avatar size="40px" class="mr-3">
                        <img src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png">
                    </v-avatar>
                <v-list-content class="px-0">{{ child.nickname }}</v-list-content>
                </v-list>
            </v-flex> -->
        </v-layout>            


        </v-container>
        <v-card-actions>
          <!-- <v-btn flat color="primary">More</v-btn> -->
          <v-spacer></v-spacer>
          <v-btn flat color="primary" @click="cancelInvite">Cancel</v-btn>
          <v-btn flat @click="inviteChannel()">Invite</v-btn>
        </v-card-actions>
      </v-card>


    </v-dialog>

  

     <modals-container />
</div>

</template>
<script>
import axios from "axios";
import * as hangul from 'hangul-js';
import { debug } from 'util';
import moment from 'moment'
import inviteChannel from './InviteChannel.vue'

var now = new moment();
var today = now.format("dddd, MMMM Do").toString()

export default {
  components : {
    'inviteChannel' : inviteChannel
    // 'InviteUserEmail' : InviteUserEmail
  },
  props:[
    'channels',
    'teamMembers'
  ],

  data() {
    return {
      // star: false,
      searchName:'',
      getUserDialog: false,
      inviteChannelDialog:false,
       userIdx: localStorage.getItem("userIdx"),
          userNickName: localStorage.getItem("userNickName"),
          channelName:'',
          channel:{
            idx:'',
            users:[
              {
                idx: '',
                nickname:''
              }
            ]
          },
          inviteUsers:[
            {
                idx: '' ,
                uid: '',
                nickname: '',
                you:''
        }
        ]
    };
  },

  computed: {
    searchList: function() {
        // debugger;
        this.inviteUsers.splice(0);
        
        if(this.searchName !== '') {
            for(var i=0; i<this.$store.state.exceptChannelUsers.length; i++) {
              if(hangul.search(this.$store.state.exceptChannelUsers[i].nickname, this.searchName) >= 0) {
                  if(this.$store.state.exceptChannelUsers[i].idx != localStorage.getItem("userIdx")) {
                      // console.log(this.teamMembers[i].idx);
                      // console.log(localStorage.getItem("userIdx"));
                    this.inviteUsers.push(this.$store.state.exceptChannelUsers[i]);
                  }
                }else {
                
                }
          }
        }
      return this.inviteUsers;
    }
  },
  created(){
    // this.inviteUsers.splice(0)
    // console.log("[Header] created"+this.inviteUsers)
  },
    
  methods: {
    cancelInvite(){
      this.inviteChannelDialog = !this.inviteChannelDialog
      for(var i=0;i<this.channel.users.length;i++){
        this.$store.state.exceptChannelUsers.push(this.channel.users[i])
      }
      
    },
    inviteChannel(){

      
      if(this.channel.users.length==0){
        alert('선택한 사용자가 없습니다')
      }else{
      

        let token = localStorage.getItem('token');
        this.channel.idx=this.$store.state.channelInfo.idx

        axios
          // .post(this.$store.state.ip + ":8083/api/channel/invite", this.channel,
          .post("/api/channel/invite", this.channel, 
          {headers: { 'X-Auth-Token': `${token}` }}
          )
          .then(response => {
              if(response.data.status===200) {
                
                for(var i=0;i<this.channel.users.length;i++){
                  this.$store.state.channelUsers.push(this.channel.users[i])
                }

                this.$store.state.channelUserCount=this.$store.state.channelUsers.length
                
              } else {
              this.errors.push(e);
              }
            })
          .catch(e => {
            this.errors.push(e);
          });
          this.inviteChannelDialog = false;
        }

    },
    clickInviteUserInChannel(userIdx, nickname) {
      this.channel.users.push({idx: userIdx, nickname: nickname});

      for(var i=0;i<this.$store.state.exceptChannelUsers.length;i++){
        for(var j=0;j<this.channel.users.length;j++){
          if(this.$store.state.exceptChannelUsers[i].idx==this.channel.users[j].idx){
            this.$store.state.exceptChannelUsers.splice(i,1)
          }
        }
      }
          // if(this.channel.users.length == 0) {
          //   this.channel.users.push({idx: userIdx, nickname: nickname});
          // }else {
          //   for(var i=0; i<this.channel.users.length; i++) {
          //     // if(this.channel.users[i].idx == userIdx) {
          //     //   // alert("이미 추가된 사용자 입니다."); //?
          //     //   break;
          //     // }
          //     if(i == this.channel.users.length-1) {
          //       this.channel.users.push({idx: userIdx, nickname: nickname});
          //     }
          //   }
          // }

      },
      cancleUserInChannel(userIdx, nickname) {
        for(var i=0;i<this.channel.users.length;i++){
          if(this.channel.users[i].idx===userIdx){
            this.channel.users.splice(i,1)
          }
        }

        this.$store.state.exceptChannelUsers.push({idx: userIdx, nickname: nickname})

      },
      

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
        // url: this.$store.state.ip + ":8083/api/channel/star?channelIdx=" + this.$store.state.channelInfo.idx + "&userIdx=" + localStorage.getItem("userIdx") + "&starFlag=" + starFlag,
        url: "/api/channel/star?channelIdx=" + this.$store.state.channelInfo.idx + "&userIdx=" + localStorage.getItem("userIdx") + "&starFlag=" + starFlag,
        
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
      this.getUserDialog = !this.getUserDialog;
    },


    popupInviteChannelDialog(){
      this.channel.users.splice(0)
      this.searchName=''
      this.inviteChannelDialog = !this.inviteChannelDialog;
      // this.$modal.show(inviteChannel,{
      //   teamMembers : this.teamMembers,
      //   channels: this.channels,
      //   teamIdx : localStorage.getItem("teamIdx"),
      //   modal : this.$modal },{
      //     name: 'dynamic-modal',
      //     width : '800px',
      //     height : '80%',
      //     draggable: true
      //   })    

    },
    exitChannel() {        
     
      let token = localStorage.getItem('token');
       axios({
        method: 'delete',
        // url: this.$store.state.ip + ":8083/api/channel",
        url: "/api/channel",
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
        // url: this.$store.state.ip + ":8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx,
        url: "/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx,
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
