<template>
    <div>
      <v-card>
        <v-card-title
          class="grey lighten-4 py-4 title"
        >
        Invite to # {{this.$store.state.channelInfo.channelName}}
        </v-card-title>
        <v-container grid-list-sm class="pa-4">
            <v-flex xs12>
                <v-text-field
                  prepend-icon="search"
                  placeholder="Search  user's  nickname"
                  v-model="channelName"
                ></v-text-field>
            </v-flex>
            
        <!-- <v-layout row wrap> -->
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
            <v-layout>
            <v-flex
              v-for="(child, i) in this.$store.state.exceptChannelUsers"
              :key="i" xs3
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

        <!-- </v-layout> -->

        <!-- <br> -->

        <v-layout row wrap>
            <v-flex xs12 align-center>
                <v-subheader>
                    <v-text style = "fontSize : 20px; font-weight:bold">Invite Lists</v-text>
                </v-subheader>
            </v-flex>


            <v-flex 
              v-for="(child, i) in this.channel.users"
              :key="i" xs3
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
              :key="i" xs3
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
          channelName:'',
          channel:{
            idx:'',
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

      cancelInvite(){
        this.$emit('close')
        for(var i=0;i<this.channel.users.length;i++){
            this.$store.state.exceptChannelUsers.push(this.channel.users[i])
        }
    },
    inviteChannel(){
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
  },

  created() {
      this.channel.users.splice(0)
    
    // this.channel.teamIdx = this.teamIdx
    // this.channel.users.pop(); // 왜 유저가 한개 들어있을까?ㅁ
    // this.channel.users.push({idx: this.userIdx, nickname: this.userNickName});
    // this.channel.teamIdx = localStorage.getItem(teamIdx);
    // this.printLog(this.teamMembers)
    // window.addEventListener('scroll', this.handleScroll); 
  },
//   destroyed() { 
//     // window.removeEventListener('scroll', this.handleScroll); 
//   } 
}
</script>

<style>
.list {
  height: 230px;
  overflow-y : scroll;
}

</style>
