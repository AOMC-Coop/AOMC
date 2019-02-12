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

      <v-btn icon>
        <v-avatar size="25px" tile>
          <img src="./../../assets/image/star_off.png" alt="Vuetify">
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
        <v-card-title
          class="grey lighten-4 py-4 title"
        >
        # {{this.$store.state.channelInfo.channelName}} users
        </v-card-title>
        <v-container grid-list-sm class="pa-4">
          <v-layout row wrap>
            <v-flex xs12 align-center justify-space-between>
              <v-layout align-center>

                <v-text-field
                  prepend-icon="notes"
                  placeholder="TeamName"
                  v-model="createTeamName"
                ></v-text-field>
              </v-layout>
            </v-flex>
            
            <!-- <v-text>invite member</v-text><v-icon right @click="">add</v-icon> -->
            

            <!-- <InviteUserEmail v-for="item in this.$store.state.components" v-bind:key="InviteUserEmail">
           
            </InviteUserEmail> -->

          </v-layout>
        </v-container>
        
        <v-card-actions>
          <v-btn flat color="primary">More</v-btn>
          <v-spacer></v-spacer>
          <v-btn flat color="primary" @click="createTeamDialog = !createTeamDialog">Cancel</v-btn>
          <v-btn flat @click="saveTeam">Save</v-btn>
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
      createTeamDialog: false
     
    };
  },
   created(){

  },  
    
  methods: {
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
        axios.delete(this.$store.state.ip + ":8083/api/channel", {
        params: {
          channelIdx: this.$store.state.channelInfo.idx,
          userIdx: this.$store.state.userIdx
        },
      }).then(response => {
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

        axios.get(this.$store.state.ip + ":8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx, {
         params: {
          start: this.$store.state.messageStartNum,
          messageLastIdx: this.$store.state.messageLastIdx
        },
      }).then((response) => {
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
  
};
</script>
<style>

</style>
