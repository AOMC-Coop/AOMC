<template >
  <v-app id="inspire">
    <v-navigation-drawer 
      class="primary"
      fixed
      app
      permanent
    >

    <!-- 클라이언트 사이드에서도 JWT 토큰을 파괴해야 함-->
    <v-list class="white--text">
        <template v-for="item in teams">
          <v-layout
            v-if="item.heading"
            :key="item.heading"
            row
            align-center
          >
          </v-layout>
          <v-list-group
            v-else-if="item.children"
            v-model="item.model"
            class="white--text"
            :key="item.text"
          >
            <v-list-tile slot="activator">
              <v-list-tile-content>
                <v-list-tile-title style = "fontSize : 33px">
                  {{ teamName }}
                </v-list-tile-title>
                <v-list-tile-title style = "fontSize : 15px">
                  {{ userName }}
                </v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>
            <v-list-tile
              v-for="(child, i) in teamsFromServer"
              :key="i"
              @click="clickTeamName(child.idx, child.name)"
            >
              <v-list-tile-action >
                <v-icon class="white--text" >widgets</v-icon>
              </v-list-tile-action>
              <v-list-tile-content>
                <v-list-tile-title class="white--text" style = "fontSize : 20px">
                  {{ child.name }}
                </v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>

             <v-flex xs6 >
              <v-subheader class="white--text" >
              <v-icon class="white--text" @click="addCreateTeamDialog">add</v-icon> 
              <v-text style = "fontSize : 15px">create Team</v-text>
              </v-subheader>
          </v-flex>

          </v-list-group>
        </template>
      </v-list> 
    

     <v-flex xs12>
      <v-subheader class="white--text">
         <v-text style = "fontSize : 18px"> Members </v-text>
      </v-subheader>

      <v-list dense class="white--text">
        <template v-for="item in teamMembers">
          
          <v-list-tile v-if :key="item.text" @click="">
            <v-list-tile-action>
              <v-icon class="white--text" >people</v-icon>
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title>
                 {{ item.nickname }} {{item.you}}
              </v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </template>
      </v-list>
    </v-flex>

     <v-flex xs6>
      <v-subheader class="white--text">
        <v-text style = "fontSize : 18px">  Channels </v-text>
        <v-icon @click="doc_del_rendar()" class="white--text" right fab>add</v-icon>
      </v-subheader>
      <v-list dense class="white--text">
        <template v-for="item in channels">
          
          <v-list-tile v-if :key="item.text" @click="clickChannel(item.idx, item.name)">
            <v-list-tile-content>
              <v-list-tile-title>
               <v-text> # {{ item.name }} </v-text>
              </v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </template>
      </v-list>
    </v-flex>

    <v-flex xs6 >
      <v-subheader class="white--text" >
        <v-icon class="white--text" @click="dialog = !dialog">add</v-icon> 
        <v-text style = "fontSize : 15px">invite people</v-text>
      </v-subheader>
    </v-flex>

    <form @submit.prevent="signOut">
      <v-btn color="warning" type="submit">Sign out</v-btn>
    </form>
    <form @submit.prevent="withdrawal">
      <v-btn color="error" type="submit">Withdrawal</v-btn>
    </form>
    <form @submit.prevent="getProfile">
      <v-btn color="success" type="submit">Profile</v-btn>
    </form>    
    </v-navigation-drawer>
    
        
    <!-- <ChatRoom :key="somevalueunderyourcontrol"></ChatRoom> -->
    <ChatRoom class="chatroom"></ChatRoom>
    
    <v-dialog v-model="dialog" width="800px" id="chat">
      <v-card>
        <v-card-title
          class="grey lighten-4 py-4 title"
        >
        Invite People
        </v-card-title>
        <v-container grid-list-sm class="pa-4">
          <v-layout row wrap>
            
            <v-text>invite member</v-text><v-icon right @click="inviteMember">add</v-icon>
            

            <InviteUserEmail v-for="item in this.$store.state.components" v-bind:key="InviteUserEmail">
           <!-- vm.currentView가 변경되면 컴포넌트가 변경됩니다! -->
            </InviteUserEmail>

          </v-layout>
        </v-container>
        <v-card-actions>
          <v-btn flat color="primary">More</v-btn>
          <v-spacer></v-spacer>
          <v-btn flat color="primary" @click="clickCancel">Cancel</v-btn>
          <v-btn flat @click="clickSave">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="createTeamDialog" width="800px" id="chat">
      <v-card>
        <v-card-title
          class="grey lighten-4 py-4 title"
        >
        Create Team
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
            
            <v-text>invite member</v-text><v-icon right @click="inviteMember">add</v-icon>
            

            <InviteUserEmail v-for="item in this.$store.state.components" v-bind:key="InviteUserEmail">
           <!-- vm.currentView가 변경되면 컴포넌트가 변경됩니다! -->
            </InviteUserEmail>

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

  </v-app>
</template>

