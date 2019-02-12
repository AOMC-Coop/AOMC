<template>


    <div class="div">
      <v-card >
        <v-card-title
          class="grey lighten-4 py-4 title"
        >
          Create Channel
        </v-card-title>
        <v-container grid-list-sm class="pa-4">
          <v-layout row wrap>
            <v-flex xs12 align-center justify-space-between>
              <v-layout align-center>
                <!-- <v-avatar size="42px" class="mr-3">
                  <img
                    src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                    alt=""
                  >
                </v-avatar> -->
                <v-text-field
                  prepend-icon="notes"
                  placeholder="ChannelName"
                  v-model="channelName"
                ></v-text-field>
              </v-layout>
            </v-flex>
            
            <!-- <v-flex xs12 align-center justify-space-between>
            <v-layout align-center>
                <v-avatar size="42px" class="mr-3">
                  <img
                    src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                    alt=""
                  >
                </v-avatar>
                <v-text-field
                  placeholder="User"
                ></v-text-field>
                <v-icon>add</v-icon>
              </v-layout>
            </v-flex> -->

              <v-list-tile
              v-for="(child, i) in teamMembers"
              :key="i"
              v-if="child.idx != userIdx"
              
            >
            <v-flex xs12 align-center justify-space-between>
            <v-layout align-center>
              <v-list-tile-action >
                <v-avatar size="42px" class="mr-3">
                  <img
                    src="//ssl.gstatic.com/s2/oz/images/sge/grey_silhouette.png"
                    alt=""
                  >
                </v-avatar>
              </v-list-tile-action>
              <v-list-tile-content>
                <v-list-tile-title justify-space-between>
                  {{ child.nickname }} 
                </v-list-tile-title>
              </v-list-tile-content>
              <v-list-tile-action>
              <v-icon right fab @click="clickInviteUserInChannel(child.idx, child.nickname)">add</v-icon>
            </v-list-tile-action>
            
            </v-layout>
            </v-flex>
            </v-list-tile>
            

            <!-- <v-flex xs12>
              <v-text-field
                type="tel"
                prepend-icon="phone"
                placeholder="(000) 000 - 0000"
                mask="phone"
              ></v-text-field>
            </v-flex> -->
            
            <!-- <v-flex xs12>
              <v-text-field
                prepend-icon="notes"
                placeholder="Notes"
              ></v-text-field>
            </v-flex> -->
          </v-layout>

      <v-subheader >
        <v-text style = "fontSize : 18px">  초대할 멤버 </v-text>
      </v-subheader>
      <hr>
       

          <v-list dense class="list">
        <template v-for="item in channel.users" v-if="item.idx !== userIdx"
        >
          
          <v-list-tile v-if :key="item.text" >
            <!-- <v-list-tile-action>
              <v-icon class="white--text">{{ item.icon }}</v-icon>
            </v-list-tile-action> -->
            <v-list-tile-content>
              <v-list-tile-title>
               <v-text>  {{ item.nickname }} </v-text>
              </v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </template>
      </v-list>
          
        </v-container>
        <v-card-actions>
          <v-btn flat color="primary">More</v-btn>
          <v-spacer></v-spacer>
          <v-btn flat color="primary" @click="del_data()">Cancel</v-btn>
          <v-btn flat @click="save_data()">Save</v-btn>
        </v-card-actions>
      </v-card>
    </div>
</template>
<script>
import axios from "axios";

export default {
  name: 'CreateChannel',
  data:function(){
      return {
          userIdx: localStorage.getItem("userIdx"),
          userNickName: localStorage.getItem("userNickName"),
          del_password:'',
          channelName:'',
          channel:{
            idx:'',
            name: '',
            teamIdx:'',
            users:[
              {
                idx: '',
                nickname:''
              }
            ]
          }
      } 
  },props : [
      // 'hot_table',
      'teamMembers',
      'teamIdx',
      'channels'
  ],methods : {
      del_data(){
          
          this.$emit('close')
      },
      save_data() {
        if(this.channelName !== '') {
          this.channel.name = this.channelName;
        //   axios
        // .post(this.$store.state.ip + ":8083/api/channel/", this.channel)
        let token = localStorage.getItem('token');
        axios
        .post(this.$store.state.ip + ":8083/api/channel/", this.channel, 
        {headers: { 'X-Auth-Token': `${token}` }}
        )
        .then(response => {
          // debugger;
            if(response.data) {
              // this.teamMembers = response.data.data;
              // console.log(response.data);
              this.channel.idx = response.data.data;
              this.channels.push(this.channel);
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
      

        this.$emit('close')
        }else {
          alert("채널 이름을 작성해 주세요.");
        }
        
      },
      printLog(log) {
        console.log(log);
      },
      clickInviteUserInChannel(userIdx, nickname) {
          // debugger;
          this.channel.name = this.channelName;
          
          console.log("users = " + this.channel.users);
          // let user = {idx: userIdx, nickname: nickname};
          var check = false;
          if(this.channel.users.length == 0) {
            this.channel.users.push({idx: userIdx, nickname: nickname});
          }else {
            for(var i=0; i<this.channel.users.length; i++) {
              if(this.channel.users[i].idx == userIdx) {
                // alert("이미 추가된 사용자 입니다."); //?
                break;
              }
              if(i == this.channel.users.length-1) {
                this.channel.users.push({idx: userIdx, nickname: nickname});
              }
          }
          }
          
          
          
          // console.log("userIdx=" + userIdx);
          // console.log("localStorage userIdx = " + localStorage.getItem("userIdx"));
          // console.log("users = " + this.channel.users);
          // console.log("teamIdx = " + this.channel.teamIdx);

      }
  },
  created() {
    // debugger;
    // console.log(this.channels);
    this.channel.teamIdx = this.teamIdx
    this.channel.users.pop(); // 왜 유저가 한개 들어있을까?ㅁ
    this.channel.users.push({idx: this.userIdx, nickname: this.userNickName});
    // this.channel.teamIdx = localStorage.getItem(teamIdx);
    this.printLog(this.teamMembers)
    window.addEventListener('scroll', this.handleScroll); 
  },
  destroyed() { 
    window.removeEventListener('scroll', this.handleScroll); 
  } 
}
</script>

<style>
.div {
  overflow-y : scroll;
}
</style>