<script>
import ChatRoom from './ChatRoom.vue'
import CreateChannel from './CreateChannel.vue'
import InviteUserEmail from './InviteUserEmail.vue'
import axios from "axios";
import moment from 'moment'

var now = new moment();
var today = now.format("dddd, MMMM Do").toString()

  export default {
    el: "Chat",
    components : {
    'ChatRoom' : ChatRoom,
    'CreateChannel' : CreateChannel,
    'InviteUserEmail' : InviteUserEmail
  },

 
  data: () => ({
    dialog: false,
    createTeamDialog: false,
    createTeamName:'',
    drawer: null,

    userIdx: '',
      
    teamName: '',
    userName:'',
    teams: [
        {
          icon: 'keyboard_arrow_up',
          'icon-alt': 'keyboard_arrow_down',
          text: 'aaa',
          model: false,
          children: [ // 서버에서 받아온 팀 리스트를 저장
            { idx: '', name: '', status: ''}
          ]
        }
      ],
    teamsFromServer: [ // 서버에서 받아온 팀 리스트를 저장
        { 
          idx: '' ,
          name: '',
          status: ''
        }
      ],
      teamMembers: [
        {
          idx: '' ,
          uid: '',
          nickname: '',
          you:''
        }
      ],
      channels: [
        {
          idx: '',
          name: '',
          star_flag: '',
          status: '',
          teamIdx: ''
        }
      ],
      inviteTeam: { //팀의 멤버 초대 
        idx:'',
        users:[],
        channels:[
          {idx:''}
        ]
      },
      createTeam: {
        name:'',
        users:[
          {uid:''}
        ]
      },
      visible: false,

      userWithToken : {
        idx : localStorage.getItem('idx'),
        token: localStorage.getItem('token')
      },
    }),
    
    props: {
      source: String,
      teamMembers: Array,
      channels: Array
    },

    methods: {
      saveTeam() {
        this.createTeam.name = this.createTeamName;
        this.createTeam.users = this.$store.state.inviteUsers;

        axios
        .post("http://localhost:8083/api/team", this.createTeam)
        .then(response => {
          // debugger;
            if(response.data) {
              // this.teamMembers = response.data.data;
              console.log(response.data);
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
        
        this.createTeamDialog = false;
      },
      addCreateTeamDialog() {
        this.createTeamDialog = !this.createTeamDialog;
        this.$store.state.inviteUsers.push({uid:this.$store.state.userId});
      },
      clickChannel(itemIdx, channelName) {
        this.$store.state.channelInfo.idx = itemIdx;
        this.$store.state.channelInfo.channelName = channelName;
        this.$store.state.messageStartNum=0
        this.getMessage();
      },
      clickSave() {
        // debugger;
        console.log(this.$store.state.inviteUsers);
        this.inviteTeam.idx = localStorage.getItem("teamIdx");
        this.inviteTeam.users = this.$store.state.inviteUsers;
        this.inviteTeam.channels.push({idx: this.channels[0].idx});//general idx 채널을 넣어줘야함

        axios
        .post("http://localhost:8083/api/team/invite", this.inviteTeam)
        .then(response => {
          // debugger;
            if(response.data) {
              // this.teamMembers = response.data.data;
              console.log(response.data);
            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });
        
        this.dialog = false;
      },
      clickCancel() {
        debugger;
        // for(item in this.inviteUsers) {
        //   console.log(item.uid)
        //   debugger;
        //   this.inviteUsers.pop();
        // }
        // components.splice(0);
        this.$store.state.components.splice(0);
        this.dialog = false;
      },
      inviteMember() {
        
        this.$store.state.components.push('component');
      },
      doc_del_rendar(){
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
      
      getMemberByTeamId(teamIdx){
        axios
        .get("http://localhost:8083/api/team/" + teamIdx)
        .then(response => {
          // debugger;
            if(response.data) {
              this.teamMembers = response.data.data;
              for(var i=0; i<this.teamMembers.length; i++) {
                if(this.teamMembers[i].idx == this.userIdx) {
                  this.teamMembers[i].you = "(you)";
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
      getChannelsByTeamIdxAndUserIdx(teamIdx, userIdx) {
        debugger
        axios
        .get("http://localhost:8083/api/team/channel/" + teamIdx + "&" + userIdx)
        .then(response => {
            if(response.data) {
              this.channels = response.data.data;
              this.$store.state.channelInfo.idx = this.channels[0].idx;
              this.$store.state.channelInfo.channelName = this.channels[0].name;
              this.$store.state.messageStartNum=0
              this.getMessage();

            } else {
            this.errors.push(e);
            }
          })
        .catch(e => {
          this.errors.push(e);
        });

      },
      getMessage() {
        this.$store.state.received_messages.splice(0);

    axios.get("http://localhost:8083/api/channel/message?channelIdx=" + this.$store.state.channelInfo.idx, {
      params: {
        start: this.$store.state.messageStartNum
      },
    }).then((response) => {
      if (response.data.status==200) {
        // this.start += 10;
        this.$store.state.messageStartNum+=10
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
        
        this.$store.state.received_messages = result      
        this.$store.state.scrollFlag=true
        } else {
          alert(response.data.message)
        }
      });
      },
      clickTeamName(teamIdx, teamName) {
        localStorage.setItem("teamIdx", teamIdx);
        axios
        .get("http://localhost:8083/api/team/user/" + teamIdx)
        .then(response => {
          debugger;
            if(response.data) {
              debugger;
              //this.teamsFromServer = response.data.data;
              this.teamName = teamName;
              this.userName = "yunjae"; //userName 받기
              this.getMemberByTeamId(teamIdx);
              this.getChannelsByTeamIdxAndUserIdx(teamIdx, 5); // 5->userId로 받아야 함

              this.$store.state.channelInfo.idx = response.data.data[0].idx;
              this.$store.state.channelInfo.channelName = response.data.data[0].name;

              
            } else {
            //   app.renderNotification('Successfully Singed Up');
            //   app.toggleSignUp();
            this.errors.push(e);
            }
          })
        .catch(e => {
          // location.href = './';
          this.errors.push(e);
        });
      },
      handleClickButton(){
      this.visible = !this.visible
    },
    signOut: function (){
      axios.post(`http://localhost:8082/logout`, this.userWithToken)
        .then(response => {
          let description = response.data.description
          if(description == "Fail Logout"){
              alert("Fail to sign out!")
           } else {
              localStorage.removeItem('token')
              localStorage.removeItem('idx')
              console.log(JSON.stringify(localStorage))
              alert("Successfully Signed out!")
              location.href = './'
           }
         }).catch(e => {
          console.log(e)
          this.errors(e)
          location.href = './'
        })      
    },
    withdrawal: function (){
      let idx = localStorage.getItem('idx')
      let url = `http://localhost:8082/members/`+ idx
      axios.put(url, this.userWithToken)
        .then(response => {
          let description = response.data.description
          if(description == "Fail Withdrawal"){
              alert("Fail to withdraw!")
           } else {
              localStorage.removeItem('token')
              localStorage.removeItem('idx')
              console.log(JSON.stringify(localStorage))
              alert("Successfully withdrew!")
              location.href = './'
           }
         }).catch(e => {
          console.log(e)
          this.errors(e)
          location.href = './'
        })      
    },
    getProfile: function (){
      let idx = localStorage.getItem('idx')
      let url = `http://localhost:8082/profile/`+ idx
      axios.post(url, this.userWithToken)
        .then(response => {
          let description = response.data.description
          if(description == "Fail Get Profile"){
              alert("Fail to get profile!")
           } else {
              let nickname = response.data.nickname
              let gender = response.data.gender
// ***** localStorage로 저장하여도 ./Profile에는 적용되지 않는다. localStorage의 범위 알아볼 것
              localStorage.setItem('nickname', nickname)
              localStorage.setItem('gender', gender)
              location.href = './profile'
           }
         }).catch(e => {
          console.log(e)
          this.errors(e)
          location.href = './'
        })      
    },


   //  else if(description == "Fail Set Profile : Wrong Idx")
  },

  
    created() {
      

      this.$store.state.inviteUsers.splice(0);
      this.inviteTeam.channels.splice(0);
      localStorage.setItem("userId", "yunjea0312@naver.com"); //test용으로 임의로 넣어놈. 원래는 로그인 할때 넣어야 함
      this.$store.state.userId = "yunjea0312@naver.com";
      this.$store.state.userIdx = 5; //test용으로 넣어놈. 로그인 할때 받아야함
      this.userIdx = this.$store.state.userIdx;
      this.$store.state.userNickName = "yunyun"; //test용으로 넣어놈. 로그인 할때 받아야함
      axios
        .get("http://localhost:8083/api/team/user/" + "5")
        .then(response => { //
          // debugger;
            if(response.data) {
              this.teamsFromServer = response.data.data;
              this.teamName = response.data.data[0].name;
              this.userName = "yunjae"; //로그인 한 후 userName 받기 -> localStorage에서 받기 
              localStorage.setItem("teamIdx", response.data.data[0].idx);
              console.log("teamIdx" + response.data.data[0].idx);
              this.getMemberByTeamId(response.data.data[0].idx);
              this.getChannelsByTeamIdxAndUserIdx(response.data.data[0].idx, 5);

              this.$store.state.channelInfo.idx = response.data.data[0].idx;
              this.$store.state.channelInfo.channelName = response.data.data[0].name;
              
              
            } else {
            //   app.renderNotification('Successfully Singed Up');
            //   app.toggleSignUp();
            this.errors.push(e);
            }
          })
        .catch(e => {
          // location.href = './';
          this.errors.push(e);
        });
      

    } 
  };

  
</script>

<style>
.v-navigation-drawer {
background-color: aqua;
color: brown;
}

.teamName {
  font-size: 50px;
}
v-dialog {
  overflow-y: scroll;
}
.chatroom{
  background-color: white;
}
</style>